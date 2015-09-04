package in.kvsc.kalpavrikshpro;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import models.LabAppointment;
import models.Patient;
import utilities.AppointmentListAdapter;
import utilities.Constant;
import utilities.Contract;
import utilities.OpenHelper;

public class AppointmentListFragment extends android.support.v4.app.Fragment {

    ArrayList<LabAppointment> mList = new ArrayList<>();


    public AppointmentListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_service_list, container, false);
        final ListView listView = (ListView)view.findViewById(R.id.appointmentListView);
        getAppointments();
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

    private void getAppointments() {
        ArrayList<LabAppointment> appointments = new ArrayList<>();
        SQLiteDatabase db = OpenHelper.getInstance(getActivity()).getReadableDatabase();
        Cursor cursor = db.query(Contract.LAB_APPOINTMENT_TABLE,null,null,null,null,null,null);
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
        }
        mList = appointments;
    }


}
