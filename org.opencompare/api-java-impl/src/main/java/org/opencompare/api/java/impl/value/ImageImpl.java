package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.Image;

/**
 * Created by macher1 on 14/04/2017.
 */
public class ImageImpl extends ValueImpl implements Image {


        private org.opencompare.model.ImageValue kImageValue;

        public ImageImpl(org.opencompare.model.ImageValue kImageValue) {
            super(kImageValue);
            this.kImageValue = kImageValue;
        }

    public org.opencompare.model.ImageValue getkImageValue() {
        return kImageValue;
    }


    public String getUrl() {
        return kImageValue.getUrl();
    }

    public void setUrl(String value) {
        kImageValue.setUrl(value);
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        Image copy = factory.createImageValue();
        copy.setUrl(this.getUrl());
        return copy;
    }


}
