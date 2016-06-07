package org.opencompare.api.java.io;

/**
 * Created by gbecan on 08/12/15.
 */
public class ExportCell extends IOCell {

    protected boolean feature;
    protected boolean inProductsKeyColumn;

    public ExportCell() {

    }

    public ExportCell(String content) {
        super(content);
    }

    public ExportCell(String content, String rawContent) {
        super(content, rawContent);
    }

    public ExportCell(String content, int rowspan, int colspan) {
        super(content, rowspan, colspan);
    }

    public ExportCell(String content, String rawContent, int rowspan, int colspan) {
        super(content, rawContent, rowspan, colspan);
    }

    public boolean isFeature() {
        return feature;
    }

    public void setFeature(boolean feature) {
        this.feature = feature;
    }

    public boolean isInProductsKeyColumn() {
        return inProductsKeyColumn;
    }

    public void setInProductsKeyColumn(boolean inProductsKeyColumn) {
        this.inProductsKeyColumn = inProductsKeyColumn;
    }
}
