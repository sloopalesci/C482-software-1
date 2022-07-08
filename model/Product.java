package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The model for Products
 *
 * @author Scott Alesci
 */
public class Product {

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id, stock, min, max;
    private String name;
    private double price;

    /**
     * Full constructor for Products
     *
     * @param id the Product id
     * @param name the Product name
     * @param price the Product price
     * @param stock the Product stock or inventory
     * @param min the Product minimum stock
     * @param max the Product maximum stock
     */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Gets the ID
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the price
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price
     *
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the stock
     *
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock
     *
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the min
     *
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the min
     *
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Gets the max
     *
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the max
     *
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Adds a part the associatedParts list
     *
     * @param part the part to add to associatedParts list
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Removes a pert from the associatedParts list
     *
     * @param part the part to delete from the associatedParts list
     */
    public void deleteAssociatedPart(Part part){
        associatedParts.remove(part);
    }

    /**
     * Gets all parts from the associatedParts list
     *
     * @return all associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

}
