package com.iseva.app.source;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;


public class Globals {
    public final static String APP_TITLE = "iSeva App";

    public static boolean isHeader = false;
    public static final int VOLLEY_TIMEOUT_MILLISECS = 10000;

    public static final String DEFAULT_APP_SERVER_PATH = "http://xercesblue.website/iservice/client_requests/";
    public static final String DEFAULT_APP_SERVER_PATH1 = "http://xercesblue.website/isevaepaper/client_requests/";
    // http://xercesblue.in/dr/client_requests/city/getAllCities
    public static final String MSG_SERVER_ERROR = "Error occured on server. Please try again";

    public static final String INTERNET_ERROR = "Please check your Internet connection";

    public static final String OPTION_SHARE = "Share";

    public static final String OPTION_CHANGE_LOCATION = "Change Location";

    public static final String OPTION_ABOUT_US = "About us";

    public static final String OPTION_RATE_US = "Rate us";
    public static final String PNAME = "com.iseva.app";
    public final static String SHARE_LINK_GENERIC = "https://play.google.com/store/apps/details?id="+PNAME;

    public final static String SHARE_APP_MSG = "Please download this awesome app 'iSeva' at  ";

    public static final String TEXT_CONNECTION_ERROR_HEADING = "Error in Connection";
    public static final String TEXT_CONNECTION_ERROR_DETAIL_TOAST ="Please check your internet connection and try again.";

    public static final int IMAGELIMIT = 6;


    /*********************************************/

    static public Point getAppButtonSize(Activity context) {

        int screenWidth = Globals.getScreenSize(context).x;

        Point size = new Point();

        size.x = 4 * screenWidth / 10;
        size.y = size.x / 3;

        return size;
    }

