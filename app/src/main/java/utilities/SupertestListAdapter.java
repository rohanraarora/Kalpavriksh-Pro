package utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;

import models.Supertest;

/**
 * Created by Rohan on 8/26/2015.
 */
public class SupertestListAdapter extends BaseExpandableListAdapter {

    Context mContext;
    ArrayList<Supertest> mList;
    public SupertestListAdapter(Context context,ArrayList<Supertest> list){
        mContext = context;
        mList = list;
    }
    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).getTests().size();
    }

    @Override
    public Supertest getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Supertest getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).getTests().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getGroup(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChild(groupPosition,childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
