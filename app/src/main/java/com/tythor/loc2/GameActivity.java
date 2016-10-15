package com.tythor.loc2;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

public class GameActivity extends Activity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Access screen information
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        System.err.println(screenWidth + " x " + screenHeight);

        // Initialize the gameView with the screen information
        gameView = new GameView(this, screenWidth, screenHeight);

        // Set gameView as the View
        setContentView(gameView);
    }

    // Let gameView know when the thread is paused
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    // Let gameView know when the thread is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    // Fullscreen
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus) {
            gameView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                   | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                   | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                   | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                                   | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                   | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
