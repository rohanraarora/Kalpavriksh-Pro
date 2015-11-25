package models;

import org.json.JSONObject;

/**
 * Created by Rohan on 10/30/2015.
 *
 */
public class Bill {
    private int bill_id;
    private String bill;
    private String date;
    private int status;
    private int appointmentId;

    public static final int UPLOADED = 1;
    public static final int NOT_UPLOADED = 0;

    public Bill(int id,String bill,String date,int status,int appointmentId){
        this.bill_id = id;
        this.bill = bill;
        this.date = date;
        this.status = status;
        this.appointmentId = appointmentId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getId(){
        return bill_id;
    }


    public int getStatus() {
        return status;
    }

    public String getBill() {
        return bill;
    }

    public String getDate() {
        return date;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
