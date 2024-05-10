
/**
 *  @author Jason Sodokoff
 *  @version PKG, VSCODE, JPK, Java 11
 *  @since CSE 017, May 2nd, 2024
 */ 

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class to represent a Country object
 */
public class Country implements Comparable<Country>{
    // data members
    private String code;
    private String name;
    private double area;
    // Linkedlist of pairs (year, population count)
    private LinkedList<MapEntry<Integer,Integer>> population;
    
    
    // Hashtable of pairs (year, carbon emissions in tons). year is the key
    private HashMapSC<Integer,Double> emission;
   
    /**
     *  Constructor with three parameters
     * @param c the country code
     * @param n the country name
     * @param a the country area
     *  */ 
    public Country(String c, String n, double a){
        code = c;
        name = n;
        area = a;
        population = new LinkedList<>();
        emission = new HashMapSC<>();
    }
    /**
     * Getter for the country code
     * @return the country code
     */
    public String getCode(){ return code;}
    /**
     * Getter for the country name
     * @return the country name
     */
    public String getName(){ return name;}
    /**
     * Getter for the area
     * @return the country area
     */
    public double getArea(){ return area;}
    /**
     * toString()
     * @return formatted string with the country attributes (code, name, and area)
     */
    public String toString(){
        return String.format("%-10s\t%-32s\t%-10.2f", code, name, area);
    }
    /**
     * Setter for the country code
     * @param c the country code
     */
    public void setCode(String c){ code = c;}
    /**
     * Setter for the name
     * @param n the country name
     */
    public void setName(String n){ name = n;}
    /**
     * Setter for te area
     * @param a the country area
     */
    public void setArea(double a){ area = a;}
    /**
     * Method to add a pair (year, count) to the linkedlist population
     * @param year
     * @param population count for year
     */
    public void addPopulation(int year, int count){
        population.add(new MapEntry<>(year, count));
    }
    /**
     * Method to get the population count for a given year
     * @paran the given year
     * @return the population for the given year if the year is found, 0 otherwise
     */

     //Time complexity: 0 (n log n)
    public int getPopulation(int year) {
        if (population == null || population.isEmpty()) {
            return 0;
        }

        int left = 0;
        int right = population.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            MapEntry<Integer, Integer> current = population.get(mid);
            int currentYear = current.getKey();

            if (currentYear == year) {
                return current.getValue();
            } else if (currentYear < year) {
                left = mid + 1; 
            } else {
                right = mid - 1; 
            }
        }

        return 0;
    }

    // Definition of Population class assuming it implements Comparable interface
    private class Population implements Comparable<Population> {
        private int year;
        private int population;

        public Population(int year, int population) {
            this.year = year;
            this.population = population;
        }

        public int getYear() {
            return year;
        }

        public int getPopulation() {
            return population;
        }

        @Override
        public int compareTo(Population other) {
            return Integer.compare(this.year, other.year);
        }
    }

    /**
     * Method o add a pair (year, carbon emission) to the hashmap emission
     * @param year the key of the new entry in the hash table
     * @param emissiony the value of the new entry in the hash table
     */
    public void addEmission(int year, double emissiony){
        emission.put(year, emissiony);
    }
    /**
     * Method to get the carbon emission of a given year
     * @param year
     * @return the carbon emission for the given year
     */
    public Double getEmission(int year){
        return emission.get(year);
    }
    /**
     * Method compareTo to define the natural ordering
     * @param c the country compared to this country
     * @return 0 if the two countries have the same country code
     *         >0 if this country has a code greater than the code of country c
     *         <0 if this country has a code less than the code of country c
     */
    @Override
    public int compareTo(Country c) {
        return this.code.compareTo(c.getCode());
    }

    
}