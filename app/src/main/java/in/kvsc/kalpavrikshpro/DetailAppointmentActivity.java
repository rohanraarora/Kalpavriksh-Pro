package in.kvsc.kalpavrikshpro;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

import models.*;
import utilities.Constant;
import utilities.Utilities;

public class DetailAppointmentActivity extends AppCompatActivity {

    private LabAppointment mAppointment;

    Button addEditButton;
    LinearLayout testsLayout;
    Button saveButton;
    ArrayList<LabItem> mLabItems;
    ArrayList<Integer> mSelectedSupertestIds;
    ArrayList<Integer> mSelectedPackagesIds;
    Intent mIntent;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSelectedSupertestIds = new ArrayList<>();
        mSelectedPackagesIds = new ArrayList<>();
        mLabItems = new ArrayList<>();
        addEditButton = (Button)this.findViewById(R.id.addEditTestButton);
        testsLayout = (LinearLayout)this.findViewById(R.id.testsLinearLayout);
        saveButton = (Button)this.findViewById(R.id.saveButton);
        mIntent = getIntent();
        long appointment_id = mIntent.getLongExtra(Constant.APPOINTMENT_ID_INTENT_KEY,0l);
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

        patient = mAppointment.getPatient();
        nameTextView.setText(patient.getName());
        addressTextView.setText(patient.getAddress());
        phoneTextView.setText(patient.getPhone());
        String genAgeString;
        String ageString;
        if(patient.getAge() == null || patient.getAge().equals("null")){
            ageString = "";
        }
        else {
            ageString = " | " + patient.getAge();
        }
        genAgeString = patient.getGender() + ageString;
        genderAgeTextView.setText(genAgeString);
        dateTextView.setText(mAppointment.getDate());
        timeTextView.setText(mAppointment.getCollection_time());
        remarksTextView.setText(mAppointment.getTests());
    }

    public void onSaveButtonClicked(View view){
        mIntent.setClass(this, ScannerActivity.class);
        mIntent.putExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY, mSelectedSupertestIds);
        mIntent.putExtra(Constant.PACKAGE_ID_LIST_INTENT_KEY,mSelectedPackagesIds);
        startActivity(mIntent);
    }

    public void onAddEditTestButtonClicked(View view){
        Intent intent = new Intent(this,MultiSelectorListActivity.class);
        intent.putExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY, mSelectedSupertestIds);
        intent.putExtra(Constant.PACKAGE_ID_LIST_INTENT_KEY,mSelectedPackagesIds);
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
        if (id == R.id.action_call) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + patient.getPhone()));
            startActivity(callIntent);
        }
        else if(id == R.id.action_navigate){
            String[] address = patient.getAddress().split(" ");
            String nav = "";
            for(int i = 0;i<address.length;i++){
                String s = address[i];
                if(i != address.length - 1){
                    s = s+"+";
                }
                nav = nav + s;
            }
            Uri geoLocation = Uri.parse("google.navigation:q=" + nav);
            showMap(geoLocation);
        }
        else if(id == R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constant.REQUEST_CODE_SUPERTESTS:
                if(resultCode == Constant.RESULT_CODE_SUCCESS && data != null){
                    mSelectedSupertestIds = data.getIntegerArrayListExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY);
                    mSelectedPackagesIds = data.getIntegerArrayListExtra(Constant.PACKAGE_ID_LIST_INTENT_KEY);
                    testsLayout.removeAllViews();
                    mLabItems.clear();
                    if(mSelectedSupertestIds.size() + mSelectedPackagesIds.size() > 0){
                        addEditButton.setText("Edit");
                        testsLayout.setVisibility(View.VISIBLE);
                        saveButton.setVisibility(View.VISIBLE);
                        for(int id:mSelectedSupertestIds){
                            Supertest supertest = Utilities.getSupertest(this,id);
                            mLabItems.add(supertest);
                            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View testRowView = inflater.inflate(R.layout.test_packages_row,null);
                            ImageView iconView = (ImageView)testRowView.findViewById(R.id.iconImageView);
                            TextView nameTextView = (TextView)testRowView.findViewById(R.id.testPackageNameTextView);
                            TextView priceTextView = (TextView)testRowView.findViewById(R.id.testPackagePriceTextView);
                            iconView.setImageResource(R.drawable.test);
                            nameTextView.setText(supertest.getName());
                            priceTextView.setText(supertest.getPrice() + "");
                            testsLayout.addView(testRowView);
                        }
                        for(int id:mSelectedPackagesIds){
                            models.Package packageObject = Utilities.getPackage(this,id);
                            mLabItems.add(packageObject);
                            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
                            View testRowView = inflater.inflate(R.layout.test_packages_row,null);
                            ImageView iconView = (ImageView)testRowView.findViewById(R.id.iconImageView);
                            TextView nameTextView = (TextView)testRowView.findViewById(R.id.testPackageNameTextView);
                            TextView priceTextView = (TextView)testRowView.findViewById(R.id.testPackagePriceTextView);
                            iconView.setImageResource(R.drawable.package_icon);
                            nameTextView.setText(packageObject.getName());
                            priceTextView.setText(packageObject.getPrice() + "");
                            testsLayout.addView(testRowView);
                        }
                    }
                    else{
                        addEditButton.setText("Add");
                        testsLayout.setVisibility(View.GONE);
                        saveButton.setVisibility(View.GONE);
                    }
                }
        }
    }
}
