package com.tythor.loc2.titleScreenUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tythor.loc2.GameActivity;

/**
 * Created by Tyler on 2/23/2017.
 */

public class NamesTextView extends TextView {
    public NamesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/munro.ttf");
        setTypeface(typeface);
        setTextColor(Color.WHITE);
        setX(GameActivity.screenWidth / 2);
    }
}
