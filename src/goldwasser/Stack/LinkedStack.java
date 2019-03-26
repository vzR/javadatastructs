package goldwasser.Stack;

public class LinkedStack<E> implements Stack<E> {
    private SinglyLinkedList<E> list = new SinglyLinkedList<>(); // an empty list
    public LinkedStack() {}
    public int size() { return list.size(); }
    public boolean isEmpty() { return list.isEmpty(); }
    public void push(E element) {
        list.addFirst(element);
    }
    public E top() { return list.first(); }
    public E pop() { return list.removeFirst(); }

    public static boolean isMatched(String expression) {
        final String opening = "({["; // opening delimiters
        final String closing = ")}]"; // respective closing delimiters
        Stack<Character> buffer = new LinkedStack<>();

        for (char c : expression.toCharArray()) {
            if (opening.indexOf(c) != -1)
                buffer.push(c); // this i a left delimiter
            else if (closing.indexOf(c) != -1) {
                if (buffer.isEmpty())
                    return false;
                if (closing.indexOf(c) != opening.indexOf(buffer.pop()))
                    return false; // mismatched delimiter
            }
        }
        return buffer.isEmpty(); // were all opening delimiter matched?
    }

    public static boolean isHTMLMatches(String html) {
        Stack<String> buffer = new LinkedStack<>();
        int j = html.indexOf('<');
        while (j != -1) {
            int k = html.indexOf('>', j + 1); // find next '>' character
            if (k == -1)
                return false; // invalid tag
            String tag = html.substring(j + 1, k); // strip away <>
            if (!tag.startsWith("/")) // this is an opening tag
                buffer.push(tag);
            else {
                if (buffer.isEmpty())
                    return false; // no tag to match
                if (!tag.substring(1).equals(buffer.pop()))
                    return false; // mismatched tag
            }
            j = html.indexOf('<', k + 1); // find next '<' char (if any)
        }
        return buffer.isEmpty(); // were all opening tags matched?
    }
}
