package com.iseva.app.source;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.inappbilling.util.IabHelper;
import com.app.inappbilling.util.IabResult;
import com.app.inappbilling.util.Inventory;
import com.app.inappbilling.util.Purchase;


public class Activity_Google_Payment extends Activity {
    //private Button clickButton;
    private Button buyButton;
    private static final String TAG = "SUSHIL";
    IabHelper mHelper;
    static final String ITEM_SKU = "android.test.purchased";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_payment);
        inti();
    }


    private void inti() {
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Payment info");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Google_Payment.this.finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        buyButton = (Button) findViewById(R.id.buttonBuy);
       // clickButton = (Button) findViewById(R.id.buttonClick);
        //clickButton.setEnabled(false);

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApjQ9RKECV00aioF/7OqWhsDj6zL55rvNw/v4fwfuwmSjU5uE20UyuHtHW5Kwz+qOU+giJqGNDp1jsahwil8ntpadPOTazJI/i4UuGbGTKCMRXHhvg9vQW+xnubLY13hXlUdzIEEefztpgTXA3tzfWSjToMsFkIEjHYFBD1xS5frroAd9rnJbSA0EGoGa+amSQOlnQrYAyI+qXGAJ8lVB3KUXZvr0dbjMYDO/qK5k1X5f2nDDX6Gi9QbId1J2fGP82R5ydzkpAKG0ZLA7KpAJW5P5OaaG6U3XRN90tkNqg+oIz7pmVi1NIURTTKy7zsN9hKfG0XCD2XZuwJj3Fu8ODQIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.i(TAG, "In-app Billing setup failed: " + result);
                } else {
                    Log.i(TAG, "In-app Billing is set up OK");
                }
            }
        });
    }

    public void buttonClicked(View view) {
        //clickButton.setEnabled(false);
        buyButton.setEnabled(true);
    }

    public void buyClick(View view) {
        mHelper.flagEndAsync();
       /* mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");*/
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                return;
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
                buyButton.setEnabled(false);
            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {

            if (result.isSuccess()) {
               // clickButton.setEnabled(true);
            } else {
                // handle error
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null)
            mHelper.dispose();
        mHelper = null;
    }


}
