package com.iseva.app.source.travel;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

/**
 * Created by xb_sushil on 1/21/2017.
 */

public class Autocompletetext_costom extends AutoCompleteTextView {


    public Autocompletetext_costom(Context context) {
        super(context);



    }

    public Autocompletetext_costom(Context arg0, AttributeSet arg1) {

        super(arg0, arg1);

    }



    public Autocompletetext_costom(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

            performFiltering(getText(), 0);


    }


    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isPopupShowing()) {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if(inputManager.hideSoftInputFromWindow(findFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS)){
                return true;
            }
            else
            {
                return false;
            }
        }

        return super.onKeyPreIme(keyCode, event);
    }




    @Override
    public void setOnDismissListener(OnDismissListener dismissListener) {
        super.setOnDismissListener(dismissListener);

    }





}
