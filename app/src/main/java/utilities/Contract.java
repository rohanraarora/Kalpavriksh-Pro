package utilities;

import android.provider.BaseColumns;

/**
 * Created by Rohan on 8/26/2015.
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
}
