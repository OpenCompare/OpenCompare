package org.diverse.pcm.io.bestbuy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gbecan on 15/01/15.
 */
public class ProductInfo {

    private String longDescription;

    private List<String> features;
    private Map<String, String> details;

    public ProductInfo() {
        features = new ArrayList<String>();
        details = new HashMap<String, String>();
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void addFeature(String feature) {
        features.add(feature);
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void addDetail(String name, String value) {
        details.put(name, value);
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "longDescription='" + longDescription + "'\n" +
                "features=" + features + "\n" +
                "details=" + details + "\n" +
                '}';
    }
}
