package utilities;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import models.*;
import models.Package;
import utilities.Contract;
import utilities.OpenHelper;

import static java.lang.Double.*;

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
            Supertest supertest = new Supertest(id,name, parseDouble(priceString),tests);
            supertestArrayList.add(supertest);
        }
        cursor.close();
        return supertestArrayList;
    }

    public static ArrayList<RetailSource> getRetailSources(Context context){
        ArrayList<RetailSource> sources = new ArrayList<>();
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query(Contract.RETAIL_SOURCE_TABLE,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(Contract.RETAIL_SOURCE_ID_COLUMN));
            String name = cursor.getString(cursor.getColumnIndex(Contract.RETAIL_SOURCE_NAME));
            String address = cursor.getString(cursor.getColumnIndex(Contract.RETAIL_SOURCE_ADDRESS));
            RetailSource retailSource = new RetailSource(id,name,address);
            sources.add(retailSource);
        }
        cursor.close();
        return sources;
    }

    public static ArrayList<Bill> getTodayBills(Context context){
        ArrayList<Bill> bills = new ArrayList<>();
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date(System.currentTimeMillis()));

        Cursor cursor = db.query(Contract.BILL_TABLE,null,Contract.BILL_DATE + " =?",new String[]{date},null,null,null);
        while (cursor.moveToNext()){
            String billString = cursor.getString(cursor.getColumnIndex(Contract.BILL_JSON_STRING));
            int status = cursor.getInt(cursor.getColumnIndex(Contract.BILL_UPLOAD_STATUS));
            int appintmentId = cursor.getInt(cursor.getColumnIndex(Contract.BILL_APPOINTMENT_ID));
            Bill bill = new Bill(billString,date,status,appintmentId);
            bills.add(bill);
        }
        return bills;
    }
    public static ArrayList<models.Package> getPackages(Context context) {
        ArrayList<Package> packages = new ArrayList<>();
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.PACKAGE_TABLE, null, null, null, null, null, null);
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
            Package packageObject = new Package(id,name, parseDouble(priceString),tests);
            packages.add(packageObject);
        }
        cursor.close();
        return packages;
    }

    public static Package getPackage(Context context,int id){
        SQLiteDatabase db = OpenHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(Contract.PACKAGE_TABLE,null,Contract.PACKAGE_ID_COLUMN + "=?",new String[]{id +""},null,null,null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(Contract.PACKAGE_NAME_COLUMN));
        String priceString = cursor.getString(cursor.getColumnIndex(Contract.PACKAGE_PRICE_COLUMN));
        ArrayList<Supertest> tests = new ArrayList<>();
        Cursor innerCursor = db.rawQuery("SELECT * FROM " + Contract.SUPERTEST_TABLE + " WHERE " + Contract.SUPERTEST_ID_COLUMN
                + " IN ( SELECT " + Contract.PACKAGE_SUPERTEST_STID_COLUMN + " FROM " + Contract.PACKAGE_SUPERTEST_TABLE
                + " WHERE " + Contract.PACKAGE_SUPERTEST_PID_COLUMN + "=? )", new String[]{id + ""});
        while (innerCursor.moveToNext()){
            int tid = innerCursor.getInt(innerCursor.getColumnIndex(Contract.SUPERTEST_ID_COLUMN));
            String test_name = innerCursor.getString(innerCursor.getColumnIndex(Contract.SUPERTEST_NAME_COLUMN));
            Supertest supertest = new Supertest(tid,test_name);
            tests.add(supertest);
        }
        innerCursor.close();
        cursor.close();
        return new Package(id,name, parseDouble(priceString),tests);
    }

    public static LabAppointment getAppointment(Context context,long id){
        SQLiteDatabase db = OpenHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(Contract.LAB_APPOINTMENT_TABLE, null, Contract.LAB_APPOINTMENT_ID_COLUMN + "=?", new String[]{id + ""}, null, null, null);
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
        patientCursor.close();
        cursor.close();
        return new LabAppointment(appointment_id,patient,date,time,tests,isDone);
    }

    public static Supertest getSupertest(Context context,int id){
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.SUPERTEST_TABLE, null, Contract.SUPERTEST_ID_COLUMN + "=?", new String[]{id + ""}, null, null, null);
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
        Supertest supertest = new Supertest(id,name, parseDouble(priceString),tests);
        cursor.close();
        return supertest;
    }

    public static void updateAppointments(Context context,String token){
        OkHttpClient client = new OkHttpClient();//creating client
        Request request = new Request.Builder()//building request
                .url(Constant.APPOINTMENT_URL)
                .header("Authorization", token)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            response.body().close();
            JSONArray appointments = new JSONArray(responseString);
            Log.i("appointments", appointments.toString());
            OpenHelper openHelper = OpenHelper.getInstance(context);
            SQLiteDatabase db = openHelper.getWritableDatabase();
            if(appointments.length()>0){
                db.execSQL("delete from "+ Contract.LAB_APPOINTMENT_TABLE);
            }
            for(int i = 0;i<appointments.length();i++){
                JSONObject appointment = appointments.getJSONObject(i);
                int patient_id = appointment.getInt("patient_id");
                int appointment_id = appointment.getInt("lab_appointment_id");
                String name = appointment.getString("patient_name");
                String address = appointment.getString("address");
                String age = appointment.getString("age");
                String gender = appointment.getString("gender");
                String phone = appointment.getString("phone");
                Patient patient = new Patient(patient_id,name,address,phone,age,gender);
                String date = appointment.getString("date");
                String time = appointment.getString("collection_time");
                String tests = appointment.getString("test_list");
                LabAppointment labAppointment = new LabAppointment(appointment_id,patient,date,time,tests,false);
                openHelper.addLabAppointment(db,labAppointment);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void updateRetailSource(Context context,String token) throws JSONException {
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String response = post(Constant.RETAIL_SOURCE_URL,token,context);
        if(response!=null){
            JSONArray sources = new JSONArray(response);
            if(sources.length() > 0){
                db.execSQL("delete from "+ Contract.RETAIL_SOURCE_TABLE);
                openHelper.addRetailSource(db,new RetailSource(0,"None",""));
                for(int i = 0;i<sources.length();i++){
                    JSONObject source = sources.getJSONObject(i);
                    int id = source.getInt("id");
                    String name = source.getString("name");
                    String address = source.getString("address");
                    RetailSource retailSource = new RetailSource(id,name,address);
                    openHelper.addRetailSource(db,retailSource);
                }
            }
        }
    }

    public static void updateDataBase(Context context,String token) throws JSONException {
        String testResponse;
        String supertestResponse;
        String packagesResponse;
        JSONArray testJSONArray;
        JSONArray supertestJSONArray;
        JSONArray packageJSONArray;
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        testResponse = post(Constant.TEST_URL, token, context);
        if (testResponse != null) {
            testJSONArray = new JSONArray(testResponse);
            if (testJSONArray.length() > 0) {
                db.execSQL("delete from "+ Contract.TEST_TABLE);
                for (int i = 0; i < testJSONArray.length(); i++) {
                    JSONObject jsonObject = testJSONArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    int id = jsonObject.getInt("id");
                    Test test = new Test(id, name);
                    openHelper.addTest(db, test);
                }
            }
            supertestResponse = post(Constant.SUPERTEST_URL, token, context);
            if (supertestResponse != null) {
                supertestJSONArray = new JSONArray(supertestResponse);
                if (supertestJSONArray.length() > 0) {
                    db.execSQL("delete from "+ Contract.SUPERTEST_TABLE);
                    for (int i = 0; i < supertestJSONArray.length(); i++) {
                        JSONObject jsonObject = supertestJSONArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        int id = jsonObject.getInt("id");
                        String price = jsonObject.getString("price");
                        ArrayList<Test> tests = new ArrayList<>();
                        JSONArray testsJSON = jsonObject.getJSONArray("tests");
                        for (int j = 0; j < testsJSON.length(); j++) {
                            JSONObject test = testsJSON.getJSONObject(j);
                            String test_name = test.getString("name");
                            int test_id = test.getInt("id");
                            tests.add(new Test(test_id, test_name));
                        }
                        Supertest supertest = new Supertest(id, name, parseDouble(price), tests);
                        openHelper.addSupertest(db, supertest);
                    }
                }
                packagesResponse = post(Constant.PACKAGE_URL, token, context);
                if (packagesResponse != null) {
                    packageJSONArray = new JSONArray(packagesResponse);
                    if (packageJSONArray.length() > 0) {
                        db.execSQL("delete from "+ Contract.PACKAGE_TABLE);
                        for (int i = 0; i < packageJSONArray.length(); i++) {
                            JSONObject jsonObject = packageJSONArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            int id = jsonObject.getInt("id");
                            Double price = jsonObject.getDouble("price");
                            ArrayList<Supertest> tests = new ArrayList<>();
                            JSONArray testsJSON = jsonObject.getJSONArray("supertests");
                            for (int j = 0; j < testsJSON.length(); j++) {
                                JSONObject test = testsJSON.getJSONObject(j);
                                String test_name = test.getString("name");
                                int test_id = test.getInt("id");
                                tests.add(new Supertest(test_id, test_name));
                            }
                            openHelper.addPackage(db, new models.Package(id, name, price, tests));
                        }
                    }
                }
            }
        }
    }

    public static RetailSource getRetailSource(Context context,int id){
        OpenHelper openHelper = OpenHelper.getInstance(context);
        SQLiteDatabase db = openHelper.getReadableDatabase();
        Cursor cursor = db.query(Contract.RETAIL_SOURCE_TABLE,null,Contract.RETAIL_SOURCE_ID_COLUMN + "=?",new String[]{id + ""},null,null,null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(Contract.RETAIL_SOURCE_NAME));
        String address = cursor.getString(cursor.getColumnIndex(Contract.RETAIL_SOURCE_ADDRESS));
        cursor.close();
        return new RetailSource(id,name,address);
    }

    public static ArrayList<LabAppointment> getAppointments(Context context) {
        ArrayList<LabAppointment> appointments = new ArrayList<>();
        SQLiteDatabase db = OpenHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(Contract.LAB_APPOINTMENT_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()){
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
            LabAppointment labAppointment = new LabAppointment(appointment_id,patient,date,time,tests,isDone);
            appointments.add(labAppointment);
            patientCursor.close();
        }
        cursor.close();
        return appointments;
    }

    static String post(String url,String token,Context context) {
        //Using OkHttp lib
        OkHttpClient client = new OkHttpClient();//creating client

        Request request = new Request.Builder()//building request
                .url(url)
                .header("Authorization",token)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            response.body().close();
            return responseString;
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Cannot connect to server. Please try again later", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static String uploadBills(Context context,String token,String billsJSONArrayString) throws Exception {
        OkHttpClient client = new OkHttpClient();//creating client
        RequestBody requestBody = new MultipartBuilder()//building body part using form-data method
                .type(MultipartBuilder.FORM)
                .addFormDataPart("bills", billsJSONArrayString)
                .build();

        Request request = new Request.Builder()//building request
                .url(Constant.BILLS_URL)
                .header("Authorization",token)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        response.body().close();
        Log.e("response",responseString);
        JSONObject responseJsonObject = new JSONObject(responseString);
        if(responseJsonObject.getInt("bills_received") != responseJsonObject.getInt("bills_created")){
            return "Server Error";
        }
        else return Constant.SUCCESS_MESSAGE;


    }

    public static boolean updateAppointmentStatus(Context context, String token, int appointmentId, int status) throws Exception {
        OkHttpClient client = new OkHttpClient();//creating client
        RequestBody requestBody = new MultipartBuilder()//building body part using form-data method
                .type(MultipartBuilder.FORM)
                .addFormDataPart("lab_appointment_id", appointmentId + "")
                .addFormDataPart("update_status_to",status + "")
                .build();

        Request request = new Request.Builder()//building request
                .url(Constant.UPDATE_APPOINTMENT_STATUS_URL)
                .header("Authorization",token)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        response.body().close();
        Log.e("response",responseString);
        JSONObject responseJsonObject = new JSONObject(responseString);
        if(responseJsonObject.getString("request_status").equals(Constant.UPDATE_APPOINTMENT_SUCCESS_MESSAGE)){
            return true;
        }
        else{
            return false;
        }
    }

    public void deleteTests(Context context){
        SQLiteDatabase db = OpenHelper.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM " + Contract.TEST_TABLE);
    }

    public void deleteSuperTests(Context context){
        SQLiteDatabase db = OpenHelper.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM " + Contract.SUPERTEST_TABLE);
    }

    public void deletePackages(Context context){
        SQLiteDatabase db = OpenHelper.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM " + Contract.PACKAGE_TABLE);
    }
}