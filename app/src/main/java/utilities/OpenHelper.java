package utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import models.*;
import models.Package;

/**
 * Created by Rohan on 8/26/2015.
 *
 */
public class OpenHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DB_NAME = "kalpavriksh_pro_db";
    Context mContext;
    private static OpenHelper mOpenHelper;

    public OpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        mContext = context;
    }

    public static OpenHelper getInstance(Context context){
        if(mOpenHelper == null){
            mOpenHelper = new OpenHelper(context);
        }
        return mOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Contract.PACKAGE_TABLE + " ("
                + Contract.PACKAGE_ID_COLUMN + " INTEGER, "
                + Contract.PACKAGE_NAME_COLUMN + " TEXT, "
                + Contract.PACKAGE_PRICE_COLUMN + " TEXT)");

        db.execSQL("CREATE TABLE " + Contract.PACKAGE_SUPERTEST_TABLE + " ("
                + Contract.PACKAGE_SUPERTEST_ID_COLUMN + " INTEGER PRIMARY KEY, "
                + Contract.PACKAGE_SUPERTEST_PID_COLUMN + " INTEGER, "
                + Contract.PACKAGE_SUPERTEST_STID_COLUMN + " INTEGER)");

        db.execSQL("CREATE TABLE " + Contract.SUPERTEST_TABLE + " ("
                + Contract.SUPERTEST_ID_COLUMN + " INTEGER, "
                + Contract.SUPERTEST_NAME_COLUMN + " TEXT, "
                + Contract.SUPERTEST_PRICE_COLUMN + " TEXT)");

        db.execSQL("CREATE TABLE " + Contract.SUPERTEST_TEST_TABLE + " ("
                + Contract.SUPERTEST_TEST_ID_COLUMN + " INTEGER PRIMARY KEY, "
                + Contract.SUPERTEST_TEST_STID_COLUMN + " INTEGER, "
                + Contract.SUPERTEST_TEST_TID_COLUMN + " INTEGER)");

        db.execSQL("CREATE TABLE " + Contract.TEST_TABLE + " ("
                + Contract.TEST_ID_COLUMN + " INTEGER, "
                + Contract.Test_Name_COLUMN + " TEXT)");

        db.execSQL("CREATE TABLE " + Contract.PATIENT_TABLE + " ("
                + Contract.PATIENT_ID_COL + " INTEGERT, "
                + Contract.PATIENT_NAME_COL + " TEXT, "
                + Contract.PATIENT_ADDRESS_COL + " TEXT, "
                + Contract.PATIENT_PHONE_COL + " TEXT, "
                + Contract.PATIENT_AGE_COL + " TEXT, "
                + Contract.PATIENT_GENDER_COL + " TEXT)");

        db.execSQL("CREATE TABLE " + Contract.LAB_APPOINTMENT_TABLE + " ("
                + Contract.LAB_APPOINTMENT_ID_COLUMN + " INTEGER, "
                + Contract.LAB_APPOINTMENT_PATIENT_ID_COL + " INTEGER, "
                + Contract.LAB_APPOINTMENT_DATE_COLUMN + " TEXT, "
                + Contract.LAB_APPOINTMENT_TIME_COLUMN + " TEXT, "
                + Contract.LAB_APPOINTMENT_EPOCH_COL + " TEXT, "
                + Contract.LAB_APPOINTMENT_TESTS_COLUMN + " TEXT, "
                + Contract.LAB_APPOINTMENT_ISDONE_COLUMN + " INTEGER)");

        db.execSQL("CREATE TABLE " + Contract.RETAIL_SOURCE_TABLE + " ("
                + Contract.RETAIL_SOURCE_ID_COLUMN + " INTEGER, "
                + Contract.RETAIL_SOURCE_NAME + " TEXT, "
                + Contract.RETAIL_SOURCE_ADDRESS + " TEXT)");

        db.execSQL("CREATE TABLE " + Contract.BILL_TABLE + " ("
                + Contract.BILL_ID + " INTEGER PRIMARY KEY, "
                + Contract.BILL_DATE + " TEXT, "
                + Contract.BILL_UPLOAD_STATUS + " INTEGER, "
                + Contract.BILL_APPOINTMENT_ID + " INTEGER, "
                + Contract.BILL_JSON_STRING + " TEXT)");

    }


    public void addRetailSource(SQLiteDatabase db, RetailSource retailSource){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.RETAIL_SOURCE_ID_COLUMN, retailSource.getId());
        contentValues.put(Contract.RETAIL_SOURCE_NAME, retailSource.getName());
        contentValues.put(Contract.RETAIL_SOURCE_ADDRESS, retailSource.getAddress());
        db.insert(Contract.RETAIL_SOURCE_TABLE, null, contentValues);
    }

    public long addTest(SQLiteDatabase db,Test test){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.TEST_ID_COLUMN,test.getId());
        contentValues.put(Contract.Test_Name_COLUMN, test.getName());
        return db.insert(Contract.TEST_TABLE,null,contentValues);
    }

    public long addSupertest(SQLiteDatabase db,Supertest supertest){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.SUPERTEST_ID_COLUMN,supertest.getId());
        contentValues.put(Contract.SUPERTEST_NAME_COLUMN,supertest.getName());
        contentValues.put(Contract.SUPERTEST_PRICE_COLUMN, supertest.getPrice());

        for(Test test:supertest.getTests()){
            ContentValues testListContentValues = new ContentValues();
            testListContentValues.put(Contract.SUPERTEST_TEST_STID_COLUMN,supertest.getId());
            testListContentValues.put(Contract.SUPERTEST_TEST_TID_COLUMN,test.getId());
            db.insert(Contract.SUPERTEST_TEST_TABLE,null,testListContentValues);
        }

        return db.insert(Contract.SUPERTEST_TABLE,null,contentValues);
    }

    public long addPackage(SQLiteDatabase db,Package packages){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.PACKAGE_ID_COLUMN,packages.getId());
        contentValues.put(Contract.PACKAGE_NAME_COLUMN,packages.getName());
        contentValues.put(Contract.PACKAGE_PRICE_COLUMN,packages.getPrice());

        for(Supertest supertest:packages.getSupertests()){
            ContentValues supertestListContentValues = new ContentValues();
            supertestListContentValues.put(Contract.PACKAGE_SUPERTEST_PID_COLUMN,packages.getId());
            supertestListContentValues.put(Contract.PACKAGE_SUPERTEST_STID_COLUMN,supertest.getId());
            db.insert(Contract.PACKAGE_SUPERTEST_TABLE,null,supertestListContentValues);
        }

        return db.insert(Contract.PACKAGE_TABLE,null,contentValues);
    }

    public void addLabAppointment(SQLiteDatabase db,LabAppointment labAppointment){
        ContentValues patientValues = new ContentValues();
        ContentValues appointmentValues = new ContentValues();

        Patient patient = labAppointment.getPatient();
        patientValues.put(Contract.PATIENT_ID_COL,patient.getId());
        patientValues.put(Contract.PATIENT_ADDRESS_COL,patient.getAddress());
        patientValues.put(Contract.PATIENT_AGE_COL,patient.getAge());
        patientValues.put(Contract.PATIENT_GENDER_COL,patient.getGender());
        patientValues.put(Contract.PATIENT_NAME_COL,patient.getName());
        patientValues.put(Contract.PATIENT_PHONE_COL,patient.getPhone());
        db.insert(Contract.PATIENT_TABLE, null, patientValues);

        appointmentValues.put(Contract.LAB_APPOINTMENT_DATE_COLUMN, labAppointment.getDate());
        appointmentValues.put(Contract.LAB_APPOINTMENT_EPOCH_COL,labAppointment.getTimeInEpoch());
        appointmentValues.put(Contract.LAB_APPOINTMENT_ID_COLUMN,labAppointment.getId());
        appointmentValues.put(Contract.LAB_APPOINTMENT_ISDONE_COLUMN,0);//TODO
        appointmentValues.put(Contract.LAB_APPOINTMENT_PATIENT_ID_COL,patient.getId());
        appointmentValues.put(Contract.LAB_APPOINTMENT_TIME_COLUMN,labAppointment.getCollection_time());
        appointmentValues.put(Contract.LAB_APPOINTMENT_TESTS_COLUMN,labAppointment.getTests());
        db.insert(Contract.LAB_APPOINTMENT_TABLE,null,appointmentValues);
    }

    public void addBill(SQLiteDatabase db,Bill bill){
        ContentValues billValues = new ContentValues();
        billValues.put(Contract.BILL_DATE,bill.getDate());
        billValues.put(Contract.BILL_JSON_STRING,bill.getBill());
        billValues.put(Contract.BILL_UPLOAD_STATUS,bill.getStatus());
        billValues.put(Contract.BILL_APPOINTMENT_ID,bill.getAppointmentId());
        db.insert(Contract.BILL_TABLE,null,billValues);
    }

    public void updateLabAppointmentStatus(SQLiteDatabase db,int labAppointmentId,int status){
        String query = "UPDATE " + Contract.LAB_APPOINTMENT_TABLE + " SET " + Contract.LAB_APPOINTMENT_ISDONE_COLUMN + " = " + status + " WHERE " + Contract.LAB_APPOINTMENT_ID_COLUMN + " = " + labAppointmentId;
        db.rawQuery(query,null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
