import org.jetbrains.annotations.NotNull;
import org.kevoree.modeling.api.Transaction;
import pcm.factory.DefaultPcmFactory;

/**
 * Created by Aymeric on 08/10/2014.
 */
public class Main {

    public static void main(String[] Args)
    {
        ShoppingWebSiteParser swsp = new ShoppingWebSiteParser() ;
        swsp.getAllExistingCategories();
     //   swsp.printProducts(87);
   //     swsp.printProductFeature("137684914");
//        System.out.println(swsp.cpt);
      //  DefaultPcmFactory d = new DefaultPcmFactory();


    }
}

