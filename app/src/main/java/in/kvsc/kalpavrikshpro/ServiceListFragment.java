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
public class ServiceListFragment extends android.support.v4.app.Fragment {

    private String mType;
    private ArrayList<Package> mPackages;
    private ArrayList<Supertest> mSupertests;

    public ServiceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_service_list, container, false);



        return fragmentView;
    }



}
