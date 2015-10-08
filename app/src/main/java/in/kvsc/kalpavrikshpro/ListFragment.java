package in.kvsc.kalpavrikshpro;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import models.*;
import models.Package;
import utilities.Constant;
import utilities.PackageListAdapter;
import utilities.SupertestListAdapter;
import utilities.Utilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends android.support.v4.app.Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_package_list, container, false);
        Bundle bundle = getArguments();
        String mType = bundle.getString("type");

        ExpandableListView listView = (ExpandableListView)fragmentView.findViewById(R.id.expandableListView);


        ArrayList<Package> mPackages;
        ArrayList<Supertest> mSupertests;

        if (mType != null) {
            if(mType.equals(Constant.PACKAGES)){
                mPackages = Utilities.getPackages(getActivity());
                PackageListAdapter adapter = new PackageListAdapter(getActivity(), mPackages);
                listView.setAdapter(adapter);
            }
            else if (mType.equals(Constant.SUPERTESTS)){
                mSupertests = Utilities.getSupertests(getActivity());
                SupertestListAdapter adapter = new SupertestListAdapter(getActivity(), mSupertests);
                listView.setAdapter(adapter);
            }
        }


        return fragmentView;
    }
}
