package com.tythor.loc2;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import com.tythor.loc2.titleScreenUtils.ChaiButton;
import com.tythor.loc2.titleScreenUtils.TitleScreenDialogFragment;

public class GameActivity extends Activity {
    private ViewController viewController;
    public static RelativeLayout parentLayout;

    public static Context context;

    public static int screenWidth;
    public static int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        // Access screen information
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        System.err.println(screenWidth + " x " + screenHeight);

        // Initialize the gameView with the screen information
        //gameView = new GameView(this, screenWidth, screenHeight);
        //setContentView(gameView);

        viewController = new ViewController();
        parentLayout = new RelativeLayout(this);
        parentLayout.addView(viewController);
        //parentLayout.addView(scrollView);
        setContentView(parentLayout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewController.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewController.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Destroy");
    }

    // Fullscreen
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            viewController.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                   | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                   | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                   | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                   | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                   | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    // The back button in title screen views
    public void back(View view) {
        GameActivity.parentLayout.removeView(TitleView.creditsView);
        GameActivity.parentLayout.removeView(TitleView.optionsView);
        TitleView.overlayOn = false;
    }

    // Clicking on the chai in CreditsView
    public void playTitleLevel(View view) {
        DialogFragment titleScreenDialogFragment = new TitleScreenDialogFragment();
        titleScreenDialogFragment.show(getFragmentManager(), "m");
    }
}
