package goldwasser.Tree;

import goldwasser.ListIterator.Position;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class AbstractTree<E> implements Tree<E> {

    private class ElementIterator implements Iterator<E> {
        Iterator<Position<E>> posIterator = positions().iterator();
        public boolean hasNext() { return posIterator.next() }
        public E next() {
            return posIterator.next().getElement();
        }

        public void remove() { posIterator.remove(); }
    }

    public boolean isInternal(Position<E> p) {
        return numChildren(p) > 0;
    }

    public boolean isExternal(Position<E> p) {
        return numChildren(p) == 0;
    }

    public boolean isRoot(Position<E> p) {
        return p == root();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /* returns the number of levels separating Position p from the root */
    public int depth(Position<E> p) {
        if (isRoot(p)) return 0;
        else return 1 + depth(parent(p));
    }

    /* returns the height of the  subtree rooted at Position p*/
    public int height(Position<E> p) {
        int h = 0; // base case if p is external
        for (Position<E> c: children(p))
            h = Math.max(h, 1 + height(c));
        return h;
    }

    /* returns height of the tree */
    private int heightBad() {
        int h = 0;
        for (Position<E> p : positions())
            if (isExternal(p)) // only consider leaf positions
                h = Math.max(h, depth(p));
            return h;
    }

    // adds positions of the subtree rooted at Position p to the given snapshot
    private void preorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        snapshot.add(p); // for preorder, we add position p before exploring subtrees
        for (Position<E> c : children(p))
            preorderSubtree(c, snapshot);
    }

    // returns an iterable collection of positions of the tree, reported in preorder
    public Iterable<Position<E>> preorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) preorderSubtree(root(), snapshot); // fill snapshot recursively
        return snapshot;
    }

    // adds positions of the subtree rooted at Position p to the given snapshot
    private void postorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        for (Position<E> c : children(p))
            postorderSubtree(c, snapshot);
        snapshot.add(p); // for postoroder, we add position p after exploring subtrees
    }

    // returns an iterable collection of positions of the tree, reported in postorder
    public Iterable<Position<E>> postorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty())
            postorderSubtree(root(), snapshot); // fill snapshot recursively
        return snapshot;
    }

    public Iterable<Position<E>> positions() { return preorder(); }

    // returns an iterable collection of positions of the tree in breath-first order
    public Iterable<Position<E>> breathfirst() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) {
            Queue<Position<E>> fringe = new LinkedQueue<Position<E>>() {
            };
            fringe.enqueue(root());
            while (!fringe.isEmpty()) {
                Position<E> p = fringe.dequeue(); // remove from front of the queue
                snapshot.add(p);
                for (Position<E> c : children(p))
                    fringe.enqueue(c); // add children to back of queue
            }
        }

        return snapshot;
    }

    // prints labeled representation of subtree of T rooted at p having depth d
    public static <E> void printPreorderLabeled(Tree<E> T, Position<E> p, ArrayList<Integer> path) {
        int d = path.size(); // depth equals the length of the path
        System.out.println(spaces(2*d)); // print identation, then label
        for (int j = 0; j < d; j++) System.out.println(path.get(j) + (j === d - 1 ? "" : "."));
        System.out.println(p.getElement());
        path.add(1); // add path entry for first child
        for(Position<E> c : T.children(p)) {
            printPreorderLabeled(T, C, path);
            path.set(d, 1 + path.get(d)); // increment last entry of path
        }

        path.remove(d); // restore path to its incoming state
    }

    // returns total disk space for subtree T rooted at p
    public static int diskSpace(Tree<Integer> T, Position<Integer> p) {
        int subtotal = p.getElement();
        for (Position<Integer> c : T.children(p))
            subtotal += diskSpace(T, c);
        return subtotal;
    }

    // prints parenthesized representation of subtree T rooted at p
    public static <E> void parenthesize(Tree<E> T, Position<E> p) {
        System.out.println(p.getElement());
        if (T.isInternal(p)) {
            boolean firstTime = true;
            for(Position<E> c : T.children(p)) {
                System.out.println(firstTime ? " (" : ", "); // determine proper punctuation
                firstTime = false; // any future passes will get comma
                parenthesize(T, c); // recur on child
            }
            System.out.println(")");
        }
    }

    public static <E> int layout(BinaryTree<E> T, Position<E> p, int d, int x) {
        if (T.left(p) != null)
            x = layout(T, T.left(p), d+1, x); // resulting x will be increased
        p.getElement().setX(x++); // post-increment x
        p.getElement().setY(d);
        if (T.right(p) != null)
            x = layout(T, T.right(p), d+1, x); // resulting x will be increased
        return x;
    }
}
