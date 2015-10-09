package models;

import java.util.ArrayList;

/**
 * Created by Rohan on 8/26/2015.
 *
 */
public class Supertest extends LabItem{

    private double price;
    private ArrayList<Test> tests;



    public Supertest(int id,String name){
        this.id = id;
        this.name = name;
        this.price = 0;
        this.tests = null;
        this.type = SUPERTEST;
    }


    public Supertest(int id,String name,double price,ArrayList<Test> tests){
        this.id = id;
        this.name = name;
        this.price = price;
        this.tests = tests;
        this.type = SUPERTEST;
    }

    public String getName(){return name;}

    public int getId(){return id;}

    public double getPrice(){return price;}

    public ArrayList<Test> getTests(){return tests;}
}
