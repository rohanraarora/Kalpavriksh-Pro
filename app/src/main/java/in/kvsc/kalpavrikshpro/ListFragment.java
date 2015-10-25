package in.kvsc.kalpavrikshpro;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

    EditText searchBox;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_package_list, container, false);
        Bundle bundle = getArguments();
        searchBox = (EditText)fragmentView.findViewById(R.id.search_fragment_listView);
        String mType = bundle.getString("type");
        ExpandableListView listView = (ExpandableListView)fragmentView.findViewById(R.id.expandableListView);
        ArrayList<Package> mPackages;
        ArrayList<Supertest> mSupertests;
        if (mType != null) {
            if(mType.equals(Constant.PACKAGES)){
                mPackages = Utilities.getPackages(getActivity());
                final PackageListAdapter adapter = new PackageListAdapter(getActivity(), mPackages);
                listView.setAdapter(adapter);
                searchBox.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
            else if (mType.equals(Constant.SUPERTESTS)){
                mSupertests = Utilities.getSupertests(getActivity());
                final SupertestListAdapter adapter = new SupertestListAdapter(getActivity(), mSupertests);
                listView.setAdapter(adapter);
                searchBox.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.filter(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }
        return fragmentView;
    }
}
