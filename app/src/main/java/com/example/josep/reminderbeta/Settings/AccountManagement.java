package com.example.josep.reminderbeta.Settings;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.josep.reminderbeta.InitApp;
import com.example.josep.reminderbeta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountManagement extends Fragment {
	TextView LogOut;
	TextView changePassbtn;
	TextView accountEmail;
	TextView accountName;
	ProgressDialog progress;
	private EditText passText;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private DatabaseReference mDatchild;
	private DatabaseReference mDatabase;

	public AccountManagement () {
		// Required empty public constructor
	}


	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_account_management, container, false);
	}

	@Override
	public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mDatabase = FirebaseDatabase.getInstance().getReference();
		mAuth = FirebaseAuth.getInstance();
		LogOut = (TextView) view.findViewById(R.id.tvLogout);
		passText = (EditText) view.findViewById(R.id.changePasswordET);
		changePassbtn = (TextView) view.findViewById(R.id.changePasswordbtn);
		accountEmail = (TextView) view.findViewById(R.id.accountEmailTV);
		accountName = (TextView) view.findViewById(R.id.accountNameTV);
		changePass();
		LogOut();
		getAccountDetails();


	}

	private void changePass () {
		changePassbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick (View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setTitle("Are you sure you want to change the password?");


				builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
					@Override
					public void onClick (DialogInterface dialog, int which) {
						if (!validateForm()) {
							return;
						} else {
							String password = passText.getText().toString();
							mAuth = FirebaseAuth.getInstance();
							if (mAuth.getCurrentUser() != null) {
								mAuth.getCurrentUser().updatePassword(password); //NOT WORKING
								signout();
							}
						}
					}
				});
				builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					@Override
					public void onClick (DialogInterface dialog, int which) {
						dialog.cancel();

					}
				});

				builder.show();

			}

		});
	}

	private void getAccountDetails () {
		mAuth = FirebaseAuth.getInstance();

		if (mAuth.getCurrentUser() != null) {
			String UserEmail = mAuth.getCurrentUser().getEmail();
			accountEmail.setText(UserEmail);
			mDatchild = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Name");
			mDatchild.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange (DataSnapshot dataSnapshot) {
					String Name = dataSnapshot.getValue(String.class);
					accountName.setText(Name);
				}

				@Override
				public void onCancelled (DatabaseError databaseError) {

				}


			});
		}
	}


	private void LogOut () {
		LogOut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View view) {
				new AlertDialog.Builder(getContext())
						.setTitle("Log Out")
						.setMessage(getString(R.string.LogOutMessage))
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							@Override
							public void onClick (DialogInterface dialogInterface, int i) {
								signout();

							}
						})
						.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							@Override
							public void onClick (DialogInterface dialogInterface, int i) {

							}
						})
						.show();


			}
		});
	}

	private void changePassword (String password) {

	/*	progress = ProgressDialog.show(getActivity(), "",
				"Changing Password", true); */


	}

	private boolean validateForm () {
		boolean valid = true;


		String password = passText.getText().toString();
		if (TextUtils.isEmpty(password)) {
			passText.setError("Required.");
			valid = false;
		} else {
			passText.setError(null);
		}

		return valid;
	}

	private void signout () {
		FirebaseAuth.getInstance().signOut();
		Intent i = new Intent(getActivity(), InitApp.class);
		startActivity(i);
		getActivity().finish();
	}
}
