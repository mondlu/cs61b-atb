package ngordnet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {    
    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
        super();
    }   
    /** Returns the years in which this time series is valid. Doesn't really
      * need to be a NavigableSet. This is a private method and you don't have 
      * to implement it if you don't want to. */
    //private NavigableSet<Integer> validYears(int startYear, int endYear) {
        //} 

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR. 
     * inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        if (ts != null) {
            for (int year = startYear; year <= endYear; year = year + 1) {
                if (ts.containsKey(year)) {
                    T copyItem = ts.get(year);
                    this.put(year, copyItem);
                }   
            }
        }   
    }       
    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        Set<Integer> keysOriginal = ts.keySet();
        for (Integer k : keysOriginal) {
            this.put(k, ts.get(k));
        }
    }
    /** Returns the quotient of this time series divided by the relevant value in ts.
      * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> dividedTimeSeries = new TimeSeries<Double>();
        Set<Integer> keysThis = this.keySet();
        for (Integer k : keysThis) {
            if (!ts.containsKey(k)) {
                throw new IllegalArgumentException("input tree is missing a key");              
            }
            double result = this.get(k).doubleValue() / ts.get(k).doubleValue();
            dividedTimeSeries.put(k, result);
        }
        return dividedTimeSeries;
    }
        

    /** Returns the sum of this time series with the given ts. The result is a 
      * a Double time series (for simplicity). */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> plusTimeSeries = new TimeSeries<Double>();
        Set<Integer> keysThis = this.keySet();
        Set<Integer> keysTs = ts.keySet();
        for (Integer k1 : keysThis) {
            if (!ts.containsKey(k1)) {
                double result = this.get(k1).doubleValue();
                plusTimeSeries.put(k1, result);
            } else {
                double result = this.get(k1).doubleValue() + ts.get(k1).doubleValue();
                plusTimeSeries.put(k1, result);
            }
        }
        for (Integer k2 : keysTs) {
            if (!this.containsKey(k2)) {
                double result = ts.get(k2).doubleValue();
                plusTimeSeries.put(k2, result);
            } else {
                double result = this.get(k2).doubleValue() + ts.get(k2).doubleValue();
                plusTimeSeries.put(k2, result);
            }
        }
        return plusTimeSeries;              
    }       
    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        Set<Integer> keysThis = this.keySet();
        ArrayList<Number> yearsOnly = new ArrayList<Number>();
        for (Integer k : keysThis) {
            yearsOnly.add(k);
        }       
        return (Collection<Number>) yearsOnly;
    } 

    /** Returns all data for this time series. 
      * Must be in the same order as years(). */
    public Collection<Number> data() {
        Set<Integer> keysThis = this.keySet();
        ArrayList<Number> dataOnly = new ArrayList<Number>();
        int i = 0;
        for (Integer k : keysThis) {
            if (this.get(k) != null) {
                dataOnly.add(i, this.get(k));
                i = i + 1; 
            }
        }
        return (Collection<Number>) dataOnly;
    }
}  
