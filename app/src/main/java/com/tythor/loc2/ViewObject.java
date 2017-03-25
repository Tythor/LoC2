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
    Context context;
    Paint paint;
    Canvas canvas;

    int screenWidth;
    int screenHeight;

    public void setObjects(Context context, Paint paint, Canvas canvas, int screenWidth, int screenHeight) {
        this.context = context;
        this.paint = paint;
        this.canvas = canvas;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public abstract void update();

    public abstract void draw();

    public abstract boolean onTouchEvent(MotionEvent motionEvent);

}
