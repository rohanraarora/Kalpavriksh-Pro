package utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

import models.*;
import models.Package;

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

    public static ArrayList<Supertest> getSupertests(Context context) {
        ArrayList<Supertest> supertestArrayList = new ArrayList<>();
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.SUPERTEST_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(Contract.SUPERTEST_ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndex(Contract.SUPERTEST_NAME_COLUMN));
            String priceString = cursor.getString(cursor.getColumnIndex(Contract.SUPERTEST_PRICE_COLUMN));
            ArrayList<Test> tests = new ArrayList<>();
            Cursor innerCursor = db.rawQuery("SELECT * FROM " + Contract.TEST_TABLE + " WHERE " + Contract.TEST_ID_COLUMN
                    + " IN ( SELECT " + Contract.SUPERTEST_TEST_TID_COLUMN + " FROM " + Contract.SUPERTEST_TEST_TABLE
                    + " WHERE " + Contract.SUPERTEST_TEST_STID_COLUMN + "=? )",new String[]{id+""});
            while (innerCursor.moveToNext()){
                int tid = innerCursor.getInt(innerCursor.getColumnIndex(Contract.TEST_ID_COLUMN));
                String test_name = innerCursor.getString(innerCursor.getColumnIndex(Contract.Test_Name_COLUMN));
                Test test = new Test(tid,test_name);
                tests.add(test);
            }
            innerCursor.close();
            Supertest supertest = new Supertest(id,name,Double.parseDouble(priceString),tests);
            supertestArrayList.add(supertest);
        }
        cursor.close();
        return supertestArrayList;
    }

    public static ArrayList<models.Package> getPackages(Context context) {
        ArrayList<Package> packages = new ArrayList<>();
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.PACKAGE_TABLE,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(Contract.PACKAGE_ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndex(Contract.PACKAGE_NAME_COLUMN));
            String priceString = cursor.getString(cursor.getColumnIndex(Contract.PACKAGE_PRICE_COLUMN));
            ArrayList<Supertest> tests = new ArrayList<>();
            Cursor innerCursor = db.rawQuery("SELECT * FROM " + Contract.SUPERTEST_TABLE + " WHERE " + Contract.SUPERTEST_ID_COLUMN
                    + " IN ( SELECT " + Contract.PACKAGE_SUPERTEST_STID_COLUMN + " FROM " + Contract.PACKAGE_SUPERTEST_TABLE
                    + " WHERE " + Contract.PACKAGE_SUPERTEST_PID_COLUMN + "=? )",new String[]{id+""});
            while (innerCursor.moveToNext()){
                int tid = innerCursor.getInt(innerCursor.getColumnIndex(Contract.SUPERTEST_ID_COLUMN));
                String test_name = innerCursor.getString(innerCursor.getColumnIndex(Contract.SUPERTEST_NAME_COLUMN));
                Supertest supertest = new Supertest(tid,test_name);
                tests.add(supertest);
            }
            innerCursor.close();
            Package packageObject = new Package(id,name,Double.parseDouble(priceString),tests);
            packages.add(packageObject);
        }
        cursor.close();
        return packages;
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
        cursor.close();
        return new LabAppointment(appointment_id,patient,date,time,tests,isDone);
    }

    public static Supertest getSupertest(Context context,int id){
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.SUPERTEST_TABLE,null,Contract.SUPERTEST_ID_COLUMN + "=?",new String[]{id + ""},null,null,null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(Contract.SUPERTEST_NAME_COLUMN));
        String priceString = cursor.getString(cursor.getColumnIndex(Contract.SUPERTEST_PRICE_COLUMN));
        ArrayList<Test> tests = new ArrayList<>();
        Cursor innerCursor = db.rawQuery("SELECT * FROM " + Contract.TEST_TABLE + " WHERE " + Contract.TEST_ID_COLUMN
                + " IN ( SELECT " + Contract.SUPERTEST_TEST_TID_COLUMN + " FROM " + Contract.SUPERTEST_TEST_TABLE
                + " WHERE " + Contract.SUPERTEST_TEST_STID_COLUMN + "=? )", new String[]{id + ""});
        while (innerCursor.moveToNext()){
            int tid = innerCursor.getInt(innerCursor.getColumnIndex(Contract.TEST_ID_COLUMN));
            String test_name = innerCursor.getString(innerCursor.getColumnIndex(Contract.Test_Name_COLUMN));
            Test test = new Test(tid,test_name);
            tests.add(test);
        }
        innerCursor.close();
        Supertest supertest = new Supertest(id,name,Double.parseDouble(priceString),tests);
        cursor.close();
        return supertest;
    }


}
