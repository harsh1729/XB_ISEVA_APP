package com.iseva.app.source;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 9/19/2016.
 */
public class Custom_Adapter_JobsList extends RecyclerView.Adapter<Custom_Adapter_JobsList.ViewHolder> {

    private ArrayList<Object_Jobs> mDataset;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtTitle;
        public TextView txtEligiblity;
        public TextView txtSalary;
        public Button btnCall;
        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.JobTitle);
            txtEligiblity = (TextView) v.findViewById(R.id.Eligibility);
            txtSalary = (TextView) v.findViewById(R.id.salary);
            btnCall = (Button) v.findViewById(R.id.buttonCall);
        }
    }

    /*public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }*/

   /* public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }*/

    // Provide a suitable constructor (depends on the kind of dataset)
    public Custom_Adapter_JobsList(ArrayList<Object_Jobs> myDataset,Context mContext) {
        mDataset = myDataset;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Custom_Adapter_JobsList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_jobs, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Object_Jobs obj = mDataset.get(position);
        holder.txtTitle.setText(obj.getJobTitle());
        holder.txtEligiblity.setText(obj.getEligiblity());
        holder.txtSalary.setText("Salary : "+obj.getSalary());
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.call(mContext,obj.getContact());
            }
        });


        holder.txtTitle.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Globals.showShortToast(mContext,"Clicked");
                naviDetails(obj.getId());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void naviDetails(int id){
        Intent i = new Intent(mContext,Activity_Job_Details.class);
        i.putExtra("id",id);
        ((Activity)mContext).startActivity(i);
    }
}
