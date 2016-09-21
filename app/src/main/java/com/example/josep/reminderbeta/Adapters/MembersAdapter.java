package com.example.josep.reminderbeta.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.josep.reminderbeta.Models.GroupMember;
import com.example.josep.reminderbeta.R;

import java.util.List;


public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {

    private List<GroupMember> memberList;

    public MembersAdapter(List<GroupMember> memberList) {
        this.memberList = memberList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memberslist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GroupMember members = memberList.get(position);
        holder.name.setText(members.getName());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);


        }
    }
}
