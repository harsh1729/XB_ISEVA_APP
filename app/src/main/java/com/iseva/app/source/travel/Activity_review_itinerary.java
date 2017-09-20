package com.iseva.app.source.travel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iseva.app.source.Custom_VolleyAppController;
import com.iseva.app.source.Custom_VolleyObjectRequest;
import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Realm_Selected_Bus_Details;
import com.iseva.app.source.Realm_objets.Realm_Selected_Seats;
import com.iseva.app.source.travel.Constants.JSON_KEYS;
import com.iseva.app.source.travel.Constants.SEAT_DETAILS;
import com.iseva.app.source.travel.Constants.URL_XB;
import com.iseva.app.source.travel.Global_Travel.TRAVEL_DATA;
import com.payUMoney.sdk.SdkConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.ContentValues.TAG;
import static com.payUMoney.sdk.PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE;
import static com.payUMoney.sdk.PayUmoneySdkInitilizer.PaymentParam;
import static com.payUMoney.sdk.PayUmoneySdkInitilizer.RESULT_BACK;
import static com.payUMoney.sdk.PayUmoneySdkInitilizer.RESULT_FAILED;
import static com.payUMoney.sdk.PayUmoneySdkInitilizer.startPaymentActivityForResult;
import static java.lang.Integer.parseInt;


public class Activity_review_itinerary extends Activity_Parent_Travel {


    Realm My_realm;

    //SoapObject soapresult_detail;

    LinearLayout layout_header_text;
    ImageView header_iv;
    TextView header_tv;
    TextView timer_tv;
    TextView travels_name_tv;
    TextView bus_label_tv;
    TextView from_city;
    TextView to_city;
    TextView dep_time_tv;
    TextView arr_time_tv;
    TextView dep_date_tv;
    TextView arr_date_tv;
    TextView no_of_travellers_tv;
    TextView seats_tv;
    TextView boarding_point_name_tv;
    TextView boarding_point_time_tv;
    TextView base_fare_tv;
    TextView promocode_tv;
    TextView cancelation_tv;
    TextView cancel_cancellation_policy_layout;
    Button promocode_cancel_btn;
    Button promocode_apply_btn;
    Button proceed_btn;
    LinearLayout promocode_main_layout;

    Boolean flag_manual_promo = false;

    String HoldKey;
    String TotalFare;
    String BaseFare;
    String ExtraCharge;
    String DiscountFare = "0";
    String ServisTex = "0";
    String BoardingPoint;
    String BoardingTime;
    String contact_email;
    String contact_phone;
    String contact_name;
    String Boarding_point_address;
    String Boarding_point_phone;
    String Boarding_point_landmark;

    String Departure_time;

    String payu_transaction_id;

    String cancellation_data_string;

    int coupan_id = 0;
    CountDownTimer countDownTimer;

    RealmResults<Realm_Selected_Bus_Details> schedule_details;
    RealmResults<Realm_Selected_Seats> Selected_seat_list;


    Session_manager session_manager;

    LinearLayout login_alert_layout;
    TextView login_alert_cancel_btn;
    TextView login_alert_login_btn;
    TextView login_alert_signup_btn;

    private int volley_timeout = 15000;

    RequestQueue requestQueue_globel;

    JSONArray ps;

    int service_calls_count = 0;

    //String total_fare_book_responce = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_itinerary);
        My_realm = Realm.getDefaultInstance();
        //   Payu.setInstance(this);


        showProgress();
        initialize();

        service_calls_count = 2;

