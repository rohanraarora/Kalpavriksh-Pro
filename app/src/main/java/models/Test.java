package models;

/**
 * Created by Rohan on 8/26/2015.
 *
 */
public class Test {

    private String name;
    private int id;



    public Test(int id,String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
