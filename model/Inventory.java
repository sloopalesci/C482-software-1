package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Objects;

/**
 * The model for Inventory
 *
 * @author Scott Alesci
 */
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * adds a part to the allParts list
     *
     * @param part the part to add
     */
    public static void addPart(Part part){
        allParts.add(part);
    }

    /**
     * adds a product to the allProducts list
     *
     * @param product the product to add
     */
    public static void addProduct(Product product){
        allProducts.add(product);
    }

    /**
     * Searches for a part using the ID of the part
     *
     * @param partId the id of the part you're searching for
     * @return the Part
     */
    public static Part lookupPart(int partId){
        for (Part p : allParts) {
            if (p.getId() == partId) {
                return p;
            }
        }
        return null;
    }

    /**
     * Searches for a product using the ID of the product
     *
     * @param productID the id of the product you're searching for
     * @return the Product
     */
    public static Product lookupProduct(int productID){
        for (Product p : allProducts) {
            if (p.getId() == productID) {
                return p;
            }
        }
        return null;
    }

    /**
     * Searches for a part using the name of the part
     *
     * @param partName the name of the part you're searching for
     * @return the Part from the list
     */
    public static ObservableList<Part> lookupPart(String partName){
        for (Part p : allParts) {
            if (Objects.equals(p.getName(), partName)) {
                return (ObservableList<Part>) p;
            }
        }
        return null;
    }

    /**
     * Searches for a product using the name of the product
     *
     * @param productName the name of the product you're searching for
     * @return the Product from the list
     */
    public static ObservableList<Product> lookupProduct(String productName){
        for (Product p : allProducts) {
            if (Objects.equals(p.getName(), productName)) {
                return (ObservableList<Product>) p;
            }
        }
        return null;
    }

    /**
     * updates an existing part by deleting and replacing with a new part
     *
     * @param index the index of the part to be removed
     * @param selectedPart the part that is replacing
     */
    public static void updatePart(int index, Part selectedPart){
        Part p = Inventory.lookupPart(index);
        Inventory.deletePart(p);
        Inventory.addPart(selectedPart);
    }

    /**
     * Updates an existing product by deleting and replacing with a new product
     *
     * @param index the index of the product to be removed
     * @param selectedProduct the product that is replacing
     */
    public static void updateProduct(int index, Product selectedProduct){
        Product p = Inventory.lookupProduct(index);
        Inventory.deleteProduct(p);
        Inventory.addProduct(selectedProduct);
    }

    /**
     * Deletes a part from the allParts list
     *
     * @param selectedPart the part that is to be deleted from the allParts list
     */
    public static void deletePart(Part selectedPart){
        allParts.remove(selectedPart);
    }

    /**
     * Deletes a product from the allProducts list
     *
     * @param selectedProduct the product that is to be deleted from the allProducts list
     */
    public static void deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
    }

    /**
     * Gets all the parts in the allParts list
     *
     * @return all the parts in the list
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Gets all the products in the allProducts list
     *
     * @return all the products in the list
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }


    /**
     * the static call to the test data method
     */
    static {
        addTestData();
    }

    /**
     * This is where the test data is stored
     */
    public static void addTestData(){
        // Some Dummy Data
        Part handle = new Outsourced(1, "chopper", 19.99, 5, 1, 10, "Acme");
        Part pedal = new InHouse(2, "pedal", 19.34, 5,1,10, 123);
        Part spoon = new Outsourced(3, "spoon", 1.99, 15, 1, 19, "Acme");

        Product product1 = new Product(1, "test1", 19.99, 5, 1, 10);
        Product product2 = new Product(2, "test2", 19.99, 5, 1, 10);

        product2.addAssociatedPart(handle);
        product2.addAssociatedPart(pedal);
        product1.addAssociatedPart(spoon);

        Inventory.addPart(handle);
        Inventory.addPart(pedal);
        Inventory.addPart(spoon);
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
    }
}
