
/**
 *  @author Jason Sodokoff
 *  @version PKG, VSCODE, JPK, Java 11
 *  @since CSE 017, May 2nd, 2024
 */ 

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Comparator;
import java.util.Locale;
import java.text.NumberFormat;

public class Test{
    public static int iterations;

    // static member to hold the world coutry codes
    private static ArrayList<String> countryCodes = new ArrayList<>();

    public static void main(String[] args){
        
        // Tree map to store the list of countries. The country code is the key and the Country object is the value
        TreeMap<String,Country> countries = new TreeMap<>();
        
        // Reading all the necessary information about the countries
        readCodes(countryCodes, "codes.txt");
        readCountries(countries, "countries.csv");
        readCountryDetails(countries, "country_info.csv");
        
        // Displaying some information about the world countries
        System.out.printf("%-15s\t%-10s\t%-32s\t%-10s\t%s\n", "Extreme", "Code", "Name", "Area(sq.ft)", "Emissions(tons)");
        Country c = getExtremeEmission(countries, 2000, true);
        if(c != null)
            System.out.printf("%-15s\t%s\t%s\n", "Highest(2000)", c, c.getEmission(2000));
        c = getExtremeEmission(countries, 2020, true);
        if(c != null)
            System.out.printf("%-15s\t%s\t%s\n", "Highest(2020)", c, c.getEmission(2020));
        c = getExtremeEmission(countries, 2000, false);
        if (c != null)
            System.out.printf("%-15s\t%s\t%s\n", "Lowest (2000)", c, c.getEmission(2000));
        c = getExtremeEmission(countries, 2020, false);
        if (c != null)
            System.out.printf("%-15s\t%s\t%s\n", "Lowest (2020)", c, c.getEmission(2020));

        System.out.println();
        long pop = getWorldPopulation(countries, 2000);
        System.out.println("\nWorld population in 2000: " + pop);

        pop = getWorldPopulation(countries, 2020);
        System.out.println("World population in 2020: " + pop);

        System.out.println("\nList of countries sorted by area");
        sortCountries(countries);
    }
    /**
     * Method to read the world country codes in an array list
     * @param codes the array list to hold the valid codes
     * @param filename the file to read from
     */
    public static void readCodes(ArrayList<String> codes, String filename){
        try{
            Scanner read = new Scanner(new File(filename));
            while(read.hasNextLine()){
                codes.add(read.nextLine());
            }
            read.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    /**
     * Method to check if a country code is valid
     * @param code the country code
     * @throws Exception if the code is not fond in the array list countryCodes
     */
    public static void checkCode(String code) throws Exception {
        if (!countryCodes.contains(code)) {
            throw new Exception("Invalid country code: " + code);
        }
    }
    /**
     * Method to read the list of countries
     * @param tree the binary search tree where the pairs (countrycode, Country object) will be stored
     * @paramm filename the file with the list of countries
     */
    public static void readCountries(TreeMap<String, Country> tm, String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] attributes = line.split(",");
                String countryCode = attributes[0].trim();
                String countryName = attributes[1].trim();
                double countryArea = Double.parseDouble(attributes[2].trim());

                try {
                    checkCode(countryCode); 
                    Country country = new Country(countryCode, countryName, countryArea);
                    tm.add(countryCode, country);
                } catch (Exception e) {
        
                    System.out.println("Invalid country code in line: " + line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }
    /**
     * Method to read the details of the countries (population and carbon emission)
     * @param tree the binary search of countries to be updated
     * @param filename the file where the data about the population and the carbon emission is stored
     */
    public static void readCountryDetails(TreeMap<String,Country> tree, String filename){
        try{
            Scanner read = new Scanner(new File(filename));
            while(read.hasNextLine()){
                String line = read.nextLine();
                String[] attributes = line.split(",");
                String countryCode = attributes[0];
                Country c = tree.get(countryCode);
                if(c != null){
                    c.addEmission(Integer.parseInt(attributes[1]), Double.parseDouble(attributes[2]));
                    c.addPopulation(Integer.parseInt(attributes[1]), Integer.parseInt(attributes[3]));
                    for(int i=2001; i<=2021;i++){
                        line = read.nextLine();
                        attributes = line.split(",");
                        c.addEmission(Integer.parseInt(attributes[1]), Double.parseDouble(attributes[2]));
                        c.addPopulation(Integer.parseInt(attributes[1]), Integer.parseInt(attributes[3]));
                    }
                }
            }
            read.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found");
        }
    }
    /**
     * Method to find the extreme value of the carbon emissions for a given year
     * @param tree the binary search tree with the list of countries
     * @param year the year for which the method will find the extreme value
     * @param maxMin true to find the highest carbon emisison, false for the lowest
     * @return the country that has the extreme carbon emission for the given year
     */
    public static Country getExtremeEmission(TreeMap<String, Country> tm, int year, boolean maxmin) {
        double extremeEmission = maxmin ? Double.MIN_VALUE : Double.MAX_VALUE;
        Country extremeCountry = null;

        for (Country country : tm.values()) {
            Double emission = country.getEmission(year);
            if (emission != null) {
                if (maxmin) {
                    if (emission > extremeEmission) {
                        extremeEmission = emission;
                        extremeCountry = country;
                    }
                } else {
                    if (emission < extremeEmission) {
                        extremeEmission = emission;
                        extremeCountry = country;
                    }
                }
            }
        }

        return extremeCountry;
    }
    /**
     * Method to get the total world population for a given year
     * @param tree the binary search tree with the list of countries
     * @param year the year for which the total population is calculated
     * @return the total population for the given year
     */
    public static long getWorldPopulation(TreeMap<String, Country> tm, int year) {
        long totalPopulation = 0;

        for (Country country : tm.values()) {
            int population = country.getPopulation(year);
            totalPopulation += population;
        }

        return totalPopulation;
    }
    /**
     * Method to display the list of countries sorted by area
     * @param tree the binary search tree with the list of countries
     */
    public static void sortCountries(TreeMap<String,Country> tm){
        ArrayList<Country> countries = new ArrayList<>(tm.values());

       // public static <E extends Comparable<E>> void comparableByArea (TreeMap <Country> countries){}
        quickSort(countries);


    }
    /**
     * QuickSort Method
     * @param list to be sorted
     * Time complexity: O(nlogn) to O(n^2)
     */
    public static <E extends Comparable<E>> void quickSort(ArrayList<E> list) {
        iterations =0;
        quickSort(list);
        
    }
    /**
     * QuickSort Recursie Helper Method
     * @param list to be sorted
     * @param first the index where to start quicksorting
     * @param last the index where to stop quicksorting
     * Time complexity: O(nlogn) to O(n^2)
     */
    public static <E extends Comparable<E>> void quickSort(ArrayList<E> list, int first, int last, Comparator<? super E> comp) {
        if (last > first) {
            int pivotIndex = partition(list, first, last, comp);
            quickSort(list, first, pivotIndex - 1, comp);
            quickSort(list, pivotIndex + 1, last, comp);
        }
    }
    /**
     * Partition method used by quicksort
     * @param list to be sorted
     * @param first the index where to start the partitioning
     * @param last the index where to stop partitioning
     * @return the index where the pivot is placed
     * Time complexity: O(n)
     */
    public static <E extends Comparable<E>> int partition(ArrayList<E> list, int first, int last, Comparator<? super E> comp) {
        E pivot = list.get(first);
        int index = first;
        for (int i = first + 1; i <= last; i++) {
            if (comp != null) {
                if (comp.compare(list.get(i), pivot) < 0) {
                    index++;
                    swap(list, index, i);
                }
            } else {
                if (list.get(i).compareTo(pivot) < 0) {
                    index++;
                    swap(list, index, i);
                }
            }
        }
        swap(list, first, index);
        return index;
    }

    /**
     * swap method
     * @param list where two elements will be swapped
     * @param index1 index of one element to be swapped
     * @param index2 index of the other element to be swapped
     * @throws an exeption if index1 or index2 are invalid
     */
    public static <E> void swap(ArrayList<E> list, int index1, int index2){
        if(index1 < 0 || index1 >= list.size() || index2 < 0 || index2 >= list.size())
            throw new ArrayIndexOutOfBoundsException();
        E temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }

    

}