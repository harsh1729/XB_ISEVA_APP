package com.iseva.app.source.customcomponent;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by xb_sushil on 2/17/2017.
 */

public class EditTextRegular extends EditText {
    public EditTextRegular(Context context) {
        super(context);
        setfont();
    }

    public EditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setfont();
    }

    public EditTextRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setfont();
    }

    public void setfont()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        setTypeface(tf);
    }
}
