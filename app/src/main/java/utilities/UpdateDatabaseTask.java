package utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import in.kvsc.kalpavrikshpro.GlobalState;

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