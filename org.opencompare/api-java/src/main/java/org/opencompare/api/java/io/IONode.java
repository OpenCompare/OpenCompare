package org.opencompare.api.java.io;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by smangin on 7/10/15.
 */
public class IONode extends DefaultMutableTreeNode {

    private int position = 0;

    public IONode(Object userObject) {
        super(userObject);
    }

    public IONode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }

    public IONode(Object userObject, boolean allowsChildren, int position) {
        super(userObject, allowsChildren);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return (String) this.getUserObject();
    }

    @Override
    public IONode getFirstLeaf() {
        return (IONode) super.getFirstLeaf();
    }

    @Override
    public IONode getLastLeaf() {
        return (IONode) super.getLastLeaf();
    }

    @Override
    public IONode getNextLeaf() {
        return (IONode) super.getNextLeaf();
    }

    @Override
    public IONode getPreviousLeaf() {
        return (IONode) super.getPreviousLeaf();
    }

    @Override
    public IONode getNextNode() {
        return (IONode) super.getNextNode();
    }

    @Override
    public IONode getNextSibling() {
        return (IONode) super.getNextSibling();
    }

    @Override
    public IONode getPreviousNode() {
        return (IONode) super.getPreviousNode();
    }

    @Override
    public IONode getPreviousSibling() {
        return (IONode) super.getPreviousSibling();
    }

    public List<IONode> iterable() {
        return Collections.list(children());
    }

    @Override
    public boolean equals(Object obj) {
        // TODO : check for entire tree if not too much
        if (this == obj) {
            return true;
        }
        if (obj != null && obj instanceof IONode) {
            IONode node = (IONode) obj;
            if (node.getDepth() != getDepth()) {
                return false;
            }
            if (node.getSiblingCount() != getSiblingCount()) {
                return false;
            }
            if (node.getLeafCount() != getLeafCount()) {
                return false;
            }
            if (node.getLevel() != getLevel()) {
                return false;
            }
            if (node.getPosition() != getPosition()) {
                return false;
            }
            IONode leaf = node.getNextLeaf();
            IONode refLeaf = getNextLeaf();
            while (leaf != null && refLeaf != null) {
                if (!leaf.equals(refLeaf)) {
                    return false;
                }
                leaf = node.getNextLeaf();
                refLeaf = getNextLeaf();

            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getName().hashCode() + position;
    }
}
