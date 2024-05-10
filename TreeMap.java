
/**
 *  @author Jason Sodokoff
 *  @version PKG, VSCODE, JPK, Java 11
 *  @since CSE 017, May 2nd, 2024
 */ 

import java.util.Comparator;
import java.util.ArrayList;
/**
 * Generic class to implement a binary search tree
 * where the nodes hold a pair (key,value)
 */
public class TreeMap<K,V> {
    // Data members
    private TreeNode root;
    private int size;
    private Comparator<K> comp;
    // Inner class TreeNode
    private class TreeNode{
        MapEntry<K,V> value;
        TreeNode left;
        TreeNode right;
        TreeNode(K key, V value){
            this.value = new MapEntry<>(key,value);
            left = right = null;
        }
    }
    /**
     * Default constructor
     * creates an empty tree that uses natural ordering to sort the keys
     */
    public TreeMap(){ 
        root = null;
        comp = null;
        size = 0; 
    }
    /**
     * Constructor with one parameters
     * creates an empty tree that uses the compare method of the object comp to sort the keys
     * @param comp Comparator object where the ordering of the key is defined
     */
    public TreeMap(Comparator<K> c){ 
        root = null;
        comp = c;
        size = 0; 
    }
    /**
     * Getter for teh size
     * @return the number of nodes in the tree
     */
    public int size() { 
        return size; 
    }
    /**
     * Method isEmpty
     * @return  true if the tree is empty, false oterwise
     */
    public boolean isEmpty() { 
        return (size == 0); 
    }
    /**
     * Method to clear the tree
     */
    public void clear() { 
        root = null; 
        size = 0;
    }
    /**
     * Search method
     * @param key the key being looked up
     * @return true if a pair that matches key is found, false otherwise
     */
    public boolean contains(K key){
        if(root == null){
            return false;
        }
        TreeNode node = root;
        while(node != null){
            int comparison = 0;
            if(comp == null){
                comparison = ((Comparable<K>)key).compareTo(node.value.getKey());
            }
            else{
                comparison = comp.compare(key,node.value.getKey());
            }
            if(comparison < 0)
                node = node.left;
            else if (comparison> 0)
                node = node.right;
            else
                return true;
        }
        return false;
    }
    /**
     * Method get(key)
     * @param key being looked up
     * @return the value that matches the guven key, null if the key was not found
     */
    public V get(K key){
        if(root == null){
            return null;
        }
        TreeNode node = root;
        while(node != null){
            int comparison = 0;
            if(comp == null){
                comparison = ((Comparable<K>)key).compareTo(node.value.getKey());
            }
            else{
                comparison = comp.compare(key,node.value.getKey());
            }
            if(comparison < 0)
                node = node.left;
            else if (comparison> 0)
                node = node.right;
            else
                return node.value.getValue();
        }
        return null;
    }
    /**
     * Method to add a new entry in the tree
     * @param key the key of the new entry
     * @param value the value of the new entry
     * @return true if a new entry was added successfully, false if the key already exists in the tree
     */
    public boolean add(K key, V value) {
        if (root == null)
            root = new TreeNode(key,value);
        else {
            TreeNode parent, node;
            parent = null; node = root;
            while (node != null) {
                parent = node;
                int comparison = 0;
                if(comp == null){
                    comparison = ((Comparable<K>)key).compareTo(node.value.getKey());
                }
                else{
                    comparison = comp.compare(key,node.value.getKey());
                }
                if(comparison < 0) {
                    node = node.left; 
                }
                else if (comparison > 0) {
                    node = node.right;
                }
                else
                    return false;
            }
            int comparison = 0;
            if(comp == null){
                comparison = ((Comparable<K>)key).compareTo(parent.value.getKey());
            }
            else{
                comparison = comp.compare(key,parent.value.getKey());
            }
            if (comparison< 0)
                parent.left = new TreeNode(key, value);
            else
                parent.right = new TreeNode(key,value);
        }
        size++;
        return true; 
    }
    /**
     * Method to remove an entry from the tree
     * @param key the key being looked up
     * @return true if a pair that matches the given key was found and removed, false if such key was not found
     */
    public boolean remove(K key) {
        TreeNode parent, node;
        parent = null; node = root;
        while (node != null) {
            int comparison = 0;
            if(comp == null){
                comparison = ((Comparable<K>)key).compareTo(node.value.getKey());
            }
            else{
                comparison = comp.compare(key,node.value.getKey());
            }
            if (comparison < 0) {
                parent = node;
                node = node.left;
            }
            else if (comparison > 0) {
                parent = node;
                node = node.right;
            }
            else
                break;
        }
        if (node == null)
            return false;
        // Case 1: node has no children
        if(node.left == null && node.right == null){
            if(parent == null){ 
                root = null; size = 0;
            }
            else
                if(parent.left == node)
                    parent.left = null;
                else
                    parent.right = null;
        }
        else if(node.left == null){ 
        //case 2: node has one right child
            if (parent == null) 
                root = node.right;
            else
                if(parent.left == node)
                    parent.left = node.right;
                else
                    parent.right = node.right;
        }
        else if(node.right == null){ 
        //case 2: node has one left child
            if (parent == null)
                root = node.left;
            else
                if(parent.left == node)
                    parent.left = node.left;
                else
                    parent.right = node.left;
        } 

        else { 
        // case 3: node has two children
            TreeNode rightMostParent = node;
            TreeNode rightMost = node.left;
            while (rightMost.right != null) {
                rightMostParent = rightMost;
                rightMost = rightMost.right;
            }
            node.value = rightMost.value;
            if(rightMostParent.left == rightMost)
                rightMostParent.left = rightMost.left;
            else
                rightMostParent.right = rightMost.left;
        }
        size--;
        return true;
    }
    
    /**
     * Inorder Traversal of the tree
     */
    public void inorder() {
        inorder(root);
    }
    /**
     * Recursive Inorder Traversal of the tree starting from node
     * @param node at which the traversal starts
     */
    private void inorder(TreeNode node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.value + " ");
            inorder(node.right);
        }
    }
    /**
     * Preorder Traversal of the tree
     */
    public void preorder() {
        preorder(root);
    }
    /**
     * Recursive Preorder Traversal of the tree starting from node
     * @param node at which the traversal starts
     */
    private void preorder(TreeNode node) {
        if (node != null) {
            System.out.print(node.value + " ");
            preorder(node.left);
            preorder(node.right);
        }
    }
    /**
     * Postorder Traversal of the tree
     */
    public void postorder() {
        postorder(root);
    }
    /**
     * Recursive Postorder Traversal of the tree starting from node
     * @param node at which the traversal starts
     */
    private void postorder(TreeNode node)  {
        if (node != null) {
            postorder(node.left);
            postorder(node.right);
            System.out.print(node.value + " "); 
        }
    }
    
    /**
     * Method values to return the values of the tree nodes
     * @return an array list with the values of all the nodes in the tree
     */ 

     //Time Complexity: 0 (n) 
     //Worst Case Scenario: The method iterates through
    
     public ArrayList<V> values() {
        ArrayList<V> result = new ArrayList<>();
        values(root, result);
        return result;
    }
    
    private void values(TreeNode node, ArrayList<V> result) {
        if (node != null) {
            values(node.left, result);
            result.add(node.value.getValue());
            values(node.right, result);
        }
    }
}    
