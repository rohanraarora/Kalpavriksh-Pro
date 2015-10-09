package models;

import java.util.ArrayList;

/**
 * Created by Rohan on 8/26/2015.
 *
 */
public class Package extends LabItem{
    private double price;
    private ArrayList<Supertest> supertests;

    public Package(int id,String name,double price,ArrayList<Supertest> supertests){
        this.id = id;
        this.name = name;
        this.price = price;
        this.supertests = supertests;
        this.type = PACKAGE;
    }

    public String getName(){return name;}

    public int getId(){return id;}

    public double getPrice(){return price;}

    public ArrayList<Supertest> getSupertests(){return supertests;}


}
