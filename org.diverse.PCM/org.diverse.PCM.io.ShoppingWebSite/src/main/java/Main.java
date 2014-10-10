import com.sun.tools.javac.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.kevoree.modeling.api.Transaction;
import org.kevoree.modeling.api.json.JSONModelSerializer;
import pcm.PCM;
import pcm.factory.DefaultPcmFactory;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by Aymeric on 08/10/2014.
 */
public class Main {

    public static void main(String[] Args)
    {

         DefaultPcmFactory myFactory = new DefaultPcmFactory();
        JSONModelSerializer jms = myFactory.createJSONSerializer() ;
        ShoppingWebSiteParser swsp = new ShoppingWebSiteParser() ;
        ShoppingWebSitePrinter swspr = new ShoppingWebSitePrinter();
        LinkedList<Pair<String,String>>  r = swsp.getAllCat();
        for (Pair<String, String> stringStringPair : r) {

            PCM m = swsp.getPcm(Integer.valueOf(stringStringPair.snd)) ;
            if(m.getProducts().size() == 0)
            {
                System.out.println("no prod in " + stringStringPair.fst);
            }else
            {
                String name = stringStringPair.fst ;
                name = name.trim() ;
                File f = new File("/Users/Aymeric/Documents/dev_PCM/PCM/org.diverse.PCM/org.diverse.PCM.io.ShoppingWebSite/PCMs/" +name +".json");
                if(f.exists())
                {
                    f.delete();
                }
                m.setName(stringStringPair.fst);
                swsp.savePcmToFile(m,f);
                System.out.println(  jms.serialize(m));
            }

        }
      //  swspr.printProducts(1725);





      //  DefaultPcmFactory d = new DefaultPcmFactory();

//
  //      swsp.getAllExistingCategories();

    }
}

