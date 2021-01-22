package cn.devlab.consistent.hash;

public class VirtualNode<T extends Node> implements Node {

    final T realNode;

    final int index;

    public VirtualNode(T realNode, int index) {
        this.realNode = realNode;
        this.index = index;
    }

    @Override
    public String getKey() {
        return realNode.getKey() + "-" + index;
    }

    public boolean isVirtualNodeOf(T node) {
        return realNode.getKey().equals(node.getKey());
    }

    public T getRealNode() {
        return realNode;
    }
}
