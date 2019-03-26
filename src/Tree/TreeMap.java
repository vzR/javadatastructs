package Tree;

import ListIterator.Position;

import java.util.Map;

public class TreeMap<K, V> extends AbstractSortedMap<K,V> {

    // to represent the underlying tree structure, we use a specialized subclass of the
    // LinkedBinaryTree class that we name BalanceableBinaryTree
    protected BalanceableBinaryTree<K, V> tree = new BalanceableBinaryTree<>();

    // constructs an empty map using the natural ordering of keys
    public TreeMap() {
        super(); // the abstractSortedMap constructor
        tree.addRoot(null); // create a sentinel leaf as root
    }

    // returns the number of entries in the map
    public int size() {
        return (tree.size() - 1) / 2; // only internal nodes have entries
    }

    // utility used when inserting a new entry at leaf of the tree
    private void expandExternal(Position<Entry<K, V>> p, Entry<K, V> entry) {
        tree.set(p, entry); // store new entry at p
        tree.addLeft(p, null); // add new sentinel leaves as children
        tree.addRight(p, null);
    }

    // missing code-- series of protected methods tha tprovide notational shorthands
    // to wrap operations on the underlying linked binary tree. For example
    // we support the protected syntax root() as shorthand for tree.root() with following
    // utility
    protected Position<Entry<K, V>> root() { return tree.root(); }

    // returns the position in p's subtree having given key (or else the terminal leaf)
    private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> p, K key) {
        if (isExternal(p)) return p; // key not found; return final leaf
        int comp = compare(key, p.getElement());
        if (comp == 0)
            return p; // key found; return its position
        else if (comp < 0)
            return treeSearch(left(p), key); // search left subtree
        else
            return treeSearch(right(p), key); // search right subtree
    }

    // returns the value associated with the specified key (or else null)
    public V get(K key) throws IllegalArgumentException {
        checkKey(key); // may throw illegal argumentexception
        Position<Entry<K, V>> p = treeSearch(root(), key);
        rebalanceAccess(p);
        if (isExternal(p)) return null; // unsuccessul search
        return p.getElement().getValue(); // match found
    }

    // associates the given value with the given key, returnin any overridden value
    public V put(K key, V value) throws IllegalArgumentException {
        checkKey(key); // may throw illegalargumentexception
        Map.Entry<K, V> newEntry = new Map.AbstractMap.MapEntry<>(key, value);
        Position<Entry<K, V>> p = treeSearch(root(), key);
        if (isExternal(p)) {
            expandExternal(p, newEntry);
            rebalanceInsert(p); // hool for balanced tree subclasses
            return null;
        } else {
            V old = p.getElement().getValue();
            set(p, newEntry);
            rebalanceAccess(p);
            return old;
        }
    }

    // removes the entry having key k (if any) and returns its associated value
    public V remove(K key) throws IllegalArgumentException {
        checkKey(key); // may throw IllegalArgumentException
        Position<Entry<K, V>> p = treeSearch(root(), key);
        if (isExternal(p)) {
            rebalanceAccess(p);
            return null;
        }
        else {
            V old = p.getElement().getValue();
            if (isInternal(left(p)) && isInternal(right(p))) {
                Position<Entry<K, V>> replacement = treeMap(left(p));
                set(p, replacement.getElement());
                p = replacement;
            }
            // now p has at most one chald that is an internal node
        }
        position<Entry<K, V>> leaf = (isExternal(left(p)) ? left(p) : right(p));
        Position<Map.Entry<K, V>> sib = sibling(leaf);
        remove(leaf);
        remove(p);
        rebalanceDelete(sib);
        return old;
    }
}
