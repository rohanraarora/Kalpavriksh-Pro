package in.kvsc.kalpavrikshpro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import models.LabAppointment;
import utilities.AppointmentListAdapter;
import utilities.Constant;
import utilities.Utilities;

public class AppointmentListFragment extends android.support.v4.app.Fragment {

    ArrayList<LabAppointment> mList = new ArrayList<>();


    public AppointmentListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);
        final ListView listView = (ListView)view.findViewById(R.id.appointmentListView);
        mList = Utilities.getAppointments(getActivity());
        AppointmentListAdapter adapter = new AppointmentListAdapter(getActivity(),mList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detail = new Intent(getActivity(),DetailAppointmentActivity.class);
                detail.putExtra(Constant.APPOINTMENT_ID_INTENT_KEY,id);
                startActivity(detail);
            }
        });
        return view;
    }
}
