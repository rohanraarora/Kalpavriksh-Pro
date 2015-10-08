package utilities;
/**
 * Created by Rohan on 8/26/2015.
 *
 */
public class Contract {
    
    public static final String PACKAGE_TABLE = "packages";
    public static final String PACKAGE_ID_COLUMN = "package_id";
    public static final String PACKAGE_NAME_COLUMN = "package_name";
    public static final String PACKAGE_PRICE_COLUMN = "package_price";
    
    public static final String PACKAGE_SUPERTEST_TABLE = "packages_supertests";
    public static final String PACKAGE_SUPERTEST_ID_COLUMN = "package_supertest_id";
    public static final String PACKAGE_SUPERTEST_PID_COLUMN = "package_id";
    public static final String PACKAGE_SUPERTEST_STID_COLUMN = "supertest_id";
    
    public static final String SUPERTEST_TABLE = "supertests";
    public static final String SUPERTEST_ID_COLUMN = "supertest_id";
    public static final String SUPERTEST_NAME_COLUMN = "supertest_name";
    public static final String SUPERTEST_PRICE_COLUMN = "supertest_price";
    
    public static final String SUPERTEST_TEST_TABLE = "supertests_tests";
    public static final String SUPERTEST_TEST_ID_COLUMN = "supertest_test_id";
    public static final String SUPERTEST_TEST_STID_COLUMN = "supertest_id";
    public static final String SUPERTEST_TEST_TID_COLUMN = "test_id";

    public static final String TEST_TABLE = "tests";
    public static final String TEST_ID_COLUMN = "test_id";
    public static final String Test_Name_COLUMN = "test_name";

    public static final String PATIENT_TABLE = "patients";
    public static final String PATIENT_ID_COL = "patient_id";
    public static final String PATIENT_NAME_COL = "patient_name";
    public static final String PATIENT_ADDRESS_COL = "address";
    public static final String PATIENT_PHONE_COL = "phone";
    public static final String PATIENT_AGE_COL = "age";
    public static final String PATIENT_GENDER_COL = "gender";

    public static final String LAB_APPOINTMENT_TABLE = "lab_appointments";
    public static final String LAB_APPOINTMENT_ID_COLUMN = "lab_appointment_id";
    public static final String LAB_APPOINTMENT_PATIENT_ID_COL = "appintment_patient_id";
    public static final String LAB_APPOINTMENT_EPOCH_COL = "appointment_epoch";
    public static final String LAB_APPOINTMENT_DATE_COLUMN = "lab_appointment_date";
    public static final String LAB_APPOINTMENT_TIME_COLUMN = "lab_appointment_time";
    public static final String LAB_APPOINTMENT_ISDONE_COLUMN = "is_done";
    public static final String LAB_APPOINTMENT_TESTS_COLUMN = "appintment_tests";

    public static final String RETAIL_SOURCE_TABLE = "retail_sources";
    public static final String RETAIL_SOURCE_ID_COLUMN = "retail_source_id";
    public static final String RETAIL_SOURCE_ADDRESS = "retail_source_address";
    public static final String RETAIL_SOURCE_NAME = "retail_source_name";

}
