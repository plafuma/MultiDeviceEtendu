package com.alcatel.multidevice.multidevice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ScannerDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_display);

        final ImageButton loginButton = (ImageButton) findViewById(R.id.imageButtonListeDevice);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ScannerDisplayActivity.this, ListeDeviceActivity.class);
                //startActivity(intent);
            }
        });

        final ImageButton optionButton = (ImageButton) findViewById(R.id.imageButton);
        optionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ScannerDisplayActivity.this, OptionActivity.class);
                //startActivity(intent);
            }
        });

        final Button phoneDetailsButton = (Button) findViewById(R.id.buttonScan);
        phoneDetailsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ScannerDisplayActivity.this);
                integrator.addExtra("SCAN_WIDTH", 640);
                integrator.addExtra("SCAN_HEIGHT", 480);
                integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");

                integrator.addExtra("PROMPT_MESSAGE", "Scanning...");
                integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        if(result != null){
            String contents = result.getContents();
            if(contents != null){
                //Lancer requete http
            }else{
                //Faire autre chose
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_scanner_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
