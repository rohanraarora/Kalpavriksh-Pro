package utilities;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.kvsc.kalpavrikshpro.R;
import models.Supertest;

/**
 * Created by Rohan on 9/7/2015.
 */
public class MultiSelectTestListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Supertest> mList;
    SparseBooleanArray mSelectedPositions;
    public MultiSelectTestListAdapter(Context context,ArrayList<Supertest> list,SparseBooleanArray selectedPositions){
        mContext = context;
        mList = list;
        mSelectedPositions = selectedPositions;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Supertest getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View output = convertView;
        if(output==null){
            LayoutInflater inflater =(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            output = inflater.inflate(R.layout.row_layout,null);
        }
        TextView nameTextView = (TextView)output.findViewById(R.id.row_textView_name);
        TextView priceTextView = (TextView)output.findViewById(R.id.row_textView_price);
        Supertest supertest = getItem(position);
        nameTextView.setText(supertest.getName());
        priceTextView.setText(supertest.getPrice() + "");

        return output;
    }

    public SparseBooleanArray getSelectedPositions(){
        return mSelectedPositions;
    }

    public void toggleSelection(int position){
        if(mSelectedPositions.get(position)){
            mSelectedPositions.delete(position);
        }
        else{
            mSelectedPositions.put(position,true);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount(){
        return mSelectedPositions.size();
    }


    public void clear() {
        mSelectedPositions.clear();
    }
}
