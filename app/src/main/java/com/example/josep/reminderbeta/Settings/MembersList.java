package com.example.josep.reminderbeta.Settings;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.josep.reminderbeta.Adapters.MembersAdapter;
import com.example.josep.reminderbeta.Decorator.DividerItemDecoration;
import com.example.josep.reminderbeta.Main;
import com.example.josep.reminderbeta.Models.GroupMember;
import com.example.josep.reminderbeta.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersList extends Fragment {

    private DatabaseReference mDatabase;
    private List<GroupMember> memberslist = new ArrayList<>();
    private MembersAdapter membersAdapter;//MEMBERS ADAPTER PARA RECYCLER VIEW


    public MembersList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_members_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        MembersList();
        super.onViewCreated(view, savedInstanceState);
    }

    private void MembersList() {


        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fabaddtogroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InviteToGroup();
            }
        });
        RecyclerView recyclerView;

        recyclerView = (RecyclerView) getView().findViewById(R.id.membersview);

        membersAdapter = new MembersAdapter(memberslist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(membersAdapter);

        getMembers();


    }

    private void getMembers() {
        DatabaseReference DatMembers = mDatabase.child("group").child(Main.Group).child("members");
        DatMembers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memberslist.clear();
                List<String> lst = new ArrayList<>(); // Result will be holded Here
                Map<String, String> map = new HashMap<String, String>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    lst.add(String.valueOf(dsp.getKey())); //add result into array list
                    GroupMember groupMember = new GroupMember(dsp.getValue().toString());
                    memberslist.add(groupMember);
                    membersAdapter.notifyDataSetChanged();

                }

                for (String data : lst) {
                    //UID
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }  // GET DOS MEMBERS PARA A LISTA

    private void InviteToGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Invite to group:");
        final EditText input = new EditText(getContext());

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input, 50, 10, 50, 10);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (input.getText() != null) {
                    Invite(input.getText().toString());
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void Invite(final String username) {
        DatabaseReference useruid = mDatabase.child("username").child(username);
        useruid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String uid = dataSnapshot.getValue(String.class);
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/group-invites/" + uid + "/" + Main.Group, username);
                    childUpdates.put("/groups/" + Main.Group + "/members-invited/" + uid, username);
                    mDatabase.updateChildren(childUpdates);
                } else {
                    Toast toast = Toast.makeText(getContext(), "Username doesn't exist", Toast.LENGTH_LONG);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    } //A ESCRITA DO USERNAME NO GROUPINVITES E NO GROUP MEMBERS-INVITED
}
