package in.kvsc.kalpavrikshpro;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import models.*;
import models.Package;
import utilities.Constant;
import utilities.Contract;
import utilities.OpenHelper;
import utilities.PackageListAdapter;
import utilities.SupertestListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends android.support.v4.app.Fragment {

    private ArrayList<Package> mPackages;
    private ArrayList<Supertest> mSupertests;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_package_list, container, false);
        Bundle bundle = getArguments();
        String mType = bundle.getString("type");

        ExpandableListView listView = (ExpandableListView)fragmentView.findViewById(R.id.expandableListView);


        mPackages = new ArrayList<>();
        mSupertests = new ArrayList<>();

        if (mType != null) {
            if(mType.equals(Constant.PACKAGES)){
                getPackages();
                PackageListAdapter adapter = new PackageListAdapter(getActivity(),mPackages);
                listView.setAdapter(adapter);
            }
            else if (mType.equals(Constant.SUPERTESTS)){
                getSupertests();
                SupertestListAdapter adapter = new SupertestListAdapter(getActivity(),mSupertests);
                listView.setAdapter(adapter);
            }
        }


        return fragmentView;
    }

    private void getSupertests() {
        ArrayList<Supertest> supertestArrayList = new ArrayList<>();
        OpenHelper openHelper = OpenHelper.getInstance(getActivity());
        SQLiteDatabase db = openHelper.getReadableDatabase();

        Cursor cursor = db.query(Contract.SUPERTEST_TABLE,null,null,null,null,null,null);
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
        mSupertests = supertestArrayList;
    }

    private void getPackages() {
        ArrayList<Package> packages = new ArrayList<>();
        OpenHelper openHelper = OpenHelper.getInstance(getActivity());
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
        mPackages = packages;
    }


}
