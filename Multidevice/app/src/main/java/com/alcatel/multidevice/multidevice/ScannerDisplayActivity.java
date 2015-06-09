package com.alcatel.multidevice.multidevice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ScannerDisplayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_display);


        final Button infosButton = (Button) findViewById(R.id._buttonProfil);
        infosButton.setOnClickListener(getInfosClickListener);

        final Button deconnexionButton = (Button) findViewById(R.id._buttonDeconnexion);
        final RESTResponseHandler responseHandler = new RESTResponseHandler() {


            @Override
            public void onResponse(String reponse) {
                if (reponse.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Disconect Failed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Disconected...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ScannerDisplayActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
        deconnexionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            DemoApplication app = (DemoApplication) getApplication();
            RESTExecutor executor = app.getRestExecutor();
            executor.closeSession(responseHandler);
        }});


        final Button phoneDetailsButton = (Button) findViewById(R.id.buttonScan);
        phoneDetailsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ScannerDisplayActivity.this);
                integrator.addExtra("SCAN_WIDTH", 1920);
                integrator.addExtra("SCAN_HEIGHT", 1080);
                integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
                //integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");

                //integrator.addExtra("PROMPT_MESSAGE", "Scanning...");
                integrator.initiateScan();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        if(result != null){
            String contents = result.getContents();
            if(contents != null){
                if(isCallActive(this.getApplicationContext()))
                {
                    RESTResponseHandler responseHandler = new RESTResponseHandler() {


                        @Override
                        public void onResponse(String reponse) {
                            if (reponse.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Transfer Failed", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Transfer Success", Toast.LENGTH_LONG).show();
						/*Intent intent = new Intent(MainActivity.this, ScannerDisplayActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		                startActivity(intent);*/
                            }
                        }
                    };

                    Toast.makeText(getApplicationContext(), "ACTIF", Toast.LENGTH_LONG).show();
                    DemoApplication app = (DemoApplication) getApplication();
                    RESTExecutor executor = app.getRestExecutor();
                    executor.transfer(responseHandler, "0", contents, false, false);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "PAS ACTIF", Toast.LENGTH_LONG).show();
                }
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
	
	
	private final View.OnClickListener getInfosClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			// Start Login
			DemoApplication app = (DemoApplication) getApplication();
			RESTExecutor executor = app.getRestExecutor();
			

			// server response
			RESTResponseHandler responseHandler = new RESTResponseHandler() {

				
				@Override
				public void onResponse(String reponse) {
					if (reponse.isEmpty()) {
						//text.setText("failed");
					} else {
                        Toast.makeText(getApplicationContext(), reponse, Toast.LENGTH_LONG).show();
					}
				}
			};

			// Ask for login
			executor.testUser(responseHandler);
			//text.setText("Work in progress");
			
			
		}

	};

    public boolean isCallActive(Context context){
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(manager.getMode()==AudioManager.MODE_IN_CALL){
            return true;
        }
        else{
            return false;
        }
    }
	
}
