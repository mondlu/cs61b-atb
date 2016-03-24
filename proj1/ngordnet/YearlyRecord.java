package ngordnet;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
  
public class YearlyRecord {
    private TreeMap<String, Integer> stringNum;
    private TreeMap<Integer, ArrayList<String>> numString;
    private TreeMap<String, Integer> rankedStringNum;
    
    private boolean needToSort = false;
    
    /** Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        stringNum = new TreeMap<String, Integer>();
        numString = new TreeMap<Integer, ArrayList<String>>();
        rankedStringNum = new TreeMap<String, Integer>();
    }

    /** Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) { 
        //intialize the ranked string map
        rankedStringNum = new TreeMap<String, Integer>();
        //create (String, Integer) map
        stringNum = new TreeMap<String, Integer>();
        stringNum.putAll(otherCountMap);
        
        //create (Integer, List) map
        numString = new TreeMap<Integer, ArrayList<String>>();
        //get the word keys in other count map
        Set<String> otherKeys = otherCountMap.keySet();
        //for each word: 
        // (1) get the integer
        // (2) if the integer is not a key in numString 
        //      --> create a new wordList array amd add the word
        // (3) if the integer is a key in numString 
        //      --> get the associated wordList Array and add the word
        for (String s : otherKeys) {
            Integer value = otherCountMap.get(s);
            if (!numString.containsKey(value)) {
                ArrayList<String> wordList = new ArrayList<String>();
                wordList.add(s);
                numString.put(value, wordList);
            } else {
                List<String> wordList = numString.get(value);
                wordList.add(s);
            }
        }
    }

    /** Returns the number of times WORD appeared in this year. */
    public int count(String word) {
        if (!stringNum.containsKey(word)) {
            throw new IllegalArgumentException("given word does not exist in map");
        } else {
            return stringNum.get(word);
        }   
    } 

    /** Records that WORD occurred COUNT times in this year. */
    public void put(String word, int count) {
        // add to numString:
        // (1) check if the word is a key in stringNum 
        // if yes:
        //      --> remove the count-string pair from numString
        //      --> add the new count-string pair to numString
        // if no:
        //      --> check if numString has the int already
        //      --> if yes, make Array, add the word to array, put in map
        //      --> if no, get associated array, add the word
        if (stringNum.containsKey(word)) {
            Integer originalCount = stringNum.get(word);
            ArrayList<String> stringOriginalList = numString.get(originalCount);
            if (stringOriginalList.size() == 1) {
                numString.remove(originalCount);
            } else {
                stringOriginalList.remove(word);
            }   
            if (!numString.containsKey(count)) {
                ArrayList<String> wordList = new ArrayList<String>();
                wordList.add(word);
                numString.put(count, wordList);
            } else {
                ArrayList<String> wordList = numString.get(count);
                wordList.add(word);
            }
        } else {
            stringNum.put(word, count);
            if (!numString.containsKey(count)) {
                ArrayList<String> wordList = new ArrayList<String>();
                wordList.add(word);
                numString.put(count, wordList);
            } else {
                ArrayList<String> wordList = numString.get(count);
                wordList.add(word);
            }
        }
        stringNum.put(word, count);
        //indicate that the rankedMap needs to be re-ranked
        needToSort = true;
    }

    /** Returns the number of words recorded this year. */
    public int size() {
        return stringNum.size();
    }   

    /** Returns all words in ascending order of count. */
    public Collection<String> words() {
        // make an ArrayList returnList that will be cast into a collection   
        ArrayList<String> returnList = new ArrayList<String>();
        // Get the integer kets of the numString TreeMap
        Set<Integer> keys = numString.keySet();
        // for each integer: 
        // (1) get the list of strings associated with the integer
        // (2) add the strings to the return list
        for (Integer k : keys) {
            ArrayList<String> wordList = numString.get(k);
            for (String s : wordList) {
                returnList.add(s);
            }
        }
        return returnList;  
    }
    

    /** Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        //get the integer keys in the numString tree
        Set<Integer> keys = numString.keySet();
        //Make an integer array and add the integer keys to the array list
        ArrayList<Integer> ascendingCount = new ArrayList<Integer>();
        for (Integer n : keys) {
            ascendingCount.add(n);
        }
        // cast the array of integers
        ArrayList<Number> ascendingCountArray = new ArrayList<Number>();
        for (Integer i: ascendingCount) {
            ascendingCountArray.add(i);
        }
        Collection<Number> returnCollection = ascendingCountArray;
        return returnCollection;
    } 

    /** Returns rank of WORD. Most common word is rank 1. 
        * If two words have the same rank, break ties arbitrarily. 
        * No two words should have the same rank.
        */
    public int rank(String word) {
        if (!needToSort) {
            return rankedStringNum.get(word);
        } else {
            rankMap();
            needToSort = false;
            return rankedStringNum.get(word);
        }
    }
    private void rankMap() {
        //get the set of Integer keys from numString
        Set<Integer> countSorted = numString.keySet();
        // start the rank at the number of available strings
        // for each integer:
        // (1) for each array associated with the integer:
        //      (2) for each string in the array:
        //          (3) put the word and the rank to the rankedStringNum treemap
        //          (4) decrement rank
        int rank = stringNum.size();
        for (Integer c : countSorted) {
            ArrayList<String> wordArray = numString.get(c);
            for (String s : wordArray) {
                rankedStringNum.put(s, rank);
                rank = rank - 1;
            }            
        }
    }
}
