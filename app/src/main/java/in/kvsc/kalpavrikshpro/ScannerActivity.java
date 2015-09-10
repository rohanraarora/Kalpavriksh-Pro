package in.kvsc.kalpavrikshpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import models.Supertest;
import utilities.Constant;

public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    ArrayList<Integer> mTestIds;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        Intent intent = getIntent();
        mTestIds = intent.getIntegerArrayListExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Intent intent = new Intent(this,SamplesActivity.class);
        intent.putExtra(Constant.SUPERTEST_ID_LIST_INTENT_KEY,mTestIds);
        intent.putExtra(Constant.BARCODE_ID,rawResult.getText());
        startActivity(intent);
        this.finish();

    }
}