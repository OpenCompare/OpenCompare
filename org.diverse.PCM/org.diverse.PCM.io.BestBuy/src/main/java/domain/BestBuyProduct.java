package domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BestBuyProduct is an immutable representation of a bestbuy product.
 * 
 * @author jmdavril
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class BestBuyProduct {

    private String sku;
    private String name;
    private String longDescription;
    private List<Feature> features;
    private List<Detail> details;

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongDescription() {
        return this.longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<Feature> getFeatures() {
        if (this.features == null) {
            return null;
        }
        return Collections.unmodifiableList(this.features);
    }

    public void setFeatures(List<Feature> features) {
        this.features = new ArrayList(features);
    }

    public List<Detail> getDetails() {
        if (this.details == null) {
            return null;
        }
        return Collections.unmodifiableList(this.details);
    }

    public void setDetails(List<Detail> details) {
        this.details = new ArrayList(details);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Product{");
        if (this.sku != null) {
            builder.append("sku={");
            builder.append(sku);
            builder.append("}, ");
        }
        if (this.sku != null) {
            builder.append("name={");
            builder.append(name);
            builder.append("}, ");
        }
        if (this.details != null) {
            builder.append("details={");
            builder.append(details.toString());
            builder.append("}, ");
        }
        if (this.features != null) {
            builder.append("features={");
            builder.append(features.toString());
            builder.append("}");
        }
        builder.append("}");
        return builder.toString();
    }

    public static class Feature {

        private String feature;

        public String getFeature() {
            return this.feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }

        public String toString() {
            return this.feature;
        }
    }

    public static class Detail {

        private String name;
        private String value;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String toString() {
            return this.name + ":" + this.value;
        }
    }
}
