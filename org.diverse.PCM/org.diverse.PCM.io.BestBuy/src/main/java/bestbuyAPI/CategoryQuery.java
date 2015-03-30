package bestbuyAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.BestBuyCategory;
import domain.BestBuyResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import util.BestBuyHelper;
import static util.BestBuyHelper.*;

/**
 * CategoryQuery provides methods to query information about product categories
 * from the bestbuy API.
 * 
 * @author jmdavril
 */
public class CategoryQuery {

    private static final String QUERY_PREFIX =
            "http://api.remix.bestbuy.com/v1/categories?format=json&apiKey={API_KEY}";
    
    private static final Logger LOG =
            LoggerFactory.getLogger(CategoryQuery.class);

    /**
     * @return the list of all product categories from the bestbuy API
     */
    public static List<BestBuyCategory> findAllCategories() {
        
        int totalNbOfCategories = findTotalNbOfCategories();
        int currentPage = 1;
        List<BestBuyCategory> categories = new ArrayList<BestBuyCategory> ();
        
        LOG.info("Fetching all categories ({}), this might take few minutes...",
                totalNbOfCategories);

        while (categories.size() < totalNbOfCategories) {
            categories.addAll(findCategories(currentPage));
            currentPage++;
        }

        LOG.info("Finished fetching all categories");
        return categories;
    }

    private static List<BestBuyCategory> findCategories(int page) {
        RestTemplate template = new RestTemplate();
        try {
            String jsonResponse = (String) template.getForObject(
                    QUERY_PREFIX + "&pageSize=20&page={page}",
                    String.class, API_KEY, page);

            ObjectMapper mapper = new ObjectMapper();
            BestBuyResponse bestBuyResponse =
                    mapper.readValue(jsonResponse, BestBuyResponse.class);
            return bestBuyResponse.getCategories();
        } catch (HttpStatusCodeException ex) {
            BestBuyHelper.waitThreeSec();
            return findCategories(page);
        } catch (IOException ex) {
            BestBuyHelper.waitThreeSec();
        }
        
        return findCategories(page);
    }

    private static int findTotalNbOfCategories() {
        RestTemplate template = new RestTemplate();
        try {
            String jsonResponse = (String) template.getForObject(
                    QUERY_PREFIX + "&pageSize=1", String.class, API_KEY);

            ObjectMapper mapper = new ObjectMapper();
            BestBuyResponse bestBuyResponse =
                    mapper.readValue(jsonResponse, BestBuyResponse.class);
            
            return bestBuyResponse.getTotal();
        } catch (HttpStatusCodeException ex) {
            BestBuyHelper.waitThreeSec();
            return findTotalNbOfCategories();
        } catch (IOException ex) {
            BestBuyHelper.waitThreeSec();
        }
        return findTotalNbOfCategories();
    }
}
