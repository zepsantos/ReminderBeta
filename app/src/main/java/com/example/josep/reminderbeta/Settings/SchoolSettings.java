package com.example.josep.reminderbeta.Settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.josep.reminderbeta.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolSettings extends Fragment {
	ListView lv;
	String[] Settings = new String[] {
			"SUBJECTS"
	};

	public SchoolSettings () {
		// Required empty public constructor
	}


	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_school_settings, container, false);
	}

	@Override
	public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lv = (ListView) view.findViewById(R.id.lvschoosettings);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				R.layout.schoollv, R.id.tvschoollv, Settings);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
				String itemclicked = Settings[position];
				Fragment selectedFragment = null;
				switch (itemclicked) {
					case "SUBJECTS":
						selectedFragment = new SubjectsList();
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