        getextracharge(); //1
        starttimer();
        setonclicklister();
        setcancellation_policy_data();
        setoffers(); //2
    }

    private void dismissProgress(){

        service_calls_count--;

        if(service_calls_count == 0 && progress != null){
            progress.dismiss();
        }
    }

    private void showProgress(){

        progress = new ProgressDialog(Activity_review_itinerary.this);
        progress.setMessage("Please wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        progress.show();

    }

    public void getextracharge() {
        StringRequest promocodeapplyrequest = new StringRequest(Request.Method.POST,
                URL_XB.GET_EXTRA_CHARGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                if (Global_Travel.build_type == 0) {
                    Log.e("vikas", s);
                }

                dismissProgress();

                JSONObject response = null;
                try {
                    response = new JSONObject(s);


                    if (response != null) {
                        if (response.get("success").toString().equals("1")) {
                            TextView tv = (TextView) findViewById(R.id.itinerary_service_tax_tv);

                            ExtraCharge = response.getJSONObject("data").getString("commition");
                            TotalFare = Float.toString(Float.parseFloat(BaseFare) + Float.parseFloat(ExtraCharge));
                            proceed_btn.setText("Proceed to Pay \u20B9 " + TotalFare);
                            tv.setText("\u20B9 " + ExtraCharge);
                        } else {
                            //Toast.makeText(Activity_review_itinerary.this, "Something is wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                dismissProgress();
                Toast.makeText(getApplicationContext(), "Error is -->> " + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;

            }
        };

        promocodeapplyrequest.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_review_itinerary.this);
        requestQueue.add(promocodeapplyrequest);
    }

    public void starttimer() {
        countDownTimer = new CountDownTimer(1000 * 60 * 10, 1000) {

            public void onTick(long millisUntilFinished) {
                long min = millisUntilFinished / (1000 * 60);
                long milisec = millisUntilFinished % (1000 * 60);
                long sec = milisec / 1000;
                if (sec > 9) {
                    timer_tv.setText(min + ":" + sec);
                } else {
                    timer_tv.setText(min + ":0" + sec);
                }

            }

            public void onFinish() {

                Activity_review_itinerary.this.finish();
            }
        }.start();
    }

    public void initialize() {

        Intent i = getIntent();
        HoldKey = i.getStringExtra("HoldKey");
        BaseFare = i.getStringExtra("TotalFare");
        BoardingPoint = i.getStringExtra("BoardingPoint");
        BoardingTime = i.getStringExtra("BoardingTime");
        cancellation_data_string = i.getStringExtra("cancellation_data");
        contact_email = i.getStringExtra("contact_email");
        contact_phone = i.getStringExtra("contact_phone");
        contact_name = i.getStringExtra("contact_name");
        Boarding_point_address = i.getStringExtra("boarding_point_address");
        Boarding_point_phone = i.getStringExtra("boarding_point_phone");
        Boarding_point_landmark = i.getStringExtra("boarding_point_landmark");

        //Toast.makeText(Activity_review_itinerary.this,contact_email,Toast.LENGTH_LONG).show();

        session_manager = new Session_manager(Activity_review_itinerary.this);

        My_realm.beginTransaction();
        schedule_details = My_realm.where(Realm_Selected_Bus_Details.class).findAll();
        Selected_seat_list = My_realm.where(Realm_Selected_Seats.class).findAll();
        My_realm.commitTransaction();

        layout_header_text = (LinearLayout) findViewById(R.id.layout_header_text);
        layout_header_text.setGravity(Gravity.CENTER_VERTICAL);

        header_iv = (ImageView) findViewById(R.id.header_back_button);
        header_tv = (TextView) findViewById(R.id.header_text);
        header_tv.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_margin_15), 0, 0, 0);
        header_tv.setText(R.string.itinerary_header_text);

        timer_tv = (TextView) findViewById(R.id.itinerary_timer_tv);

        from_city = (TextView) findViewById(R.id.itinerary_from_city_name);
        to_city = (TextView) findViewById(R.id.itinerary_to_city_name);
        from_city.setText(TRAVEL_DATA.FROM_CITY_NAME);
        to_city.setText(TRAVEL_DATA.TO_CITY_NAME);

        travels_name_tv = (TextView) findViewById(R.id.itinerary_travals_name);
        bus_label_tv = (TextView) findViewById(R.id.itinerary_bus_label);
        dep_time_tv = (TextView) findViewById(R.id.itinerary_dep_time);
        arr_time_tv = (TextView) findViewById(R.id.itinerary_arr_time);
        dep_date_tv = (TextView) findViewById(R.id.itinerary_dep_date);
        arr_date_tv = (TextView) findViewById(R.id.itinerary_arr_date);
        no_of_travellers_tv = (TextView) findViewById(R.id.itinerary_no_of_travellers);
        seats_tv = (TextView) findViewById(R.id.itinerary_no_of_seats);
        boarding_point_name_tv = (TextView) findViewById(R.id.itinerary_boarding_point_name);
        boarding_point_time_tv = (TextView) findViewById(R.id.itinerary_boarding_time);
        base_fare_tv = (TextView) findViewById(R.id.itinerary_base_fare_tv);
        promocode_tv = (TextView) findViewById(R.id.itinerary_promocode_tv);
        proceed_btn = (Button) findViewById(R.id.itinerary_proceed_btn);
        promocode_cancel_btn = (Button) findViewById(R.id.itinerary_promocode_cancel_btn);
        promocode_apply_btn = (Button) findViewById(R.id.itinerary_promocode_apply_btn);
        promocode_main_layout = (LinearLayout) findViewById(R.id.itinerary_promocode_main_layout);
        cancelation_tv = (TextView) findViewById(R.id.itinerary_cancelation_tv);
        cancel_cancellation_policy_layout = (TextView) findViewById(R.id.cancel_cancellation_layout);
        login_alert_layout = (LinearLayout) findViewById(R.id.login_alert_layout);
        login_alert_cancel_btn = (TextView) findViewById(R.id.login_alert_cancel_btn);
        login_alert_login_btn = (TextView) findViewById(R.id.login_alert_login_btn);
        login_alert_signup_btn = (TextView) findViewById(R.id.login_alert_signup_btn);


        travels_name_tv.setText(schedule_details.get(0).getCompanyName());
        bus_label_tv.setText(schedule_details.get(0).getBusLabel());
        dep_time_tv.setText(schedule_details.get(0).getDepTimeOnly());
        arr_time_tv.setText(schedule_details.get(0).getArrTimeOnly());
        dep_date_tv.setText(change_date_form(schedule_details.get(0).getDepDateTime()));
        arr_date_tv.setText(change_date_form(schedule_details.get(0).getArrDateTime()));
        Departure_time = schedule_details.get(0).getDepDateTime();
        no_of_travellers_tv.setText("" + Selected_seat_list.size());
        String seat = "";
        for (int l = 0; l < Selected_seat_list.size(); l++) {
            if (l == 0) {
                seat = seat + Selected_seat_list.get(l).getSeatNo();
            } else {
                seat = seat + ", " + Selected_seat_list.get(l).getSeatNo();
            }

        }
        seats_tv.setText(seat);
        boarding_point_name_tv.setText(BoardingPoint);
        boarding_point_time_tv.setText(BoardingTime);

        base_fare_tv.setText(BaseFare);


        requestQueue_globel = Volley.newRequestQueue(Activity_review_itinerary.this);


    }


    public void setonclicklister() {
        cancelation_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hidekeyboard();
                LinearLayout cl = (LinearLayout) findViewById(R.id.cancellation_layout);
                cl.setVisibility(View.VISIBLE);
            }
        });

        cancel_cancellation_policy_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hidekeyboard();
                LinearLayout cl = (LinearLayout) findViewById(R.id.cancellation_layout);
                cl.setVisibility(View.GONE);
            }
        });

        header_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hidekeyboard();
                activity_dismiss();
            }
        });

        promocode_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hidekeyboard();
                if (isNetworkConnected(false)) {
                    if (session_manager.isLoggedIn()) {
                        flag_manual_promo = true;
                        promocode_main_layout.setVisibility(View.VISIBLE);
                    } else {
                        login_alert_layout.setVisibility(View.VISIBLE);
                    }


                } else {

                    Global_Travel.showAlertDialog(Activity_review_itinerary.this, getResources().getString(R.string.internet_connection_error_title), getResources().getString(R.string.internet_connection_error_message), "Ok");

                }


            }
        });

        promocode_cancel_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hidekeyboard();
                LinearLayout error_layout = (LinearLayout) findViewById(R.id.itinerary_promocode_error_layout);
                EditText promocode_et = (EditText) findViewById(R.id.itinerary_promocode_et);
                promocode_main_layout.setVisibility(View.INVISIBLE);
                error_layout.setVisibility(View.INVISIBLE);
                promocode_et.setText("");
                flag_manual_promo = false;


            }
        });

        proceed_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hidekeyboard();

                if (isNetworkConnected(false)) {
                    if (session_manager.isLoggedIn()) {

                        payu_transaction_id = "iSeva" + System.currentTimeMillis();
                        add_transaction_server();

                        makepayment();

                    } else {
                        login_alert_layout.setVisibility(View.VISIBLE);
                    }


                } else {
                    Global_Travel.showAlertDialog(Activity_review_itinerary.this, getResources().getString(R.string.internet_connection_error_title), getResources().getString(R.string.internet_connection_error_message), "Ok");
                }

            }
        });

        promocode_apply_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                hidekeyboard();

                final EditText promocode_et = (EditText) findViewById(R.id.itinerary_promocode_et);
                final String promocode_et_txt = promocode_et.getText().toString().trim();
                apply_promocode(promocode_et_txt, session_manager.getid(), "0");


            }
        });

        login_alert_cancel_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login_alert_layout.setVisibility(View.GONE);
            }
        });

        login_alert_login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login_alert_layout.setVisibility(View.GONE);
                Intent i = new Intent(Activity_review_itinerary.this, Activity_login.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
            }
        });

        login_alert_signup_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                login_alert_layout.setVisibility(View.GONE);
                Intent i = new Intent(Activity_review_itinerary.this, Activity_register.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
            }
        });
    }

    public void makepayment() {
        session_manager = new Session_manager(Activity_review_itinerary.this);

        String phone = session_manager.getphone();
        String productName = "Book Ticket";
        String firstName = session_manager.getname();
        String txnId = payu_transaction_id;
        String email = session_manager.getusername();
        String sUrl = "http://xercesblue.website/iservice/success";
        String fUrl = "http://xercesblue.website/iservice/failiar";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        boolean isDebug = false;
        String key = "EaXKMGaq";
        String merchantId = "5755743";

       /* String key = "dRQuiA";
        String merchantId = "4928174" ;
      String salt = "teEkuVg2";*/

        PaymentParam.Builder builder = new PaymentParam.Builder();


        Double total_fare = Double.parseDouble(TotalFare);

        builder.setAmount(total_fare)
                .setTnxId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(sUrl)
                .setfUrl(fUrl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setIsDebug(isDebug)
                .setKey(key)
                .setMerchantId(merchantId);

        PaymentParam paymentParam = builder.build();


        calculateServerSideHashAndInitiatePayment(paymentParam);

       /* String serverCalculatedHash=hashCal(key+"|"+txnId+"|"+"5"+"|"+productName+"|"
                +firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(Activity_review_itinerary.this, paymentParam);*/
    }


    private void calculateServerSideHashAndInitiatePayment(final PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        // String url = "https://test.payumoney.com/payment/op/calculateHashForTest";
        String url = "http://xercesblue.website/iservice/client_requests/transaction/create_hash";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);


                            paymentParam.setMerchantHash(hash);

                            startPaymentActivityForResult(Activity_review_itinerary.this, paymentParam);
                        } else {

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(Activity_review_itinerary.this,
                            Activity_review_itinerary.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Activity_review_itinerary.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PAYU_SDK_PAYMENT_REQUEST_CODE) {

            /*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*/


            if (resultCode == RESULT_OK) {

                countDownTimer.cancel();
                if (Global_Travel.build_type == 0) {
                    Log.e("vikas", "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                }

                send_success_payment_status(data.getStringExtra(SdkConstants.PAYMENT_ID));
                bookSeat();


            } else if (resultCode == RESULT_CANCELED) {
                if (Global_Travel.build_type == 0) {
                    Log.i(TAG, "failure");
                }
                //showAlertDialog(getResources().getString(R.string.validating_error_title),"Payment not success","ok");

            } else if (resultCode == RESULT_FAILED) {
                if (Global_Travel.build_type == 0) {
                    Log.i("app_activity", "failure");
                }
                Global_Travel.showAlertDialog(Activity_review_itinerary.this, getResources().getString(R.string.validating_error_title), "Transaction Failed!", "ok");
                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {

                    }
                }
                //Write your code if there's no result
            } else if (resultCode == RESULT_BACK) {
                if (Global_Travel.build_type == 0) {
                    Log.i(TAG, "User returned without login");
                }

            }
        }
    }


    public void add_transaction_server() {
        StringRequest addtransaction = new StringRequest(Request.Method.POST,
                Constants.URL_XB.ADD_TRANSACTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", session_manager.getid());
                params.put("payu_transaction_id", payu_transaction_id);
                params.put("coupan_id", "" + coupan_id);
                params.put("username", contact_name);
                params.put("phone", contact_phone);
                params.put("email", contact_email);
                params.put("ticket_date", Departure_time);
                params.put("from_city", TRAVEL_DATA.FROM_CITY_NAME);
                params.put("to_city", TRAVEL_DATA.TO_CITY_NAME);
                params.put("total_fare", TotalFare);
                params.put("extra_charge", ExtraCharge);

                return params;

            }
        };


       /* RequestQueue requestQueue = Volley.newRequestQueue(Activity_review_itinerary.this);*/
        addtransaction.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_globel.add(addtransaction);
    }

    public void send_success_payment_status(final String payu_payment_id) {
        StringRequest success_payment = new StringRequest(Request.Method.POST,
                URL_XB.SUCCESS_PAYMENT, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("payu_transaction_id", payu_transaction_id);
                params.put("payu_payment_id", payu_payment_id);

                return params;

            }
        };


        /*RequestQueue requestQueue = Volley.newRequestQueue(Activity_review_itinerary.this);*/
        success_payment.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_globel.add(success_payment);
    }

    public void send_start_booking_status() {
        StringRequest start_booking = new StringRequest(Request.Method.POST,
                URL_XB.START_BOOKING, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.i("HARSH","send_start_booking_status Success");

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("HARSH","send_start_booking_status Error");

                //Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("payu_transaction_id", payu_transaction_id);

                return params;

            }
        };


        /*RequestQueue requestQueue = Volley.newRequestQueue(Activity_review_itinerary.this);*/
        start_booking.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_globel.add(start_booking);
    }


    public void send_success_booking_status(final String mentis_transaction_id, final String pnr_no, final String ticket_no, final String order_id, final String repoting_time, final String status) {
        StringRequest success_booking = new StringRequest(Request.Method.POST,
                URL_XB.SUCCESS_BOOKING, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.i("HARSH","send_success_booking_status Success");
                sendmessage(order_id, ticket_no, pnr_no, TRAVEL_DATA.FROM_CITY_NAME, TRAVEL_DATA.TO_CITY_NAME, TRAVEL_DATA.JOURNEY_DATE, repoting_time, BoardingTime, status, ps.toString(), Boarding_point_address, schedule_details.get(0).getBusLabel(), schedule_details.get(0).getCompanyName(), contact_phone, TotalFare, cancellation_data_string);
                sendmail(order_id, ticket_no, pnr_no, TRAVEL_DATA.FROM_CITY_NAME, TRAVEL_DATA.TO_CITY_NAME, TRAVEL_DATA.JOURNEY_DATE, repoting_time, BoardingTime, status, ps.toString(), Boarding_point_address, schedule_details.get(0).getBusLabel(), schedule_details.get(0).getCompanyName(), Boarding_point_phone, TotalFare, cancellation_data_string);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HARSH","send_success_booking_status Error");

                navigateToBookingDetails(pnr_no,ticket_no);

                //Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("payu_transaction_id", payu_transaction_id);
                params.put("mentis_id", mentis_transaction_id);
                params.put("pnr_no", pnr_no);
                params.put("ticket_no", ticket_no);


                return params;

            }
        };


        /*RequestQueue requestQueue = Volley.newRequestQueue(Activity_review_itinerary.this);*/
        success_booking.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_globel.add(success_booking);
    }

    public void apply_promocode(final String coupan_code, final String userid, final String isglobel) {

        showProgress();

        service_calls_count++;

        final EditText promocode_et = (EditText) findViewById(R.id.itinerary_promocode_et);
        final LinearLayout error_layout = (LinearLayout) findViewById(R.id.itinerary_promocode_error_layout);
        final TextView error_tv = (TextView) findViewById(R.id.itinerary_promocode_error_tv);

        StringRequest promocodeapplyrequest = new StringRequest(Request.Method.POST,
                URL_XB.GET_OFFER_WITH_COUPON_NO, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.i("HARSH","apply_promocode Success");
                if (Global_Travel.build_type == 0) {
                    Log.e("vikas", s);
                }

                dismissProgress();
                JSONObject response = null;
                try {
                    response = new JSONObject(s);


                    if (response != null) {
                        if (response.get("success").toString().equals("1")) {

                            JSONArray offers = response.getJSONArray("data");
                            if (Global_Travel.build_type == 0) {
                                Log.e("vikas", offers.toString());
                            }


                            if (offers.length() != 0) {
                                final LinearLayout discount_layout = (LinearLayout) findViewById(R.id.itinerary_discount_layout);
                                TextView discount_tv = (TextView) findViewById(R.id.itinerary_discount_tv);

                                Float basefare = Float.parseFloat(BaseFare);
                                int discount_percentage = parseInt(offers.getJSONObject(0).getString("discount_percentage"));
                                int discount_amount = parseInt(offers.getJSONObject(0).getString("discount_amount"));
                                int discount_amount_according_to_basefare = (int) (basefare * discount_percentage) / 100;
                                if (discount_amount == 0) {
                                    DiscountFare = Float.toString(discount_amount_according_to_basefare);
                                    TotalFare = Float.toString(basefare - discount_amount_according_to_basefare + Float.parseFloat(ExtraCharge));
                                } else {
                                    if (discount_amount < discount_amount_according_to_basefare) {
                                        DiscountFare = Integer.toString(discount_amount);
                                        TotalFare = Float.toString(basefare - discount_amount + Float.parseFloat(ExtraCharge));
                                    } else {
                                        DiscountFare = Float.toString(discount_amount_according_to_basefare);
                                        TotalFare = Float.toString(basefare - discount_amount_according_to_basefare + Float.parseFloat(ExtraCharge));
                                    }
                                }
                                coupan_id = offers.getJSONObject(0).getInt("id");
                                discount_tv.setText("\u20B9 " + DiscountFare);
                                discount_layout.setVisibility(View.VISIBLE);
                                proceed_btn.setText("Proceed to Pay \u20B9 " + TotalFare);


                                final LinearLayout offer_top_layout = (LinearLayout) findViewById(R.id.itinerary_offer_top_layout);
                                final LinearLayout offer_selected_layout = (LinearLayout) findViewById(R.id.itinerary_promocode_selected_offer_layout);
                                TextView offer_selected_tag = (TextView) findViewById(R.id.activity_itinerary_selected_offer_tag_tv);
                                TextView offer_selected_detail = (TextView) findViewById(R.id.activity_itinerary_selected_offer_detail_tv);
                                TextView offer_selected_remove_btn = (TextView) findViewById(R.id.itinerare_selected_offer_remove_btn);

                                offer_selected_tag.setText(offers.getJSONObject(0).getString("tag"));
                                offer_selected_detail.setText(offers.getJSONObject(0).getString("detail"));
                                offer_top_layout.setVisibility(View.GONE);
                                offer_selected_layout.setVisibility(View.VISIBLE);

                                offer_selected_remove_btn.setOnClickListener(new View.OnClickListener() {


                                    @Override
                                    public void onClick(View view) {

                                       /* TextView title_tv = new TextView(Activity_review_itinerary.this);
                                        title_tv.setPadding(0,getResources().getDimensionPixelSize(R.dimen.padding_margin_10),0,0);
                                        title_tv.setTextColor(ContextCompat.getColor(Activity_review_itinerary.this,R.color.black));
                                        title_tv.setTextSize(16);
                                        title_tv.setGravity(Gravity.CENTER);
                                        title_tv.setText("Alert");*/
                                        LayoutInflater inflater = (LayoutInflater) Activity_review_itinerary.this.getSystemService
                                                (Context.LAYOUT_INFLATER_SERVICE);

                                        View v = inflater.inflate(R.layout.textview, null);


                                        TextView title_tv = (TextView) v.findViewById(R.id.alert_title);
                                        title_tv.setText("Alert");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_review_itinerary.this);
                                        builder.setCustomTitle(title_tv)
                                                .setMessage("You are sure to remove promocode")
                                                .setCancelable(false)

                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                })
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        coupan_id = 0;
                                                        TotalFare = Float.toString(Float.parseFloat(BaseFare) + Float.parseFloat(ExtraCharge));
                                                        proceed_btn.setText("Proceed to Pay \u20B9 " + TotalFare);
                                                        setoffers();
                                                        offer_top_layout.setVisibility(View.VISIBLE);
                                                        offer_selected_layout.setVisibility(View.GONE);
                                                        discount_layout.setVisibility(View.GONE);
                                                        enable_promocode_server(coupan_code);
                                                    }
                                                });


                                        AlertDialog alert = builder.create();
                                        alert.show();

                                        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                                        b.setTextColor(ContextCompat.getColor(Activity_review_itinerary.this, R.color.app_theme_color));
                                        Button c = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                                        c.setTextColor(ContextCompat.getColor(Activity_review_itinerary.this, R.color.app_theme_color));
                                    }
                                });


                                error_layout.setVisibility(View.INVISIBLE);
                                promocode_et.setText("");
                                promocode_main_layout.setVisibility(View.INVISIBLE);
                                flag_manual_promo = false;
                            } else {

                                if (flag_manual_promo) {
                                    error_tv.setText(response.getString("message"));
                                    error_layout.setVisibility(View.VISIBLE);
                                } else {
                                    Global_Travel.showAlertDialog(Activity_review_itinerary.this, "Alert", "This code is Already used", "Ok");
                                }
                            }
                        } else {
                           // Toast.makeText(Activity_review_itinerary.this, "Something is wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dismissProgress();
                Log.i("HARSH","apply_promocode Error");
                //Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("coupan_no", coupan_code);
                params.put("user_id", userid);
                params.put("isglobel", isglobel);


                return params;

            }
        };

        promocodeapplyrequest.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_review_itinerary.this);
        requestQueue.add(promocodeapplyrequest);
    }

    public void enable_promocode_server(final String promocode) {

        showProgress();
        service_calls_count++;
        StringRequest enable_promocode = new StringRequest(Request.Method.POST,
                URL_XB.ENABLE_PROMOCODE, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.i("HARSH","enable_promocode_server Success");
                dismissProgress();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("HARSH","enable_promocode_server Error");
                dismissProgress();

                //Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("promocode", promocode);

                return params;

            }
        };


        /*RequestQueue requestQueue = Volley.newRequestQueue(Activity_review_itinerary.this);*/
        enable_promocode.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_globel.add(enable_promocode);


    }

    public void hidekeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    public void setcancellation_policy_data() {
        JSONArray cancellation_obj;
        try {

            cancellation_obj = new JSONArray(cancellation_data_string);
            LinearLayout cancellation_text = (LinearLayout) findViewById(R.id.cancellation_text);
            for (int j = 0; j < cancellation_obj.length(); j++) {


                LinearLayout divider = new LinearLayout(Activity_review_itinerary.this);
                LinearLayout single_layout = new LinearLayout(Activity_review_itinerary.this);
                single_layout.setOrientation(LinearLayout.HORIZONTAL);
                single_layout.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_margin_15), getResources().getDimensionPixelSize(R.dimen.padding_margin_15), getResources().getDimensionPixelSize(R.dimen.padding_margin_15), getResources().getDimensionPixelSize(R.dimen.padding_margin_15));


                LinearLayout.LayoutParams divider_param = new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT, 1
                );

                divider.setLayoutParams(divider_param);
                divider.setBackgroundColor(Color.GRAY);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        ActionBar.LayoutParams.MATCH_PARENT, 6.0f);

                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                        0,
                        ActionBar.LayoutParams.MATCH_PARENT, 4.0f);

                TextView tv1 = new TextView(Activity_review_itinerary.this);
                TextView tv2 = new TextView(Activity_review_itinerary.this);


                tv1.setLayoutParams(param);
                tv2.setLayoutParams(param1);

                if (j == 0) {
                    tv1.setText("Between 0 Hrs. to " + cancellation_obj.getJSONObject(j).getString("condition") + " Hrs.");
                } else if (j == cancellation_obj.length() - 1) {
                    tv1.setText("Above " + cancellation_obj.getJSONObject(j).getString("condition") + "Hrs.");
                } else {
                    tv1.setText("Between " + cancellation_obj.getJSONObject(j - 1).getString("condition") + " Hrs. to " + cancellation_obj.getJSONObject(j).getString("condition") + "Hrs.");
                }

                int percentage_int = parseInt(cancellation_obj.getJSONObject(j).getString("percentage"));

                tv2.setText("" + (100 - percentage_int));

                single_layout.addView(tv1);
                single_layout.addView(tv2);
                cancellation_text.addView(divider);
                cancellation_text.addView(single_layout);
            }

        } catch (Throwable t) {
        }
    }



    public void activity_dismiss() {
       /* TextView title_tv = new TextView(this);
        title_tv.setPadding(0,getResources().getDimensionPixelSize(R.dimen.padding_margin_10),0,0);
        title_tv.setTextColor(ContextCompat.getColor(Activity_review_itinerary.this,R.color.black));
        title_tv.setTextSize(16);
        title_tv.setGravity(Gravity.CENTER);
        title_tv.setText("Alert");*/

        LayoutInflater inflater = (LayoutInflater) Activity_review_itinerary.this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.textview, null);


        TextView title_tv = (TextView) v.findViewById(R.id.alert_title);
        title_tv.setText("Alert");

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_review_itinerary.this);
        builder.setCustomTitle(title_tv)
                .setMessage("You will not be able to book this ticket till next 10 minutes.")
                .setCancelable(false)

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        countDownTimer.cancel();

                        Activity_review_itinerary.this.finish();
                    }
                });


        AlertDialog alert = builder.create();
        alert.show();

        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setTextColor(ContextCompat.getColor(this, R.color.app_theme_color));
        Button c = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        c.setTextColor(ContextCompat.getColor(this, R.color.app_theme_color));

    }

    @Override
    public void onBackPressed() {
        LinearLayout cl = (LinearLayout) findViewById(R.id.cancellation_layout);
        LinearLayout pc = (LinearLayout) findViewById(R.id.itinerary_promocode_main_layout);

        if (cl.getVisibility() == View.VISIBLE || pc.getVisibility() == View.VISIBLE) {
            cl.setVisibility(View.GONE);
            pc.setVisibility(View.GONE);
        } else {
           /* TextView title_tv = new TextView(this);
            title_tv.setPadding(0,getResources().getDimensionPixelSize(R.dimen.padding_margin_10),0,0);
            title_tv.setTextColor(ContextCompat.getColor(Activity_review_itinerary.this,R.color.black));
            title_tv.setTextSize(16);
            title_tv.setGravity(Gravity.CENTER);
            title_tv.setText("Alert");
*/
            LayoutInflater inflater = (LayoutInflater) Activity_review_itinerary.this.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            View v = inflater.inflate(R.layout.textview, null);


            TextView title_tv = (TextView) v.findViewById(R.id.alert_title);
            title_tv.setText("Alert");

            AlertDialog.Builder builder = new AlertDialog.Builder(Activity_review_itinerary.this);
            builder.setCustomTitle(title_tv)
                    .setMessage("You will not be able to book this ticket till next 10 minutes.")
                    .setCancelable(false)

                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            countDownTimer.cancel();

                            Activity_review_itinerary.this.finish();
                        }
                    });


            AlertDialog alert = builder.create();
            alert.show();

            Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            b.setTextColor(ContextCompat.getColor(this, R.color.app_theme_color));
            Button c = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            c.setTextColor(ContextCompat.getColor(this, R.color.app_theme_color));
        }


    }


    public String getreportingtime(String time) {
        String reporting_time_local = "";
        int hour = parseInt(time.substring(0, 2));
        int min = parseInt(time.substring(3, 5));

        int total_min = (hour * 60) + min;

        total_min = total_min - 15;
        hour = total_min / 60;
        min = total_min % 60;
        if (hour > 9 && min > 9) {
            reporting_time_local = hour + ":" + min + " " + time.substring(6, time.length());
        } else if (hour < 10 && min < 10) {
            reporting_time_local = "0" + hour + ":0" + min + " " + time.substring(6, time.length());
        } else if (hour > 9 && min < 10) {
            reporting_time_local = hour + ":0" + min + " " + time.substring(6, time.length());
        } else {
            reporting_time_local = "0" + hour + ":" + min + " " + time.substring(6, time.length());
        }
        return reporting_time_local;
    }


    public void setoffers() {
        StringRequest offerrequest = new StringRequest(Request.Method.POST,
                URL_XB.GET_OFFER_GLOBAL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.i("HARSH","setoffers Success " + s);

                JSONObject response = null;
                try {
                    response = new JSONObject(s);

                    dismissProgress();

                    if (response != null) {
                        if (response.get("success").toString().equals("1")) {
                            LinearLayout offer_main_layout = (LinearLayout) findViewById(R.id.activity_itinerary_offer_main_layout);
                            offer_main_layout.removeAllViews();
                            JSONArray offers = response.getJSONArray("data");
                            if (Global_Travel.build_type == 0) {
                                Log.e("vikas", offers.toString());
                            }

                            if (offers.length() != 0) {
                                for (int i = 0; i < offers.length(); i++) {
                                    LayoutInflater inflater = (LayoutInflater) Activity_review_itinerary.this.getSystemService
                                            (Context.LAYOUT_INFLATER_SERVICE);

                                    View view = inflater.inflate(R.layout.offer_row, null);
                                    LinearLayout check_btn = (LinearLayout) view.findViewById(R.id.activity_itinerary_offer_check_btn);
                                    TextView tag_tv = (TextView) view.findViewById(R.id.activity_itinerary_offer_tag_tv);
                                    TextView detail_tv = (TextView) view.findViewById(R.id.activity_itinerary_offer_detail_tv);

                                    check_btn.setTag(R.string.promocode_id, offers.getJSONObject(i).getString("id"));
                                    check_btn.setTag(R.string.max_uses_count, offers.getJSONObject(i).getString("max_uses_count"));
                                    check_btn.setTag(R.string.discount_amount, offers.getJSONObject(i).getString("discount_amount"));
                                    check_btn.setTag(R.string.discount_percentage, offers.getJSONObject(i).getString("discount_percentage"));
                                    check_btn.setTag(R.string.coupan_code, offers.getJSONObject(i).getString("coupan_code"));

                                    tag_tv.setText(offers.getJSONObject(i).getString("coupan_code"));
                                    detail_tv.setText(offers.getJSONObject(i).getString("detail"));

                                    check_btn.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View view) {
                                            String max_uses = view.getTag(R.string.max_uses_count).toString();
                                            LinearLayout check_btn_middle = (LinearLayout) findViewById(R.id.activity_itinerary_offer_check_btn_middle);
                                            if (isNetworkConnected(false)) {
                                                if (session_manager.isLoggedIn()) {
                                                    apply_promocode(view.getTag(R.string.coupan_code).toString(), session_manager.getid(), "1");
                                                } else {
                                                    login_alert_layout.setVisibility(View.VISIBLE);
                                                }


                                            } else {

                                                Global_Travel.showAlertDialog(Activity_review_itinerary.this, getResources().getString(R.string.internet_connection_error_title), getResources().getString(R.string.internet_connection_error_message), "Ok");

                                            }
                                        }
                                    });

                                    offer_main_layout.addView(view);
                                }
                            } else {
                                LinearLayout.LayoutParams lp_tv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                lp_tv.setMargins(0, 10, 0, 10);
                                TextView tv = new TextView(Activity_review_itinerary.this);
                                tv.setText("No Offers");
                                tv.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_margin_5), 0, 0, getResources().getDimensionPixelSize(R.dimen.padding_margin_10));
                                tv.setTextColor(ContextCompat.getColor(Activity_review_itinerary.this, R.color.black));
                                offer_main_layout.addView(tv);
                                offer_main_layout.setGravity(Gravity.CENTER);
                            }
                        } else {
                           // Toast.makeText(Activity_review_itinerary.this, "Something is wrong", Toast.LENGTH_LONG).show();
                        }
                    } else {
                       // Toast.makeText(Activity_review_itinerary.this, "Something is wrong", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("HARSH","setoffers Error ");
                dismissProgress();

               // Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                return params;

            }
        };


       /* RequestQueue requestQueue = Volley.newRequestQueue(this);*/
        requestQueue_globel.add(offerrequest);
    }


    public void sendmessage(final String order_no, final String ticket_no, final String pnr_no, final String from_city_name, final String to_city_name,
                            final String journey_date, final String reporting_time, final String departure_time, final String status, final String passanger_detail, final String boarding_address,
                            final String bus_type, final String company_name, final String boarding_contact_no, final String totalFare, final String cancellation_data) {
        if (Global_Travel.build_type == 0) {
            Log.e("vikas", "call volley");
        }
        StringRequest send_message = new StringRequest(Request.Method.POST,
                URL_XB.SEND_MESSAGE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.i("HARSH","sendmessage Success ");

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("HARSH","sendmessage Error ");

               // Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no", order_no);
                params.put("pnr_no", pnr_no);
                params.put("from_city", from_city_name);
                params.put("to_city", to_city_name);
                params.put("date_of_journey", journey_date);
                params.put("reporing_time", reporting_time);
                params.put("departure_time", departure_time);
                params.put("status", status);
                params.put("boarding_address", boarding_address);
                params.put("bus_type", bus_type);
                params.put("company_name", company_name);
                params.put("contact_no", boarding_contact_no);
                params.put("total_fare", totalFare);
                params.put("to_mail", contact_email.toString().trim());
                params.put("contact_name", contact_name);
                params.put("ticket_no", ticket_no);
                try {
                    JSONArray ps = new JSONArray(passanger_detail);
                    JSONArray cd = new JSONArray(cancellation_data);

                    for (int i = 0; i < ps.length(); i++) {
                        params.put("p_name" + i, ps.getJSONObject(i).getString("Name"));
                        params.put("p_seat" + i, ps.getJSONObject(i).getString("SeatNo"));
                        params.put("p_gender" + i, ps.getJSONObject(i).getString("Gender"));

                    }
                    params.put("p_length", "" + ps.length());

                    for (int j = 0; j < cd.length(); j++) {
                        params.put("cd_condition" + j, cd.getJSONObject(j).getString("condition"));
                        params.put("cd_percentage" + j, cd.getJSONObject(j).getString("percentage"));
                    }
                    params.put("cd_length", "" + cd.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return params;

            }
        };

        //Adding the string request to the queue
        /*RequestQueue requestQueue = Volley.newRequestQueue(this);*/
        send_message.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_globel.add(send_message);
    }

    public void navigateToBookingDetails(String pnr_no,String ticket_no){

        progress.dismiss();

        Intent i = new Intent(Activity_review_itinerary.this, Activity_ticket_confirmation.class);
        i.putExtra("pnr_no", pnr_no);
        i.putExtra("ticket_no", ticket_no);
        i.putExtra("passanger", ps.toString());
        i.putExtra("from_city", TRAVEL_DATA.FROM_CITY_NAME);
        i.putExtra("to_city", TRAVEL_DATA.TO_CITY_NAME);
        i.putExtra("booking_date", TRAVEL_DATA.JOURNEY_DATE);
        i.putExtra("boarding_time", BoardingTime);
        i.putExtra("boarding_point_address", Boarding_point_address);
        i.putExtra("boarding_point_name", BoardingPoint);
        i.putExtra("company_name", schedule_details.get(0).getCompanyName());
        i.putExtra("bus_label", schedule_details.get(0).getBusLabel());
        i.putExtra("passenger_name", contact_name);
        i.putExtra("boarding_point_landmark", Boarding_point_landmark);
        i.putExtra("boarding_point_mobile", Boarding_point_phone);

        i.putExtra("total_fare", TotalFare );//total_fare_book_responce

        startActivity(i);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
        Activity_review_itinerary.this.finish();

    }

    public void sendmail(final String order_no, final String ticket_no, final String pnr_no, final String from_city_name, final String to_city_name,
                         final String journey_date, final String reporting_time, final String departure_time, final String status, final String passanger_detail, final String boarding_address,
                         final String bus_type, final String company_name, final String boarding_contact_no, final String totalFare, final String cancellation_data) {
        if (Global_Travel.build_type == 0) {
            Log.e("vikas", "call volley");
        }

        StringRequest send_mail = new StringRequest(Request.Method.POST,
                URL_XB.SEND_MAIL_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.i("HARSH","sendmail Success ");



                navigateToBookingDetails(pnr_no,ticket_no);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("HARSH","sendmail Error ");

                navigateToBookingDetails(pnr_no,ticket_no);

               // Toast.makeText(getApplicationContext(), "Something is wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("order_no", order_no);
                params.put("ticket_no", ticket_no);
                params.put("pnr_no", pnr_no);
                params.put("from_city", from_city_name);
                params.put("to_city", to_city_name);
                params.put("date_of_journey", journey_date);
                params.put("reporing_time", reporting_time);
                params.put("departure_time", departure_time);
                params.put("status", status);
                params.put("boarding_address", boarding_address);
                params.put("bus_type", bus_type);
                params.put("company_name", company_name);
                params.put("contact_no", boarding_contact_no);
                params.put("total_fare", totalFare);
                params.put("to_mail", contact_email.toString().trim());
                params.put("contact_name", contact_name);
                try {
                    JSONArray ps = new JSONArray(passanger_detail);
                    JSONArray cd = new JSONArray(cancellation_data);

                    for (int i = 0; i < ps.length(); i++) {
                        params.put("p_name" + i, ps.getJSONObject(i).getString("Name"));
                        params.put("p_seat" + i, ps.getJSONObject(i).getString("SeatNo"));
                        params.put("p_gender" + i, ps.getJSONObject(i).getString("Gender"));
                    }
                    params.put("p_length", "" + ps.length());

                    for (int j = 0; j < cd.length(); j++) {
                        params.put("cd_condition" + j, cd.getJSONObject(j).getString("condition"));
                        params.put("cd_percentage" + j, cd.getJSONObject(j).getString("percentage"));
                    }
                    params.put("cd_length", "" + cd.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return params;

            }
        };

        //Adding the string request to the queue
        /*RequestQueue requestQueue = Volley.newRequestQueue(this);*/
        send_mail.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_globel.add(send_mail);
    }



    public String change_date_form(String date) {
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        String day = date.substring(8, 10);
        String month = date.substring(5, 7);
        String year = date.substring(0, 4);

        int day_int = parseInt(day);
        int month_int = parseInt(month);
        int year_int = parseInt(year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year_int, month_int - 1, day_int);

        int day_of_weak_int = calendar.get(calendar.DAY_OF_WEEK);
        String day_of_weak = days[day_of_weak_int - 1];


        //String final_date = day+"-"+months[month_int]+"-"+year+","+day_of_weak;

        String final_date = day_of_weak + ", " + day + " " + months[month_int - 1] + " " + year.substring(2, year.length());


        return final_date;
    }


    private void bookSeat() {

        showProgress();
        service_calls_count++;

        try {

            HashMap<String, String> paramsMap = new HashMap<String, String>(); // No Params required to fetch cities

            paramsMap.put("HoldId",HoldKey);

            send_start_booking_status();

            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Method.POST,
                    Constants.URL_TY.BOOK_SEATS,
                    paramsMap, new Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Gopal", "json Response recieved !!" + response);

                    dismissProgress();

                    try {

                        if (response.getBoolean(JSON_KEYS.SUCCESS)) {

                            JSONObject data = response.getJSONObject(Constants.JSON_KEYS.DATA);

                            String order_id = data.optString("HoldId");

                            String pnr_no =  data.optString("PNRNo");
                            String ticket_no = data.optString("TicketNo");
                            //total_fare_book_responce = data.optString("TotalFare");
                            String status = "BOOKED";



                            String repoting_time = getreportingtime(BoardingTime);

                    ps = new JSONArray();
                    for (int i = 0; i < Selected_seat_list.size(); i++) {

                        JSONObject p = new JSONObject();
                        Realm_Selected_Seats selectedSeat = Selected_seat_list.get(i);

                        String gender = "Male";

                        if(selectedSeat.getSelectedGender().equals("F") ){
                            gender = "Female";
                        }

                        String seatType = "Seating";

                        switch (selectedSeat.getSeat_Type()){

                            case SEAT_DETAILS.VALUE_SEAT_TYPE_SEATING:
                                 seatType = "Seating";
                                break;

                            case SEAT_DETAILS.VALUE_SEAT_TYPE_SEMI_SLEEPER:
                                seatType = "SemiSleeper";
                                break;

                            case SEAT_DETAILS.VALUE_SEAT_TYPE_SLEEPER:
                                seatType = "Sleeper";
                                break;
                        }


                        p.put("Name", selectedSeat.getName());
                        p.put("Age", selectedSeat.getAge());
                        p.put("Gender",gender);
                        p.put("SeatNo", selectedSeat.getSeatNo());
                        p.put("SeatType",seatType);
                        p.put("IsAcSeat", TRAVEL_DATA.IS_AC_STR);
                        ps.put(p);

                    }


                    send_success_booking_status(order_id,pnr_no,ticket_no,order_id,repoting_time,status);





                        }else{

                            String msg = "";

                            if(response.has(Constants.JSON_KEYS.ERROR)){

                                JSONObject objJsonError = response.getJSONObject(Constants.JSON_KEYS.ERROR);

                                if(objJsonError.has("Msg")){

                                    msg =  "\n"+getResources().getString(R.string.error_message_detail_text)+" " + objJsonError.optString("Msg");
                                }
                            }

                            callAlertBox(getResources().getString(R.string.server_error_title),getResources().getString(R.string.server_error_message_try_again) + msg,getResources().getString(R.string.alert_cancel_btn_text_ok));
                        }

                    } catch (JSONException e) {

                        callAlertBox(getResources().getString(R.string.server_error_title),getResources().getString(R.string.server_error_message_try_again),getResources().getString(R.string.alert_cancel_btn_text_ok));
                        e.printStackTrace();

                    }
                }


            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");

                    dismissProgress();
                    callAlertBox(getResources().getString(R.string.server_error_title),getResources().getString(R.string.server_error_message_try_again),getResources().getString(R.string.alert_cancel_btn_text_ok));
                }
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();

                    headers.put("access-token", TRAVEL_DATA.TOKEN_ID);

                    return headers;
                }

            };


            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception ex) {

            ex.printStackTrace();

        }


    }

}


