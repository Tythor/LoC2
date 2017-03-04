package com.tythor.loc2.titleScreenUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Tyler on 2/19/2017.
 */

public class MunroSmallTextView extends TextView {
    public MunroSmallTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/munro_small.ttf");
        setTypeface(typeface);
        setTextColor(Color.WHITE);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
    }
}
