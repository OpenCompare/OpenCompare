package org.diverse.pcm.api.java.impl.value;

import org.diverse.pcm.api.java.impl.ValueImpl;
import org.diverse.pcm.api.java.util.PCMVisitor;
import org.diverse.pcm.api.java.value.Version;
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
}
