package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @Mondeee [yournamehere mcjones]
 */
public class NgordnetUI {
    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");

        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile +
                           ", and " + hyponymFile + ".");
        
        NGramMap nGramMap = new NGramMap(wordFile, countFile);
        WordNet wordNet = new WordNet(synsetFile, hyponymFile);
        TimeSeries<Long> totalTime = nGramMap.totalCountHistory();
        
        int startDate = totalTime.firstKey().intValue();
        int endDate = totalTime.lastKey().intValue();
         
        while (true) {
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            switch (command) {
                case "quit": 
                    return;
                case "help":
                    In inHelp = new In("help.txt");
                    String helpStr = inHelp.readAll();
                    System.out.println(helpStr);
                    break;  
                case "range": 
                    startDate = Integer.parseInt(tokens[0]); 
                    endDate = Integer.parseInt(tokens[1]);
                    System.out.println("Start date: " + startDate);
                    System.out.println("End date: " + endDate);
                    break;
                case "count":
                    String word = tokens[0];
                    int year = Integer.parseInt(tokens[1]);
                    System.out.println(nGramMap.countInYear(word, year));
                    break;
                case "hyponyms":
                    String hypoWord = tokens[0];
                    System.out.println(wordNet.hyponyms(hypoWord));
                    break;
                case "history":
                    try {
                        Plotter.plotAllWords(nGramMap, tokens, startDate, endDate);
                    }   
                    catch (IllegalArgumentException e) {
                        System.out.println("Word does not exist");
                    }   
                    break;
                case "hypohist":
                    Plotter.plotCategoryWeights(nGramMap, wordNet, tokens, startDate, endDate);
                    break;
                case "wordlength":
                    YearlyRecordProcessor processor = new WordLengthProcessor();
                    Plotter.plotProcessedHistory(nGramMap, startDate, endDate, processor);
                    break;
                case "zipf":
                    int time = Integer.parseInt(tokens[0]);
                    Plotter.plotZipfsLaw(nGramMap, time);
                    break;
                default:
                    System.out.println("Invalid command.");  
                    break;
                }
            }   
       } 
} 
