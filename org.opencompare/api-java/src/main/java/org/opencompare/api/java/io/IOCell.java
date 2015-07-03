package org.opencompare.api.java.io;

/**
 * Created by smangin on 02/07/15.
 */
public class IOCell {

    private String content;
    private String rawContent;
    private int rowspan;
    private int colspan;

    public IOCell() {
        new IOCell("", "", 0, 0);
    }
    public IOCell(String content) {
        new IOCell(content, content, 0, 0);
    }
    public IOCell(String content, String rawContent) {
        new IOCell(content, rawContent, 0, 0);
    }
    public IOCell(String content, String rawContent, int rowspan, int colspan) {
        setContent(content);
        setRawContent(rawContent);
        this.rowspan = rowspan;
        this.colspan = colspan;
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

    public int getRowspan() {
        return rowspan;
    }

    public int getColspan() {
        return colspan;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof IOCell) {
            IOCell IOCell = (IOCell) obj;
            return getContent().equals(IOCell.getContent()) && getRawContent().equals(IOCell.getRawContent()) &&
                    getColspan() == IOCell.getColspan() && getRowspan() == IOCell.getRowspan();
        }
        return false;
    }
}
