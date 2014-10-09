package org.diverse.pcm.api.java.impl;

import org.diverse.pcm.api.java.Cell;
import org.diverse.pcm.api.java.Value;
import org.diverse.pcm.api.java.impl.value.BooleanValueImpl;
import pcm.BooleanValue;

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
        pcm.Value kInterpretation = kCell.getInterpretation();
        if (kInterpretation == null) {
            return null;
        } else if (kInterpretation instanceof BooleanValue) {
            return new BooleanValueImpl((BooleanValue) kInterpretation);
        } else {
            throw new UnsupportedOperationException(kInterpretation.getClass() + " interpretation type is not yet supported");
        }
    }

    @Override
    public void setInterpretation(Value value) {
       kCell.setInterpretation(((ValueImpl) value).getkValue());
    }
}
