package kleve.PartAndProductManager.datamodel;

/** This class is a subclass of Part for parts which are outsourced and therefore have a company name.
 * @author Jonathan Kleve
 * */
public class Outsourced extends Part {
    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /** @return the companyName. */
    public String getCompanyName() {
        return companyName;
    }
    /** @param companyName the companyName to set. */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
