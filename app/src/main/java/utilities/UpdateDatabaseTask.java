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
        String testResponse;
        String supertestResponse;
        String packagesResponse;
        JSONArray testJSONArray;
        JSONArray supertestJSONArray;
        JSONArray packageJSONArray;
        OpenHelper openHelper = OpenHelper.getInstance(mContext);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        try {
            testResponse = post(Constant.TEST_URL);
            if(testResponse!=null){
                testJSONArray = new JSONArray(testResponse);
                if(testJSONArray.length() > 0) {
                    db.delete(Contract.TEST_TABLE,null,null);
                    for (int i = 0; i < testJSONArray.length(); i++) {
                        JSONObject jsonObject = testJSONArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        int id = jsonObject.getInt("id");
                        Test test = new Test(id,name);
                        openHelper.addTest(db,test);
                    }
                }
                supertestResponse = post(Constant.SUPERTEST_URL);
                if(supertestResponse!=null){
                    supertestJSONArray = new JSONArray(supertestResponse);
                    if(supertestJSONArray.length() > 0){
                        db.delete(Contract.SUPERTEST_TEST_TABLE,null,null);
                        for(int i = 0;i<supertestJSONArray.length();i++){
                            JSONObject jsonObject = supertestJSONArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            int id = jsonObject.getInt("id");
                            String price = jsonObject.getString("price");
                            ArrayList<Test> tests = new ArrayList<>();
                            JSONArray testsJSON = jsonObject.getJSONArray("tests");
                            for(int j = 0;j<testsJSON.length();j++){
                                JSONObject test = testsJSON.getJSONObject(j);
                                String test_name = test.getString("name");
                                int test_id = test.getInt("id");
                                tests.add(new Test(test_id,test_name));
                            }
                            Supertest supertest = new Supertest(id,name,Double.parseDouble(price),tests);
                            openHelper.addSupertest(db,supertest);
                        }
                    }
                    packagesResponse = post(Constant.PACKAGE_URL);
                    if(packagesResponse!=null){
                        packageJSONArray = new JSONArray(packagesResponse);
                        if(packageJSONArray.length() > 0){
                            db.delete(Contract.PACKAGE_TABLE,null,null);
                            for(int i = 0;i<packageJSONArray.length();i++){
                                JSONObject jsonObject = packageJSONArray.getJSONObject(i);
                                String name = jsonObject.getString("name");
                                int id = jsonObject.getInt("id");
                                Double price = jsonObject.getDouble("price");
                                ArrayList<Supertest> tests = new ArrayList<>();
                                JSONArray testsJSON = jsonObject.getJSONArray("supertests");
                                for(int j = 0;j<testsJSON.length();j++){
                                    JSONObject test = testsJSON.getJSONObject(j);
                                    String test_name = test.getString("name");
                                    int test_id = test.getInt("id");
                                    tests.add(new Supertest(test_id,test_name));
                                }
                                openHelper.addPackage(db,new models.Package(id,name,price,tests));
                            }
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

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
    String post(String url) throws IOException {
        //Using OkHttp lib
        OkHttpClient client = new OkHttpClient();//creating client

        Request request = new Request.Builder()//building request
                .url(url)
                .header("Authorization",mToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            response.body().close();
            return responseString;
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Cannot connect to server. Please try again later", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}