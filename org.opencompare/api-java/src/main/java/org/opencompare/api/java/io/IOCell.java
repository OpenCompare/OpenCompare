package org.opencompare.api.java.io;

import java.util.Observable;

/**
 * Created by smangin on 02/07/15.
 */
public class IOCell implements Cloneable {

    private String content;

    public IOCell(String content) {
        setContent(content);
    }

    public String getContent() {
        return content;
    }

    public IOCell setContent(String content) {
        if (content == null) {
            content = "";
        }
        this.content = content;
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
