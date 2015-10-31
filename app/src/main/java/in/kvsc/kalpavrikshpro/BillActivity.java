package in.kvsc.kalpavrikshpro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import models.*;
import models.Package;
import utilities.Constant;
import utilities.OpenHelper;
import utilities.Utilities;

public class BillActivity extends AppCompatActivity {

    private JSONObject bill;
    private Intent mIntent;
    private double totalCost = 0;
    private int isBillPaid = 2;
    private int paidBy = 1;
    String billJSONArrayString;
    EditText discountEditText;
    TextView netAmountTextView;
    TextView discountTextView;
    int netAmount;
    int discountAmount = 0;
    View mainView;
    int appointmentId;
    int totalTests = 0;
    int totalPackages = 0;
    LabAppointment mLabAppointment;
    String[] dialogListTitles = {"No. of Tests","No. of Packages","Total Amount","Discount","Net Amount","Amount Paid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        mainView = findViewById(R.id.billView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        discountEditText = (EditText)findViewById(R.id.billDiscountEditText);
        netAmountTextView = (TextView)findViewById(R.id.billNetAmountTextView);
        discountTextView = (TextView)findViewById(R.id.billDiscountAmountTextView);
        mIntent = getIntent();
        final LinearLayout paymentOptions = (LinearLayout)findViewById(R.id.billPaymentOptionsLayout);
        Spinner spinner = (Spinner)findViewById(R.id.billPaidBySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,new String[]{"Cash","Card"});
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        paidBy = 1;
                        break;
                    case 1:
                        paidBy = 2;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paidBy = 1;
            }
        });
//        CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    isBillPaid = 2;
//                    paymentOptions.setVisibility(View.VISIBLE);
//                }
//                else {
//                    isBillPaid = 1;
//                    paymentOptions.setVisibility(View.GONE);
//                }
//            }
//        });
        try {
            generateBill();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        netAmount = (int)totalCost;
        netAmountTextView.setText(netAmount + " /- Rs");
        discountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String discount = s.toString();
                if(discount!=null) {
                    discount = discount.isEmpty() || discount.equals("") || discount.charAt(0) == '.' ? 0 + discount : discount;
                    Double discountValue = 0.0;
                    try {
                        discountValue = Double.parseDouble(discount);
                    } catch (NumberFormatException e) {
                        discountValue = 0.0;
                        discountEditText.setText("0.0");
                    }
                    if (discountValue > 100.0) {
                        discountValue = 100.0;
                        discountEditText.setText("100.0");
                    } else if (discountValue < 0.0) {
                        discountValue = 0.0;
                        discountEditText.setText("0.0");
                    }
                    netAmount = (int) (totalCost - totalCost * (discountValue / 100.0));
                    discountAmount = (int)totalCost - netAmount;
                    netAmountTextView.setText(netAmount + " /- Rs");
                    discountTextView.setText(discountAmount + " /- Rs");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void generateBill() throws JSONException {
        LinearLayout layout = (LinearLayout)findViewById(R.id.billTestPackageContainer);
        bill = new JSONObject();
        bill.put("patient_type", "old");

        long appointment_id = mIntent.getLongExtra(Constant.APPOINTMENT_ID_INTENT_KEY, 0);
        appointmentId = (int)appointment_id;
        mLabAppointment = Utilities.getAppointment(this, appointment_id);
        Patient patient = mLabAppointment.getPatient();
        TextView patientNameTextView = (TextView)findViewById(R.id.billNameTextView);
        patientNameTextView.setText(patient.getName());
        TextView genderAgeTextView = (TextView)findViewById(R.id.billGenAgeTextView);
        String ageString;
        if(patient.getAge() == null || patient.getAge().equals("null")){
            ageString = "";
        }
        else {
            ageString = " | " + patient.getAge();
        }
        String genderAge = patient.getGender() + ageString;
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
        if(retailSourceId != 0) {
            bill.put("patient_source", retailSourceId);
        }
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
            totalTests++;
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
            totalPackages++;
        }
        bill.put("bill_total",totalCost);
        bill.put("supertest_packages", packagesJSON);
        billTotalTextView.setText(totalCost + " /- Rs");
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

        EditText amountPaidEditText = (EditText)findViewById(R.id.billAmountPaidEditText);
        Double discount = 0.0;
        if(discountEditText.getEditableText().toString() != null && !discountEditText.getEditableText().toString().equals(""))
         discount = Double.parseDouble(discountEditText.getEditableText().toString());
        String amountPaid = amountPaidEditText.getEditableText().toString();
        if(amountPaid == null || amountPaid.trim().equals("")){
            amountPaid = "0.0";
        }
        try {
            bill.put("bill_discount", discount + "");
            bill.put("bill_status", isBillPaid);
            bill.put("paid_by",paidBy);
            bill.put("amount_paid",amountPaid);
            JSONArray billsJSONArray = new JSONArray();
            billsJSONArray.put(bill);
            billJSONArrayString  = billsJSONArray.toString();
            final Context context = this;
            int discountAmount = (int)(discount*totalCost)/100;
            String[] dialogValues = new String[]{totalTests+"",totalPackages+"",totalCost+"",discountAmount+"",(totalCost - discountAmount)+"",amountPaid+""};
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            LinearLayout dialogLayout = new LinearLayout(this);
            dialogLayout.setOrientation(LinearLayout.VERTICAL);
            for(int i = 0;i<6;i++){
                LinearLayout row = (LinearLayout)inflater.inflate(R.layout.row_layout,null);
                TextView title = (TextView)row.findViewById(R.id.row_textView_name);
                TextView  value = (TextView)row.findViewById(R.id.row_textView_price);
                title.setText(dialogListTitles[i]);
                value.setText(dialogValues[i]);
                dialogLayout.addView(row);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm")
                    .setView(dialogLayout)
                    .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String token = GlobalState.getInstance().getToken();
                            AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
                                ProgressDialog progressDialog;

                                @Override
                                protected void onPreExecute() {
                                    progressDialog = ProgressDialog.show(context, null, "Uploading...");
                                }

                                @Override
                                protected String doInBackground(String... params) {
                                    try {
                                        String s = Utilities.uploadBills(context, token, billJSONArrayString);
                                        if (s.equals(Constant.SUCCESS_MESSAGE)) {
                                            if (Utilities.updateAppointmentStatus(context, token, appointmentId, 2)) {
                                                Utilities.updateAppointments(context, token);
                                            }
                                        } else {
                                            //TODO
                                        }
                                        return s;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return e.getMessage();
                                    }
                                }

                                @Override
                                protected void onPostExecute(String a) {
                                    progressDialog.dismiss();
                                    OpenHelper openHelper = OpenHelper.getInstance(context);
                                    SQLiteDatabase db = openHelper.getWritableDatabase();
                                    openHelper.updateLabAppointmentStatus(db, mLabAppointment.getId(), 1);
                                    Snackbar.make(mainView, a, Snackbar.LENGTH_SHORT).show();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String date = format.format(new Date(System.currentTimeMillis()));
                                    Bill saveBill = new Bill(bill.toString(), date, Bill.NOT_UPLOADED, mLabAppointment.getId());
                                    if (a.equals(Constant.SUCCESS_MESSAGE)) {
                                        saveBill.setStatus(Bill.UPLOADED);
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        openHelper.addBill(db, saveBill);
                                        startActivity(intent);
                                    } else {
                                        openHelper.addBill(db, saveBill);
                                    }
                                }
                            };
                            task.execute();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            Log.e("json", bill.toString());
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
        else if(id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
