package in.kvsc.kalpavrikshpro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import models.LabAppointment;
import models.Patient;
import utilities.Constant;
import utilities.OpenHelper;
import utilities.Utilities;

public class LoginActivity extends AppCompatActivity {
    EditText mEmailEditText;//username EditText
    EditText mPassEditText;//password EditText
    String mEmailString;
    String mPassString;
    View parentView;//Container for snackbar
    Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalState globalState = GlobalState.getInstance();
        if(globalState.isAunthenticated()){
            Intent homeIntent = new Intent(this,HomeActivity.class);
            startActivity(homeIntent);
            finish();
        }
        else {
            setContentView(R.layout.activity_login);
            parentView = this.findViewById(R.id.login_screen);
            thisActivity = this;
            mEmailEditText = (EditText) this.findViewById(R.id.editText_username);
            mPassEditText = (EditText) this.findViewById(R.id.editText_password);
        }
    }

    public void onLoginClicked(View view){
        View currentView = this.getCurrentFocus();
        if (currentView != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
        }
        mEmailString = mEmailEditText.getText().toString();
        mPassString = mPassEditText.getText().toString();
        if(mEmailString.replaceAll("\\s+","").equals("")){
            //To check empty email
            mEmailEditText.setError("Enter username");
        }
        else if(mPassString.replaceAll("\\s+","").equals("")){
            mPassEditText.setError("Enter password");
        }
        else {
            LogIn();
        }
    }
    public void LogIn(){
        if(Utilities.isConnectionAvailable(this)){
            new LogInAsyncTask().execute();
        }
        else{
            Snackbar.make(parentView, "Please connect to Internet", Snackbar.LENGTH_LONG).show();
        }
    }
    private class LogInAsyncTask extends AsyncTask<String, Void, String> {
        private static final String SUCCESS = "success";
        ProgressDialog mProgressDialog;

        private void getAppointments(Context context){
            OkHttpClient client = new OkHttpClient();//creating client

            Request request = new Request.Builder()//building request
                    .url(Constant.APPOINTMENT_URL)
                    .header("Authorization", GlobalState.getInstance().getToken())
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseString = response.body().string();
                response.body().close();
                JSONArray appointments = new JSONArray(responseString);
                Log.i("appointments",appointments.toString());
                OpenHelper openHelper = OpenHelper.getInstance(context);
                SQLiteDatabase db = openHelper.getWritableDatabase();
                for(int i = 0;i<appointments.length();i++){
                    JSONObject appointment = appointments.getJSONObject(i);
                    int patient_id = appointment.getInt("patient_id");
                    int appointment_id = appointment.getInt("lab_appointment_id");
                    String name = appointment.getString("patient_name");
                    String address = appointment.getString("address");
                    String age = appointment.getString("age");
                    String gender = appointment.getString("gender");
                    String phone = appointment.getString("phone");
                    Patient patient = new Patient(patient_id,name,address,phone,age,gender);
                    String date = appointment.getString("date");
                    String time = appointment.getString("collection_time");
                    String tests = appointment.getString("test_list");
                    LabAppointment labAppointment = new LabAppointment(appointment_id,patient,date,time,tests,false);
                    openHelper.addLabAppointment(db,labAppointment);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            String response;
            JSONObject jsonObject = new JSONObject();
            try {
                response = post(Constant.LOGIN_URL);
                jsonObject = new JSONObject(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonObject!= null) {
                if(jsonObject.has("id")){
                    try {
                        long id = jsonObject.getLong("id");
                        String token = jsonObject.getString("token");
                        JSONArray groups = jsonObject.getJSONArray("groups");
                        boolean isSampleCollector = false;
                        for(int i = 0;i<groups.length();i++){
                            JSONObject group = groups.getJSONObject(i);
                            if(group.getInt("id") == Constant.SAMPLE_COLLECTOR_ID){
                                isSampleCollector = true;
                            }
                        }
                        if(isSampleCollector){
                            SharedPreferences user_pref = getSharedPreferences(Constant.USER_SHARED_PREFS,MODE_PRIVATE);
                            SharedPreferences.Editor editor = user_pref.edit();
                            editor.putLong(Constant.USER_ID, id);
                            editor.putString(Constant.USER_TOKEN, token);
                            editor.putInt(Constant.USER_GROUP_ID, Constant.SAMPLE_COLLECTOR_ID);
                            editor.apply();
                            GlobalState.getInstance().login(token);
                            getAppointments(thisActivity);
                            return SUCCESS;
                        }
                        else{
                            return "Access Denied. Please login from a different account";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(jsonObject.has("error")){
                    try {
                        return jsonObject.getString("error");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                return "Cannot login now. Please try after some time";
            }
            return "Unexpected Error";
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            if(result.equals(SUCCESS)) {
                Intent loginIntent = new Intent();
                loginIntent.setClass(LoginActivity.this, HomeActivity.class);
                startActivity(loginIntent);
                thisActivity.finish();
            }
            else{
                Snackbar.make(parentView,result,Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(LoginActivity.this, null, "Loading...");
        }

        private String post(String url) throws IOException {
            //Using OkHttp lib
            OkHttpClient client = new OkHttpClient();//creating client
            RequestBody requestBody = new MultipartBuilder()//building body part using form-data method
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("username", mEmailString)
                    .addFormDataPart("password", mPassString)
                    .build();
            Request request = new Request.Builder()//building request
                    .url(url)
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseString = response.body().string();
                response.body().close();
                return responseString;
            }
            catch (Exception e) {
                e.printStackTrace();
                Snackbar.make(parentView,"Cannot connect to server. Please try again later",Snackbar.LENGTH_SHORT).show();
                return null;
            }
        }
    }

}
