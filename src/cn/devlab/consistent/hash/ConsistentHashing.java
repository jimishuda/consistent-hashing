package cn.devlab.consistent.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashing<T extends Node> {
    private final SortedMap<Long, VirtualNode<T>> ring = new TreeMap<>();
    private HashFunction hashFunction;


    public ConsistentHashing(Collection<T> pNodes, int vNodeCount) {
        this(pNodes,vNodeCount, new MD5Hash());
    }

    public ConsistentHashing(Collection<T> nodes, int virtualCnt, HashFunction hf) {

        this.hashFunction = hf;
        for (T node : nodes) {
            addNode(node, virtualCnt);
        }
    }

    private void addNode(T node, int virtualCnt) {

        int replicas = getExistingReplicas(node);
        for (int i = 0; i < virtualCnt; i++) {
            VirtualNode<T> virtualNode = new VirtualNode<>(node, i + replicas);
            ring.put(hashFunction.hash(virtualNode.getKey()), virtualNode);
        }
    }


    public void removeNode(T realNode) {
        Iterator<Long> it = ring.keySet().iterator();
        while (it.hasNext()) {
            Long key = it.next();
            VirtualNode<T> virtualNode = ring.get(key);
            if (virtualNode.isVirtualNodeOf(realNode)) {
                it.remove();
                ;
            }
        }
    }


    public T rout(String key) {
        if (ring.isEmpty()) {
            return null;
        }

        Long hash = hashFunction.hash(key);
        SortedMap<Long, VirtualNode<T>> tailMap = ring.tailMap(hash);
        Long nodeKey = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
        return ring.get(nodeKey).getRealNode();
    }

    public int getExistingReplicas(T node) {
        int replicas = 0;
        for (VirtualNode<T> virtualNode : ring.values()) {
            if (virtualNode.isVirtualNodeOf(node)) {
                replicas++;
            }
        }
        return replicas;
    }


    private static class MD5Hash implements HashFunction {
        MessageDigest instance;

        public MD5Hash() {
            try {
                instance = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
            }
        }

        @Override
        public long hash(String key) {
            instance.reset();
            instance.update(key.getBytes());
            byte[] digest = instance.digest();

            long h = 0;
            for (int i = 0; i < 4; i++) {
                h <<= 8;
                h |= ((int) digest[i]) & 0xFF;
            }
            return h;
        }
    }
}
