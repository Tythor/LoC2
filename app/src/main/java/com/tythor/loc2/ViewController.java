package com.tythor.loc2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Tyler on 2/7/2017.
 */

public class ViewController extends SurfaceView implements Runnable {
    // Drawing objects
    public static Paint paint;
    public static Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private static Context context;

    // Thread objects
    private volatile boolean running;
    private Thread gameThread = null;

    // For FPS calculation
    private long frameStartTime;
    public static int FPS = 60;

    private static int screenWidth;
    private static int screenHeight;

    public static ViewObject currentView;

    public ViewController() {
        // Pass context to SurfaceView
        super(GameActivity.context);
        context = GameActivity.context;
        this.screenWidth = GameActivity.screenWidth;
        this.screenHeight = GameActivity.screenHeight;

        // Initialize the drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        currentView = new TitleView(context, paint, canvas, screenWidth, screenHeight);
    }

    public static void changeToTitleView() {
        currentView = new TitleView(context, paint, canvas, screenWidth, screenHeight);
    }

    public static void changeToGameView(String levelName) {
        currentView = new GameView(context, paint, canvas, screenWidth, screenHeight, levelName);
    }

    @Override
    public void run() {
        while(running) {
            frameStartTime = System.currentTimeMillis();

            update();
            draw();

            // Calculate time taken with update() and draw()
            long differenceBetweenFrames = System.currentTimeMillis() - frameStartTime;
            if(differenceBetweenFrames >= 1) {
                FPS = (int) (1000 / differenceBetweenFrames);
            }
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Restart thread on resume()
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void update() {
        currentView.update();
    }

    private void draw() {
        if(surfaceHolder.getSurface().isValid()) {
            // Lock the canvas
            canvas = surfaceHolder.lockCanvas();

            // Destroy last frame
            destroyFrame(Color.argb(255, 0, 0, 0));
            //canvas.save();
            //canvas.scale(1.5f, 1.5f);

            currentView.setObjects(context, paint, canvas, screenWidth, screenHeight);
            currentView.draw();
            //canvas.restore();

            // Unlock and update canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void destroyFrame(int color) {
        paint.setColor(color);
        canvas.drawColor(color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        currentView.onTouchEvent(motionEvent);
        return true;
    }
}
