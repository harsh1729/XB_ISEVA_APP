package com.iseva.app.source;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 9/21/2016.
 */
public class Custom_Adapter_Home_Category extends RecyclerView.Adapter<Custom_Adapter_Home_Category.ViewHolder> {
    private ArrayList<Object_Category> listCat;
    private Context context;

    public Custom_Adapter_Home_Category(Context context,ArrayList<Object_Category> list) {
        this.listCat = list;
        this.context = context;
    }

    @Override
    public Custom_Adapter_Home_Category.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_card_view_home, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Custom_Adapter_Home_Category.ViewHolder viewHolder, int i) {

        Object_Category objectCategory = listCat.get(i);
        viewHolder.catName.setText(objectCategory.name);
        String imagename = objectCategory.image;
        // img.setImageBitmap(BitmapFactory.decodeByteArray(objectCategory.image, 0, objectCategory.image.length));
        int resID = context.getResources().getIdentifier(imagename, "drawable", context.getPackageName());
        viewHolder.catImg.setBackgroundDrawable(context.getResources().getDrawable(resID));

        //Picasso.with(context).load(android.get(i).getAndroid_image_url()).resize(240, 120).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return listCat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView catName;
        private ImageView catImg;
        public ViewHolder(View view) {
            super(view);

            catName = (TextView)view.findViewById(R.id.txtcategoryName);
            catImg = (ImageView) view.findViewById(R.id.imageViewCate_Home);
        }
    }
}
