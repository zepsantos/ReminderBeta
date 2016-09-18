package com.example.josep.reminderbeta.Settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.josep.reminderbeta.Main;
import com.example.josep.reminderbeta.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupManagement extends Fragment {
	TextView currentGroup;
	public GroupManagement() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_group_management, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (Main.Group != null) {
			ArrayAdapter<String> itemsAdapter =
					new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, Main.Group);
			ListView lv = (ListView) view.findViewById(R.id.currentgroup);
			lv.setAdapter(itemsAdapter);


		}
	}
}
