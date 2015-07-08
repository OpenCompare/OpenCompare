package org.opencompare.api.java.io;

import java.util.Observable;

/**
 * Created by smangin on 02/07/15.
 */
public class IOCell extends Observable implements Cloneable {

    private String content;
    private String rawContent;
    private int row = -1;
    private int column = -1;
    private int rowspan = 1;
    private int colspan = 1;

    public IOCell(String content, String rawContent) {
        setContent(content);
        setRawContent(rawContent);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content == null) {
            content = "";
        }
        this.content = content;
        this.setChanged();
        this.notifyObservers();
    }

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        if (rawContent == null) {
            rawContent = "";
        }
        this.rawContent = rawContent;
        this.setChanged();
        this.notifyObservers();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
        this.setChanged();
        this.notifyObservers();
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
        this.setChanged();
        this.notifyObservers();
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        assert rowspan >= 1;
        this.rowspan = rowspan;
        this.setChanged();
        this.notifyObservers();
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        assert colspan >= 1;
        this.colspan = colspan;
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null && obj instanceof IOCell) {
            IOCell cell = (IOCell) obj;
            return getContent().equals(cell.getContent()) && getRawContent().equals(cell.getRawContent()) &&
                    getColspan() == cell.getColspan() && getRowspan() == cell.getRowspan() &&
                    getColumn() == cell.getColumn() && getRow() == cell.getRow();
        }
        return false;
    }

    public IOCell clone() throws CloneNotSupportedException {
        return (IOCell) super.clone();
    }
}
