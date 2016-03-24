package ngordnet;
import edu.princeton.cs.introcs.In;
import java.io.File;
import java.util.HashMap;
import java.util.Collection;
import java.util.Set;

//had debugging party with lynn ly(ays)
public class NGramMap {
    private HashMap<Integer, YearlyRecord> yearRecord;
    private HashMap<String, TimeSeries<Integer>> wordsHistory;
    private TimeSeries<Long> wordTotalByYear;
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        //initialize the HashMaps and timeSeries
        yearRecord = new HashMap<Integer, YearlyRecord>();
        wordsHistory = new HashMap<String, TimeSeries<Integer>>();
        wordTotalByYear = new TimeSeries<Long>();
        //make the files
        File wordsFile = new File(wordsFilename);
        File countsFile = new File(countsFilename);
        
        //make the yearRecord and wordsHistory maps
        useWordFile(wordsFile);
        
        //make the wordTotalByYear timeseries
        useCountsFile(countsFile);
        
    }
    
    private void useWordFile(File x) {
        In stream = new In(x);
        while (stream.exists() && stream.hasNextLine()) {
            String line = stream.readLine();
            String[] lineSplit = line.split("\t");
            String word = lineSplit[0];
            int year = Integer.parseInt(lineSplit[1]);
            int wordAppearance = Integer.parseInt(lineSplit[2]);
            
            //put word in the wordTimeSeries map
            // if wordsHistory does not contain the word:
            //  --> add the word and a new time series
            // if it already contains the word:
            //  --> get the associated time series and put(year, appearance) 
            if (!wordsHistory.containsKey(word)) {
                TimeSeries<Integer> newTimeSeries = new TimeSeries<Integer>();
                newTimeSeries.put(year, wordAppearance);
                wordsHistory.put(word, newTimeSeries);
            } else {
                TimeSeries<Integer> associatedTs = wordsHistory.get(word);
                associatedTs.put(year, wordAppearance);
            }
            // put the year in yearRecord
            // if yearRecord does not contain the year:
            //  --> add the year and a new YearlyRecord
            // if it already contains the word:
            //  --> get the associated yearlyRecord and put(word, appearance)
            if (!yearRecord.containsKey(year)) {
                YearlyRecord newYearlyRecord = new YearlyRecord();
                newYearlyRecord.put(word, wordAppearance);
                yearRecord.put(year, newYearlyRecord);
            } else {
                YearlyRecord associatedYr = yearRecord.get(year);
                associatedYr.put(word, wordAppearance);
            }
        }
    } 
    
    private void useCountsFile(File x) {
        In stream = new In(x);
        while (stream.exists() && stream.hasNextLine()) {
            String line = stream.readLine();
            String[] lineSplit = line.split(",");
            int year = Integer.parseInt(lineSplit[0]);
            Long totalWords = Long.parseLong(lineSplit[1]);
            Long totalWordsLong = new Long(totalWords);
            
            wordTotalByYear.put(year, totalWordsLong);
        }
    }  
        
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year) {
        if (wordsHistory.get(word) == null) {
            return 0;
        }   
        TimeSeries<Integer> tsWord = wordsHistory.get(word);
        if (tsWord.get(year) == null) {
            return 0;
        }   
        return tsWord.get(year).intValue(); 
    }

    /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year) {
        YearlyRecord returnRecord = new YearlyRecord();
        YearlyRecord associatedRecord = yearRecord.get(year);
        Collection<String> words = associatedRecord.words();
        for (String w : words) {
            returnRecord.put(w, associatedRecord.count(w));
        }
        return returnRecord;        
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        TimeSeries<Long> returnTs = new TimeSeries<Long>(wordTotalByYear);
        return returnTs;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        TimeSeries<Integer> input = wordsHistory.get(word);
        TimeSeries<Integer> returnTs = new TimeSeries<Integer>(input, startYear, endYear);
        return returnTs;       
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word) {
        TimeSeries<Integer> returnTs = new TimeSeries<Integer>(wordsHistory.get(word));
        return returnTs;
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        TimeSeries<Double> returnTs = new TimeSeries<Double>();
        TimeSeries<Integer> count = countHistory(word, startYear, endYear);
        returnTs = count.dividedBy(wordTotalByYear);
        return returnTs;
    }

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word) {
        TimeSeries<Integer> wordTs = wordsHistory.get(word);
        int firstYear = wordTs.firstKey();
        int lastYear = wordTs.lastKey();
        return weightHistory(word, firstYear, lastYear);
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. If a word does not exist, ignore it rather
      * than throwing an exception. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear) {
        TimeSeries<Double> returnTs = new TimeSeries<Double>();
        for (String w : words) {
            returnTs = returnTs.plus(weightHistory(w, startYear, endYear));
        }
        return returnTs;                          
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        TimeSeries<Double> returnTs = new TimeSeries<Double>();
        for (String w : words) {
            returnTs = returnTs.plus(weightHistory(w));
        }
        return returnTs;
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                               YearlyRecordProcessor yrp) {
        TimeSeries<Double> returnTs = new TimeSeries<Double>();  
        int year = startYear;
        while (year <= endYear) {
            if (yearRecord.containsKey(year)) {
                YearlyRecord associatedRecord = yearRecord.get(year);
                double result = yrp.process(associatedRecord);
                returnTs.put(year, result);
            }   
            year = year + 1;
        }
        return returnTs;
    }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> returnTs = new TimeSeries<Double>();
        Set<Integer> keys = yearRecord.keySet();
        for (Integer k : keys) {
            double result = yrp.process(yearRecord.get(k));
            returnTs.put(k, result);
        }
        return returnTs;
    }
}       

