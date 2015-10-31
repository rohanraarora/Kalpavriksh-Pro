package in.kvsc.kalpavrikshpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import models.Bill;
import utilities.Constant;
import utilities.OpenHelper;
import utilities.Utilities;

public class SummaryActivity extends AppCompatActivity {

    String name;
    int totalBills = 0;
    int uploadedBills = 0;
    ArrayList<Bill> bills;
    TextView totalBillsTextView;
    TextView billsUploadedTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        SharedPreferences user_pref = getSharedPreferences(Constant.USER_SHARED_PREFS, MODE_PRIVATE);
        name = user_pref.getString(Constant.USER_NAME, "");
        totalBillsTextView = (TextView)findViewById(R.id.totalBillsTextView);
        billsUploadedTextView = (TextView)findViewById(R.id.billsUploadedTextView);
        refresh();
    }

    public void refresh(){
        bills = Utilities.getTodayBills(this);
        totalBills = bills.size();
        for(Bill bill:bills){
            if(bill.getStatus() == Bill.UPLOADED){
                uploadedBills++;
            }
        }
        totalBillsTextView.setText(totalBills + "");
        billsUploadedTextView.setText(uploadedBills + "");
    }

    public void uploadBills(View view){
        final String token = GlobalState.getInstance().getToken();
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(getApplicationContext(), null, "Uploading...");
            }

            @Override
            protected String doInBackground(String... params) {
                for(Bill bill:bills) {
                    if (bill.getStatus() == Bill.NOT_UPLOADED) {
                        try {
                            JSONObject billJson = new JSONObject(bill.getBill());
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(billJson);
                            String s = Utilities.uploadBills(getApplicationContext(), token, jsonArray.toString());
                            if (s.equals(Constant.SUCCESS_MESSAGE)) {

                                if (Utilities.updateAppointmentStatus(getApplicationContext(), token, bill.getAppointmentId(), 2)) {
                                }
                            } else {
                                //TODO
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                return "Done";

            }

            @Override
            protected void onPostExecute(String a) {
                progressDialog.dismiss();
                refresh();
            }
        };
        task.execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