//
//private class BookSeat extends AsyncTask<Void, Void, Void> {
//
//
//    @Override
//    protected void onPostExecute(Void result) {
//        super.onPostExecute(result);
//
//        if (soapresult_detail != null) {
//            String Is_success = ((SoapObject) soapresult_detail.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString();
//            if (Is_success.equals("true")) {
//                //Toast.makeText(Activity_review_itinerary.this,soapresult_detail.toString(),Toast.LENGTH_LONG).show();
//                try {
//                    if(Global_Travel.build_type ==0)
//                    {
//                        Log.e("vikas", soapresult_detail.toString());
//                    }
//
//
//                    String order_id = soapresult_detail.getPrimitivePropertyAsString("TransactionId");
//                    String pnr_no = soapresult_detail.getPrimitivePropertyAsString("PNRNo");
//                    String ticket_no = soapresult_detail.getPrimitivePropertyAsString("TicketNo");
//                    String status = "BOOKED";
//
//
//
//                    String repoting_time = getreportingtime(BoardingTime);
//
//                    int passanger_count = ((SoapObject) soapresult_detail.getProperty("Passengers")).getPropertyCount();
//                    ps = new JSONArray();
//                    for (int i = 0; i < passanger_count; i++) {
//                        JSONObject p = new JSONObject();
//
//                        p.put("Name", ((SoapObject) ((SoapObject) soapresult_detail.getProperty("Passengers")).getProperty(i)).getPrimitiveProperty("Name").toString());
//                        p.put("Age", ((SoapObject) ((SoapObject) soapresult_detail.getProperty("Passengers")).getProperty(i)).getPrimitiveProperty("Age").toString());
//                        p.put("Gender", ((SoapObject) ((SoapObject) soapresult_detail.getProperty("Passengers")).getProperty(i)).getPrimitiveProperty("Gender").toString());
//                        p.put("SeatNo", ((SoapObject) ((SoapObject) soapresult_detail.getProperty("Passengers")).getProperty(i)).getPrimitiveProperty("SeatNo").toString());
//                        p.put("SeatType", ((SoapObject) ((SoapObject) soapresult_detail.getProperty("Passengers")).getProperty(i)).getPrimitiveProperty("SeatType").toString());
//                        p.put("IsAcSeat", TRAVEL_DATA.IS_AC_STR);
//                        ps.put(p);
//
//                    }
//
//
//                    send_success_booking_status(order_id,pnr_no,ticket_no,order_id,repoting_time,status);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//            else
//            {
//                Global_Travel.showAlertDialog(Activity_review_itinerary.this,getResources().getString(R.string.validating_error_title),((SoapObject)soapresult_detail.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");
//            }
//
//        } else {
//            Global_Travel.showAlertDialog(Activity_review_itinerary.this,getResources().getString(R.string.validating_error_title),getResources().getString(R.string.slow_internet_error),"Ok");
//        }
//
//
//
//
//
//
//
//
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//        progress = new ProgressDialog(Activity_review_itinerary.this);
//        progress.setMessage("Please wait...");
//        progress.setCanceledOnTouchOutside(false);
//        progress.setCancelable(false);
//
//        progress.show();
//
//        send_start_booking_status();
//    }
//
//    @Override
//    protected Void doInBackground(Void... arg0) {
////            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE, Constants.METHOD_BookSeats);
////
////
////            SoapObject sa = new SoapObject(null,"Authentication");
////
////            PropertyInfo userid = new PropertyInfo();
////            userid.setName("UserID");
////
////            userid.setValue(LoginCridantial.UserId.trim());
////            userid.setType(Integer.class);
////            sa.addProperty(userid);
////
////            PropertyInfo usertype = new PropertyInfo();
////            usertype.setName("UserType");
////            usertype.setValue(LoginCridantial.UserType.trim());
////
////
////            usertype.setType(String.class);
////            sa.addProperty(usertype);
////
////            PropertyInfo userkey = new PropertyInfo();
////            userkey.setName("Key");
////            userkey.setValue(LoginCridantial.UserKey.trim());
////
////            userkey.setType(String.class);
////            sa.addProperty(userkey);
////            request.addSoapObject(sa);
////
////            PropertyInfo holdkey = new PropertyInfo();
////            holdkey.setName("HoldKey");
////            holdkey.setValue(HoldKey);
////            userkey.setType(Integer.class);
////            request.addProperty(holdkey);
////            if(Global_Travel.build_type == 0)
////            {
////                Log.e("vikas request print",request.toString());
////            }
////
////
////
////
////
////
////            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
////            envelope.implicitTypes = true;
////            envelope.dotNet = true;
////            envelope.setOutputSoapObject(request);
////
////            HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);
////            httpTransport.debug =true;
////
////
////            try {
////                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_BookSeats, envelope);
////            } catch (HttpResponseException e) {
////                e.printStackTrace();
////            } catch (IOException e) {
////                e.printStackTrace();
////            } catch (XmlPullParserException e) {
////                e.printStackTrace();
////            }
////            soapresult_detail = null;
////
////            try {
////                soapresult_detail  = (SoapObject)envelope.getResponse();
////
////            } catch (SoapFault e) {
////
////                e.printStackTrace();
////            }
//
//
//
//        return null;
//    }
//}


//    public String getTime(String time)
//    {
//        String final_time="";
//        time = time.substring(11,16);
//        String post="";
//        int hour = parseInt(time.substring(0,2));
//        String min = time.substring(2,time.length());
//
//        if(hour > 12)
//        {
//            hour = hour%12;
//            post = "PM";
//        }
//        else if(hour == 0)
//        {
//            hour = 12;
//            post = "AM";
//        }
//        else if(hour == 12)
//        {
//            post = "PM";
//        }
//        else
//        {
//            post = "AM";
//        }
//
//        if(hour >9)
//        {
//            final_time = hour+min+" "+post;
//        }
//        else
//        {
//            final_time = "0"+hour+min+" "+post;
//        }
//
//        return final_time;
//
//    }


