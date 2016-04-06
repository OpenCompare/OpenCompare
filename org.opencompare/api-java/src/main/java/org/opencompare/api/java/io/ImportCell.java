package org.opencompare.api.java.io;

import org.opencompare.api.java.Value;

/**
 * Created by gbecan on 08/12/15.
 */
public class ImportCell extends IOCell {

    private Value interpretation;

    public ImportCell() {
        super("");
    }

    public ImportCell(String content) {
        super(content);
    }

    public ImportCell(String content, String rawContent) {
        super(content, rawContent);
    }

    public ImportCell(String content, int rowspan, int colspan) {
        super(content, rowspan, colspan);
    }

    public ImportCell(String content, String rawContent, int rowspan, int colspan) {
        super(content, rawContent, rowspan, colspan);
    }

    public Value getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(Value interpretation) {
        this.interpretation = interpretation;
    }
}
