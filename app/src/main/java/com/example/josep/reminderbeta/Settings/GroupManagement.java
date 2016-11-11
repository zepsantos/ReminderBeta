package com.example.josep.reminderbeta.Settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.josep.reminderbeta.Adapters.InvitesAvailableAdapter;
import com.example.josep.reminderbeta.Adapters.MembersAdapter;
import com.example.josep.reminderbeta.Main;
import com.example.josep.reminderbeta.Models.GroupMember;
import com.example.josep.reminderbeta.R;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupManagement extends PreferenceFragment {
    public static String GroupUpdated = null;
    private String uid;
    private DatabaseReference mDatabase; //Referencia Geral
    private FirebaseAuth mAuth; // AUTH STATE
    private Preference LeaveGroup; // Botão de dar leave do group
    private Preference Group; // CRIAR GRUPO || VER NOME DO GRUPO
    private Preference Members;// VER MEMBROS DO GRUPO || VER OS CONVITES DISPONIVIES EM CASO DE NÃO TER GRUPO
    private List<GroupMember> memberslist = new ArrayList<>();
    private List<GroupMember> inviteslist = new ArrayList<>();// MEMBERS LIST VOID || LISTA DE MEMBROS
    private InvitesAvailableAdapter invitesAdapter;
    private MembersAdapter membersAdapter;//MEMBERS ADAPTER PARA RECYCLER VIEW

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.groupsettings);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        Group = getPreferenceManager().findPreference("Group");
        Members = getPreferenceManager().findPreference("Members");
        LeaveGroup = getPreferenceManager().findPreference("LeaveGroup");
        CheckIfHasGroup();   //VERIFICAR SE O UTILIZADOR TEM GRUPO
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void CheckIfHasGroup() {
        if (Main.Group != null) {
            GroupUpdated = Main.Group;
        }
        if (GroupUpdated != null) {
            Group.setSummary(Main.Group);
            Members.setTitle(R.string.memberslist);
            LeaveGroupFunction();
            MembersListFragment();
        } else {
            Members.setTitle(R.string.InviteGroup);
            CreateGroup();// CRIAR GRUPO CASO NAO TENHA
            InvitesFragment();

        }

    }

    private void CreateGroup() {


        LeaveGroup.setEnabled(false); //EM CASO DE NAO TER GRUPO NAO DAR PARA DAR LEAVE
        Group.setSummary(R.string.ClicktoCreateGroup); //SUMARY PARA STRING CRIAR NOVO GRUPO
        Group.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(final Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Group Name:");


                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                builder.setView(input, 50, 10, 50, 10);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String mText = input.getText().toString();
                        final String upperString = mText.substring(0, 1).toUpperCase() + mText.substring(1);
                        Map<String, Object> childUpdates = new HashMap<>();
                        final String useruid = mAuth.getCurrentUser().getUid();
                        childUpdates.put("/groups/" + upperString + "/owner/", useruid);
                        childUpdates.put("/group-names/" + upperString, useruid);
                        childUpdates.put("/groups/" + upperString + "/members/" + useruid, Settings.mName);
                        childUpdates.put("/groups/" + upperString + "/name/", upperString);


                        mDatabase.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        addUserToGroup(useruid, upperString);
                                        groupRefresh();
                                        Group.setOnPreferenceClickListener(null);
                                    }
                                });
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                        });

                        /*final DatabaseReference mDatGroups = mDatabase.child("/");
                        mDatGroups.updateChildren(childUpdates);
                       mDatGroups.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    dialog.cancel();
                                    Toast toast = Toast.makeText(getContext(), "Group name isn't available", Toast.LENGTH_LONG);
                                    toast.show();
                                } else {
                                    if (mAuth.getCurrentUser() != null) {

                                        groupRefresh();
                                        Group.setOnPreferenceClickListener(null);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }); */


                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

                builder.show();

                return false;
            }
        });
    }

    private void addUserToGroup(String useruid, String upperString) {
        Map<String, Object> addusertogroup = new HashMap<>();
        addusertogroup.put("/users/" + useruid + "/group/", upperString);
        mDatabase.updateChildren(addusertogroup);
    }

    private void groupRefresh() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            DatabaseReference userGroup = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("group");
            userGroup.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GroupUpdated = dataSnapshot.getValue(String.class);
                    CheckIfHasGroup();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }

    private void LeaveGroupFunction() {
        LeaveGroup.setEnabled(true);
        LeaveGroup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.LeaveGroup);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mAuth.getCurrentUser() != null) {
                            DatabaseReference userGroup = mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("group");

                            userGroup.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    LeaveGroup.setEnabled(false);
                                    CreateGroup();
                                    groupRefresh();
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

                builder.show();
                return false;
            }
        });
    }

    private void MembersListFragment() {
        Members.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MembersList fragment = new MembersList();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainframe, fragment)
                        .addToBackStack(null)
                        .commit();
                return false;
            }
        });

    }

    private void InvitesFragment() {
        Members.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                InvitesList fragment = new InvitesList();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainframe, fragment)
                        .addToBackStack(null)
                        .commit();
                return false;
            }
        });
    }

}
