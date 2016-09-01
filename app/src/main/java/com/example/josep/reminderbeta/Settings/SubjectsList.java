package com.example.josep.reminderbeta.Settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.josep.reminderbeta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectsList extends Fragment {
	private DatabaseReference mDatabase;
	private DatabaseReference mDatChild;
	private FirebaseAuth mAuth;
	private ListView lv;
	private ArrayList<Subject> arrayOfSubjects;
	private SubjectAdapter adapter;
	public SubjectsList() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_subjects_list, container, false);

	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mDatabase = FirebaseDatabase.getInstance().getReference();
		FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
		assert fab != null;
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});


	 lv = (ListView) view.findViewById(R.id.subjectlist);
		 arrayOfSubjects = new ArrayList<Subject>();
		 adapter = new SubjectAdapter(getContext(),arrayOfSubjects);
		lv.setAdapter(adapter);
		DatabaseRetrieve();
	}
	private void DatabaseRetrieve(){
		mAuth = FirebaseAuth.getInstance();
		if(mAuth.getCurrentUser().getUid() != null) {
		mDatChild = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Subjects");
			if(adapter.getCount() == 0 ) {
				Subject loading = new Subject("Loading Subjects");
				adapter.add(loading);
			}
		mDatChild.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				adapter.clear();
				List<String> lst = new ArrayList<>(); // Result will be holded Here

				for (DataSnapshot dsp : dataSnapshot.getChildren()) {
					lst.add(String.valueOf(dsp.getKey())); //add result into array list

				}
				for (String data : lst) {
					if(data.length() == 0 ) {
						Subject nosubject = new Subject("No Subjects to Show");
						adapter.add(nosubject);
					}
					Subject newSubject = new Subject(data);

					adapter.add(newSubject);




				}
	}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});
	}
	}
}
