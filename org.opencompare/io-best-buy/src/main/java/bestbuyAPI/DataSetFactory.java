package bestbuyAPI;

import domain.BestBuyProduct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataSetFactory provides methods to create local datasets of bestbuy products.
 * 
 * @author jmdavril
 */
public class DataSetFactory {

    private static final Logger LOG
            = LoggerFactory.getLogger(DataSetFactory.class);

    /**
     * 
     * @requires categoryId != null
     * @modifies this
     * @effects creates a local bestbuy product dataset for the product category
     *          identified by categoryId
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public static void createLocalDataset(String categoryId)
            throws FileNotFoundException, UnsupportedEncodingException, IOException {
        assert (categoryId != null);

        List products = ProductQuery.findAllProductsForCategory(categoryId);

        JSONArray productList = new JSONArray(products.toArray(
                new BestBuyProduct[products.size()]));

        File file = new File(String.format(
                "%s%s.txt", "data/datasets/", categoryId));
        System.out.println("the name: " + file.getPath());

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        if (!file.exists()) {
            file.createNewFile();
        } else {
            LOG.warn(String.format(
                    "File %s.txt already exists, the file will be overwritten",
                    categoryId));
        }

        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        output.write(productList.toString());
        output.close();
    }
}
