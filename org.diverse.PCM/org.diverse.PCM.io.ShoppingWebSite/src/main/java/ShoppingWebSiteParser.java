import com.shopping.api.sdk.SdcCategories;
import com.shopping.api.sdk.SdcQuery;
import com.shopping.api.sdk.SdcQueryException;
import com.shopping.api.sdk.SdkConfiguration;
import com.shopping.api.sdk.response.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Aymeric on 08/10/2014.
 */
public class ShoppingWebSiteParser {

    SdkConfiguration config ;
    public int cpt = 0 ;

    public ShoppingWebSiteParser()  {
        URL url = this.getClass().getResource("sdk.properties") ;
        File propertiesFile = new File(url.getFile());
        try {
            config = new SdkConfiguration(propertiesFile);
        }catch (Exception e)
        {
            System.err.println(e.toString());
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

    private  void printTreeBranch(CategoryListType list, int indent) {
        if (list != null) {
            for (CategoryType category : list.getCategories()) {
                for (int i = 0; i < indent; i++) {
                    System.out.print(' ');
                }


                    printProducts(category.getId());

                System.out.println(category.getName());

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

                            printSpecs(product);



                        }
                    }
                }


            }
        }
    }
}
