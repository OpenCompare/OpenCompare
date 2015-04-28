package impl;

import bestbuyAPI.DataSetFactory;
import bestbuyAPI.ProductQuery;
import persistence.BestBuyProductRepository;
import persistence.impl.JsonBestBuyProductRepositoryImpl;
import java.io.IOException;
import java.util.List;
import org.opencompare.api.java.PCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtractionDemo {

    private static final Logger LOG = LoggerFactory.getLogger(ProductQuery.class);

    public static void main(String[] args)
            throws IOException {
        
        // 1. Create a local json dataset for the category. If there was already
        //    a local dataset prior to the call, it will be overwritten. You can
        //    comment this line out to use the existing local dataset and if you
        //    do so, you won't connect to the bestbuy API.
        DataSetFactory.createLocalDataset("abcat0401000");
        
        // 2. Get the bestbuy products from the local dataset.
        BestBuyProductRepository productRepository =
                new JsonBestBuyProductRepositoryImpl();
        List products = productRepository
                .findAllProductsForCategory("abcat0401000");

        // 3. Create the PCM from the list of products.
        BestBuyMiner miner = new BestBuyMiner();
        PCM pcm = miner.createPCM(products);

        // 4. Calculate clusters of products
        Clusterer clusterer = new Clusterer();


        clusterer.printSimilarityMatrixOfProjects(clusterer.calculateProductSimilarityMatrix(pcm),"data/datasets/",pcm);


        LOG.info("Nb of products in PCM = {}", pcm.getProducts().size());
        LOG.info("Nb of features in PCM = {}", pcm.getFeatures().size());
    }
}
