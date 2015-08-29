package utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.kvsc.kalpavrikshpro.R;
import models.Supertest;
import models.Test;

/**
 * Created by Rohan on 8/26/2015.
 *
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
    public Test getChild(int groupPosition, int childPosition) {
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
        View output = convertView;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            output = inflater.inflate(R.layout.row_layout,null);
        }
        TextView nameTextView = (TextView)output.findViewById(R.id.row_textView_name);
        TextView priceTextView = (TextView)output.findViewById(R.id.row_textView_price);
        Supertest supertest = getGroup(groupPosition);
        nameTextView.setText(supertest.getName());
        priceTextView.setText(supertest.getPrice() + "");

        return output;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View output = convertView;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            output = inflater.inflate(R.layout.expandedrow_layout,null);
        }
        TextView nameTextView = (TextView)output.findViewById(R.id.expandedrow_textView_name);
        Test test = getChild(groupPosition,childPosition);
        nameTextView.setText(test.getName());

        return output;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
