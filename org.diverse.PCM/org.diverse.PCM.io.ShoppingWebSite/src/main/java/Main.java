import org.jetbrains.annotations.NotNull;
import org.kevoree.modeling.api.Transaction;
import org.kevoree.modeling.api.json.JSONModelSerializer;
import pcm.PCM;
import pcm.factory.DefaultPcmFactory;

/**
 * Created by Aymeric on 08/10/2014.
 */
public class Main {

    public static void main(String[] Args)
    {
         DefaultPcmFactory myFactory = new DefaultPcmFactory();
        ShoppingWebSiteParser swsp = new ShoppingWebSiteParser() ;
     //   swsp.getAllExistingCategories();
        swsp.printProducts(87);
        PCM m = swsp.getPcm(87) ;
        JSONModelSerializer jms = myFactory.createJSONSerializer() ;


          System.out.println(  jms.serialize(m));
      //  DefaultPcmFactory d = new DefaultPcmFactory();


    }
}

