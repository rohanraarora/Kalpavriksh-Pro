package in.kvsc.kalpavrikshpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
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
    Button uploadButton;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        SharedPreferences user_pref = getSharedPreferences(Constant.USER_SHARED_PREFS, MODE_PRIVATE);
        name = user_pref.getString(Constant.USER_NAME, "");
        totalBillsTextView = (TextView)findViewById(R.id.totalBillsTextView);
        billsUploadedTextView = (TextView)findViewById(R.id.billsUploadedTextView);
        uploadButton = (Button)this.findViewById(R.id.uploadButton);
        mContext = this;
        refresh();
    }

    public void refresh(){
        uploadedBills = 0;
        bills = Utilities.getTodayBills(this);
        totalBills = bills.size();
        for(Bill bill:bills){
            if(bill.getStatus() == Bill.UPLOADED){
                uploadedBills++;
            }
        }
        uploadButton.setEnabled(totalBills != uploadedBills);
        totalBillsTextView.setText(totalBills + "");
        billsUploadedTextView.setText(uploadedBills + "");
    }

    public void uploadBills(View view){
        final String token = GlobalState.getInstance().getToken();
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(mContext, null, "Uploading...");
                if(!Utilities.isConnectionAvailable(mContext)){
                    Toast.makeText(mContext,"No Internet Connection",Toast.LENGTH_LONG).show();
                    cancel(true);
                    progressDialog.dismiss();
                }

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
                                bill.setStatus(Bill.UPLOADED);
                                OpenHelper openHelper = OpenHelper.getInstance(mContext);
                                SQLiteDatabase db = openHelper.getWritableDatabase();
                                openHelper.updateBillStatus(db, bill.getId(), Bill.UPLOADED);
                                if (Utilities.updateAppointmentStatus(getApplicationContext(), token, bill.getAppointmentId(), 2)) {
                                    Utilities.updateAppointments(getApplicationContext(),token);
                                }
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

}
