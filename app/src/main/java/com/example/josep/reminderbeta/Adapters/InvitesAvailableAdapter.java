package com.example.josep.reminderbeta.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.josep.reminderbeta.Models.Invite;
import com.example.josep.reminderbeta.R;
import com.example.josep.reminderbeta.Settings.InvitesList;

import java.util.List;


public class InvitesAvailableAdapter extends RecyclerView.Adapter<InvitesAvailableAdapter.MyViewHolder> {
    private String InviteGroupname;
    private List<Invite> memberList;

    public InvitesAvailableAdapter(List<Invite> memberList) {
        this.memberList = memberList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invitesavailable_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Invite members = memberList.get(position);
        holder.name.setText(members.getName());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Invite member = memberList.get(position);
                InviteGroupname = member.getName();
                InvitesList invitesList = new InvitesList();
                invitesList.InviteAccepted();
            }
        });
    }

    public String getInviteGroupname() {
        return InviteGroupname;
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView accept;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            accept = (ImageView) view.findViewById(R.id.accept);


        }


    }


}



