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

    public IOCell setRawContent(String rawContent) {
        if (rawContent == null) {
            this.rawContent = "";
        }
        this.rawContent = rawContent;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj != null &&obj instanceof IOCell) {
            IOCell cell = (IOCell) obj;
            return getContent().equals(cell.getContent());
        }
        return false;
    }

    public IOCell clone() throws CloneNotSupportedException {
        return (IOCell) super.clone();
    }
}
