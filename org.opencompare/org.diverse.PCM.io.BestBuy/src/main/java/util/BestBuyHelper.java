package util;

/**
 * BestBuyHelper provides utility methods and constants related to bestbuy.
 * 
 * @author jmdavril
 */
public class BestBuyHelper {

    public static final String API_KEY = "ye723adnkr5x3qafvvvgc7r3";
    public static final String DATASETS_PATH = "Other Sources/datasets/";

    public static void waitThreeSec() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
