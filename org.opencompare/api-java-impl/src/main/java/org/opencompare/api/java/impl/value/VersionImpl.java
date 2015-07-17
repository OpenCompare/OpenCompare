package org.opencompare.api.java.impl.value;

import org.opencompare.api.java.PCMElement;
import org.opencompare.api.java.PCMFactory;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.Version;
import pcm.Value;

/**
 * Created by gbecan on 30/01/15.
 */
public class VersionImpl extends ValueImpl implements Version {

    private pcm.Version kVersion;

    public VersionImpl(pcm.Version kVersion) {
        super(kVersion);
        this.kVersion = kVersion;
    }

    public pcm.Version getkVersion() {
        return kVersion;
    }

    @Override
    public void accept(PCMVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public PCMElement clone(PCMFactory factory) {
        Version copy = factory.createVersion();
        return copy;
    }
}
