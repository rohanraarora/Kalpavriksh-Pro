package models;

/**
 * Created by Rohan on 9/2/2015.
 *
 */
public class Patient {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String age;
    private String gender;

    public Patient(int id,String name,String address,String phone,String age,String gender){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

}
