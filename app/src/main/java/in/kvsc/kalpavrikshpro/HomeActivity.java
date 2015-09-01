package in.kvsc.kalpavrikshpro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import utilities.Constant;
import utilities.TabsAdapter;
import utilities.Utilities;

public class HomeActivity extends AppCompatActivity {


    TabLayout mTabLayout;
    ViewPager mPager;
    View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        parentView = this.findViewById(R.id.home_screen);

        //View Pager
        mPager = (ViewPager) this.findViewById(R.id.viewpager);
        if(mPager!=null) {
            setupViewPager();
        }


        //Tab Layout
        mTabLayout = (TabLayout)this.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mPager);

        updateDatabase();
    }

    private void updateDatabase(){

    }
    private void setupViewPager() {

        //Set Up View pager
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(new ServiceListFragment(), "Services");

        ListFragment packagesFragment = new ListFragment();
        Bundle packagesBundle = new Bundle();
        packagesBundle.putString("type", Constant.PACKAGES);
        packagesFragment.setArguments(packagesBundle);
        adapter.addFragment(packagesFragment, getString(R.string.packages));

        ListFragment testsFragment = new ListFragment();
        Bundle testsBundle = new Bundle();
        testsBundle.putString("type", Constant.SUPERTESTS);
        testsFragment.setArguments(testsBundle);
        adapter.addFragment(testsFragment, getString(R.string.tests));

        mPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            GlobalState globalState = GlobalState.getInstance();
            globalState.logout();
            Intent loginIntent = new Intent(this,LoginActivity.class);
            startActivity(loginIntent);
            this.finish();
            return true;
        }
        else if(id == R.id.action_update_database){
            if(Utilities.isConnectionAvailable(this)){
                new UpdateDatabseAsyncTask().execute();
            }
            else{
                Snackbar.make(parentView, "Please connect to Internet", Snackbar.LENGTH_LONG).show();
            }
        }


        return super.onOptionsItemSelected(item);
    }

    public class UpdateDatabseAsyncTask extends AsyncTask<String, Void, JSONArray> {
        ProgressDialog mProgressDialog;
        String token;
        @Override
        protected JSONArray doInBackground(String... params) {

            SharedPreferences user_pref = getSharedPreferences(Constant.USER_SHARED_PREFS,MODE_PRIVATE);
            token = user_pref.getString(Constant.USER_TOKEN,"");
            String superTestResponse;
            JSONArray superTestResponseJsonObject = new JSONArray();
            try {
                superTestResponse = post(Constant.SUPERTEST_URL);
                Log.i("string", superTestResponse);
                superTestResponseJsonObject = new JSONArray(superTestResponse);
                Log.i("Object" , superTestResponseJsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return superTestResponseJsonObject;
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(HomeActivity.this, null, "Loading...");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            mProgressDialog.dismiss();
        }
        String post(String url) throws IOException {
            //Using OkHttp lib
            OkHttpClient client = new OkHttpClient();//creating client

            Request request = new Request.Builder()//building request
                    .url(url)
                    .header("Authorization",token)
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
