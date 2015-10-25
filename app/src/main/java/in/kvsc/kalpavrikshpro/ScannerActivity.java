package in.kvsc.kalpavrikshpro;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import utilities.Constant;
import utilities.UpdateDatabaseTask;
import utilities.Utilities;

public class ScannerActivity extends AppCompatActivity {

    Intent mIntent;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scanner);
        Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mIntent = getIntent();
    }

    public void handleResult(String barcodeString){
        mIntent.setClass(this,VialsActivity.class);
        mIntent.putExtra(Constant.BARCODE_ID,barcodeString);
        startActivity(mIntent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scanner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Enter Barcode");

            // Setting Dialog Message
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(16,16,16,16);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            //alertDialog.setView(input);


            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("Done",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            // Write your code here to execute after dialog
                            String s = input.getEditableText().toString();
                            if(s!= null && !s.trim().equals(""))
                                handleResult(input.getEditableText().toString());
                            else{
                                Toast.makeText(getApplicationContext(),"Please enter the barcode",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            dialog.cancel();
                        }
                    });

            // closed

            // Showing Alert Message
            alertDialog.show();

        }
        else if (id == android.R.id.home){
            this.finish();
        }


        return super.onOptionsItemSelected(item);
    }
}