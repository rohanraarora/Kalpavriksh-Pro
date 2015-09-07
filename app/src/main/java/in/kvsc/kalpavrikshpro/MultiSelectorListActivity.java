package in.kvsc.kalpavrikshpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import models.Supertest;
import utilities.Constant;
import utilities.MultiSelectTestListAdapter;
import utilities.Utilities;

public class MultiSelectorListActivity extends AppCompatActivity {

    ListView mListView;
    SparseBooleanArray mSelectedPositions;
    MultiSelectTestListAdapter mAdapter;
    ArrayList<Supertest> mTests;
    ArrayList<Integer> mSelectedSupertestPositions;
    ArrayList<Integer> mSelectedSupertestIds;
    Activity thisActivity;
    private int statusBarColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_selector_list);
        Intent intent = getIntent();
        thisActivity = this;

        mSelectedSupertestPositions = intent.getIntegerArrayListExtra(Constant.SUPERTEST_POSITIONS_LIST_INTENT_KEY);
        mSelectedSupertestIds = new ArrayList<>();
        mTests = Utilities.getSupertests(this);
        mSelectedPositions = new SparseBooleanArray();
        mListView = (ListView)this.findViewById(R.id.multiselect_listView);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                final int checkedCount = mListView.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                mAdapter.toggleSelection(position);
            }
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //hold current color of status bar
                    statusBarColor = getWindow().getStatusBarColor();
                    //set your gray color
                    getWindow().setStatusBarColor(Color.DKGRAY);
                }
                mode.getMenuInflater().inflate(R.menu.menu_multi_selector_list, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_done:
                        // Calls getSelectedIds method from ListViewAdapter Class

                        mode.finish();
                        thisActivity.finish();
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
                mSelectedPositions = mAdapter.getSelectedPositions();
                mSelectedSupertestIds.clear();
                mSelectedSupertestPositions.clear();
                // Captures all selected ids with a loop
                for (int i =0;i<mSelectedPositions.size(); i ++) {
                    if (mSelectedPositions.valueAt(i)) {
                        int position = mSelectedPositions.keyAt(i);
                        mSelectedSupertestPositions.add(position);
                        mSelectedSupertestIds.add((int)mListView.getItemIdAtPosition(position));
                    }
                }
                Intent resultIntent = new Intent();
                resultIntent.putIntegerArrayListExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY, mSelectedSupertestIds);
                resultIntent.putIntegerArrayListExtra(Constant.SUPERTEST_POSITIONS_LIST_INTENT_KEY, mSelectedSupertestPositions);
                thisActivity.setResult(Constant.RESULT_CODE_SUCCESS, resultIntent);

            }
        });
        mAdapter = new MultiSelectTestListAdapter(this,mTests,mSelectedPositions);
        mListView.setAdapter(mAdapter);
        for(int i = 0;i<mSelectedSupertestPositions.size();i++){
            int position = mSelectedSupertestPositions.get(i);
            mListView.setItemChecked(position,true);
            mSelectedPositions.put(position,true);
        }

    }
}
