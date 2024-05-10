
/**
 *  @author Jason Sodokoff
 *  @version PKG, VSCODE, JPK, Java 11
 *  @since CSE 017, May 2nd, 2024
 */ 

import java.util.ArrayList;
import java.util.LinkedList;
/**
 * Class HashMap: An implementation of the hash table using separate chaining
 */
public class HashMapSC <K,V>{
    // data members
	private LinkedList<MapEntry<K,V>>[] hashTable;
	private double loadFactor;
	private int size;
    /**
     * Default constructor 
     * default capacity: 100
     * default load factor: 0.9
	 * Time complexity: O(1)
     */
	public HashMapSC() {
		this(100, 0.9);
	}
    /**
     * Constructor with one parameter
     * @param c for the capacity
     * default load factor: 0.9
	 * Time complexity: O(log n)
     */
	public HashMapSC(int c) {
		this(c, 0.9);
	}
    /**
     * Constructor with two parameters
     * @param c for the capacity
     * @param lf for the load factor
	 * Time complexity: O(log n)
     */
	public HashMapSC(int c, double lf) {
        loadFactor = lf;
		hashTable = new LinkedList[trimToPowerOf2(c)];
	}
	/**
     * Method to dteremine the closest power of 2 to a given integer
     * @param c for the capacity
     * @return a power of 2 such that 2^k >= c
	 * Time complexity: O(log n)
     */
    private int trimToPowerOf2(int c){
		int capacity = 1;
		while(capacity < c){
			capacity = capacity << 1;
		}
		return capacity;
	}
    /**
     * Method hash
     * @param hashCode
     * @return a valid index in the hashtable
	 * Time complexity: O(1)
     */
    protected int hash(int hashCode) {
		return hashCode & (hashTable.length-1);
	}
    
    /**
     * Method clear to clear the hashtable
	 * Time complexity: O(n)
     */
	public void clear() {
		size = 0;
		for(int i=0; i<hashTable.length; i++)
			if(hashTable[i] != null)
				hashTable[i].clear();
	}
    /**
     * Method size
     * @return the number of pairs (key,value) stored the hashtable
	 * Time complexity: O(1)
     */
	public int size(){
		return size;
	}
	/**
     * Method containsKey
	 * @param key being looked up
     * @return true if key was found, false otherwise
	 * Time complexity: O(1)
     */
	public boolean containsKey(K key){
		return get(key) != null;
	}
    /**
     * Method get to find an entry in the hashtable
     * @param key the value of the key being searched for
     * @return the value associated with the key if key is found, null otherwise
	 * Time complexity: O(1)
     */
	
	public V get(K key) {
		int HTIndex = hash(key.hashCode());
		if(hashTable[HTIndex] != null) {
			LinkedList<MapEntry<K,V>> ll = hashTable[HTIndex];
			for(MapEntry<K,V> entry: ll) {
				if(entry.getKey().equals(key))
					return entry.getValue();
			}
		}
		return null;
	}
    /**
     * Method remove to remote an entry from the hashtable
     * @param key the key to be removed
     * if the key is found, the pair (key and it associated value) will be removed from the hashtable
     * the hashtable is not modified if key is not found
	 * Time complexity: O(1)
     */
	public void remove(K key) {
		int HTIndex = hash(key.hashCode());
		if (hashTable[HTIndex]!=null) { //key is in the hash map
			LinkedList<MapEntry<K,V>> ll = hashTable[HTIndex];
			for(MapEntry<K,V> entry: ll) {
				if(entry.getKey().equals(key)) {
					ll.remove(entry);
					size--;
					break;
				}
			}
		}		
	}
    /**
     * Method put to add a new entry to the hashtable
     * @param key the value of the key of the new entry
     * @param value the value associated with the key
     * @return the old value of the entry if an entry is found for key
     *         or the new value if a new entry was added to the hashtable
	 * Time complexity: O(1) on average, but can reach O(n) if rehashing is required
     */
    public V put(K key, V value) {
		V val = get(key);
	    if(val != null) {
		    int HTIndex = hash(key.hashCode());
		    LinkedList<MapEntry<K,V>> ll;
            ll = hashTable[HTIndex];
		    for(MapEntry<K,V> entry: ll){
			    if(entry.getKey().equals(key)) {
                    V old = entry.getValue();
                    entry.setValue(value); 
                    return old;
			    }
		    }
        }
        if(size >= hashTable.length * loadFactor)
		    rehash();
        int HTIndex = hash(key.hashCode());
        if(hashTable[HTIndex] == null){
		    hashTable[HTIndex] = new LinkedList<>();
        }
        hashTable[HTIndex].add(new MapEntry<>(key, value));
        size++; 
        return value;
    }
    /**
     * Method rehash
     * creates a new hashtable with double capacity
     * puts all the entries from the old hashtable into the new table
	 * Time complexity: O(n)
     */
	protected void rehash() {
		ArrayList<MapEntry<K,V>> list = toList(); 
		hashTable = new LinkedList[hashTable.length << 1];
		size = 0;
		for(MapEntry<K,V> entry: list)
			put(entry.getKey(), entry.getValue());
		
	}
    /**
     * Method toList
     * @return an arraylist with all the entries in the hashtable
	 * Time complexity: O(n)
     */
	public ArrayList<MapEntry<K,V>> toList(){
		ArrayList<MapEntry<K,V>> list = new ArrayList<>();
		for(int i=0; i< hashTable.length; i++) {
			if(hashTable[i]!= null) {
				LinkedList<MapEntry<K,V>> ll = hashTable[i];
				for(MapEntry<K,V> entry: ll)
					list.add(entry);
			}
		} 
		return list;
	}
    /**
     * Method toString
     * @return a formatted string with all the entries in the hashtable
	 * Time complexity: O(n)
     */
	public String toString() {
		String out = "[";
		for(int i=0; i<hashTable.length; i++) {
			if(hashTable[i]!=null) {
				for(MapEntry<K,V> entry: hashTable[i])
					out += entry.toString();
				out += "\n";
			}
		}
		out += "]"; 
		return out;
	}
	/**
     * Method values
     * @return an array list with the all the values in the hashtable
	 * Time complexity: O(n)
     */
	public ArrayList<V> values(){
		ArrayList<V> list = new ArrayList<>();
		for(int i=0; i< hashTable.length; i++) {
			if(hashTable[i]!= null) {
				LinkedList<MapEntry<K,V>> ll = hashTable[i];
				for(MapEntry<K,V> entry: ll)
					list.add(entry.getValue());
			}
		} 
		return list;
	}
}