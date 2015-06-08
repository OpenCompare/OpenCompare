import com.shopping.api.sdk.SdcCategories;
import com.shopping.api.sdk.SdcQuery;
import com.shopping.api.sdk.SdcQueryException;
import com.shopping.api.sdk.SdkConfiguration;
import com.shopping.api.sdk.response.*;
import org.kevoree.modeling.api.json.JSONModelSerializer;
import pcm.*;
import pcm.factory.DefaultPcmFactory;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Created by Aymeric on 08/10/2014.
 */
public class ShoppingWebSitePrinter {


    private DefaultPcmFactory myFactory ;

    SdkConfiguration config ;
    public int cpt = 0 ;

    public ShoppingWebSitePrinter()  {
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


    public void printAllCat()
    {
        SdcCategories  categories = null;
        try {
            categories = new SdcCategories(config);
            categories.setCategoryId(0);
            categories.setShowAllDescendants(true);
            CategoryTreeResponseType   tree = categories.submit();
            printTree(tree);

        } catch (SdcQueryException e) {
            e.printStackTrace();
        }


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

    public void printProducts(int categoryId) {
        try {
            SdcQuery query = new SdcQuery(config);
            query.setCategoryId(categoryId);
            query.setShowProductSpecs(true);
            printProducts(query.submit().getCategories());
        } catch (Exception e) {
        System.err.println(e.toString());
        }
    }

    public void printProductFeature(String productId) {
        try {
            SdcQuery query = new SdcQuery(config);
            query.addProductId(productId);
            query.setShowProductSpecs(true);
            GeneralSearchResponseType gsrt = query.submit();
            printProducts(gsrt.getCategories());

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    private boolean hasProduct(CategoryType category){
       ItemListType lst = category.getItems() ;
        if(lst != null) {
            if (lst.getProductOrOffer().size() > 0) {
                return true;
            } else {
                return false;

            }
        }else {
            return false;
        }
    }

    private Feature getFeature(PCM _pcm, String FeatureName)
    {
        Feature f = (Feature)_pcm.findFeaturesByID(FeatureName) ;
        System.out.println("FeatureName" + FeatureName);
        if (f == null)
        {
         f =   myFactory.createFeature() ;
            f.setGenerated_KMF_ID(FeatureName);
            f.setName(FeatureName);

            _pcm.addFeatures(f);
        }
        return f;
    }

    private FeatureGroup getFeatureGroup(PCM _pcm, String groupFeatureName)
    {
        FeatureGroup f = (FeatureGroup)_pcm.findFeaturesByID(groupFeatureName) ;
        if (f == null)
        {
            f =   myFactory.createFeatureGroup() ;
            f.setGenerated_KMF_ID(groupFeatureName);
            f.setName(groupFeatureName);
            _pcm.addFeatures(f);
        }
        return f;
    }



    private void populateSpecProduct(Product pcmProd,PCM _pcm, ProductType product) {
        ProductSpecificationsType specifications = product.getSpecifications();
        if (specifications != null) {
            List<FeatureGroupType> featureGroups = specifications.getFeatureGroup();

            for (FeatureGroupType featureGroup : featureGroups) {
                if (featureGroup.getName() != null || !featureGroup.getName().isEmpty()) {
                    AbstractFeature ft = null;
                    if (featureGroup.getFeature().size() == 1) {


                    } else {
                        FeatureGroup pcmFeatureGroup = getFeatureGroup(_pcm, featureGroup.getName());
                        List<FeatureType> featureList = featureGroup.getFeature();

                        for (FeatureType feature : featureList) {
                            if (feature.getName() != null || !feature.getName().isEmpty()) {
                                Feature f = getFeature(_pcm, feature.getName());
                                pcmFeatureGroup.addSubFeatures(f);
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
                                        Multiple m = myFactory.createMultiple();
                                        for (String value : values) {
                                            StringValue sv = myFactory.createStringValue();
                                            sv.setValue(value);
                                            m.addSubvalues(sv);
                                        }
                                        c.setInterpretation(m);
                                        c.setFeature(f);
                                        pcmProd.addCells(c);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public PCM getPcm(int categoryId){

      PCM pcmRes = myFactory.createPCM() ;
        System.out.println("Dealing with : " + categoryId) ;
        populatePCM(pcmRes, categoryId);

      return pcmRes ;
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

    private  void printTreeBranch(CategoryListType list, int indent) {
        if (list != null) {
            for (CategoryType category : list.getCategories()) {
                for (int i = 0; i < indent; i++) {
                    System.out.print(' ');
                }

                System.err.println(category.getName() + " " + category.getId());

                printTreeBranch(category.getCategories(), indent + 2);

            }
        }
    }

    public  void printTree(CategoryTreeResponseType tree) {
        CategoryListType categories = tree.getCategory().getCategories();
        printTreeBranch(categories, 0);
        System.out.println();
    }

    private  void printSpecs(ProductType product) {
        ProductSpecificationsType specifications = product.getSpecifications();
        if (specifications != null) {
            List<FeatureGroupType> featureGroups = specifications.getFeatureGroup();
            System.out.println("      Specifications:");
            for(FeatureGroupType featureGroup : featureGroups) {
                System.out.print("        " + featureGroup.getName() + ": ");
                List<FeatureType> featureList = featureGroup.getFeature();
                boolean first = true;
                for (FeatureType feature : featureList) {
                    if (first) {
                        first = false;
                    } else {
                        System.out.print(", ");
                    }
                    System.out.print(feature.getName() + ": " + feature.getValue());
                }
                System.out.println();
            }
        }
    }



    private void printProducts(CategoryListType categories)
    {

        if (categories != null) {
            for (CategoryType category : categories.getCategories()) {

                ItemListType items = category.getItems();
                if (items != null) {
                    cpt++ ;
                    for (Object productOrOffer : items.getProductOrOffer()) {
                        if (productOrOffer instanceof ProductType) {
                            ProductType product = (ProductType) productOrOffer;
                            System.out.println("    Product: " + product.getName() + " ($" +
                                    product.getMinPrice().getValue() + "-$" +
                                    product.getMaxPrice().getValue() + ")");
                            System.out.println(product.getFullDescription());
                            printSpecs(product);
                        }
                    }
                }
            }
        }
    }
}
