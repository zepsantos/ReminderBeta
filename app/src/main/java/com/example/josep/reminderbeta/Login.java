package com.example.josep.reminderbeta;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {
	private static final String TAG = "MainActivity";
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private EditText emailText;
	private EditText passText;
	private TextView loginbtn;
	ProgressDialog progress;

	public Login() {
		// Required empty public constructor
	}

	private void signIn(String email, String password) {
		//    Log.d(TAG, "signIn:" + email);
		if (!validateForm()) {
			return;
		}


		// [START sign_in_with_email]
		mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						// Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
						if (task.isSuccessful()) {
							mAuth.addAuthStateListener(mAuthListener);
							progress.dismiss();
							Intent i = new Intent(getActivity(), Main.class);
							startActivity(i);
							getActivity().finish();

						}
						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						if (!task.isSuccessful()) {
							// Log.w(TAG, "signInWithEmail", task.getException());
							Toast.makeText(getActivity(), "Authentication failed.",
									Toast.LENGTH_SHORT).show();
							progress.dismiss();

						}

					}
				});
		// [END sign_in_with_email]
	}


	private boolean validateForm() {
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		if (user != null) {
			Intent i = new Intent(getActivity(), Main.class);
			startActivity(i);
		} else {
			loginbtn = (TextView) view.findViewById(R.id.logintv);
			loginbtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					progress = ProgressDialog.show(getActivity(), "",
							"Login in", true);
					signIn(emailText.getText().toString(), passText.getText().toString());
				}
			});

			emailText = (EditText) view.findViewById(R.id.email);
			passText = (EditText) view.findViewById(R.id.pass);

			mAuth = FirebaseAuth.getInstance();
			mAuthListener = new FirebaseAuth.AuthStateListener() {
				@Override
				public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
					FirebaseUser user = firebaseAuth.getCurrentUser();
					if (user != null) {
						// User is signed in

						Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
					} else {
						// User is signed out
						//    Log.d(TAG, "onAuthStateChanged:signed_out");
					}

				}
			};
		}
	}

}


