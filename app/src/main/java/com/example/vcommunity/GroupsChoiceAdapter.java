package com.example.vcommunity;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class GroupsChoiceAdapter extends RecyclerView.Adapter<GroupsChoiceAdapter.ViewHolder> {

    private ArrayList<ArrayList<String>> groupsParameters;
    private Activity gcaActy;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView GroupTitleView;
        private final TextView GroupMembersView;
        private String group_id;
        private Activity gcActy;

        public ViewHolder(View v, Activity acty) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(v1 -> {
                Intent i = new Intent();
                i.putExtra("group_id", group_id);
                gcActy.setResult(Activity.RESULT_OK, i);
                gcActy.finish();
            });
            GroupTitleView = v.findViewById(R.id.GroupTitleView);
            GroupMembersView = v.findViewById(R.id.GroupMembersView);
            gcActy = acty;
        }

        public TextView getGroupTitleView() {
            return GroupTitleView;
        }

        public TextView getGroupMembersView() {
            return GroupMembersView;
        }

        public void setGroup_id(String gid) {
            group_id = gid;
        }
    }

    public GroupsChoiceAdapter(ArrayList<ArrayList<String>> dataSet, Activity acty) {
        groupsParameters = dataSet;
        gcaActy = acty;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.group_choice_list_item, viewGroup, false);

        return new ViewHolder(v, gcaActy);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getGroupTitleView().setText(groupsParameters.get(position).get(0));
        viewHolder.getGroupMembersView().setText(groupsParameters.get(position).get(1));
        viewHolder.setGroup_id(groupsParameters.get(position).get(2));
    }

    @Override
    public int getItemCount() {
        return groupsParameters.size();
    }
}