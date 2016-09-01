package com.example.josep.reminderbeta;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;


public class Main extends AppCompatActivity {
	private BottomBar mBottomBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
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
}

