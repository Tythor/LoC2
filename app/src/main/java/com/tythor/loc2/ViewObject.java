package com.tythor.loc2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * Created by Tyler on 2/7/2017.
 */

public abstract class ViewObject {
    public abstract void update();

    public abstract void draw();

    public abstract boolean onTouchEvent(MotionEvent motionEvent);
}
