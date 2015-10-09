package utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import in.kvsc.kalpavrikshpro.R;
import models.LabAppointment;
import models.Patient;

/**
 * Created by Rohan on 9/3/2015.
 *
 */
public class AppointmentListAdapter extends BaseAdapter{

    Context mContext;
    ArrayList<LabAppointment> mList;
    public AppointmentListAdapter(Context context,ArrayList<LabAppointment> list){
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public LabAppointment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View output = convertView;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            output = inflater.inflate(R.layout.appointment_row,null);
        }
        LabAppointment labAppointment = getItem(position);
        final Patient patient = labAppointment.getPatient();
        ImageView callAction = (ImageView)output.findViewById(R.id.callActionImageView);
        callAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + patient.getPhone()));
                mContext.startActivity(callIntent);
            }
        });
        TextView nameTextView = (TextView)output.findViewById(R.id.patientName_textView);
        nameTextView.setText(patient.getName());

        TextView addressTextView = (TextView)output.findViewById(R.id.address_textView);
        addressTextView.setText(patient.getAddress());

        TextView dateTextView = (TextView)output.findViewById(R.id.date_textView);
        dateTextView.setText(labAppointment.getDate());

        TextView timeTextView = (TextView)output.findViewById(R.id.time_textView);
        timeTextView.setText(labAppointment.getCollection_time());
        return output;
    }
}
