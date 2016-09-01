package com.example.josep.reminderbeta;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {
	YesNoPreference logout;

	public SettingsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.settings);
		logout = (YesNoPreference) getPreferenceManager().findPreference("LogOut");
		SharedPreferences prefs = logout.getSharedPreferences();
		prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
				Boolean yn = sharedPreferences.getBoolean("LogOut", false);
				if (yn) {
					signout();
					getActivity().finish();
					sharedPreferences.edit().putBoolean("LogOut", false).apply();
				}


			}
		});


	}


	public void onViewCreated(View view, Bundle savedInstanceState) {

		Preference disciplina = findPreference("Disciplinas");
		if (disciplina != null) {
			disciplina.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					SubjectsList fragment = new SubjectsList();
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.mainframe, fragment)
							.addToBackStack(null)
							.commit();
					return false;

				}
			});
		}
	}

	private void signout() {
		FirebaseAuth.getInstance().signOut();
		Intent i = new Intent(getActivity(), InitApp.class);
		startActivity(i);

		Log.d("SIGNOUT", "   ");
	}
}





