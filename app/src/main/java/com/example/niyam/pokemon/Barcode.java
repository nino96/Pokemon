package com.example.niyam.pokemon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Barcode extends AppCompatActivity {
    static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //check for camera permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String scanContent = scanResult.getContents();

        if (scanResult != null && scanContent != null)
        {
            if(scanContent.equalsIgnoreCase("magikarp")||scanContent.equalsIgnoreCase("unown")||scanContent.equalsIgnoreCase("sunkern"))
            {
                //shared preference to prevent re login when device reboots,or app clears out of memory
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean("logincred", true);
                edit.commit();     //dont forget this!!

                Intent i = new Intent(getApplicationContext(),MenuActivity.class);
                //once this is done we need to remove barcode activity from stack so that
                //on pressing back button from menu activity,we go back to parent activity and not barcode
                this.finish();

                startActivity(i);
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Incorrect Barcode,CONSIDER PAYING 50Rs!!", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

    public void ScanOnClick(View v)
    {
        //check for camera permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setResultDisplayDuration(0);
        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
