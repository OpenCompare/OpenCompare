package bestbuyAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.BestBuyProduct;
import domain.BestBuyResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import static util.BestBuyHelper.*;

/**
 * CategoryQuery provides methods to query information about products from the
 * bestbuy API.
 * 
 * @author jmdavril
 */
public class ProductQuery {

    private static final String QUERY_PREFIX
            = "https://api.remix.bestbuy.com/v1/products";
    private static final Logger LOG = LoggerFactory.getLogger(ProductQuery.class);

    public static List<BestBuyProduct> findAllProductsForCategory(String categoryId) {
        int totalNbOfProducts = findTotalNbOfProductForCategory(categoryId);
        int currentPage = 1;
        List products = new ArrayList();
        LOG.info("Fetching all products ({}) for category {}, this might take few minutes...",
                Integer.valueOf(totalNbOfProducts),
                categoryId);
        while (products.size() < totalNbOfProducts) {
            products.addAll(findProductsForCategory(categoryId, currentPage));
            currentPage++;
        }

        LOG.info("Finished fetching all products for category {}", categoryId);

        return products;
    }

    private static List<BestBuyProduct> findProductsForCategory(String categoryId, int page) {
        RestTemplate template = new RestTemplate();
        try {
            String jsonResponse = template.getForObject(
                    QUERY_PREFIX + "(categoryPath.id={categoryId})?&show=sku,name,details,features&apiKey={apiKey}&format=json&pageSize=20&page={page}",
                    String.class, categoryId, API_KEY, page);

            ObjectMapper mapper = new ObjectMapper();

            BestBuyResponse bestBuyResponse = (BestBuyResponse) mapper
                    .readValue(jsonResponse, BestBuyResponse.class);

            return bestBuyResponse.getProducts();
        } catch (HttpStatusCodeException ex) {
            waitThreeSec();
            return findProductsForCategory(categoryId, page);
        } catch (IOException ex) {
            waitThreeSec();
        }
        return findProductsForCategory(categoryId, page);
    }

    private static int findTotalNbOfProductForCategory(String categoryId) {
        RestTemplate template = new RestTemplate();
        try {
            String jsonResponse = (String) template.getForObject(
                    QUERY_PREFIX + "(categoryPath.id={categoryId})?&apiKey={apiKey}&format=json&pageSize=1",
                    String.class, categoryId, API_KEY);

            ObjectMapper mapper = new ObjectMapper();

            BestBuyResponse bestBuyResponse = (BestBuyResponse) mapper
                    .readValue(jsonResponse, BestBuyResponse.class);

            return bestBuyResponse.getTotal();
        } catch (HttpStatusCodeException ex) {
            waitThreeSec();
            return findTotalNbOfProductForCategory(categoryId);
        } catch (IOException ex) {
            waitThreeSec();
        }
        return findTotalNbOfProductForCategory(categoryId);
    }
}
