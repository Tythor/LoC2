package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    Context context;

    // For FPS calculation
    long frameStartTime;
    long fps;

    boolean follow = false;
    private boolean debugging = true;
    private volatile boolean running;
    private Thread gameThread = null;
    // Drawing objects
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    // Custom objects
    private LevelManager levelManager;
    private Viewport viewport;
    InputController inputController;
    SoundManager soundManager;
    Animation animation;

    GameView(Context context, int screenWidth, int screenHeight) {
        // Pass context to SurfaceView
        super(context);

        this.context = context;

        // Initialize the drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        viewport = new Viewport(screenWidth, screenHeight);

        soundManager = new SoundManager();
        soundManager.loadSound(context);

        loadLevel("LevelOne", new WorldLocation(1, 17));
    }

    public void loadLevel(String level, WorldLocation playerLocation) {
        // Unnecessary?
        levelManager = null;

        levelManager = new LevelManager(context,
                                        viewport.getPixelsPerMeter(),
                                        viewport.getScreenWidth(),
                                        inputController,
                                        level,
                                        playerLocation);

        inputController = new InputController(context, viewport.getPixelsPerMeter(), viewport.getScreenWidth(),
                                              viewport.getScreenHeight());

        animation = new Animation(context, levelManager, viewport);

        // Set the viewport's location as the player's location
        viewport.setViewportLocation(new WorldLocation(1, 17));
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
                fps = 1000 / differenceBetweenFrames;
            }
        }
    }

    private void update() {
        // If gameObject is on-screen, then render it
        for(int i = 0; i < levelManager.gameObjects.size(); i++) {
            GameObject gameObject = levelManager.gameObjects.get(i);
            if(gameObject.isActive()) {
                if(viewport.renderObject(gameObject.getWorldLocation())) {
                    gameObject.setVisible(true);
                }
                else {
                    gameObject.setVisible(false);
                }
            }
        }

        final int distanceLimit = 3;


        /*if(levelManager.player.getWorldLocation().x < viewport.getViewportLocation().x && follow) {
            follow = false;
        }*/

        /*if(levelManager.player.getWorldLocation().x > viewport.getViewportLocation().x + distanceLimit) {
            follow = true;
            //viewport.setViewportLocation(new WorldLocation(levelManager.player.getWorldLocation().x, viewport.getViewportLocation().y));
        }

        if(levelManager.player.getWorldLocation().x < viewport.getViewportLocation().x - distanceLimit) {
            follow = true;
        }*/

        //if(follow) {
        //if(!levelManager.player.jumping) {
        viewport.setViewportLocation(new WorldLocation(levelManager.player.getWorldLocation().x,
                                                       levelManager.player.getWorldLocation().y + levelManager.player.getWorldLocation().height));
        //}
        //}

        /*if(levelManager.player.getWorldLocation().y > viewport.getViewportLocation().y + distanceLimit || levelManager.player.getWorldLocation().y < viewport.getViewportLocation().y - distanceLimit) {
            viewport.setViewportLocation(new WorldLocation(viewport.getViewportLocation().x,
                                                           levelManager.player.getWorldLocation().y));
        }*/
        /*viewport.setViewportLocation(new WorldLocation(levelManager.player.getWorldLocation().x,
                                                       viewport.getViewportLocation().y));*/
    }

    private void draw() {
        if(surfaceHolder.getSurface().isValid()) {
            // Lock the canvas
            canvas = surfaceHolder.lockCanvas();

            // Destroy last frame
            destroyFrame(Color.argb(255, 0, 0, 0));

            drawObjects();

            // Debugging
            if(debugging) {
                paint.setTextSize(24);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));

                canvas.drawText("FPS: " + fps, 10, 30, paint);
                canvas.drawText("Total Objects: " + (levelManager.gameObjects.size() - viewport.getNumberOfClippedObjects()) + "/" + levelManager.gameObjects.size(),
                                10,
                                60,
                                paint);
                canvas.drawText("Current Gravity: " + levelManager.player.upGravity, 10, 90, paint);
                canvas.drawText("Player's Velocity " + levelManager.player.getXVelocity() + "/" + levelManager.player.getYVelocity(),
                                10,
                                120,
                                paint);
                canvas.drawText("Player's Location: " + levelManager.player.getWorldLocation().x + ", " + levelManager.player.getWorldLocation().y,
                                10,
                                150,
                                paint);

                /*Player player = (Player) levelManager.gameObjects.get(levelManager.playerIndex);
                canvas.drawRect(player.rectLeft, paint);
                canvas.drawRect(player.rectTop, paint);
                canvas.drawRect(player.rectRight, paint);
                canvas.drawRect(player.rectBottom, paint);
                System.out.println(player.rectLeft);*/

                //for reset the number of clipped objects each frame
                viewport.resetNumberOfClippedObjects();
            }

            drawButtons();

            // Unlock and update canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void destroyFrame(int color) {
        paint.setColor(color);
        canvas.drawColor(color);
    }

    private void drawObjects() {
        RectF bitmapTemplate = new RectF();
        for(int layer = -1; layer <= 1; layer++) {
            for(int i = 0; i < levelManager.gameObjects.size(); i++) {
                GameObject gameObject = levelManager.gameObjects.get(i);

                if(gameObject.isVisible() && gameObject.getWorldLocation().z == layer) {
                    // Confine collision detections
                    if((gameObject.getWorldLocation().x - levelManager.player.getWorldLocation().x <= 5 && gameObject.getWorldLocation().x - levelManager.player.getWorldLocation().x >= -5) || (gameObject.getWorldLocation().y - levelManager.player.getWorldLocation().y <= 5 && gameObject.getWorldLocation().y - levelManager.player.getWorldLocation().y >= -5)) {
                        // Let the player check for collisions
                        levelManager.player.checkForCollisions(gameObject);
                    }

                    // Setup bitmapTemplate
                    bitmapTemplate.set(viewport.worldToScreen(gameObject.getWorldLocation()));

                    paint.setColor(Color.argb(255, 255, 255, 255));

                    if(inputController.bitmapsOn) {
                        // Animate the player
                        animation.animatePlayer(gameObject);

                        // Draw the bitmap
                        canvas.drawBitmap(levelManager.bitmapArray[levelManager.getBitmapIndex(
                                gameObject.getBlockType())],
                                          bitmapTemplate.left,
                                          bitmapTemplate.top,
                                          paint);
                    }

                    //canvas.drawRect(bitmapTemplate, paint);

                    /*canvas.drawLine(hitboxPlayer.left, hitboxPlayer.top, hitboxPlayer.right, hitboxPlayer.top, paint);
                    canvas.drawLine(hitboxPlayer.right, hitboxPlayer.top, hitboxPlayer.right, hitboxPlayer.bottom, paint);
                    canvas.drawLine(hitboxPlayer.left, hitboxPlayer.bottom, hitboxPlayer.right, hitboxPlayer.bottom, paint);
                    canvas.drawLine(hitboxPlayer.left, hitboxPlayer.bottom, hitboxPlayer.left, hitboxPlayer.top, paint);*/

                   /*if(i == levelManager.playerIndex) {
                       paint.setColor(Color.argb(255, 255, 0, 0));
                   }*/

                    if(!inputController.bitmapsOn) {
                        WorldLocation leftHitbox = new WorldLocation(levelManager.player.leftHitbox.left,
                                                                     levelManager.player.leftHitbox.top);
                        leftHitbox.width = 0.05f;
                        leftHitbox.height = 0.05f;
                        RectF rectLeft = new RectF();
                        rectLeft.set(viewport.worldToScreen(leftHitbox));

                        paint.setColor(Color.argb(255, 0, 255, 0));
                        canvas.drawRect(rectLeft, paint);

                        WorldLocation topHitbox = new WorldLocation(levelManager.player.topHitbox.left,
                                                                    levelManager.player.topHitbox.top);
                        topHitbox.width = 0.05f;
                        topHitbox.height = 0.05f;
                        RectF rectTop = new RectF();
                        rectTop.set(viewport.worldToScreen(topHitbox));

                        paint.setColor(Color.RED);
                        canvas.drawRect(rectTop, paint);

                        WorldLocation rightHitbox = new WorldLocation(levelManager.player.rightHitbox.left,
                                                                      levelManager.player.rightHitbox.top);
                        rightHitbox.width = 0.05f;
                        rightHitbox.height = 0.05f;
                        RectF rectRight = new RectF();
                        rectRight.set(viewport.worldToScreen(rightHitbox));

                        paint.setColor(Color.BLUE);
                        canvas.drawRect(rectRight, paint);

                        WorldLocation bottomHitbox = new WorldLocation(levelManager.player.bottomHitbox.left,
                                                                       levelManager.player.bottomHitbox.top);
                        bottomHitbox.width = 0.05f;
                        bottomHitbox.height = 0.05f;
                        RectF rectBottom = new RectF();
                        rectBottom.set(viewport.worldToScreen(bottomHitbox));

                        paint.setColor(Color.YELLOW);
                        canvas.drawRect(rectBottom, paint);

                        paint.setColor(Color.WHITE);
                        canvas.drawLine(bitmapTemplate.left,
                                        bitmapTemplate.top,
                                        bitmapTemplate.right,
                                        bitmapTemplate.top,
                                        paint);
                        canvas.drawLine(bitmapTemplate.right,
                                        bitmapTemplate.top,
                                        bitmapTemplate.right,
                                        bitmapTemplate.bottom,
                                        paint);
                        canvas.drawLine(bitmapTemplate.left,
                                        bitmapTemplate.bottom,
                                        bitmapTemplate.right,
                                        bitmapTemplate.bottom,
                                        paint);
                        canvas.drawLine(bitmapTemplate.left,
                                        bitmapTemplate.bottom,
                                        bitmapTemplate.left,
                                        bitmapTemplate.top,
                                        paint);
                        canvas.drawLine(0, 0, inputController.leftArea.left, inputController.leftArea.bottom, paint);
                        canvas.drawLine(inputController.rightArea.left, 0, inputController.rightArea.left, inputController.rightArea.bottom, paint);
                        canvas.drawLine(inputController.rightArea.right, 0, inputController.rightArea.right, inputController.rightArea.bottom, paint);

                        canvas.drawLine(inputController.upArea.left, inputController.upArea.top, inputController.upArea.left, inputController.upArea.bottom, paint);
                    }

                    // Force the gameObjects to update themselves
                    gameObject.update(fps);
                }
            }
        }
    }

    private void drawButtons() {
        paint.setColor(Color.argb(80, 255, 255, 255));
        // Debugging: set i = 3 to i = 0
        for(int i = 3; i < inputController.getButtonList().size(); i++) {
            RectF button = new RectF(inputController.getButtonList().get(i).left,
                                     inputController.getButtonList().get(i).top,
                                     inputController.getButtonList().get(i).right,
                                     inputController.getButtonList().get(i).bottom);

            canvas.drawRoundRect(button, 15, 15, paint);
        }
        for(int i = 0; i < inputController.getBitmapList().size(); i++) {
            canvas.drawBitmap(inputController.getBitmapList().get(i),
                              inputController.getButtonList().get(i).left,
                              inputController.getButtonList().get(i).top,
                              null);
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

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // Verify that levelManager is initialized
        if(levelManager != null) {
            inputController.handleInput(motionEvent, levelManager, soundManager, viewport);
        }
        return true;
    }
}
