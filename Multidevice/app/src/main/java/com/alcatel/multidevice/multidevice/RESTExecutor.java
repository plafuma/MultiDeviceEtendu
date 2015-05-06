package com.alcatel.multidevice.multidevice;

import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.PasswordAuthentication;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class RESTExecutor {

	Context context;
	boolean authenticating; // Authent in progress

	public RESTExecutor(Context context) {
		this.context = context;

		// Accept HTTP cookies
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		trustAllHosts();
	}

	// Store login and password
	public void setCredential(final String login, final String password) {
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				if (authenticating) {
					authenticating = false;
					return new PasswordAuthentication(login, password.toCharArray());
				}

				return null;
			}
		});
	}

	public void authenticate(final RESTResponseHandler responseHandler) {
		DemoApplication app = (DemoApplication)context.getApplicationContext();

		// Run the Web service in another thread
		app.getExecutor().execute(new Runnable() {

			@Override
			public void run() {
				RESTConnector connector = new RESTConnector();

				// Run the Web service
				authenticating = true;
				final String response = connector.authenticate();

				// When the web service is finished, call the response handler on the UI thread.
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						responseHandler.onResponse(response);
					}
				});
			}

		});
	}

	// always verify the host - dont check for certificate
	private final HostnameVerifier fakeHostnameVerifier = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Trust every server - dont check for any certificate
	 */
	private void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(fakeHostnameVerifier);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void infosUser(final RESTResponseHandler responseHandler) {
		DemoApplication app = (DemoApplication)context.getApplicationContext();

		// Run the Web service in another thread
		app.getExecutor().execute(new Runnable() {

			@Override
			public void run() {
				RESTConnector connector = new RESTConnector();

				// Run the Web service
				authenticating = true;
				final String response = connector.getInfoUser();

				// When the web service is finished, call the response handler on the UI thread.
				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						responseHandler.onResponse(response);
					}
				});
			}

		});
	}
	
}
