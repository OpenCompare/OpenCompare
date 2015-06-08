import pcm.*;
import pcm.factory.DefaultPcmFactory;

import java.util.List;

/**
 * Created by Aymeric on 20/10/2014.
 */
public class  PCMUtils {

    static DefaultPcmFactory myFactory = new DefaultPcmFactory();

    public static void pcmNormalizer(PCM m)
    {

        pcmNormRec(m, m.getFeatures());
    }

    private static void pcmNormRec(PCM m,  List<AbstractFeature> listFeature){


        for (AbstractFeature abstractFeature : listFeature) {
            if(abstractFeature instanceof Feature){
                List<Product> productList =  m.getProducts() ;
                for (Product product : productList) {
                    if(!hasFeature(product,(Feature)abstractFeature))
                    {

                        Cell c = myFactory.createCell();
                        c.setContent("");
                        c.setFeature((Feature)abstractFeature);
                        product.addCells(c);
                    }
                }
            }else{
                pcmNormRec(m, ((FeatureGroup)abstractFeature).getSubFeatures());
            }
        }
    }

    private static boolean hasFeature(Product p, Feature f){
        boolean res = false ;
        List<Cell> cellList = p.getCells() ;
        for (Cell cell : cellList) {
            if(cell.getFeature().equals(f))
            {
                return true ;
            }
        }


        return res ;
    }
}
