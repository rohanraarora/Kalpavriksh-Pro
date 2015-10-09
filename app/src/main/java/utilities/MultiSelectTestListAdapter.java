package utilities;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import in.kvsc.kalpavrikshpro.R;
import models.*;
import models.Package;

/**
 * Created by Rohan on 9/7/2015.
 */
public class MultiSelectTestListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<LabItem> mList;
    SparseBooleanArray mSelectedTestsIds;
    SparseBooleanArray mSelectedPackageIds;
    ArrayList<LabItem> originalList;
    public MultiSelectTestListAdapter(Context context,ArrayList<LabItem> list,SparseBooleanArray selectedTestIds,SparseBooleanArray selectedPackageIds){
        mContext = context;
        mList = list;
        originalList = new ArrayList<>();
        originalList.addAll(list);
        mSelectedTestsIds = selectedTestIds;
        mSelectedPackageIds = selectedPackageIds;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public LabItem getItem(int position) {
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
            output = inflater.inflate(R.layout.test_packages_row,null);
        }
        ImageView imageView = (ImageView)output.findViewById(R.id.iconImageView);
        TextView nameTextView = (TextView)output.findViewById(R.id.testPackageNameTextView);
        TextView priceTextView = (TextView)output.findViewById(R.id.testPackagePriceTextView);
        LabItem labItem = getItem(position);
        switch (labItem.getType()){
            case LabItem.SUPERTEST:
                imageView.setImageResource(R.drawable.test);
                Supertest supertest = (Supertest)labItem;
                nameTextView.setText(supertest.getName());
                priceTextView.setText(supertest.getPrice() + "");
                if(mSelectedTestsIds.get(supertest.getId())){
                    output.setBackgroundColor(mContext.getResources().getColor(R.color.accent));
                }
                else {
                    output.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
                }
                break;
            case LabItem.PACKAGE:
                imageView.setImageResource(R.drawable.package_icon);
                Package packageObject = (models.Package)labItem;
                nameTextView.setText(packageObject.getName());
                priceTextView.setText(packageObject.getPrice() + "");
                if(mSelectedPackageIds.get(packageObject.getId())){
                    output.setBackgroundColor(mContext.getResources().getColor(R.color.accent));
                }
                else {
                    output.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
                }
                break;
        }
        return output;
    }

    public SparseBooleanArray getSelectedTestsIds(){
        return mSelectedTestsIds;
    }

    public SparseBooleanArray getSelectedPackageIds() {
        return mSelectedPackageIds;
    }

    public void toggleSelection(int position){
        LabItem labItem = getItem(position);
        switch (labItem.getType()){
            case LabItem.SUPERTEST:
                int testId = labItem.getId();
                if(mSelectedTestsIds.get(testId)){
                    mSelectedTestsIds.delete(testId);
                }
                else {
                    mSelectedTestsIds.put(testId,true);
                }
                break;

            case LabItem.PACKAGE:
                int packageId = labItem.getId();
                if(mSelectedPackageIds.get(packageId)){
                    mSelectedPackageIds.delete(packageId);
                }
                else {
                    mSelectedPackageIds.put(packageId,true);
                }
                break;
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount(){
        return mSelectedTestsIds.size() + mSelectedPackageIds.size();
    }


    public void clear() {
        mSelectedTestsIds.clear();
        mSelectedPackageIds.clear();
    }

    public void filter(String s) {
        s = s.toLowerCase();
        mList.clear();
        if (s.length() == 0) {
            mList.addAll(originalList);
        }
        else
        {
            for (LabItem labItem : originalList)
            {
                switch (labItem.getType()){
                    case LabItem.PACKAGE:
                        Package packageObject = (Package)labItem;
                        if (packageObject.getName().toLowerCase().contains(s))
                        {
                            mList.add(labItem);
                        }
                        break;
                    case LabItem.SUPERTEST:
                        Supertest supertest = (Supertest)labItem;
                        if (supertest.getName().toLowerCase().contains(s))
                        {
                            mList.add(labItem);
                        }
                        break;
                }
            }
        }
        notifyDataSetChanged();
    }
}
