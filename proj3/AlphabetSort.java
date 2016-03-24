import java.util.Scanner;
import java.util.ArrayList;

/** 
* Alphabet sorts, sorts things by a given alphabet
* @author Mondee
*/

public class AlphabetSort {
    
    /** 
    * Builds the Trie and prints the sorted results
    * @param args Takes in an alphabet and a list of words
    */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String order = sc.nextLine();
        if (!sc.hasNextLine()) {
            throw new IllegalArgumentException("No words or alphabet given.");
        }
        if (!checkForUnique(order)) {
            throw new IllegalArgumentException("A letter appears multiple times in the alphabet.");
        }
        
        ArrayList<String> wordsToSort = new ArrayList<String>();
        if (!sc.hasNextLine()) {
            throw new IllegalArgumentException("No words or alphabet given.");
        }
        while (sc.hasNextLine()) {
            wordsToSort.add(sc.nextLine());
        }
        Trie t = new Trie();
        for (String s : wordsToSort) {
            t.insert(s);    
        }
        sort(order, t.root(), "");
    }   
    
    
    //**********************************
    //***********Helper Functions*******
    //**********************************
    /**
	* checking if string has only unique integers
    * copied from:
    * stackoverflow.com/questions/19484406/detecting-if-a-
    * string-has-unique-characters-comparing-my-solution-to-cracking
    * @param str the alphabet   
    * @return containsUnique whether or not it's unique
    */
    private static boolean checkForUnique(String str) {
        boolean containsUnique = false;
        for (char c : str.toCharArray()) {
            if (str.indexOf(c) == str.lastIndexOf(c)) {
                containsUnique = true;
            } else {
                containsUnique = false;
            }
        }
        return containsUnique;
    }
    
    /** 
    * Prints the stuff
    * @param o the alphabet
    * @param n the starting node
    * @param word the current buildup of word
    */
    private static void sort(String o, Node n, String word) {
        for (int i = 0; i < o.length(); i += 1) {
            char c = o.charAt(i);
            if (n.links().containsKey(c)) {
                if (n.links().get(c).fullWord()) {
                    System.out.println(word + c);
                }
                sort(o, n.links().get(c), word + c);
            }         
        }
    }
}
 