package models;

/**
 * Created by Rohan on 10/6/2015.
 *
 */
public class RetailSource {
    private int id;
    private String name;
    private String address;

    public RetailSource(int id,String name,String address){
        this.id = id;
        this.address = address;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
