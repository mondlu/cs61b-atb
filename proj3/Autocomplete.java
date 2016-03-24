import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeSet;
/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 * @author Mondee
 */
public class Autocomplete {
    
    private TSTSet tst;
    private HashMap<String, Double> termWeights;  
    
    /**
     * Initializes required data structures from parallel arrays.
     * @param terms Array of terms.
     * @param weights Array of weights.
     */  
    
    public Autocomplete(String[] terms, double[] weights) {
        if (terms.length != weights.length) {
            throw new IllegalArgumentException("inputs not the same lengths.");
        }
        if (duplicateTerms(terms)) {
            throw new IllegalArgumentException("cannot have duplicate terms");
        }
        if (negativeWeights(weights)) {
            throw new IllegalArgumentException("cannot have negative weights");
        }
        tst = new TSTSet();
        for (int i = 0; i < terms.length; i += 1) {
            String word = terms[i];
            Double weight = weights[i];
            tst.put(word, weight);
        }    
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     * @param term is the word that is being searched
     * @return the weight of the word
     */
    public Double weightOf(String term) {
        return tst.get(term);
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return returnString Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        ArrayList<String> matches = (ArrayList<String>) topMatches(prefix, 1);
        if (matches == null) {
            return null;
        }
        if (matches.size() < 1) {
            return null;
        }
        return matches.get(0);
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     * @param prefix the str for which top matches are being found
     * @param k is the number of topMatches you want
     * @return an interable list of the k matches
     */
    public Iterable<String> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException("k is negative");
        }
        if (prefix == null) {
            throw new IllegalArgumentException("prefix is null");
        }
        TernaryNodeComparator comparator = new TernaryNodeComparator();
        TernaryValueComparator comparator2 = new TernaryValueComparator();
        TreeSet<TernaryNode> queue = new TreeSet<TernaryNode>(comparator2);
        ArrayList<String> returnList = new ArrayList<String>();

        TreeSet<TernaryNode> orderTraverse = new TreeSet<TernaryNode>(comparator);
        TernaryNode n = tst.get(tst.root(), prefix, 0);
        if (n == null) {
            return returnList;
        }
        TernaryNode nMid = n.middle();
        TernaryNode maxInsert = new TernaryNode();
        maxInsert.setMax(0.0);
        maxInsert.setVal(0.0);
        if (nMid == null) {
            if (n.val() != null) {
                returnList.add(prefix);
            }
        } else {
            Double max;
            TernaryNode start;
            max = n.middle().max();
            start = n;
            if (prefix.equals("")) {
                collect(start, queue, k, max, maxInsert, orderTraverse);
            } else {
                collectFirst(start, queue, k, max, maxInsert, orderTraverse);
            }
            while (!queue.isEmpty() && k >= 0) {
                returnList.add(queue.pollLast().fullString());
                k -= 1;
            }
        }   
        return returnList;
    }

