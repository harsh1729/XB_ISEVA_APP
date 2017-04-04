package com.iseva.app.source.customcomponent;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by xb_sushil on 2/17/2017.
 */

public class EditTextBold extends EditText {
    public EditTextBold(Context context) {
        super(context);
        setfont();
    }

    public EditTextBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setfont();
    }

    public EditTextBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setfont();
    }

    public void setfont()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto.bold.ttf.ttf");
        setTypeface(tf);
    }
}
