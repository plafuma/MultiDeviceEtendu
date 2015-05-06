package com.alcatel.multidevice.multidevice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;

public class DemoApplication extends Application {

	ExecutorService executor; // action queue for web services
	RESTExecutor restExecutor; // used to make all web services

	@Override
	public void onCreate() {
		super.onCreate();

		executor = Executors.newFixedThreadPool(1);
		restExecutor = new RESTExecutor(this);
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public RESTExecutor getRestExecutor() {
		return restExecutor;
	}


}
