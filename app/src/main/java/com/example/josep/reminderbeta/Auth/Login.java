package com.example.josep.reminderbeta.Auth;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
public class Login extends Fragment {
	private static final String TAG = "MainActivity";
	ProgressBar progress;
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private EditText emailText;
	private EditText passText;
	private String mText;
	private Button loginbtn;
	private Button signbtn;
	private DatabaseReference mDatchild;
	private DatabaseReference mDatabase;

	public Login () {
		// Required empty public constructor
	}

	private void signIn (String email, String password) {
		//    Log.d(TAG, "signIn:" + email);
		if (!validateForm()) {
			return;
		}


		// [START sign_in_with_email]
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete (@NonNull Task<AuthResult> task) {
						// Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
						if (task.isSuccessful()) {
							mAuth.addAuthStateListener(mAuthListener);
							hasName();

						}
						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							// Log.w(TAG, "signInWithEmail", task.getException());
							Toast.makeText(getActivity(), "Authentication failed.",
									Toast.LENGTH_SHORT).show();
							progress.setVisibility(View.INVISIBLE);
							passText.setText("");


						}

					}
				});
		// [END sign_in_with_email]
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
						Intent i = new Intent(getActivity(), Main.class);
						startActivity(i);
						getActivity().finish();
						progress.setVisibility(View.INVISIBLE);
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


	private boolean validateForm () {
		boolean valid = true;

		String email = emailText.getText().toString();
		if (TextUtils.isEmpty(email)) {
			emailText.setError("Required.");
			valid = false;
		} else {
			emailText.setError(null);
		}

		String password = passText.getText().toString();
		if (TextUtils.isEmpty(password)) {
			passText.setError("Required.");
			valid = false;
		} else {
			passText.setError(null);
		}

		return valid;
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onViewCreated (View view, Bundle savedInstanceState) {
		mDatabase = FirebaseDatabase.getInstance().getReference();
		progress = (ProgressBar) view.findViewById(R.id.progressBar);

		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		if (user != null) {
			Intent i = new Intent(getActivity(), Main.class);
			startActivity(i);
		} else {
			loginbtn = (Button) view.findViewById(R.id.btn_login);
			loginbtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick (View v) {

					progress.setVisibility(View.VISIBLE);
					View view = getActivity().getCurrentFocus();
					if (view != null) {
						InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
					signIn(emailText.getText().toString(), passText.getText().toString());
				}
			});
			signbtn = (Button) view.findViewById(R.id.btn_signup);
			signbtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick (View v) {
					CreateAccount fragment = new CreateAccount();
					FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
				}
			});
			emailText = (EditText) view.findViewById(R.id.email);
			passText = (EditText) view.findViewById(R.id.pass);

			mAuth = FirebaseAuth.getInstance();
			/*mAuthListener = new FirebaseAuth.AuthStateListener() {
				@Override
				public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth) {
					FirebaseUser user = firebaseAuth.getCurrentUser();
					if (user != null) {
						// User is signed in

						//	Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
					} else {
						// User is signed out
						//    Log.d(TAG, "onAuthStateChanged:signed_out");
					}

				}
			}; */
		}
	}

}


