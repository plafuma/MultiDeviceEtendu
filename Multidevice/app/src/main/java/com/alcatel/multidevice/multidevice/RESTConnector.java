package com.alcatel.multidevice.multidevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class RESTConnector {
	private static final String TAG = RESTConnector.class.getSimpleName();

	private static String SERVER_URL = "https://ot2.sqanet.fr";
	private static final String AUTH_PATH = "/api/rest/authenticate?version=1.0";
	

	public RESTConnector() {
		SERVER_URL = MainActivity.ser;
	}


	public String authenticate() {
		URL url;
		String ret = "";
		HttpURLConnection connection = null;

		try {
			url = new URL(SERVER_URL + AUTH_PATH);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();

			int responseCode = connection.getResponseCode();
			Log.v(TAG, "Response code is: " + responseCode);

			if (responseCode == 200) {

				// Read the response JSON
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				Log.v(TAG, builder.toString());

				ret = builder.toString();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return ret;

	}
	
	public String getInfoUser(){
		URL url;
		String ret = "";
		HttpURLConnection connection = null;

		try {
			url = new URL(SERVER_URL + "/api/rest/1.0/sessions");
			ret="ok";
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);		
			connection.connect();

			int responseCode = connection.getResponseCode();
			Log.v(TAG, "Response code is: " + responseCode);

			if (responseCode == 200) {

				// Read the response JSON
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line).append("\n");
				}

				Log.v(TAG, builder.toString());

				ret = builder.toString();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return ret;
	}
}