package in.kvsc.kalpavrikshpro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import utilities.Constant;

public class LoginActivity extends AppCompatActivity {

    EditText mEmailEditText;
    EditText mPassEditText;
    String mEmailString;
    String mPassString;
    View parentView;
    Activity thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parentView = this.findViewById(R.id.login_screen);
        thisActivity = this;
        mEmailEditText = (EditText)this.findViewById(R.id.editText_username);
        mPassEditText = (EditText)this.findViewById(R.id.editText_password);
    }


    public void onLoginClicked(View view){
        mEmailString = mEmailEditText.getText().toString();
        mPassString = mPassEditText.getText().toString();
        LogIn();
    }

    public void LogIn(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected){
            new LogInAsyncTask().execute();
        }
        else{
            Snackbar.make(parentView, "Please connect to Internet", Snackbar.LENGTH_LONG).show();
        }
    }

    private class LogInAsyncTask extends AsyncTask<String, Void, JSONObject> {


        ProgressDialog mProgressDialog;


        @Override
        protected JSONObject doInBackground(String... strings) {
            String request = null;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", mEmailString);
                jsonObject.put("password", mPassString);
                request = jsonObject.toString();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            String response = null;
            JSONObject resposneJsonObject = new JSONObject();

            try {
                response = post(Constant.LOGIN_URL,request);
                resposneJsonObject = new JSONObject(response);

            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            return resposneJsonObject;

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mProgressDialog.dismiss();
            if (jsonObject!= null)
            Log.i("json", jsonObject.toString());
            Intent loginIntent = new Intent();
            loginIntent.setClass(LoginActivity.this, HomeActivity.class);
            startActivity(loginIntent);
            thisActivity.finish();
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(LoginActivity.this, null, "Loading...");
        }

        public final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();

            Log.i("json_post", response.toString());
            return response.body().string();
        }
    }
}
