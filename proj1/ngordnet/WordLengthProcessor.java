package ngordnet;
import java.util.Collection;

public class WordLengthProcessor implements YearlyRecordProcessor {
    public double process(YearlyRecord yearlyRecord) {
        Long totalLength = 0l;
        Collection<String> theWords = yearlyRecord.words();
        for (String w : theWords) {
            totalLength = w.length() + totalLength;
        }
        Long avgLength = totalLength / theWords.size();
        return avgLength.doubleValue(); 
    }
}