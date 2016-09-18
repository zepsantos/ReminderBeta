package com.example.josep.reminderbeta;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;

import com.example.josep.reminderbeta.Calendar.CalendarFragment;
import com.example.josep.reminderbeta.RecentContent.NewsFragment;
import com.example.josep.reminderbeta.School.SchoolFragment;
import com.example.josep.reminderbeta.Settings.AppSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.ArrayList;
import java.util.List;


public class Main extends AppCompatActivity {
	public static List<String> Group;
	private BottomBar mBottomBar;
	private DatabaseReference mDatchild;
	private DatabaseReference mDatabase;
	private FirebaseAuth mAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDatabase = FirebaseDatabase.getInstance().getReference();
		mAuth = FirebaseAuth.getInstance();
		hasGroup();
		showBottomBar(savedInstanceState);

	}

	private void showBottomBar(Bundle savedInstanceState) {
		mBottomBar = BottomBar.attach(this,
				savedInstanceState);
		mBottomBar.setMaxFixedTabs(4);
		mBottomBar.setItems(R.menu.bottombar_menu);
		mBottomBar.setActiveTabColor(Color.BLACK);

		mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
			@Override
			public void onMenuTabSelected(@IdRes int menuItemId) {
				switch (menuItemId) {
					case R.id.NewsMenu:
						initView();
						break;
					case R.id.CalendarMenu:
						CalendarInit();
						break;
					case R.id.SchoolMenu:
						SchoolInit();
						break;
					case R.id.SettingsMenu:
						Settings();
						break;
				}

			}

			@Override
			public void onMenuTabReSelected(@IdRes int menuItemId) {
				switch (menuItemId) {
					case R.id.SettingsMenu:
						Settings();
						break;
				}

			}
		});
	}

	private void Settings() {
		AppSettings fragment = new AppSettings();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.mainframe, fragment)
				.commitNowAllowingStateLoss();

	}

	private void initView() {
		NewsFragment fragment = new NewsFragment();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.mainframe, fragment)
				.commitNow();
	}

	private void CalendarInit() {
		CalendarFragment fragment = new CalendarFragment();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.mainframe, fragment)
				.commitNow();

	}

	private void SchoolInit() {
		SchoolFragment fragment = new SchoolFragment();
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.mainframe, fragment)
				.commitNow();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Necessary to restore the BottomBar's state, otherwise we would
		// lose the current tab on orientation change.
		mBottomBar.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		int tabposition = mBottomBar.getCurrentTabPosition();

		if (tabposition == 3) {
			if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
				getSupportFragmentManager().popBackStackImmediate();
			}

		}
	}

	private void hasGroup() {
		mAuth = FirebaseAuth.getInstance();
		if (mAuth.getCurrentUser() != null) {
			mDatchild = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("group");
			mDatchild.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot) {
					List<String> lst = new ArrayList<>(); // Result will be holded Here

					for (DataSnapshot dsp : dataSnapshot.getChildren()) {
						lst.add(String.valueOf(dsp.getKey())); //add result into array list

					}
					for (String data : lst) {
						Group = lst;

					}


				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});
		}

	}
}

