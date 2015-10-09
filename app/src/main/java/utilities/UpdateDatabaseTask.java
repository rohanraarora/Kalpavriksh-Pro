package utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import in.kvsc.kalpavrikshpro.GlobalState;
import models.*;

public class UpdateDatabaseTask extends AsyncTask<String, Void, Boolean> {

    Context mContext;
    ProgressDialog mProgressDialog;
    String mToken;

    public UpdateDatabaseTask(Context context){
        mContext = context;
    }
    @Override
    protected Boolean doInBackground(String... params) {
        mToken = GlobalState.getInstance().getToken();
        try {
            Utilities.updateDataBase(mContext,mToken);
            Utilities.updateAppointments(mContext, mToken);
            Utilities.updateRetailSource(mContext,mToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = ProgressDialog.show(mContext, null, "Loading...");
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);
        mProgressDialog.dismiss();
    }
}