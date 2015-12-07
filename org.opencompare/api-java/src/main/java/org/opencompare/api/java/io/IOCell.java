package org.opencompare.api.java.io;

import java.util.Observable;

/**
 * Created by smangin on 02/07/15.
 */
public class IOCell implements Cloneable {

    private String content;
    private String rawContent;

    public IOCell(String content) {
        this(content, content);
    }

    public IOCell(String content, String rawContent) {
        setContent(content);
        setRawContent(rawContent);
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

    public IOCell clone() throws CloneNotSupportedException {
        return (IOCell) super.clone();
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
