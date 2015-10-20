package impl;

import domain.BestBuyProduct;
import java.util.List;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.impl.PCMFactoryImpl;

/**
 * BestBuyMiner provides methods to manipulate and analyze bestbuy products.
 * 
 * @author jmdavril
 */
public class BestBuyMiner {

    private final PCMFactory pcmFactory;

    public BestBuyMiner() {
        this.pcmFactory = new PCMFactoryImpl();
    }

    /**
     * @requires products != null
     * @return the PCM corresponding to the given list of bestbuy products
     */
    public PCM createPCM(List<BestBuyProduct> products) {
        assert (products != null);
        for (BestBuyProduct p : products) {
            assert (p != null);
        }

        PCM pcm = this.pcmFactory.createPCM();

        Feature productsKey = pcmFactory.createFeature();
        pcm.addFeature(productsKey);
        pcm.setProductsKey(productsKey);

        for (BestBuyProduct p : products) {
            Product product = this.pcmFactory.createProduct();

            Cell productKey = pcmFactory.createCell();
            productKey.setFeature(productsKey);
            productKey.setContent(p.getName());

            pcm.addProduct(product);
            if (p.getDetails() != null) {
                for (BestBuyProduct.Detail d : p.getDetails()) {
                    Feature feature = pcm.getOrCreateFeature(d.getName(), this.pcmFactory);
                    Cell cell = this.pcmFactory.createCell();
                    cell.setContent(d.getValue());
                    cell.setFeature(feature);
                    product.addCell(cell);
                }
            }
        }
        
        pcm.normalize(this.pcmFactory);

        return pcm;
    }
}
