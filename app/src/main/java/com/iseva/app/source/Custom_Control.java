package com.iseva.app.source;

import android.content.Context;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;



/**
 * Created by com.bitsandbytes on 11/28/2015.
 */
public class Custom_Control {

    public static int DEFAULT_MARGIN = 8;
    public static int EditextID = 1;
    public static int ImageViewID = 2;
    public static int MultiImageID = 3;
    public static int SpinnerID = 4;

    public static TextView getTextView(Context mContext,int weight,String name){
        TextView txt = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,weight);
        layoutParam.setMargins(DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN);
        txt.setLayoutParams(layoutParam);
        txt.setText(name);
        return txt;
    }

    public static TextView getTextViewWithoutWeight(Context mContext,String name){
        TextView txt = new TextView(mContext);
        LinearLayout.LayoutParams layoutParam =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN);
        txt.setLayoutParams(layoutParam);
        txt.setText(name);
        return txt;
    }

    public static EditText getEditText(Context mContext,int weight,String content){
        EditText edt = new EditText(mContext);
        LinearLayout.LayoutParams layoutParam =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,weight );
        layoutParam.setMargins(DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN);
        edt.setLayoutParams(layoutParam);
        edt.setText(content);
        return edt;
    }

    public static Button getButton(Context mContext){
        Button btn = new Button(mContext);
        LinearLayout.LayoutParams layoutParam =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT );
        layoutParam.setMargins(DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN);
        btn.setPadding(3,0,3,0);
        btn.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.selector_btn_app));
        btn.setTextColor(mContext.getResources().getColor(R.color.app_white));
        btn.setLayoutParams(layoutParam);
        return btn;
    }

    public static CheckBox getCheckBox(Context context){
        CheckBox checkBox = new CheckBox(context);
        LinearLayout.LayoutParams layoutParam =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT );
        checkBox.setLayoutParams(layoutParam);
        return checkBox;
    }

    public static ImageView getImageView(Context mContext,int width,int height,int weight){
        ImageView img = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParam =
                new LinearLayout.LayoutParams(
                        width,
                        height,weight);
        layoutParam.setMargins(DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN);
        img.setLayoutParams(layoutParam);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.default_offer));
        return img;

    }
  public  static Spinner getSpinnerView(Context context,int weight){
      Spinner spin = new Spinner(context);
      LinearLayout.LayoutParams layoutParam =
              new LinearLayout.LayoutParams(
                      0,
                      LinearLayout.LayoutParams.WRAP_CONTENT ,weight);
      layoutParam.setMargins(DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN,DEFAULT_MARGIN);
      spin.setLayoutParams(layoutParam);
      return  spin;
  }

}
