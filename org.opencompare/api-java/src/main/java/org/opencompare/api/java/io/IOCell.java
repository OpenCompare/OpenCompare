package org.opencompare.api.java.io;

import java.util.Observable;

/**
 * Created by smangin on 02/07/15.
 */
public class IOCell {

    protected String content;
    protected String rawContent;
    protected int rowspan;
    protected int colspan;

    public IOCell() {
        this("", "", 1, 1);
    }

    public IOCell(String content) {
        this(content, content);
    }

    public IOCell(String content, String rawContent) {
        this(content, rawContent, 1, 1);
    }

    public IOCell(String content, int rowspan, int colspan) {
        this(content, content, rowspan, colspan);
    }

    public IOCell(String content, String rawContent, int rowspan, int colspan) {
        setContent(content);
        setRawContent(rawContent);
        setRowspan(rowspan);
        setColspan(colspan);
    }

    public String getContent() {
        return content;
    }

    public IOCell setContent(String content) {
        if (content == null) {
            this.content = "";
        }
        this.content = content;
        return this;
    }

    public String getRawContent() {
        return rawContent;
    }

    public IOCell setRawContent(String rawContent) {
        if (rawContent == null) {
            this.rawContent = "";
        }
        this.rawContent = rawContent;
        return this;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IOCell ioCell = (IOCell) o;

        if (content != null ? !content.equals(ioCell.content) : ioCell.content != null) return false;
        return !(rawContent != null ? !rawContent.equals(ioCell.rawContent) : ioCell.rawContent != null);

    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (rawContent != null ? rawContent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IOCell{" +
                "content='" + content + '\'' +
                ", rawContent='" + rawContent + '\'' +
                '}';
    }
}
