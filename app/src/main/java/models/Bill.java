package models;

import org.json.JSONObject;

/**
 * Created by Rohan on 10/30/2015.
 */
public class Bill {
    private String bill;
    private String date;
    private int status;
    private int appointmentId;

    public static final int UPLOADED = 1;
    public static final int NOT_UPLOADED = 0;

    public Bill(String bill,String date,int status,int appointmentId){
        this.bill = bill;
        this.date = date;
        this.status = status;
        this.appointmentId = appointmentId;
    }

    public int getAppointmentId() {
        return appointmentId;
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
