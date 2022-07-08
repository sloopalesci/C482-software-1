package model;

/**
 * The model for inHouse parts
 *
 * @author Scott Alesci
 */
public class InHouse extends Part{
    private int machineID;

    /**
     * Full Constructor for the InHouse object
     *
     * @param id the id for the part
     * @param name the name of the part
     * @param price the price of the part
     * @param stock the stock or inventory of the part
     * @param min the minimum amount of inventory for the part
     * @param max the max amount of inventory for the part
     * @param machineID the machineID for an InHouse type part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Gets the machineID
     *
     * @return the machineID
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     * Sets the machineID
     *
     * @param machineID the machineID to set
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
