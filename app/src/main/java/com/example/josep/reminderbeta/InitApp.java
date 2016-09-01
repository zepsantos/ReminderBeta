package com.example.josep.reminderbeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.josep.reminderbeta.Auth.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InitApp extends AppCompatActivity {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);

		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		if (user != null) {
			Intent i = new Intent(this, Main.class);
			startActivity(i);
		} else {
			Login fragment = new Login();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.content_frame, fragment);
			fragmentTransaction.commitNow();

		}

	}

	@Override
	public void onBackPressed () {
		Log.d("INITAPP", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			getSupportFragmentManager().popBackStackImmediate();
		}
	}
}
