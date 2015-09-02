package utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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
                + Contract.PACKAGE_ID_COLUMN + " INTEGER PRIMARY KEY, "
                + Contract.PACKAGE_NAME_COLUMN + " TEXT, "
                + Contract.PACKAGE_PRICE_COLUMN + " TEXT)");

        db.execSQL("CREATE TABLE " + Contract.PACKAGE_SUPERTEST_TABLE + " ("
                + Contract.PACKAGE_SUPERTEST_ID_COLUMN + " INTEGER PRIMARY KEY, "
                + Contract.PACKAGE_SUPERTEST_PID_COLUMN + " INTEGER, "
                + Contract.PACKAGE_SUPERTEST_STID_COLUMN + " INTEGER)");

        db.execSQL("CREATE TABLE " + Contract.SUPERTEST_TABLE + " ("
                + Contract.SUPERTEST_ID_COLUMN + " INTEGER PRIMARY KEY, "
                + Contract.SUPERTEST_NAME_COLUMN + " TEXT, "
                + Contract.SUPERTEST_PRICE_COLUMN + " TEXT)");

        db.execSQL("CREATE TABLE " + Contract.SUPERTEST_TEST_TABLE + " ("
                + Contract.SUPERTEST_TEST_ID_COLUMN + " INTEGER PRIMARY KEY, "
                + Contract.SUPERTEST_TEST_STID_COLUMN + " INTEGER, "
                + Contract.SUPERTEST_TEST_TID_COLUMN + " INTEGER)");

        db.execSQL("CREATE TABLE " + Contract.TEST_TABLE + " ("
                + Contract.TEST_ID_COLUMN + " INTEGER PRIMARY KEY, "
                + Contract.Test_Name_COLUMN + " TEXT)");

        db.execSQL("CREATE TABLE " + Contract.PATIENT_TABLE + " ("
                + Contract.PATIENT_ID_COL + " INTEGERT PRIMARY KEY, "
                + Contract.PATIENT_NAME_COL + " TEXT, "
                + Contract.PATIENT_ADDRESS_COL + " TEXT, "
                + Contract.PATIENT_PHONE_COL + " TEXT, "
                + Contract.PATIENT_AGE_COL + " TEXT, "
                + Contract.PATIENT_GENDER_COL + " TEXT)");

        db.execSQL("CREATE TABLE " + Contract.LAB_APPOINTMENT_TABLE + " ("
                + Contract.LAB_APPOINTMENT_ID_COLUMN + " INTEGER PRIMARY KEY, "
                + Contract.LAB_APPOINTMENT_PATIENT_ID_COL + " INTEGER, "
                + Contract.LAB_APPOINTMENT_DATE_COLUMN + " TEXT, "
                + Contract.LAB_APPOINTMENT_TIME_COLUMN + " TEXT, "
                + Contract.LAB_APPOINTMENT_EPOCH_COL + " TEXT, "
                + Contract.LAB_APPOINTMENT_TESTS_COLUMN + " TEXT, "
                + Contract.LAB_APPOINTMENT_ISDONE_COLUMN + " INTEGER)");

        addDummyData(db);

    }

    private void addDummyData(SQLiteDatabase db) {



        Test test1 = new Test(1,"Thyroxine \\r\\n(T4)");
        Test test2 = new Test(2,"Thyroid Stimulating Hormone\\r\\n(TSH)");
        Test test3 = new Test(3,"Triiodothyronine\\r\\n(T3)");
        Test test4 = new Test(4,"Dehydroepiandrosterone Sulfate\\r\\n(DHEA-S)");
        Test test5 = new Test(5,"Progesterone");


        addTest(db,test1);
        addTest(db,test2);
        addTest(db,test3);
        addTest(db,test4);
        addTest(db,test5);

        ArrayList<Test> testList1 = new ArrayList<>();
        testList1.add(test1);

        ArrayList<Test> testList2 = new ArrayList<>();
        testList2.add(test1);
        testList2.add(test2);

        ArrayList<Test> testList3 = new ArrayList<>();
        testList3.add(test1);
        testList3.add(test2);
        testList3.add(test3);

        Supertest supertest1 = new Supertest(358,"Thyroxine (T4)",160.0,testList1);
        Supertest supertest2 = new Supertest(359,"Thyroid Stimulating Hormone (TSH)",350.0,testList2);
        Supertest supertest3 = new Supertest(360,"Thyroid Function Test (TFT)",500.0,testList3);


        addSupertest(db,supertest1);
        addSupertest(db,supertest2);
        addSupertest(db,supertest3);


        ArrayList<Supertest> supertestArrayList1 = new ArrayList<>();
        supertestArrayList1.add(supertest1);

        ArrayList<Supertest> supertestArrayList2 = new ArrayList<>();
        supertestArrayList2.add(supertest1);
        supertestArrayList2.add(supertest2);

        ArrayList<Supertest> supertestArrayList3 = new ArrayList<>();
        supertestArrayList3.add(supertest1);
        supertestArrayList3.add(supertest2);
        supertestArrayList3.add(supertest3);

        Package package1 = new Package(1,"Package 1",1999.0,supertestArrayList1);
        Package package2 = new Package(2,"Package 2",1699.0,supertestArrayList2);
        Package package3 = new Package(3,"Package 3",2499.0,supertestArrayList3);

        addPackage(db,package1);
        addPackage(db,package2);
        addPackage(db,package3);

        Patient patient = new Patient(1,"Sahib","Hostel","9909090900","21","M");
        LabAppointment appointment = new LabAppointment(1,patient,"Sep-6","4:00","TEST 1",false);
        addLabAppointment(db,appointment);
    }

    public long addTest(SQLiteDatabase db,Test test){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.TEST_ID_COLUMN,test.getId());
        contentValues.put(Contract.Test_Name_COLUMN,test.getName());
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


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
