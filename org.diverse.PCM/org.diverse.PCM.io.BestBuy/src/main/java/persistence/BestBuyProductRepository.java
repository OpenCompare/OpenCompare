package persistence;

import domain.BestBuyProduct;
import java.util.List;

/**
 * BestBuyProductRepository represents an immutable repository of bestbuy
 * products.
 * 
 * @author jmdavril
 */
public interface BestBuyProductRepository {

    /**
     * @requires categoryId != null
     * @return the list of bestbuy products from the bestbuy product category
     *         identified by categoryId
     */
    List<BestBuyProduct> findAllProductsForCategory(String categoryId);
}
