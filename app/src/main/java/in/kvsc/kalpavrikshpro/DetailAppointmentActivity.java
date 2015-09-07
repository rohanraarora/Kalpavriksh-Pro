package in.kvsc.kalpavrikshpro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import models.LabAppointment;
import models.Patient;
import utilities.Constant;
import utilities.Utilities;

public class DetailAppointmentActivity extends AppCompatActivity {

    private LabAppointment mAppointment;

    ArrayList<Integer> mSelectedSupertestIds;
    ArrayList<Integer> mSelectedSsupertestPositions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSelectedSupertestIds = new ArrayList<>();
        mSelectedSsupertestPositions = new ArrayList<>();
        Intent intent = getIntent();
        long appointment_id = intent.getLongExtra(Constant.APPOINTMENT_ID_INTENT_KEY,0l);
        mAppointment = Utilities.getAppointment(this,appointment_id);
        setData();
    }

    public void setData(){
        TextView nameTextView = (TextView)this.findViewById(R.id.name_textView);
        TextView addressTextView = (TextView)this.findViewById(R.id.address_textView);
        TextView phoneTextView = (TextView)this.findViewById(R.id.phone_textView);
        TextView genderAgeTextView = (TextView)this.findViewById(R.id.gen_age_textView);
        TextView dateTextView = (TextView)this.findViewById(R.id.date_textView);
        TextView timeTextView = (TextView)this.findViewById(R.id.time_textView);
        TextView remarksTextView = (TextView)this.findViewById(R.id.remarks_textView);

        Patient patient = mAppointment.getPatient();
        nameTextView.setText(patient.getName());
        addressTextView.setText(patient.getAddress());
        phoneTextView.setText(patient.getPhone());
        String genAgeString;
        if(patient.getAge()!=null){
            genAgeString = patient.getGender() + " | " + patient.getAge();
        }
        else{
            genAgeString = patient.getGender();
        }
        genderAgeTextView.setText(genAgeString);
        dateTextView.setText(mAppointment.getDate());
        timeTextView.setText(mAppointment.getCollection_time());
        remarksTextView.setText(mAppointment.getTests());
    }

    public void onSaveButtonClicked(View view){

    }

    public void onAddEditTestButtonClicked(View view){
        Intent intent = new Intent(this,MultiSelectorListActivity.class);
        intent.putIntegerArrayListExtra(Constant.SUPERTEST_POSITIONS_LIST_INTENT_KEY,mSelectedSsupertestPositions);
        startActivityForResult(intent, Constant.REQUEST_CODE_SUPERTESTS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_appointment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUEST_CODE_SUPERTESTS:
                if(resultCode == Constant.RESULT_CODE_SUCCESS && data != null){
                    mSelectedSsupertestPositions = data.getIntegerArrayListExtra(Constant.SUPERTEST_POSITIONS_LIST_INTENT_KEY);
                    mSelectedSupertestIds = data.getIntegerArrayListExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY);
                }
        }
    }
}