    public static void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }

    public static void setAppFontTextView(Context mContext, TextView txt) {
        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "Quicksand-Regular.otf");
        txt.setTypeface(custom_font);
    }

    static public String getStringFromResources(Context con, int resourceId){
        return con.getResources().getString(resourceId);
    }
    static  public String getSimnumber(Context con){
        TelephonyManager TMgr = (TelephonyManager)con.getSystemService(Context.TELEPHONY_SERVICE);
        return TMgr.getSubscriberId();
    }

    public static void setAppFontEditView(Context mContext, EditText txt) {
        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "Quicksand-Regular.otf");
        txt.setTypeface(custom_font);
    }

	/*
     * public static float dpFromPx(final Context context, final float px) {
	 * return px / context.getResources().getDisplayMetrics().density; }
	 * 
	 * public static float pxFromDp(final Context context, final float dp) {
	 * return dp * context.getResources().getDisplayMetrics().density; }
	 */

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    static public Point getScreenSize(Activity currentActivity) {
        Display display = currentActivity.getWindowManager()
                .getDefaultDisplay();
        Point size = new Point();

        if (android.os.Build.VERSION.SDK_INT >= 13) {
            display.getSize(size);
        } else {
            size.x = display.getWidth();
            size.y = display.getHeight();
        }

        return size;
    }

    static public void showAlertDialogOneButton(String title, String msg, Context context, String positiveButtonText, DialogInterface.OnClickListener listnerPositive, Boolean isCancelable) {

        Globals.showAlertDialog(title, msg, context, positiveButtonText, listnerPositive, null, null, isCancelable);
    }

    static public String getdeviceId(Context context) {
        // GET DEVICE ID
        String deviceId = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
            if (deviceId.equals("") && deviceId == null) {
                deviceId = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return deviceId;
    }

    static public String getImei(Context con){
        TelephonyManager telephonyManager = (TelephonyManager)con.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    static public int getAppFontSize(Activity context) {

        return (getScreenSize(context).x / 120 + 12);
    }

    static public int getAppFontSize_Small(Activity context) {

        return (getScreenSize(context).x / 120 + 10);
    }

    static public int getAppFontSize_Large(Activity context) {

        return (getScreenSize(context).x / 120 + 15);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 10;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    static public Bitmap scaleToWidth(Bitmap bitmap, int scaledWidth) {
        if (bitmap != null) {

            int bitmapHeight = bitmap.getHeight();
            int bitmapWidth = bitmap.getWidth();

            // scale According to WIDTH
            int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;

            try {

                bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
                        scaledHeight, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static void showAlert(String tiString, String msgString,
                                 Context context) {
        AlertDialog.Builder builder = null;
        if (Build.VERSION.SDK_INT >= 11) {
            //API level 11 and above ctor here
            builder = new AlertDialog.Builder(context,
                    AlertDialog.THEME_HOLO_LIGHT);
        } else {
            //Lower than API level 11 code here
            builder = new AlertDialog.Builder(context);
        }


        builder.setTitle(tiString);
        builder.setMessage(msgString).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static ProgressDialog showLoadingDialog(ProgressDialog mDialog,
                                                   Activity act, Boolean cancelable, String title) {

        if (mDialog == null) {
            mDialog = new ProgressDialog(act, ProgressDialog.THEME_HOLO_LIGHT);
            mDialog.setTitle(title);
            mDialog.setMessage("Please wait for a moment...");
            mDialog.setCancelable(cancelable);
            mDialog.setProgressDrawable(null);
            mDialog.show();
        } else if (!mDialog.isShowing()) {
            mDialog.show();
        }

        return mDialog;
    }

    public static void hideLoadingDialog(ProgressDialog mDialog) {

        if (mDialog != null) {
            mDialog.dismiss();
        }

    }

    public static void showShortToast(Context con, String msg) {
        Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showAlertDialog(String title, String msg,
                                       Context context, String positiveButtonText,
                                       DialogInterface.OnClickListener listnerPositive,
                                       String negativeButtonText,
                                       DialogInterface.OnClickListener listnerNegative,
                                       Boolean isCancelable) {

        AlertDialog alertDialog = null;
		 /*= new AlertDialog.Builder(context,
				AlertDialog.THEME_HOLO_LIGHT).create();*/

        if (Build.VERSION.SDK_INT >= 11) {
            //API level 11 and above ctor here
            alertDialog = new AlertDialog.Builder(context,
                    AlertDialog.THEME_HOLO_LIGHT).create();
        } else {
            //Lower than API level 11 code here
            alertDialog = new AlertDialog.Builder(context).create();
        }

        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(isCancelable);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveButtonText,
                listnerPositive);

        if (negativeButtonText != null && !negativeButtonText.equals("")) {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                    negativeButtonText, listnerNegative);
        }
        alertDialog.show();

    }

    public static void preloadImage(Context context, String url){
        try{
            Picasso.with(context)
                    .load(url)
                    .fetch();
        }catch(Exception ex){

        }
    }

    public static String getMonthName(int month){
        String format = "";
        if(month==1){
            format = "Jan";
        }else if(month==2){
            format = "Feb";
        }else if(month==3){
            format = "Mar";
        }else if(month==4){
            format = "Apr";
        }else if(month==5){
            format = "May";
        }else if(month==6){
            format = "Jun";
        }else if(month==7){
            format = "Jul";
        }else if(month==8){
            format = "Aug";
        }else if(month==9){
            format = "Sep";
        }else if(month==10){
            format = "Oct";
        }else if(month==11){
            format = "Nov";
        }else if(month==12){
            format = "Dec";
        }
        return format;
    }

    public static String getdateFormat(int day,int month,int year){
        String monthname = getMonthName(month);
        String format =  day+" " + monthname+", "+year;
        return format;
    }

    public static String getCalculatedDate(String date, String dateFormat, int days) {
        //Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        //cal.add(Calendar.DAY_OF_YEAR, days);
        long moveByMilli = days*24*60*60*1000;
        try {
            return s.format(new Date(s.parse(date).getTime() + moveByMilli));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            Log.e("SUSHIL", "Error in Parsing Date : " + e.getMessage());
        }
        return null;
    }



    public static void loadImageIntoImageView(ImageView iv, String imgURL,
                                              Context context) {

        loadImageIntoImageView(iv, imgURL, context, 0, 0, 0, 0, 0, 0);
    }

    public static void loadImageIntoImageView(ImageView iv, String imgURL,
                                              Context context, int loadingImgId, int errorImgId) {

        loadImageIntoImageView(iv, imgURL, context, loadingImgId, errorImgId,
                0, 0, 0, 0);
    }

    public static void loadImageIntoImageView(ImageView iv, String imgURL,
                                              int transformRadius, int transformMargin, Context context) {

        loadImageIntoImageView(iv, imgURL, context, 0, 0, transformRadius,
                transformMargin, 0, 0);
    }

    public static void loadImageIntoImageView(ImageView iv, String imgURL,
                                              int transformRadius, int transformMargin, Context context,
                                              int height, int width) {

        loadImageIntoImageView(iv, imgURL, context, 0, 0, transformRadius,
                transformMargin, height, width);
    }


    public static void loadImageIntoImageView(final ImageView iv, String imgURL,
                                              Context context, final int loadingImgId, int errorImgId,
                                              int transformRadius, int transformMargin, int height, int width) {

        try {
            /*int maxSize = 1024 * 1024 * 200;
            Picasso p = new Picasso.Builder(context)
                    .memoryCache(new LruCache(maxSize))
                    .build();*/
            /*Picasso p=  new Picasso.Builder(context).downloader(new OkHttpDownloader(context.getCacheDir(), 250000000)).build();
            Picasso.setSingletonInstance(p);*/
            Picasso p = Picasso.with(context);

            RequestCreator rq = null;

            if (!imgURL.trim().isEmpty()) {
                rq = p.load(imgURL);
                if (loadingImgId != 0)
                    rq.placeholder(loadingImgId);
                if (errorImgId != 0)
                    rq.error(errorImgId);
                if (transformRadius != 0) {
                    // rq.transform(new
                    // Custom_PiccasoRoundedTransformation(transformRadius,
                    // transformMargin));
                }
                if (width != 0) {
                    // Log.i("SUSHIL",
                    // "size of height width "+width+","+height);
                    // rq.resize(width, height);
                    // rq.centerCrop();
                }
            } else
                rq = p.load(errorImgId);

           /* final AtomicBoolean playAnimation = new AtomicBoolean(true);
            rq.into(iv, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    if (playAnimation.get()) {
                        //play fade
                        Animation fadeOut = new AlphaAnimation(0, 1);
                        fadeOut.setInterpolator(new AccelerateInterpolator());
                        fadeOut.setDuration(1000);
                        iv.startAnimation(fadeOut);

                        Animation fadeOutPlaceholder = new AlphaAnimation(1, 0);
                        fadeOutPlaceholder.setInterpolator(new AccelerateInterpolator());
                        fadeOutPlaceholder.setDuration(1000);
                        //(ImageView)loadingImgId.startAnimation(fadeOutPlaceholder);
                    }
                }
                @Override
                public void onError() {
                    Log.e("SUSHIL", "Image Loaded ERRROR");
                }
                //..
            });
            playAnimation.set(false);*/

            rq.into(iv, new com.squareup.picasso.Callback() {

                @Override
                public void onSuccess() {
                    //Log.i("SUSHIL", "Image Loaded");
                }

                @Override
                public void onError() {
                    Log.e("SUSHIL", "Image Loaded ERRROR");
                }

            });

        } catch (Exception e) {
            Log.e("SUSHIL", "Error in loading image with url " + imgURL);
        }

    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
	
 /*public static LatLng getCurrentLocation(Context mContext) {

	Custom_GPSTrack gps = new Custom_GPSTrack(mContext);
	// check if GPS enabled
	LatLng obj = null;
	if (gps.canGetLocation()) {
		double latitude = gps.getLatitude();
		double longitude = gps.getLongitude();
		obj = new LatLng(latitude, longitude);
	} else {
		gps.showSettingsAlert();
	}
	return obj;
}*/


    public static boolean isGoogleMapsInstalled(Context mContext) {
        try {
            ApplicationInfo info = mContext.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
