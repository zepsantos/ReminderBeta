package com.example.josep.reminderbeta;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by zepsantos on 15/09/2016.
 */
public class OfflineCapabilities extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
	/* Enable disk persistence  */
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

	}
}
