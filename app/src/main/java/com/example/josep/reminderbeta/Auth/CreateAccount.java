package com.example.josep.reminderbeta.Auth;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.josep.reminderbeta.Main;
import com.example.josep.reminderbeta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccount extends Fragment {
	Button createAccbtn;
	EditText newemailText;
	EditText newpassText;
	FirebaseAuth mAuth;
	ProgressBar progress;
	String mText;
	private DatabaseReference mDatabase;
	private DatabaseReference mDatchild;

	public CreateAccount () {
		// Required empty public constructor
	}


	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		return inflater.inflate(R.layout.fragment_create_account, container, false);
	}

	public void onViewCreated (View view, Bundle savedInstanceState) {
		mDatabase = FirebaseDatabase.getInstance().getReference();
		progress = (ProgressBar) view.findViewById(R.id.progressBar);
		createAccbtn = (Button) view.findViewById(R.id.sign_up_button);
		newemailText = (EditText) view.findViewById(R.id.newEmail);
		newpassText = (EditText) view.findViewById(R.id.newPassword);
		createAccbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				progress.setVisibility(View.VISIBLE);
				createAccount(newemailText.getText().toString(), newpassText.getText().toString());


			}
		});


	}

	private void createAccount (String email, String password) {
		mAuth = FirebaseAuth.getInstance();

		if (!validateForm()) {
			return;
		}


		// [START create_user_with_email]
		mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete (@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					onAuthSuccess(task.getResult().getUser());


				}
				// If sign in fails, display a message to the user. If sign in succeeds
				// the auth state listener will be notified and logic to handle the
				// signed in user can be handled in the listener.
				if (!task.isSuccessful()) {
					progress.setVisibility(View.INVISIBLE);
					Toast.makeText(getContext(), "Failed to Create.",
							Toast.LENGTH_SHORT).show();
				}

				// [START_EXCLUDE]

				// [END_EXCLUDE]
			}
		});
		// [END create_user_with_email] */


	}

	private void onAuthSuccess (FirebaseUser user) {


		// Write new user
		writeNewUser(user.getUid(), user.getEmail());
		hasName();
	}

	private void hasName () {
		mAuth = FirebaseAuth.getInstance();
		if (mAuth.getCurrentUser() != null) {
			mDatchild = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("Name");
			mDatchild.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange (DataSnapshot dataSnapshot) {
					String name = dataSnapshot.getValue(String.class);
					if (name != null) {
						progress.setVisibility(View.INVISIBLE);

						// Go to MainActivity
						Intent i = new Intent(getActivity(), Main.class);
						startActivity(i);
						getActivity().finish();
					} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
						builder.setTitle("What's your name?");


						final EditText input = new EditText(getContext());

						input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
						builder.setView(input);

						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick (DialogInterface dialog, int which) {
								mText = input.getText().toString();
								mDatchild.setValue(mText);
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick (DialogInterface dialog, int which) {
								dialog.cancel();

							}
						});

						builder.show();

					}

				}

				@Override
				public void onCancelled (DatabaseError databaseError) {

				}
			});

		}
	}

	private void writeNewUser (String userId, String email) {
		User user = new User(email);


		mDatabase.child("users").child(userId).setValue(user);

	}


	private boolean validateForm () {
		boolean valid = true;

		String email = newemailText.getText().toString();
		if (TextUtils.isEmpty(email)) {
			newemailText.setError("Required.");
			valid = false;
		} else {
			newemailText.setError(null);
		}

		String password = newpassText.getText().toString();
		if (TextUtils.isEmpty(password)) {
			newpassText.setError("Required.");
			valid = false;
		} else {
			newpassText.setError(null);
		}

		return valid;
	}
}


