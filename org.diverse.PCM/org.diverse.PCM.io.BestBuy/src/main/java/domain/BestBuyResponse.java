package domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BestBuyResponse is an immutable representation of a response sent by the
 * bestbuy API.
 * 
 * @author jmdavril
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class BestBuyResponse {

    private int from;
    private int to;
    private int total;
    private int currentPage;
    private int totalPages;
    private double queryTime;
    private double totalTime;
    private boolean partial;
    private String canonicalUrl;
    private List<BestBuyProduct> products;
    private List<BestBuyCategory> categories;

    public int getFrom() {
        return this.from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return this.to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public double getQueryTime() {
        return this.queryTime;
    }

    public void setQueryTime(double queryTime) {
        this.queryTime = queryTime;
    }

    public double getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public boolean isPartial() {
        return this.partial;
    }

    public void setPartial(boolean partial) {
        this.partial = partial;
    }

    public String getCanonicalUrl() {
        return this.canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public List<BestBuyProduct> getProducts() {
        if (this.products == null) {
            return null;
        }
        return Collections.unmodifiableList(this.products);
    }

    public void setProducts(List<BestBuyProduct> products) {
        this.products = new ArrayList(products);
    }

    public List<BestBuyCategory> getCategories() {
        if (this.categories == null) {
            return null;
        }
        return Collections.unmodifiableList(this.categories);
    }

    public void setCategories(List<BestBuyCategory> categories) {
        this.categories = new ArrayList(categories);
    }
}
