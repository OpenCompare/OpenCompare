import com.shopping.api.sdk.SdcCategories;
import com.shopping.api.sdk.SdcQuery;
import com.shopping.api.sdk.SdcQueryException;
import com.shopping.api.sdk.SdkConfiguration;
import com.shopping.api.sdk.response.*;
import org.kevoree.modeling.api.json.JSONModelSerializer;
import pcm.*;
import pcm.factory.DefaultPcmFactory;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Aymeric on 08/10/2014.
 */
public class ShoppingWebSiteParser {


    private DefaultPcmFactory myFactory ;
    private SdkConfiguration config ;


    public void savePcmToFile(PCM m, File f)
    {
        JSONModelSerializer jml = myFactory.createJSONSerializer() ;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            jml.serializeToStream(m,fos);
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ShoppingWebSiteParser()  {
        URL url = this.getClass().getResource("sdk.properties") ;
        File propertiesFile = new File(url.getFile());
        try {
            config = new SdkConfiguration(propertiesFile);
            myFactory = new DefaultPcmFactory() ;

        }catch (Exception e)
        {
            System.err.println(e.toString());
        }
    }

    public PCM getPcm(int categoryId){
        PCM pcmRes = myFactory.createPCM() ;
        populatePCM(pcmRes, categoryId);
        return pcmRes ;
    }


    public LinkedList<Pair<String,String>> getAllCat()
    {
       LinkedList<Pair<String,String>> res = new  LinkedList<Pair<String,String>>() ;
        SdcCategories  categories = null;

        try {
            categories = new SdcCategories(config);
            categories.setCategoryId(0);
            categories.setShowAllDescendants(true);
            CategoryTreeResponseType   tree = categories.submit();
            getTree(tree, res);
        } catch (SdcQueryException e) {
            e.printStackTrace();
        }
        return res ;
    }


    private  void getTreeBranch(CategoryListType list,LinkedList<Pair<String,String>> r)
    {
        if (list != null) {
            for (CategoryType category : list.getCategories()) {
                r.add(new Pair<String, String>(category.getName(),String.valueOf(category.getId())));
                getTreeBranch(category.getCategories(), r);

            }
        }
    }

    public  void getTree(CategoryTreeResponseType tree,LinkedList<Pair<String,String>> r) {
        CategoryListType categories = tree.getCategory().getCategories();
        getTreeBranch(categories, r);

    }



    public CategoryTreeResponseType getAllExistingCategories()  {
        CategoryTreeResponseType resultTree = null;
        SdcCategories categories = null;
        try {
            categories = new SdcCategories(config);
            categories.setCategoryId(0);
            categories.setShowAllDescendants(true);
            resultTree = categories.submit();
            printTree(resultTree);
        } catch (SdcQueryException e) {
            e.printStackTrace();
        }
        return resultTree ;
    }






    private Feature getFeature(PCM _pcm, String FeatureName)
    {

        Feature f = null ;
        List<AbstractFeature> features =  _pcm.getFeatures() ;
        for (AbstractFeature feature : features) {

            if(feature.getName().equals(FeatureName))
            {

                if(feature instanceof Feature) {
                    f = (Feature) feature;
                }
            }
        }
        if (f == null)
        {
            f = myFactory.createFeature() ;
            f.setName(FeatureName);
            _pcm.addFeatures(f);
        }
        return f;
    }

    private FeatureGroup getFeatureGroup(PCM _pcm, String groupFeatureName)
    {
        FeatureGroup f = null ;
        List<AbstractFeature> features =  _pcm.getFeatures() ;
        for (AbstractFeature feature : features) {
            if(feature.getName().equals(groupFeatureName) && feature instanceof FeatureGroup)
            {
                f = (FeatureGroup)feature ;
            }
        }
        if (f == null)
        {
            f = myFactory.createFeatureGroup() ;
            f.setName(groupFeatureName);
            _pcm.addFeatures(f);
        }
        return f;
    }



    private Feature isFeatureInGroup(String featureName, FeatureGroup fg)
    {
         Feature f = null ;
        List<AbstractFeature> features = fg.getSubFeatures() ;
        for (AbstractFeature feature : features) {
            if(feature.getName().equals(featureName))
            {

                if(feature instanceof Feature) {

                    f = (Feature) feature;
                }
            }
        }
        if (f == null)
        {

            f = myFactory.createFeature() ;
            f.setName(featureName);
            fg.addSubFeatures(f);
        }

        return f ;
        }




    private void populateSpecProduct(Product pcmProd,PCM _pcm, ProductType product) {
        ProductSpecificationsType specifications = product.getSpecifications();
        if (specifications != null) {
            List<FeatureGroupType> featureGroups = specifications.getFeatureGroup();

            for (FeatureGroupType featureGroup : featureGroups) {

                if (featureGroup.getName() != null || !featureGroup.getName().isEmpty()) {

                        FeatureGroup pcmFeatureGroup = getFeatureGroup(_pcm, featureGroup.getName());

                        List<FeatureType> featureList = featureGroup.getFeature();

                        for (FeatureType feature : featureList) {
                            if (feature.getName() != null || !feature.getName().isEmpty()) {

                                Feature f = isFeatureInGroup( feature.getName(),pcmFeatureGroup);
                                List<String> values = feature.getValue();
                                if (values.size() == 0) {
                                    // do nothing
                                } else {
                                    if (values.size() == 1) {
                                        Cell c = myFactory.createCell();
                                        c.setContent(values.get(0));
                                        c.setFeature(f);
                                        pcmProd.addCells(c);

                                    } else {

                                        Cell c = myFactory.createCell();
                                        c.setContent(values.toString());
                                        c.setFeature(f);
                                        Multiple m = myFactory.createMultiple();
                                        for (String value : values) {
                                            StringValue sv = myFactory.createStringValue();
                                            sv.setValue(value);
                                            m.addSubvalues(sv);
                                        }
                                        c.setInterpretation(m);
                                        pcmProd.addCells(c);
                                    }
                                }
                            }
                        }
                    }

            }
        }
    }



    private void populatePCM(PCM _pcm, int categoryId)
    {
        SdcQuery query = null;
        try {
            query = new SdcQuery(config);
            query.setCategoryId(categoryId);
            query.setShowProductSpecs(true);
    System.out.println(categoryId) ;
            CategoryListType categories  = query.submit().getCategories();

            if (categories != null) {
                for (CategoryType category : categories.getCategories()) {

                    ItemListType items = category.getItems();
                    if (items != null) {

                        for (Object productOrOffer : items.getProductOrOffer()) {
                            if (productOrOffer instanceof ProductType) {

                                Product pcmProd = myFactory.createProduct() ;

                                ProductType product = (ProductType) productOrOffer;
                                pcmProd.setName(product.getName());
                                Cell cMin = myFactory.createCell();
                                cMin.setFeature(getFeature(_pcm,"MinPrice"));
                                cMin.setContent(String.valueOf(product.getMinPrice().getValue()));
                                RealValue vMin = myFactory.createRealValue() ;
                                vMin.setValue(Double.valueOf(product.getMinPrice().getValue()));
                                cMin.setInterpretation(vMin);

                                Cell cMax = myFactory.createCell();
                                cMax.setFeature(getFeature(_pcm,"MaxPrice"));
                                cMax.setContent(String.valueOf(product.getMaxPrice().getValue()));
                                RealValue vMax = myFactory.createRealValue() ;
                                vMax.setValue(Double.valueOf(product.getMaxPrice().getValue()));
                                cMax.setInterpretation(vMax);

                                pcmProd.addCells(cMin);
                                pcmProd.addCells(cMax);
                                populateSpecProduct(pcmProd, _pcm, product);

                                _pcm.addProducts(pcmProd) ;
                            }
                        }
                    }
                }
            }
        } catch (SdcQueryException e) {
            e.printStackTrace();
        }
    }

    private  void printTreeBranch(CategoryListType list) {
        if (list != null) {
            for (CategoryType category : list.getCategories()) {
                System.out.println("Parsing category : " + category.getName());
                PCM m = getPcm(category.getId()) ;
                JSONModelSerializer jms = myFactory.createJSONSerializer() ;
                System.out.println(  jms.serialize(m));
                printTreeBranch(category.getCategories());
                System.err.println("--- the end pdt---");
            }
        }
    }

    public  void printTree(CategoryTreeResponseType tree) {
        CategoryListType categories = tree.getCategory().getCategories();
        printTreeBranch(categories);
        System.out.println();
    }




}
