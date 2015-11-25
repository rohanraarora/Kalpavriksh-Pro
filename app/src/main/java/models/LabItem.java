package models;

/**
 * Created by Rohan on 10/8/2015.
 *
 */
public class LabItem {

    public static final int TEST = 1;
    public static final int SUPERTEST = 2;
    public static final int PACKAGE = 3;

    protected int type;
    protected int id;
    protected String name;

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
