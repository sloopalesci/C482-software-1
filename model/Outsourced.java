package model;

/**
 * The model for Outsourced parts
 *
 * @author Scott Alesci
 */
public class Outsourced extends Part{

    private String companyName;

    /**
     * Full constructor for an Outsourced part
     *
     * @param id the id for the part
     * @param name the name of the part
     * @param price the price of the part
     * @param stock the stock or inventory of the part
     * @param min the minimum amount of inventory for the part
     * @param max the max amount of inventory for the part
     * @param companyName the name of the company if it's an Outsourced part
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Getter for companyName
     *
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Setter for companyName
     *
     * @param companyName the name to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
