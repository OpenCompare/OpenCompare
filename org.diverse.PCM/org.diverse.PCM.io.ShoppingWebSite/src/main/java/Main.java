
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

/*
        PCM m = swsp.getPcm(87207);
        String name = "RR";
        name = name.trim() ;
        File f = new File("/Users/Aymeric/Documents/dev_PCM/PCM/org.diverse.PCM/org.diverse.PCM.io.ShoppingWebSite/PCMs/" +name +".json");
        if(f.exists())
        {
            f.delete();
        }
        PCMUtils.pcmNormalizer(m);
        m.setName("RRR");
        swsp.savePcmToFile(m,f);

        System.out.println(  jms.serialize(m));

            //
*/
        LinkedList<Pair<String,String>>  r = swsp.getAllCat();
        for (Pair<String, String> stringStringPair : r) {

            PCM m = swsp.getPcm(Integer.valueOf(stringStringPair.getValue())) ;
             if(m.getProducts().size() == 0)
            {
                System.out.println("no prod in " + stringStringPair.getKey());
            }else
            {
                String name = stringStringPair.getKey() ;
                name = name.trim() ;
                File f = new File("/Users/Aymeric/Documents/dev_PCM/PCM/org.diverse.PCM/org.diverse.PCM.io.ShoppingWebSite/PCMs/" +name +".json");
                if(f.exists())
                {
                    f.delete();
                }
                m.setName(stringStringPair.getValue());
                PCMUtils.pcmNormalizer(m);
                swsp.savePcmToFile(m,f);


            }

        }
      //  swspr.printProducts(1725);





      //  DefaultPcmFactory d = new DefaultPcmFactory();

//
  //      swsp.getAllExistingCategories();

    }
}

