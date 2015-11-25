package in.kvsc.kalpavrikshpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import models.*;
import utilities.Constant;
import utilities.MultiSelectTestListAdapter;
import utilities.Utilities;

public class MultiSelectorListActivity extends AppCompatActivity{

    ListView mListView;
    SparseBooleanArray mSelectedTestIds;//To store selected tests id - using sparse to save looping through the list
    SparseBooleanArray mSelectedPackageIds;//To store packages id
    MultiSelectTestListAdapter mAdapter;
    ArrayList<LabItem> mLabItems;//List consisting of both packages and tests
    ArrayList<Integer> mSelectedSupertestIds;//List of selected test ids to be passed in intent
    ArrayList<Integer> mSelectedPackagesIds;//list of selected package ids to be passed in intent
    Activity thisActivity;
    ActionMode actionMode;//action mode
    private int statusBarColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_selector_list);
        thisActivity = this;

        //creating combined list of tests and packages
        mLabItems = new ArrayList<>();
        ArrayList<Supertest> supertests = Utilities.getSupertests(this);
        ArrayList<models.Package> packages = Utilities.getPackages(this);
        mLabItems.addAll(packages);
        mLabItems.addAll(supertests);

        //retrieving selected tests and packages ids if any from detailActivityIntent and adding them to sparse
        Intent intent = getIntent();
        mSelectedSupertestIds = intent.getIntegerArrayListExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY);
        mSelectedPackagesIds = intent.getIntegerArrayListExtra(Constant.PACKAGE_ID_LIST_INTENT_KEY);
        mSelectedTestIds = new SparseBooleanArray();
        mSelectedPackageIds = new SparseBooleanArray();
        for(int id:mSelectedPackagesIds){
            mSelectedPackageIds.put(id,true);
        }
        for(int id:mSelectedSupertestIds){
            mSelectedTestIds.put(id,true);
        }
        int totalSelected = mSelectedPackagesIds.size() + mSelectedSupertestIds.size();

        //Search Implementation using text watcher
        EditText searchEditText = (EditText)findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mListView = (ListView)this.findViewById(R.id.multiselect_listView);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        //ActionMode Callback
        final ActionMode.Callback callback = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_multi_selector_list, menu);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //hold current color of status bar
                    statusBarColor = getWindow().getStatusBarColor();
                    getWindow().setStatusBarColor(Color.DKGRAY);
                }
                return true;
            }
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_done:
                        //On pressing done action in actionMode
                        //updating sparse and selected tests and packages list
                        mSelectedTestIds = mAdapter.getSelectedTestsIds();
                        mSelectedPackageIds = mAdapter.getSelectedPackageIds();
                        mSelectedSupertestIds.clear();
                        mSelectedPackagesIds.clear();
                        for (int i =0;i<mSelectedTestIds.size(); i ++) {
                            if (mSelectedTestIds.valueAt(i)) {
                                mSelectedSupertestIds.add(mSelectedTestIds.keyAt(i));
                            }
                        }
                        for(int i = 0;i<mSelectedPackageIds.size();i++){
                            if(mSelectedPackageIds.valueAt(i)){
                                mSelectedPackagesIds.add(mSelectedPackageIds.keyAt(i));
                            }
                        }

                        //sending result
                        Intent resultIntent = new Intent();
                        resultIntent.putIntegerArrayListExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY, mSelectedSupertestIds);
                        resultIntent.putIntegerArrayListExtra(Constant.PACKAGE_ID_LIST_INTENT_KEY, mSelectedPackagesIds);
                        thisActivity.setResult(Constant.RESULT_CODE_SUCCESS, resultIntent);
                        mode.finish();
                        actionMode = null;

                        return true;
                    default:
                        return false;
                }
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //return to "old" color of status bar
                    getWindow().setStatusBarColor(statusBarColor);
                }

                if(mAdapter.getSelectedCount() == 0){
                    mSelectedSupertestIds.clear();
                    mSelectedPackagesIds.clear();
                    Intent resultIntent = new Intent();
                    resultIntent.putIntegerArrayListExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY, mSelectedSupertestIds);
                    resultIntent.putIntegerArrayListExtra(Constant.PACKAGE_ID_LIST_INTENT_KEY, mSelectedPackagesIds);
                    thisActivity.setResult(Constant.RESULT_CODE_SUCCESS, resultIntent);
                }
                else
                    thisActivity.finish();


            }
        };

        //if containes previously selected items start action mode
        if(totalSelected > 0){
            if(actionMode == null){
                actionMode = startActionMode(callback);
                actionMode.setTitle(totalSelected + " Selected");
            }
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Start the action mode is not started
                if(actionMode==null)
                    actionMode = startActionMode(callback);
                mAdapter.toggleSelection(position);
                final int checkedCount = mAdapter.getSelectedCount();
                actionMode.setTitle(checkedCount + " Selected");
                if (checkedCount == 0){
                    actionMode.finish();
                    actionMode = null;
                }
            }
        });

        //setting adapter
        mAdapter = new MultiSelectTestListAdapter(this,mLabItems,mSelectedTestIds,mSelectedPackageIds);
        mListView.setAdapter(mAdapter);
    }

}
