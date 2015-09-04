package utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import models.LabAppointment;
import models.Patient;

/**
 * Created by Rohan on 8/29/2015.
 *
 */
public class Utilities {

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // checking if network is present
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static LabAppointment getAppointment(Context context,long id){
        SQLiteDatabase db = OpenHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(Contract.LAB_APPOINTMENT_TABLE,null,Contract.LAB_APPOINTMENT_ID_COLUMN + "=?",new String[]{id +""},null,null,null);
        cursor.moveToFirst();
        int appointment_id = cursor.getInt(cursor.getColumnIndex(Contract.LAB_APPOINTMENT_ID_COLUMN));
        int patient_id = cursor.getInt(cursor.getColumnIndex(Contract.LAB_APPOINTMENT_PATIENT_ID_COL));
        String tests = cursor.getString(cursor.getColumnIndex(Contract.LAB_APPOINTMENT_TESTS_COLUMN));
        String date = cursor.getString(cursor.getColumnIndex(Contract.LAB_APPOINTMENT_DATE_COLUMN));
        String time = cursor.getString(cursor.getColumnIndex(Contract.LAB_APPOINTMENT_TIME_COLUMN));
        int isDoneInt = cursor.getInt(cursor.getColumnIndex(Contract.LAB_APPOINTMENT_ISDONE_COLUMN));
        boolean isDone = false;
        if (isDoneInt!=0){
            isDone = true;
        }
        Cursor patientCursor = db.query(Contract.PATIENT_TABLE,null,Contract.PATIENT_ID_COL + "=?",new String[]{patient_id + ""},null,null,null);
        patientCursor.moveToFirst();
        String name = patientCursor.getString(patientCursor.getColumnIndex(Contract.PATIENT_NAME_COL));
        String address = patientCursor.getString(patientCursor.getColumnIndex(Contract.PATIENT_ADDRESS_COL));
        String phone = patientCursor.getString(patientCursor.getColumnIndex(Contract.PATIENT_PHONE_COL));
        String age = patientCursor.getString(patientCursor.getColumnIndex(Contract.PATIENT_AGE_COL));
        String gender = patientCursor.getString(patientCursor.getColumnIndex(Contract.PATIENT_GENDER_COL));
        Patient patient = new Patient(patient_id,name,address,phone,age,gender);
        return new LabAppointment(appointment_id,patient,date,time,tests,isDone);
    }

}
