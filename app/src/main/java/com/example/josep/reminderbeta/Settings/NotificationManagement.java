package com.example.josep.reminderbeta.Settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.josep.reminderbeta.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationManagement extends Fragment {


	public NotificationManagement() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_notification_management, container, false);
	}

}
