import com.shopping.api.sdk.response.*;

import java.util.List;

/**
 * Simple demo utility to print some of the information in a Shopping.com Publisher API response
 * to the console.<p>
 * <p/>
 * Demonstrates how to loop over different parts of the response.
 */
public class DemoResponsePrinter
{
    private static final Integer CROSS_CATEGORY_ID = 0;

    /**
     * Write some of the information in the response to the console.
     *
     * @param response The response from the SdcQuery.submit().
     */
    public static void printResponse(GeneralSearchResponseType response) {
        CategoryListType categories = response.getCategories();
        if (categories != null) {
            for (CategoryType category : categories.getCategories()) {

                printCategoryName(category);
                printItems(category);
                printAttributes(category);
            }
        }
    }

    /**
     * Prints the attributes returned for the given category, and their values. Attributes are
     * used to pare-down results to a smaller list based on similar features.
     *
     * @param category The category whose attributes are to be printed.
     */
    private static void printAttributes(CategoryType category) {
        AttributeListType attributes = category.getAttributes();
        if (attributes != null) {
            for (AttributeType attribute : attributes.getAttributes()) {
                System.out.print("    Attribute: ");
                System.out.print(attribute.getName());
                AttributeValueListType attributeValues = attribute.getAttributeValues();
                if (attributeValues != null) {
                    System.out.print(" [");
                    boolean addComma = false;
                    for (AttributeValueType value : attributeValues.getAttributeValueList()) {
                        if (addComma) {
                            System.out.print(", ");
                        } else {
                            addComma = true;
                        }
                        System.out.print(value.getName());
                    }
                    System.out.print("]");
                }
                System.out.println();
            }
        }
    }

    /**
     * Prints out products and offers returned for the given category.  Offers can be for a particular
     * product, or they can be associated with the category itself (like the "flowers" category, which
     * doesn't contain specific products).
     *
     * @param category The returned category whose products and offers are
     *                 to be printed.
     */
    private static void printItems(CategoryType category) {
        ItemListType items = category.getItems();
        if (items != null) {
            for (Object productOrOffer : items.getProductOrOffer()) {
                if (productOrOffer instanceof ProductType) {
                    ProductType product = (ProductType) productOrOffer;
                    System.out.println("    Product: " + product.getName() + " ($" +
                            product.getMinPrice().getValue() + "-$" +
                            product.getMaxPrice().getValue() + ")" + " " + product.getId());

                    printSpecs(product);
                    printReviews(product);

                    OfferListType offers = product.getOffers();
                    if (offers != null) {
                        for (OfferType offer : offers.getOffers()) {
                            System.out.print("      ");
                            printOffer(offer);
                        }
                    }
                } else if (productOrOffer instanceof OfferType) {
                    OfferType offer = (OfferType) productOrOffer;
                    System.out.print("    ");
                    printOffer(offer);
                }
            }
        }
    }

    private static void printReviews(ProductType product) {
        ProductReviewsListType reviews = product.getReviews();
        if (reviews != null) {
            RatingDetailType averageRating = reviews.getAverageRating();
            String avg = averageRating == null ? "n/a" : averageRating.getOverallRating().toString();
            System.out.println(
                    "        Reviews: Avg: " + avg +
                            " Page: " + reviews.getPageNumber() +
                            " Count: " + reviews.getMatchedReviewCount() +
                            " Returned: " + reviews.getReturnedReviewCount());
            for (IndividualReviewType review : reviews.getConsumerReview()) {
                System.out.println("          " + review.getAuthorID() + ": " + review.getSummary());
            }
        }
    }

    private static void printSpecs(ProductType product) {
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

    /**
     * Prints out an offer, including SmartBuy and/or Featured tags associated with the offer.
     *
     * @param offer The offer to be printed.
     */
    private static void printOffer(OfferType offer) {
        System.out.print("Offer: " + offer.getStore().getName());
        if (offer.isSmartBuy()) {
            System.out.print(" *SmartBuy!*");
        }
        if (offer.isFeatured()) {
            System.out.print(" *Featured!*");
        }
        PriceType price = offer.getBasePrice();
        if (price != null) {
            System.out.print(" ($" + price.getValue() + ")");
        }
        Float shipping = offer.getShippingCost() != null ? offer.getShippingCost() .getValue(): null;
        if (shipping != null) {
            if (shipping == 0f) {
                System.out.println(" (free shipping)");
            } else {
                System.out.println(" ($" + shipping + " shipping)");
            }
        } else {
            System.out.println();
        }
    }

    /**
     * Prints the name of the given category. Indicates whether or not items are grouped
     * by category.
     *
     * @param category The category to be printed.
     */
    private static void printCategoryName(CategoryType category) {
        System.out.print("  Category: ");
        if (category.getId().equals(CROSS_CATEGORY_ID)) {
            System.out.print("Items not grouped by category.");
            String keyword = category.getName();
            if ((keyword != null) && (keyword.trim().length() > 0)) {
                System.out.println(" Keyword: " + keyword);
            } else {
                System.out.println();
            }
        } else {
            System.out.println(category.getName());
        }
    }

    private static void printTreeBranch(CategoryListType list, int indent) {
        if (list != null) {
            for (CategoryType category : list.getCategories()) {
                for (int i = 0; i < indent; i++) {
                    System.out.print(' ');
                }
                System.out.println(category.getName() + " " + category.getId() );
                printTreeBranch(category.getCategories(), indent + 2);
            }
        }
    }

    public static void printTree(CategoryTreeResponseType tree) {
        CategoryListType categories = tree.getCategory().getCategories();
        printTreeBranch(categories, 0);
        System.out.println();
    }
}
