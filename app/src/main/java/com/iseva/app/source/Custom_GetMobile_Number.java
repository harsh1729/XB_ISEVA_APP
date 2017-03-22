package com.iseva.app.source;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by xb_sushil on 4/25/2016.
 */
public class Custom_GetMobile_Number {

    private final static int DAYS_UNTIL_PROMPT = 0;
    public final static int LAUNCHES_UNTIL_PROMPT = 7;

    public static boolean app_launched(Context mContext) {
        Boolean returnStatus = false;

        SharedPreferences prefs = mContext.getSharedPreferences("getnum", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return returnStatus;
        }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count %LAUNCHES_UNTIL_PROMPT == 0) {
            if (System.currentTimeMillis() >= date_firstLaunch
                    + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showDialog(mContext, editor);
                returnStatus = true;
            }
        }

        editor.commit();

        return returnStatus;
    }

    private static void showDialog(final Context mContext,
                                       final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("" + Globals.APP_TITLE);


        //Window window = dialog.getWindow();
        //window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams l1lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l1lp.setMargins(20,0,20,0);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(l1lp);
        ll.setPadding(20,0,20,20);

        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("Register with us to WIN EXICITING PRIZE and get offers & discounts updates");
        tv.setWidth(240);
        tv.setPadding(6, 10, 6, 10);
        ll.addView(tv);
        //Name
        final EditText edtGetName = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(3, 6, 3, 6);
        edtGetName.setLayoutParams(lp);
        edtGetName.setHint("Type your Name");
        ll.addView(edtGetName);

        //number
       final EditText edtGetNum = new EditText(mContext);
        LinearLayout.LayoutParams lpnum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lpnum.setMargins(3, 6, 3, 6);
        edtGetNum.setLayoutParams(lpnum);
        edtGetNum.setHint("Type your number");
        edtGetNum.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        ll.addView(edtGetNum);

        Button b1 = new Button(mContext);
        b1.setText("Register");
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //rateIt(mContext);
                if(edtGetNum.getText().toString()!=null && !edtGetNum.getText().toString().equals("")) {
                    sendNumbers(mContext, edtGetNum.getText().toString(),edtGetName.getText().toString(),editor);
                    dialog.dismiss();
                   /* if (editor != null) {
                        editor.putBoolean("dontshowagain", false);
                        editor.commit();
                    }*/
                }else{
                    Globals.showShortToast(mContext,"Plz add valid Number. Thanks");
                }
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }

    public static void sendNumbers(final Context mContext,String num,String name, final SharedPreferences.Editor editor){
        try {
            if(!num.equals("")) {
                Custom_ConnectionDetector con = new Custom_ConnectionDetector(mContext);
                if (con.isConnectingToInternet()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("number", num);
                    map.put("name", name);
                    map.put("deviceid", Globals.getdeviceId(mContext));
                    Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                            Request.Method.POST,
                            Custom_URLs_Params.getURL_SENDNUMBERS(),
                            map, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("SUSHIL", "json Response recieved !!"+response);
                            parseResponse(response, editor, mContext);
                            // parseProfiledata(response);

                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Log.i("SUSHIL", "ERROR VolleyError");

                        }
                    });

                    Custom_VolleyAppController.getInstance().addToRequestQueue(
                            jsonObjectRQST);
                } else {
                    Object_AppConfig config = new Object_AppConfig(mContext);
                    config.setUserName(name);
                    config.setNumber(num);
                }
            }
        } catch (Exception ex) {

            ex.printStackTrace();

        }

    }

    public static void parseResponse(JSONObject obj, final SharedPreferences.Editor editor,Context mContext) {
        try {
            if (obj == null) {
                return;
            } else {
                if (obj.has("success")) {
                    if (obj.getString("success").equals("1")) {
                        Object_AppConfig config = new Object_AppConfig(mContext);
                        config.setisSendNumber(true);
                        if (editor != null) {
                            Log.i("SUSHIL", "dontshowagain Call !!");
                            editor.putBoolean("dontshowagain", true);
                            editor.commit();
                        }
                        return;
                    }
                    return;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

}