    /**
     * Returns the highest weighted matches within k edit distance of the word.
     * If the word is in the dictionary, then return an empty list.
     * @param word The word to spell-check
     * @param dist Maximum edit distance to search
     * @param k    Number of results to return 
     * @return Iterable in descending weight order of the matches
     */
    public Iterable<String> spellCheck(String word, int dist, int k) {
        LinkedList<String> results = new LinkedList<String>();  
        /* YOUR CODE HERE; LEAVE BLANK IF NOT PURSUING BONUS */
        return results;
    }
    /**
     * Test client. Reads the data from the file, 
     * then repeatedly reads autocomplete queries from standard input and prints out 
     * the top k matching terms.
     * @param args takes the name of an input file and an integer k as command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);
        //process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k))
            StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
        }
    }
    
    //*******************************************************
    //******************Helper Functions*********************
    //*******************************************************
    
    /**
     * reports if there are duplicate terms in the string array
     * @param words is an array of strings
     * @return boolean is false if no duplicates, true if there are
     */
    private boolean duplicateTerms(String[] words) {
        HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
        for (String s: words) {
            if (wordCount.containsKey(s)) {
                int count = wordCount.get(s);
                count += 1;
                wordCount.put(s, count);
            } else {
                int count = 1;
                wordCount.put(s, count);
            }
        }
        Collection<Integer> numbers = wordCount.values();
        for (int n : numbers) {
            if (n > 1) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * reports if there are negative weights in the list array
     * @param list if a list of weights
     * @return boolean is false if no negatives, true if there are
     */
    private boolean negativeWeights(double[] list) {
        for (Double d : list) {
            if (d < 0.0) {
                return true;
            }
        }
        return false;
    }
    
    /**
    * called in topMatches
    * adapted from Algorithms textbook pg 738
    * @param x node to start at
    * @param q queue of words to return
    * @param k number of top words
    * @param max the max of x
    * @param maxInsert the number x.val must be higher than to be included
    * @param orderTraverse list of nodes to be travelled, in order by weight
    */
    private void collectFirst(TernaryNode x, TreeSet<TernaryNode> q, int k, Double max, TernaryNode maxInsert, 
        TreeSet<TernaryNode> orderTraverse) {
        if (x == null) {
            return;
        }    
        if (x.val() != null) {
            if (x.val() > maxInsert.max()) {
                q.add(x);
                if (q.size() > k) {
                    maxInsert = q.pollFirst();
                }
            }       
        }
        if (maxInsert.val() > x.max()) {
            return;
        }
        if (x.middle() != null) {   
            Double xMiddle = x.middle().max();
            if (xMiddle > maxInsert.val()) {
                orderTraverse.add(x.middle());
            }
        }
        while (!orderTraverse.isEmpty()) {
            TernaryNode w = orderTraverse.pollLast();   
            collect(w, q, k, w.max(), maxInsert, orderTraverse);
        }
    }
    
    /**
    * called in topMatches
    * adapted from Algorithms textbook pg 738
    * @param x node to start at
    * @param q queue of words to return
    * @param k number of top words
    * @param max the max of x
    * @param maxInsert the number x.val must be higher than to be included
    * @param orderTraverse list of nodes to be travelled, in order by weight
    */
    private void collect(TernaryNode x, TreeSet<TernaryNode> q, int k, Double max, TernaryNode maxInsert, 
        TreeSet<TernaryNode> orderTraverse) {
        if (x == null) {
            return;
        }    
        if (x.val() != null) {
            if (x.val() > maxInsert.val()) {
                q.add(x);
                if (q.size() > k) {
                    maxInsert = q.pollFirst();
                }
            }       
        }
        if (maxInsert.val() <= x.max()) {
            if (x.left() != null) {
                Double xLeft = x.left().max();
                if (xLeft > maxInsert.val()) {
                    orderTraverse.add(x.left());
                }
            }
            if (x.right() != null) {    
                Double xRight = x.right().max();
                if (xRight > maxInsert.val()) {
                    orderTraverse.add(x.right());
                }
            }
            if (x.middle() != null) {   
                Double xMiddle = x.middle().max();
                if (xMiddle > maxInsert.val()) {
                    orderTraverse.add(x.middle());
                }
            }
        }
        while (!orderTraverse.isEmpty()) {
            TernaryNode w = orderTraverse.pollLast();   
            collect(w, q, k, w.max(), maxInsert, orderTraverse);
        }
    }
    
    /**
    * comparator to compare node weights
    */
    private class TernaryValueComparator implements Comparator<TernaryNode> {
        @Override
        public int compare(TernaryNode t1, TernaryNode t2) {
            if (t1.val().compareTo(t2.val()) == 0) {
                return 1;
            }    
            return t1.val().compareTo(t2.val());
        }
    }
    
    /**
    * comparator to compare nodes
    */
    private class TernaryNodeComparator implements Comparator<TernaryNode> {
        @Override
        public int compare(TernaryNode t1, TernaryNode t2) {
            if (t1.max().compareTo(t2.max()) == 0) {
                return 1;
            }    
            return t1.max().compareTo(t2.max());
        }
    } 
}
