package com.example.josep.reminderbeta.Settings;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.josep.reminderbeta.Adapters.InvitesAvailableAdapter;
import com.example.josep.reminderbeta.Decorator.DividerItemDecoration;
import com.example.josep.reminderbeta.Models.GroupMember;
import com.example.josep.reminderbeta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class InvitesList extends Fragment {
    private String uid;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private List<GroupMember> inviteslist = new ArrayList<>();// MEMBERS LIST VOID || LISTA DE MEMBROS
    private InvitesAvailableAdapter invitesAdapter;

    public InvitesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invites_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = mAuth.getCurrentUser().getUid();
        invitesAdapter = new InvitesAvailableAdapter(inviteslist);
        CheckInvitesAvailable();
        InvitesPage();
        super.onViewCreated(view, savedInstanceState);
    }

    private void InvitesPage() {

        RecyclerView recyclerView;

        recyclerView = (RecyclerView) getView().findViewById(R.id.invitesview);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(invitesAdapter);


    }

    private void CheckInvitesAvailable() {
        DatabaseReference InvitesAvailable = mDatabase.child("group-invites").child(uid);

        InvitesAvailable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inviteslist.clear();
                List<String> lst = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        lst.add(String.valueOf(dsp.getKey()));
                    }
                    for (String data : lst) {
                        GroupMember group = new GroupMember(data);
                        inviteslist.add(group);
                        invitesAdapter.notifyDataSetChanged();

                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
