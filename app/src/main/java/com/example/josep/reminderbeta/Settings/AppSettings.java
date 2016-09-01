package com.example.josep.reminderbeta.Settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.josep.reminderbeta.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppSettings extends Fragment {
	ListView lv;
	String[] settings = {
			"SUBJECTS",
			"GROUP",
			"ACCOUNT",
			"NOTIFICATION",
			"SHARE"

	};
	Integer[] imgid = {
			R.drawable.ic_school_black_24dp,
			R.drawable.ic_group_black_24dp,
			R.drawable.ic_lock_open_black_24dp,
			R.drawable.ic_notifications_black_24dp,
			R.drawable.ic_share_black_24dp,

	};

	public AppSettings () {
		// Required empty public constructor
	}


	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_app_settings, container, false);
	}

	@Override
	public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		AppSettingsAdapter adapter = new AppSettingsAdapter(getContext(), settings, imgid);
		lv = (ListView) view.findViewById(R.id.lvappsettings);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
				String Slecteditem = settings[i];
				Fragment selectedFragment = null;

				switch (Slecteditem) {
					case "SUBJECTS":
						selectedFragment = new SubjectsList();

						break;
					case "GROUP":
						selectedFragment = new GroupManagement();

						break;
					case "ACCOUNT":
						selectedFragment = new AccountManagement();

						break;
					case "NOTIFICATION":
						selectedFragment = new NotificationManagement();

						break;
					case "SHARE":
						selectedFragment = new ShareApp();

						break;


				}
				if (selectedFragment != null) {
					getFragmentManager()
							.beginTransaction()
							.replace(R.id.mainframe, selectedFragment)
							.addToBackStack(null)
							.commit();
				}

			}
		});


	}


}
