package models;

/**
 * Created by Rohan on 8/26/2015.
 *
 */
public class Test extends LabItem{


    public Test(int id,String name){
        this.id = id;
        this.name = name;
        this.type = TEST;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
