package in.kvsc.kalpavrikshpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import models.*;
import models.Package;
import utilities.Constant;
import utilities.Utilities;

public class BillActivity extends AppCompatActivity {

    private JSONObject bill;
    private Intent mIntent;
    private double totalCost = 0;
    private int isBillPaid = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mIntent = getIntent();
        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    isBillPaid = 2;
                else
                    isBillPaid = 1;
            }
        });
        try {
            generateBill();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void generateBill() throws JSONException {
        LinearLayout layout = (LinearLayout)findViewById(R.id.billTestPackageContainer);
        bill = new JSONObject();
        bill.put("patient_type", "old");

        long appointment_id = mIntent.getLongExtra(Constant.APPOINTMENT_ID_INTENT_KEY, 0);
        LabAppointment appointment = Utilities.getAppointment(this, appointment_id);
        Patient patient = appointment.getPatient();
        TextView patientNameTextView = (TextView)findViewById(R.id.billNameTextView);
        patientNameTextView.setText(patient.getName());
        TextView genderAgeTextView = (TextView)findViewById(R.id.billGenAgeTextView);
        String genderAge = patient.getGender() + " | " + patient.getAge();
        genderAgeTextView.setText(genderAge);
        TextView phoneTextView = (TextView)findViewById(R.id.billPhoneTextView);
        phoneTextView.setText(patient.getPhone());
        TextView addressTextView = (TextView)findViewById(R.id.billAddressTextView);
        addressTextView.setText(patient.getAddress());

        bill.put("patient_id", patient.getId());

        String referredBy = mIntent.getStringExtra(Constant.REFERRED_BY_INTENT_KEY);
        TextView referredByTextView = (TextView)findViewById(R.id.billReferredByTextView);
        referredByTextView.setText(referredBy);
        bill.put("referred_by", referredBy);

        TextView retailSourceTextView = (TextView)findViewById(R.id.billRetailSourceTextView);
        int retailSourceId = mIntent.getIntExtra(Constant.RETAIL_SOURCE_ID_INTENT_KEY, 0);
        bill.put("patient_source", retailSourceId);
        RetailSource retailSource = Utilities.getRetailSource(this, retailSourceId);
        retailSourceTextView.setText(retailSource.getName());

        String sampleJsonString = mIntent.getStringExtra(Constant.SAMPLE_JSON_STRING);
        JSONObject sample = new JSONObject(sampleJsonString);
        TextView barcodeTextView = (TextView)findViewById(R.id.billBarcodeTextView);
        barcodeTextView.setText(sample.getString("sample_barcode"));
        TextView dateTimeTextView = (TextView)findViewById(R.id.billDateTimeTextView);
        String dateTime = sample.getString("date") + " " + sample.getString("time");
        dateTimeTextView.setText(dateTime);
        TextView plainVialTextView = (TextView)findViewById(R.id.billPlainVialTextView);
        plainVialTextView.setText(sample.getString("plain_vial"));
        TextView edtaVialTextView = (TextView)findViewById(R.id.billEdtaVialTextView);
        edtaVialTextView.setText(sample.getString("edta_vial"));
        TextView fluorideVialTextView = (TextView)findViewById(R.id.billFluorideVialTextView);
        fluorideVialTextView.setText(sample.getString("fluoride_vial"));
        TextView sodCitTextView = (TextView)findViewById(R.id.billSodCitVialTextView);
        sodCitTextView.setText(sample.getString("sodium_citrate_vial"));
        TextView heparinTextView = (TextView)findViewById(R.id.billHeparinVialsTextView);
        heparinTextView.setText(sample.getString("heparin_vial"));
        TextView containerTextView = (TextView)findViewById(R.id.billContainersTextView);
        containerTextView.setText(sample.getString("container"));
        bill.put("sample", sample);

        TextView billTotalTextView = (TextView)findViewById(R.id.billTotalTextView);
        JSONArray supertestsJSON = new JSONArray();
        JSONArray packagesJSON = new JSONArray();
        ArrayList<Integer> supertestIds = mIntent.getIntegerArrayListExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY);
        for(int id:supertestIds){
            Supertest supertest = Utilities.getSupertest(this,id);
            JSONObject supertestJsonObject = new JSONObject();
            supertestJsonObject.put("id",supertest.getId());
            supertestJsonObject.put("name",supertest.getName());
            supertestJsonObject.put("price",supertest.getPrice());
            supertestsJSON.put(supertestJsonObject);
            totalCost = totalCost + supertest.getPrice();
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View testRowView = inflater.inflate(R.layout.test_packages_row,null);
            ImageView iconView = (ImageView)testRowView.findViewById(R.id.iconImageView);
            TextView nameTextView = (TextView)testRowView.findViewById(R.id.testPackageNameTextView);
            TextView priceTextView = (TextView)testRowView.findViewById(R.id.testPackagePriceTextView);
            iconView.setImageResource(R.drawable.test);
            nameTextView.setText(supertest.getName());
            priceTextView.setText(supertest.getPrice() + "");
            layout.addView(testRowView);
        }
        bill.put("supertests", supertestsJSON);

        ArrayList<Integer> packageIds = mIntent.getIntegerArrayListExtra(Constant.PACKAGE_ID_LIST_INTENT_KEY);
        for(int id:packageIds){
            Package packageObject = Utilities.getPackage(this,id);
            JSONObject packageJsonObject = new JSONObject();
            packageJsonObject.put("id",packageObject.getId());
            packageJsonObject.put("name",packageObject.getName());
            packageJsonObject.put("price",packageObject.getPrice());
            packagesJSON.put(packageJsonObject);
            totalCost = totalCost + packageObject.getPrice();
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View testRowView = inflater.inflate(R.layout.test_packages_row,null);
            ImageView iconView = (ImageView)testRowView.findViewById(R.id.iconImageView);
            TextView nameTextView = (TextView)testRowView.findViewById(R.id.testPackageNameTextView);
            TextView priceTextView = (TextView)testRowView.findViewById(R.id.testPackagePriceTextView);
            iconView.setImageResource(R.drawable.package_icon);
            nameTextView.setText(packageObject.getName());
            priceTextView.setText(packageObject.getPrice() + "");
            layout.addView(testRowView);
        }
        bill.put("bill_total",totalCost);
        bill.put("supertest_packages", packagesJSON);
        billTotalTextView.setText(totalCost + "");
//
//        JSONObject patient_details = new JSONObject();
//        patient_details.put("first_name", patient.getName());
//        patient_details.put("last_name", patient.getName());
//        patient_details.put("age", patient.getAge());
//        patient_details.put("gender", patient.getGender());
//        patient_details.put("address", patient.getAddress());
//        patient_details.put("mobile_no", patient.getPhone());
//        bill.put("patient_details",patient_details);

    }

    public void uploadBill(View view){
        EditText discountEditText = (EditText)findViewById(R.id.billDiscountEditText);
        Double discount = Double.parseDouble(discountEditText.getEditableText().toString());
        try {
            bill.put("bill_discount", discount + "");
            bill.put("bill_status", isBillPaid);
            JSONArray billsJSONArray = new JSONArray();
            billsJSONArray.put(bill);
            final Context context = this;
            final String billJSONArrayString = billsJSONArray.toString();
            final String token = GlobalState.getInstance().getToken();
            AsyncTask<String,Void,Boolean> task = new AsyncTask<String, Void, Boolean>() {
                ProgressDialog progressDialog;
                @Override
                protected void onPreExecute() {
                    progressDialog = ProgressDialog.show(context,null,"Uploading...");
                }

                @Override
                protected Boolean doInBackground(String... params) {
                    try {
                        return Utilities.uploadBills(context,token,billJSONArrayString);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    progressDialog.dismiss();
                    if(aBoolean){
                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                    }
                }
            };
            task.execute();
            Log.e("json",bill.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


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


}
