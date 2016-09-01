package com.example.josep.reminderbeta;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccount extends Fragment {
    private DatabaseReference mDatabase;
    TextView createAccbtn;
    EditText newemailText;
    EditText newpassText;
    FirebaseAuth mAuth;
    ProgressDialog progress;
    public CreateAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView sign = (TextView) view.findViewById(R.id.tvsign);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login fragment = new Login();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
	            fragmentTransaction.addToBackStack(null);
	            fragmentTransaction.commit();

            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();

        createAccbtn = (TextView) view.findViewById(R.id.createAcc);
        newemailText = (EditText) view.findViewById(R.id.emailNewAcc);
        newpassText = (EditText) view.findViewById(R.id.passNewAcc);
        createAccbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = ProgressDialog.show(getActivity(), "",
                        "Creating Account", true);
                createAccount(newemailText.getText().toString(), newpassText.getText().toString());


            }
        });


        }
    private void createAccount(String email, String password) {
        mAuth = FirebaseAuth.getInstance();

        if (!validateForm()) {
            return;
        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onAuthSuccess(task.getResult().getUser());
                    progress.dismiss();

                }
                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    progress.dismiss();
                    Toast.makeText(getContext(), "Failed to Create.",
                            Toast.LENGTH_SHORT).show();
                }

                // [START_EXCLUDE]

                // [END_EXCLUDE]
            }
        });
        // [END create_user_with_email] */


    }

    private void onAuthSuccess(FirebaseUser user) {


        // Write new user
        writeNewUser(user.getUid(), user.getEmail());

        // Go to MainActivity
        Intent i = new Intent(getActivity(), Main.class);
        startActivity(i);
        getActivity().finish();
    }

    private void writeNewUser(String userId, String email) {
        User user = new User(email);


        mDatabase.child("users").child(userId).setValue(user);
    }


    private boolean validateForm() {
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


