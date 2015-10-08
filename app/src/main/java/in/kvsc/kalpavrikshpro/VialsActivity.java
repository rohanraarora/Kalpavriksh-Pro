package in.kvsc.kalpavrikshpro;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import models.RetailSource;
import utilities.Constant;
import utilities.Utilities;

public class VialsActivity extends AppCompatActivity {

    EditText plainVialEditText;
    EditText edtaVialEditText;
    EditText fluorideVialEditText;
    EditText sodCitVialEditText;
    EditText heparinVialEditText;
    EditText containerEditText;
    EditText referredByEditText;
    Intent mIntent;
    Spinner mSpinner;
    ArrayList<RetailSource> mList;
    private int retail_source_id = 0;

    class RetailSourceSpinnerAdapter extends BaseAdapter{

        Context mContext;
        ArrayList<RetailSource> list;
        RetailSourceSpinnerAdapter(Context context,ArrayList<RetailSource> list){
            mContext = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public RetailSource getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (convertView ==null){
                LayoutInflater inflater =(LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.spinner_row_layout,null);
            }
            TextView nameTextView = (TextView)row.findViewById(R.id.spinnerNameTextView);
            TextView addressTextView = (TextView)row.findViewById(R.id.spinnerAdressTextView);
            RetailSource retailSource = getItem(position);
            nameTextView.setText(retailSource.getName());
            addressTextView.setText(retailSource.getAddress());
            return row;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vials);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mIntent = getIntent();
        mSpinner = (Spinner)findViewById(R.id.spinner);
        mList = Utilities.getRetailSources(this);
        RetailSourceSpinnerAdapter arrayAdapter = new RetailSourceSpinnerAdapter(this,mList);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RetailSource retailSource = (RetailSource)parent.getItemAtPosition(position);
                retail_source_id = retailSource.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                retail_source_id = 0;

            }
        });
        plainVialEditText = (EditText)this.findViewById(R.id.plainVialEditText);
        edtaVialEditText = (EditText)this.findViewById(R.id.edtaVialEditText);
        fluorideVialEditText = (EditText)this.findViewById(R.id.fluorideVialEditText);
        sodCitVialEditText = (EditText)this.findViewById(R.id.sodcitVialEditText);
        heparinVialEditText = (EditText)this.findViewById(R.id.heparinVialEditText);
        containerEditText = (EditText)this.findViewById(R.id.containerEditText);
        referredByEditText = (EditText)this.findViewById(R.id.referredByEditText);
    }

    public void done(View view){
        JSONObject sample_detail = new JSONObject();
        try {
            sample_detail.put("sample_barcode",mIntent.getStringExtra(Constant.BARCODE_ID));
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            sample_detail.put("date",dateFormat.format(date));
            sample_detail.put("time",timeFormat.format(date));
            sample_detail.put("plain_vial",plainVialEditText.getEditableText().toString());
            sample_detail.put("edta_vial",edtaVialEditText.getEditableText().toString());
            sample_detail.put("fluoride_vial",fluorideVialEditText.getEditableText().toString());
            sample_detail.put("sodium_citrate_vial",sodCitVialEditText.getEditableText().toString());
            sample_detail.put("heparin_vial",heparinVialEditText.getEditableText().toString());
            sample_detail.put("container",containerEditText.getEditableText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String sampleJsonString = sample_detail.toString();
        mIntent.putExtra(Constant.SAMPLE_JSON_STRING, sampleJsonString);
        mIntent.putExtra(Constant.RETAIL_SOURCE_ID_INTENT_KEY,retail_source_id);
        mIntent.putExtra(Constant.REFERRED_BY_INTENT_KEY,referredByEditText.getEditableText().toString());
        mIntent.setClass(this,BillActivity.class);
        startActivity(mIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vials, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
