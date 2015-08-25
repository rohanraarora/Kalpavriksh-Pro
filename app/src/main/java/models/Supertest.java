package models;

import java.util.ArrayList;

/**
 * Created by Rohan on 8/26/2015.
 */
public class Supertest {
    private String name;
    private int id;
    private double price;
    private ArrayList<Supertest> tests;

    public Supertest(int id,String name){
        this.id = id;
        this.name = name;
        this.price = -1;
        this.tests = null;
    }


    public Supertest(int id,String name,double price,ArrayList<Supertest> tests){
        this.id = id;
        this.name = name;
        this.price = price;
        this.tests = tests;
    }

    public String getName(){return name;}

    public int getId(){return id;}

    public double getPrice(){return price;}

    public ArrayList<Supertest> getTests(){return tests;}
}
