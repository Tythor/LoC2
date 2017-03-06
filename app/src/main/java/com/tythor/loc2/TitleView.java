package com.tythor.loc2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Tyler on 2/4/2017.
 */

public class TitleView extends ViewObject {
    boolean drawn = false;

    private Bitmap menuFrame;
    private Bitmap title;
    public static Bitmap onChai;
    public static Bitmap offChai;

    public static FrameLayout creditsView;
    public static RelativeLayout optionsView;

    private RectF optionsArea;
    private RectF startArea;
    private RectF creditsArea;
    private RectF backArea;


    public static boolean overlayOn = false;

    public TitleView(Context context, Paint paint, Canvas canvas, int screenWidth, int screenHeight) {
        this.context = context;
        this.paint = paint;
        this.canvas = canvas;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        prepareBounds();
    }

    private void prepareBounds() {
        menuFrame = prepareBitmap("menuframe1");
        title = prepareBitmap("title");
        onChai = prepareBitmap("onchai");
        offChai = prepareBitmap("offchai");

        float pixelsPerMeter = (float) screenWidth / 568;

        float buttonWidth = pixelsPerMeter * 55;
        float buttonHeight = pixelsPerMeter * 25;

        optionsArea = new RectF(buttonWidth,
                                screenHeight - buttonHeight - menuFrame.getHeight(),
                                buttonWidth + menuFrame.getWidth(),
                                screenHeight - buttonHeight);

        startArea = new RectF(buttonWidth + (screenWidth - buttonWidth - menuFrame.getWidth() - buttonWidth) / 2,
                              screenHeight - buttonHeight - menuFrame.getHeight(),
                              buttonWidth + (screenWidth - buttonWidth - menuFrame.getWidth() - buttonWidth) / 2 + menuFrame.getWidth(),
                              screenHeight - buttonHeight);

        creditsArea = new RectF(screenWidth - buttonWidth - menuFrame.getWidth(),
                                screenHeight - buttonHeight - menuFrame.getHeight(),
                                screenWidth - buttonWidth,
                                screenHeight - buttonHeight);


        Rect bounds = new Rect();
        paint.setTextSize(pixelsPerMeter * 20);
        String back = "Back";
        paint.getTextBounds(back, 0, back.length(), bounds);

        backArea = new RectF(screenWidth - bounds.width() * 2, screenHeight - bounds.height() * bounds.height(), screenWidth, screenHeight);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw() {
        float pixelsPerMeter = (float) screenWidth / 568;

        float buttonWidth = pixelsPerMeter * 55;
        float buttonHeight = pixelsPerMeter * 25;

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/munro.ttf");
        paint.setTypeface(typeface);
        paint.setTextSize(pixelsPerMeter * 30);
        paint.setColor(Color.argb(255, 255, 255, 255));
        paint.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();

        if(!overlayOn) {
            // Draw title
            canvas.drawBitmap(title, (screenWidth - title.getWidth()) / 2, (int) (buttonHeight * 1.5), paint);

            // Draw 3 menuframes
            canvas.drawBitmap(menuFrame,
                              buttonWidth,
                              screenHeight - buttonHeight - menuFrame.getHeight(),
                              null);
            String options = "Options";
            paint.getTextBounds(options, 0, options.length(), bounds);
            canvas.drawText(options,
                            buttonWidth + menuFrame.getWidth() / 2,
                            screenHeight - buttonHeight - menuFrame.getHeight() / 2 + (bounds.height() - paint.descent()) / 2,
                            paint);

            canvas.drawBitmap(menuFrame,
                              buttonWidth + (screenWidth - buttonWidth - menuFrame.getWidth() - buttonWidth) / 2,
                              screenHeight - buttonHeight - menuFrame.getHeight(),
                              null);
            String start = "Start";
            paint.getTextBounds(start, 0, start.length(), bounds);
            canvas.drawText(start,
                            buttonWidth + (screenWidth - buttonWidth - menuFrame.getWidth() - buttonWidth) / 2 + menuFrame.getWidth() / 2,
                            screenHeight - buttonHeight - menuFrame.getHeight() / 2 + bounds.height() / 2,
                            paint);

            canvas.drawBitmap(menuFrame,
                              screenWidth - buttonWidth - menuFrame.getWidth(),
                              screenHeight - buttonHeight - menuFrame.getHeight(),
                              null);
            String credits = "Credits";
            paint.getTextBounds(credits, 0, credits.length(), bounds);
            canvas.drawText(credits,
                            screenWidth - buttonWidth - menuFrame.getWidth() + menuFrame.getWidth() / 2,
                            screenHeight - buttonHeight - menuFrame.getHeight() / 2 + bounds.height() / 2,
                            paint);

            drawn = true;
        }
    }


    // Label, fetch, scale, then return the bitmap
    public Bitmap prepareBitmap(String bitmapName) {
        // Label the bitmap
        int resID = context.getResources().getIdentifier(bitmapName,
                                                         "drawable",
                                                         context.getPackageName());


        // Fetch the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

        float pixelsPerMeter = (float) screenWidth / 568;
        // Round pixelsPerMeter to the first decimal place
        pixelsPerMeter = Math.round(pixelsPerMeter * 10f) / 10f;

        switch(bitmapName) {
            case "menuframe1":
                // Scale the bitmap; 2.4
                bitmap = Bitmap.createScaledBitmap(bitmap,
                                                   (int) (120 * pixelsPerMeter),
                                                   (int) (50 * pixelsPerMeter),
                                                   false);
                break;
            case "title":
                bitmap = Bitmap.createScaledBitmap(bitmap,
                                                   (int) (bitmap.getWidth() / (pixelsPerMeter / 2.25)),
                                                   (int) (bitmap.getHeight() / (pixelsPerMeter / 2.25)),
                                                   false);
                break;
            case "onchai":
                bitmap = Bitmap.createScaledBitmap(bitmap,
                                                   (int) (bitmap.getWidth() / (pixelsPerMeter * 4)),
                                                   (int) (bitmap.getHeight() / (pixelsPerMeter * 4)),
                                                   false);
                break;
            case "offchai":
                bitmap = Bitmap.createScaledBitmap(bitmap,
                                                   (int) (bitmap.getWidth() / (pixelsPerMeter * 4)),
                                                   (int) (bitmap.getHeight() / (pixelsPerMeter * 4)),
                                                   false);
                break;
        }
        return bitmap;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if(!overlayOn) {
            int pointerCount = motionEvent.getPointerCount();

            for(int i = 0; i < pointerCount; i++) {
                int x = (int) motionEvent.getX(i);
                int y = (int) motionEvent.getY(i);

                switch(motionEvent.getActionMasked() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if(optionsArea.contains(x, y)) {
                            System.out.println("OPTIONS HAS BEEN TOUCHED");
                            optionsView = (RelativeLayout) View.inflate(context,
                                                                        R.layout.options_view,
                                                                        null);

                            GameActivity.parentLayout.addView(optionsView);
                            overlayOn = true;
                        }

                        if(startArea.contains(x, y)) {
                            System.out.println("START HAS BEEN TOUCHED");

                            // Temporary level selector
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("LevelName");
                            final EditText editText = new EditText(context);
                            builder.setView(editText);
                            builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ViewController.changeToGameView(editText.getText().toString());
                                }
                            });
                            builder.setNeutralButton("Default", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ViewController.changeToGameView("Level6-10");
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }

                        if(creditsArea.contains(x, y)) {
                            System.out.println("CREDITS HAS BEEN TOUCHED");
                            creditsView = (FrameLayout) View.inflate(context,
                                                                     R.layout.credits_view,
                                                                     null);

                            GameActivity.parentLayout.addView(creditsView);
                            overlayOn = true;
                        }
                }
            }
        }
            return true;
    }
}
