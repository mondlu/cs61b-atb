package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;


public class WordNetTest {
    @Test
    public void testBasic() {
        WordNet test = new WordNet("./p1data/wordnet/synsets14.txt", "./p1data/wordnet/hyponyms14.txt");
        assertEquals(false, test.isNoun("dummy"));
        assertEquals(true, test.isNoun("action"));
        assertEquals(true, test.isNoun("change"));
        assertEquals(true, test.isNoun("variation"));
        assertEquals(true, test.isNoun("human_action"));    
        //System.out.print(test.nouns());
        //System.out.print(test.hyponyms("act"));
            
    }
    @Test
    public void testTimeSeries(){
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1779, 15.3);
        Collection<Number> years = ts.years();
        assertTrue(years.contains(1992));
        assertTrue(years.contains(1993));
        assertTrue(years.contains(1779));
        //System.out.println(years);
        //System.out.println(ts.data());
        
    }   
    @Test
    public void testTimeSeriesPlus(){
        TimeSeries<Double> ts2 = new TimeSeries<Double>();
        ts2.put(2001, 4.5);
        ts2.put(2002, 10.0);
        ts2.put(2003, 1.3);
        TimeSeries<Double> ts3 = new TimeSeries<Double>();
        ts3.put(2001, 5.6);
        ts3.put(2002, 4.2);
        ts3.put(2003, .7);
        TimeSeries<Double> ts4 = ts2.plus(ts3);
        Collection<Number> years2 = ts4.years();
        assertTrue(years2.contains(2001));
        assertTrue(years2.contains(2002));
        assertTrue(years2.contains(2003));
        Collection<Number> data = ts4.data();
        //System.out.println(data);
        assertTrue(data.contains(10.1));
        assertTrue(data.contains(14.2));
        assertTrue(data.contains(2.0));
        
    }
    @Test
    public void testTimeSeriesDivide() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(2001, 10.0);
        ts.put(2002, 6.0);
        ts.put(2003, 2.0);
        TimeSeries<Double> ts2 = new TimeSeries<Double>();
        ts2.put(2001, 2.0);
        ts2.put(2002, 3.0);
        ts2.put(2003, 1.0);
        TimeSeries<Double> ts3 = ts.dividedBy(ts2);
        Collection<Number> data = ts3.data();
        assertTrue(data.contains(10.0/2.0));
        assertTrue(data.contains(6.0/3.0));
        assertTrue(data.contains(2.0/1.0));
        //System.out.println(data);
    }
    @Test
    public void testYearlyRecord() {
        YearlyRecord y = new YearlyRecord();
        y.put("pawnee", 100);
        y.put("councilmember jam", 14);
        y.put("ron swanson", 19);
        y.put("ann perkins!", 4);
        y.put("waffles", 99);
        System.out.println(y.counts());
        assertEquals(1, y.rank("pawnee"));
        assertEquals(2, y.rank("waffles"));
        assertEquals(3, y.rank("ron swanson"));
        assertEquals(4, y.rank("councilmember jam"));
        assertEquals(5, y.rank("ann perkins!"));

    }
	@Test
    
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(WordNetTest.class);
    }
} 
        