package models;

/**
 * Created by Rohan on 9/2/2015.
 *
 */
public class LabAppointment {
    private int lab_appointment_id;
    private String date;
    private String collection_time;
    private String tests;
    private Patient patient;
    private boolean isDone = false;

    public LabAppointment(int id, Patient patient, String date, String collection_time, String tests,boolean isDone){
        lab_appointment_id = id;
        this.patient = patient;
        this.date = date;
        this.collection_time = collection_time;
        this.tests = tests;
        this.isDone = isDone;
    }

    public Patient getPatient() {
        return patient;
    }

    public int getId(){
        return lab_appointment_id;
    }

    public String getCollection_time() {
        return collection_time;
    }

    public String getDate() {
        return date;
    }

    public String getTests() {
        return tests;
    }

    public boolean isDone(){
        return isDone;
    }

    public long getTimeInEpoch(){
        //TODO
        return 0l;
    }


}


