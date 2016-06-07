package org.opencompare.api.java.io;

import java.util.*;

/**
 * Created by smangin on 7/10/15.
 */
public class IONode<T> {

    private T content;
    private List<IONode<T>> children = new ArrayList<>();
    private Set<Integer> positions = new HashSet<>();

    public IONode(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public List<IONode<T>> getChildren() {
        return children;
    }

    public Set<Integer> getPositions() {
        return positions;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public List<IONode<T>> getLeaves() {
        List<IONode<T>> result = new ArrayList<>();
        if (isLeaf()) {
            result.add(this);
        } else {
            for (IONode<T> child : children) {
                result.addAll(child.getLeaves());
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IONode<?> ioNode = (IONode<?>) o;

        if (content != null ? !content.equals(ioNode.content) : ioNode.content != null) return false;
        return !(children != null ? !children.equals(ioNode.children) : ioNode.children != null);

    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IONode{" +
                "content=" + content +
//                ", children=" + children +
                '}';
    }
}
