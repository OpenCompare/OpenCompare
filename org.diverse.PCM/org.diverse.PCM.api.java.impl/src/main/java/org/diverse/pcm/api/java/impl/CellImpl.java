package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Value;

/**
 * Created by gbecan on 08/10/14.
 */
public class CellImpl implements Cell {

    private pcm.Cell kCell;

    public CellImpl(pcm.Cell kCell) {
        this.kCell = kCell;
    }

    public pcm.Cell getkCell() {
        return kCell;
    }

    @Override
    public String getContent() {
        return kCell.getContent();
    }

    @Override
    public void setContent(String s) {
        kCell.setContent(s);
    }

    @Override
    public Value getInterpretation() {
        return new ValueImpl(kCell.getInterpretation());
    }

    @Override
    public void setInterpretation(Value value) {
        kCell.setInterpretation(((ValueImpl) value).getkValue());
    }
}
