package org.opencompare.api.java.io;

/**
 * Created by smangin on 02/07/15.
 */
public class Cell {

    private String content;
    private String rawContent;
    private boolean isHeader;
    private int row;
    private int column;
    private int rowspan;
    private int colspan;

    public Cell(String content, String rawContent, boolean isHeader, int row, int column, int rowspan, int colspan) {
        setContent(content);
        setRawContent(rawContent);
        setHeader(isHeader);
        setRow(row);
        setColumn(column);
        setRowspan(rowspan);
        setColspan(colspan);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    @Override
    public String toString() {
        return content;
    }
}
