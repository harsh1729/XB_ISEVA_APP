package com.iseva.app.source;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by hp on 9/12/2016.
 */
public class Custom_Adapter_ResumeList extends RecyclerView.Adapter<Custom_Adapter_ResumeList.ViewHolder> {

    private ArrayList<Object_Resume> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtName;
        public TextView txtQualification;
        public TextView txtExperience;
        public ImageView imgProfile;
        public ViewHolder(View v) {
            super(v);
            txtName = (TextView) v.findViewById(R.id.personName);
            txtQualification = (TextView) v.findViewById(R.id.personQualification);
            txtExperience = (TextView) v.findViewById(R.id.personExperience);
            imgProfile = (ImageView)v.findViewById(R.id.imgPrifile);
            //ImageView Resize
            int totalContent = Globals.getScreenSize((Activity) mContext).x;
            int imgWidth = totalContent - ((totalContent * 75) / 100);


            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imgProfile
                    .getLayoutParams();
            lp.width = imgWidth;
            lp.height = (int) (imgWidth * 1.3);

            imgProfile.setLayoutParams(lp);
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
    public Custom_Adapter_ResumeList(ArrayList<Object_Resume> myDataset, Context mContext) {
        mDataset = myDataset;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Custom_Adapter_ResumeList.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_resume_person, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Object_Resume obj = mDataset.get(position);
        Globals.loadImageIntoImageView(holder.imgProfile,obj.getImgUrl(),mContext,R.drawable.default_offer,R.drawable.default_offer);
        holder.txtName.setText(obj.getName()+" ("+obj.getGender()+")");
        holder.txtExperience.setText("Experience :"+obj.getExperience());
        holder.txtQualification.setText("Qualification :"+obj.getQualification());
        holder.txtName.getRootView().setOnClickListener(new View.OnClickListener() {
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

   private  void naviDetails(int id){
       //Object_AppConfig config = new Object_AppConfig(mContext);
      // config.setEmpId(id);
       Intent i = new Intent(mContext,Activity_Resume_Details.class);
       i.putExtra("id",id);
       ((Activity)mContext).startActivity(i);
   }
}
