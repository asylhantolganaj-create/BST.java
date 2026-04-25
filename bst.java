import java.util.*;

// simple BST (no recursion)
public class BST<K extends Comparable<K>, V> implements Iterable<BST.Entry<K, V>> {

    private Node root;
    private int size = 0;

    // node
    private class Node {
        K key;
        V val;
        Node left, right;

        Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    // entry (for iteration)
    public static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
    }

    // size
    public int size() {
        return size;
    }

    // put
    public void put(K key, V val) {
        if (root == null) {
            root = new Node(key, val);
            size++;
            return;
        }

        Node cur = root;
        Node parent = null;

        while (cur != null) {
            parent = cur;

            if (key.compareTo(cur.key) < 0)
                cur = cur.left;
            else if (key.compareTo(cur.key) > 0)
                cur = cur.right;
            else {
                cur.val = val; // update
                return;
            }
        }

        if (key.compareTo(parent.key) < 0)
            parent.left = new Node(key, val);
        else
            parent.right = new Node(key, val);

        size++;
    }

    // get
    public V get(K key) {
        Node cur = root;

        while (cur != null) {
            if (key.compareTo(cur.key) < 0)
                cur = cur.left;
            else if (key.compareTo(cur.key) > 0)
                cur = cur.right;
            else
                return cur.val;
        }

        return null;
    }

    // delete (simple version)
    public void delete(K key) {
        Node parent = null;
        Node cur = root;

        while (cur != null && !cur.key.equals(key)) {
            parent = cur;

            if (key.compareTo(cur.key) < 0)
                cur = cur.left;
            else
                cur = cur.right;
        }

        if (cur == null) return;

        // 1 child or 0
        if (cur.left == null || cur.right == null) {
            Node newNode;

            if (cur.left == null)
                newNode = cur.right;
            else
                newNode = cur.left;

            if (parent == null)
                root = newNode;
            else if (parent.left == cur)
                parent.left = newNode;
            else
                parent.right = newNode;
        }
        else {
            // 2 children
            Node succParent = cur;
            Node succ = cur.right;

            while (succ.left != null) {
                succParent = succ;
                succ = succ.left;
            }

            cur.key = succ.key;
            cur.val = succ.val;

            if (succParent.left == succ)
                succParent.left = succ.right;
            else
                succParent.right = succ.right;
        }

        size--;
    }

    // iterator (in-order)
    public Iterator<Entry<K, V>> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<Entry<K, V>> {
        Stack<Node> stack = new Stack<>();

        MyIterator() {
            Node cur = root;
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public Entry<K, V> next() {
            Node node = stack.pop();

            Node cur = node.right;
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }

            return new Entry<>(node.key, node.val);
        }
    }
}
