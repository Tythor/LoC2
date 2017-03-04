package com.tythor.loc2.titleScreenUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Tyler on 2/20/2017.
 */

public class BackTextView extends TextView {
    public BackTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/munro.ttf");
        setTypeface(typeface);
        setTextColor(Color.WHITE);
    }
}
