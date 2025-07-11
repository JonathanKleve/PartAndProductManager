package kleve.PartAndProductManager.datamodel;

/** This class is a subclass of Part for parts which are in house and therefore have a machineId.
 * @author Jonathan Kleve
 * */
public class InHouse extends Part {
    private int machineId;
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    /** @return the machineId. */
    public int getMachineId() {
        return machineId;
    }
    /** @param machineId the id to set */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
