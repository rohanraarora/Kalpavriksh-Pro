package in.kvsc.kalpavrikshpro;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import utilities.Constant;
import utilities.TabsAdapter;

public class HomeActivity extends AppCompatActivity {


    TabLayout mTabLayout;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //View Pager
        mPager = (ViewPager) this.findViewById(R.id.viewpager);
        if(mPager!=null) {
            setupViewPager();
        }


        //Tab Layout
        mTabLayout = (TabLayout)this.findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mPager);
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

        return super.onOptionsItemSelected(item);
    }
}
