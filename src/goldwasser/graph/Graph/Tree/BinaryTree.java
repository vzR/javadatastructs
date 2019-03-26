package goldwasser.graph.Graph.Tree;

import goldwasser.graph.Graph.ListIterator.Position;

public interface BinaryTree<E> extends Tree<E> {
    /*returns the position of p's left child (or null if no child exists*/
    Position<E> left (Position<E> p) throws IllegalArgumentException;
    Position<E> right (Position<E> p) throws IllegalArgumentException;
    Position<E> sibling(Position<E> p) throws IllegalArgumentException;
}
