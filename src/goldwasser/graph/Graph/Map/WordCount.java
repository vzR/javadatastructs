package goldwasser.graph.Graph.Map;

import java.util.Scanner;

public class WordCount {

    public static void main(String[] args) {
        Map<String, Integer> freq = new ChainHashMap<>(); // or anay concrete map
        // scan input for words, using all nonletters as delimites
        Scanner doc = new Scanner(System.in).useDelimiter("[^a-zA-Z]+");
        while (doc.hasNext()) {
            String word = doc.next().toLowerCase(); // convert next word to lower case
            Integer count = freq.get(word); // get the previous count for this word
            if (count == null)
                count = 0; // if not in map, previous count is zero
            freq.put(word, 1 + count);
        }

        int maxCount = 0;
        String maxWord = "no word";
        for (java.util.Map.Entry<String, Integer> ent : freq.entrySet())
            if (ent.getValue() > maxCount) {
                maxWord = ent.getKey();
                maxCount = ent.getValue();
            }

        System.out.println("the most frequent word is " + maxWord);
        System.out.println("with " + maxCount + " occurrences");
    }
}
