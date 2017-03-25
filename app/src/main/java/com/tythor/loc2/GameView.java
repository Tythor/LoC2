package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

public class GameView extends ViewObject {
    public static Context context;

    boolean follow = false;
    private boolean debugging = true;
    // Custom objects
    public static Animation animation;
    public static InputController inputController;
    public static LevelManager levelManager;
    public static SoundManager soundManager;
    public static Viewport viewport;

    public GameView(Context context, Paint paint, Canvas canvas, int screenWidth, int screenHeight, String levelName) {
        this.context = context;
        this.paint = paint;
        this.canvas = canvas;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        viewport = new Viewport(screenWidth, screenHeight);

        soundManager = new SoundManager();
        soundManager.loadSound();

        loadLevel(levelName);
    }

    public void loadLevel(String levelName) {
        // Unnecessary?
        levelManager = null;

        levelManager = new LevelManager(levelName);

        inputController = new InputController();

        animation = new Animation();

        viewport.setViewportLocation(levelManager.player.spawnLocation);
        setSpawnViewportLocation();
    }

    private void setSpawnViewportLocation() {
        RectF min = new RectF(0, 0, 0, 0);
        RectF max = new RectF(screenWidth, screenHeight, screenWidth, screenHeight);

        // You are not expected to understand this.
        if(!(viewport.screenToWorld(min).x > 0)) {
            viewport.setViewportLocation(new WorldLocation(levelManager.player.getWorldLocation().x - viewport.screenToWorld(
                    min).x, viewport.getViewportLocation().y));
        }
        if(!(viewport.screenToWorld(max).x < (levelManager.gameObjects.length - 1) * 20)) {
            viewport.setViewportLocation(new WorldLocation(levelManager.player.getWorldLocation().x - (viewport.screenToWorld(
                    max).x - (levelManager.gameObjects.length - 1) * 20),
                                                           viewport.getViewportLocation().y));
        }
        if(!(viewport.screenToWorld(min).y > 0)) {
            viewport.setViewportLocation(new WorldLocation(viewport.getViewportLocation().x,
                                                           levelManager.player.getWorldLocation().y - viewport.screenToWorld(
                                                                   min).y));
        }
        if(!(viewport.screenToWorld(max).y < (levelManager.gameObjects[0].length - 1) * 20)) {
            viewport.setViewportLocation(new WorldLocation(viewport.getViewportLocation().x,
                                                           levelManager.player.getWorldLocation().y - (viewport.screenToWorld(
                                                                   max).y - (levelManager.gameObjects[0].length - 1) * 20)));
        }
    }

    /*@Override
    public void run() {
        while(running) {
            frameStartTime = System.currentTimeMillis();

            update();
            draw();

            // Calculate time taken with update() and draw()
            long differenceBetweenFrames = System.currentTimeMillis() - frameStartTime;
            if(differenceBetweenFrames >= 1) {
                FPS = 1000 / differenceBetweenFrames;
            }
        }
    }*/

    public static int updateCount = 0;

    @Override
    public void update() {
        // If gameObject is on-screen, then render it
        for(int i = 0; i < levelManager.gameObjects.length; i++) {
            for(int j = 0; j < levelManager.gameObjects[i].length; j++) {
                GameObject gameObject = levelManager.gameObjects[i][j];
                if(gameObject != null && gameObject.isActive()) {
                    if(viewport.renderObject(gameObject.getWorldLocation())) {
                        gameObject.setVisible(true);
                    }
                    else {
                        gameObject.setVisible(false);
                    }
                    // if((i < levelManager.gameObjects[i].length + 1 && i > 0) && (levelManager.gameObjects[i - 1][j] != null && levelManager.gameObjects[i + 1][j] != null) && (levelManager.gameObjects[i - 1][j].getClass().equals("class com.tythor.loc2.Block") && levelManager.gameObjects[i + 1][j].getClass().equals("class com.tythor.loc2.Block"))) {
                    if((i < levelManager.gameObjects[i].length + 1 && i > 0) && !(levelManager.gameObjects[i - 1][j] instanceof Block)) {
                        gameObject.checkLeftBounds = true;
                    }
                    if((i < levelManager.gameObjects[i].length + 1 && i > 0) && !(levelManager.gameObjects[i + 1][j] instanceof Block)) {
                        gameObject.checkRightBounds = true;
                    }

                    if(gameObject.instructionList != null)
                        gameObject.update(ViewController.FPS);
                }

                if(gameObject != null) {

                    if(gameObject.isVisible()) {
                        boolean setHitbox = false;
                        if(gameObject.equals(levelManager.player)) {
                            setHitbox = true;
                        }

                        if((gameObject.getWorldLocation().x - levelManager.player.getWorldLocation().x <= 5 && gameObject.getWorldLocation().x - levelManager.player.getWorldLocation().x >= -5) || (gameObject.getWorldLocation().y - levelManager.player.getWorldLocation().y <= 5 && gameObject.getWorldLocation().y - levelManager.player.getWorldLocation().y >= -5)) {
                            //levelManager.player.checkForCollisions(gameObject, setHitbox);

                        }


                        //if(!(levelManager.player.getWorldLocation().x < 0 || levelManager.player.getWorldLocation().x > (i + 1) * 20)) {
                        // Set viewport to player
                        //}
                        //if(!(levelManager.player.getWorldLocation().y < 0 || levelManager.player.getWorldLocation().y > (j + 1) * 20)) {
                        // Set viewport to player
                        //}
                    }
                }
            }
        }
        //System.out.println(levelManager.player.getWorldLocation().x - viewport.worldToScreen(new WorldLocation(screenWidth / 2, 0)).left);
        //System.out.println(updateCount);
        updateCount = 0;
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
        //}
        //}

        /*if(levelManager.player.getWorldLocation().y > viewport.getViewportLocation().y + distanceLimit || levelManager.player.getWorldLocation().y < viewport.getViewportLocation().y - distanceLimit) {
            viewport.setViewportLocation(new WorldLocation(viewport.getViewportLocation().x,
                                                           levelManager.player.getWorldLocation().y));
        }*/
        /*viewport.setViewportLocation(new WorldLocation(levelManager.player.getWorldLocation().x,
                                                       viewport.getViewportLocation().y));*/
        updateViewportLocation();
    }

    private void updateViewportLocation() {
        boolean frozenLeft = false;
        boolean frozenRight = false;
        boolean frozenTop = false;
        boolean frozenBottom = false;

        // Check if viewport should be frozen base on levelSize
        if(!(viewport.screenToWorld(new RectF(0, 0, 0, 0)).x > 0))
            frozenLeft = true;
        if(!(viewport.screenToWorld(new RectF(screenWidth, screenHeight, screenWidth, screenHeight)).x < (levelManager.gameObjects.length - 1) * 20))
            frozenRight = true;
        if(!(viewport.screenToWorld(new RectF(0, 0, 0, 0)).y > 0))
            frozenTop = true;
        if(!(viewport.screenToWorld(new RectF(screenWidth, screenHeight, screenWidth, screenHeight)).y < (levelManager.gameObjects[0].length - 1) * 20))
            frozenBottom = true;

        // Set viewport if it's not frozen
        if((viewport.screenToWorld(new RectF(0, 0, 0, 0)).x > 0 && !frozenRight) || (viewport.screenToWorld(new RectF(screenWidth, screenHeight, screenWidth, screenHeight)).x < (levelManager.gameObjects.length - 1) * 20 && !frozenLeft)) {
            viewport.setViewportLocation(new WorldLocation(levelManager.player.getWorldLocation().x,
                                                           viewport.getViewportLocation().y));
        }

        if((viewport.screenToWorld(new RectF(0, 0, 0, 0)).y > 0 && !frozenBottom) || (viewport.screenToWorld(new RectF(screenWidth, screenHeight, screenWidth, screenHeight)).y < (levelManager.gameObjects[0].length - 1) * 20 && !frozenTop)) {
            viewport.setViewportLocation(new WorldLocation(viewport.getViewportLocation().x,
                                                           levelManager.player.getWorldLocation().y));
        }
    }

    int loopCount;
    long savedFPS;

    @Override
    public void draw() {
        drawObjects();

        // Debugging
        if(debugging) {
            paint.setTextSize(36);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.argb(255, 255, 255, 255));

            if(loopCount % 10 == 0)
                savedFPS = ViewController.FPS;

            canvas.drawText("FPS: " + savedFPS, 930, 50, paint);
            // Incorrect total objects
            canvas.drawText("Total Objects: " + viewport.getNumberOfRenderedObjects() + "/" + viewport.numberOfObjects,
                            960,
                            90,
                            paint);
            canvas.drawText("Current Gravity: " + levelManager.player.upGravity,
                            960,
                            130,
                            paint);
            canvas.drawText("Player's Velocity " + levelManager.player.getXVelocity() + "/" + levelManager.player.getYVelocity(),
                            960,
                            170,
                            paint);
            canvas.drawText("Player's Location: " + levelManager.player.getWorldLocation().x + ", " + levelManager.player.getWorldLocation().y,
                            960,
                            210,
                            paint);
            canvas.drawText("PixelsPerMeter: " + viewport.pixelsPerMeter,
                            960,
                            250,
                            paint);
            canvas.drawText("MextersToShow: " + viewport.METERSTOSHOWX,
                            960,
                            290,
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
        loopCount++;
    }


    private void drawObjects() {
        RectF bitmapTemplate = new RectF();
        for(int layer = 0; layer <= 2; layer++) {
            for(int i = 0; i < levelManager.gameObjects.length; i++) {
                for(int j = 0; j < levelManager.gameObjects[i].length; j++) {
                    GameObject gameObject = levelManager.gameObjects[i][j];

                    if(gameObject != null) {

                        if(gameObject.isVisible() && gameObject.getWorldLocation().z == layer) {

                            // Confine collision detections
                            //if((gameObject.getWorldLocation().x - levelManager.player.getWorldLocation().x <= 5 && gameObject.getWorldLocation().x - levelManager.player.getWorldLocation().x >= -5) || (gameObject.getWorldLocation().y - levelManager.player.getWorldLocation().y <= 5 && gameObject.getWorldLocation().y - levelManager.player.getWorldLocation().y >= -5)) {
                            // Let the player check for collisions
                            // Force the gameObjects to update themselves
                            // Set viewport to player
                            //}

                            // Setup bitmapTemplate
                            bitmapTemplate.set(viewport.worldToScreen(gameObject.getWorldLocation()));

                            paint.setColor(Color.argb(255, 255, 255, 255));
                            if(inputController.bitmapsOn) {
                                // Animate the player
                                //animation.animatePlayer(gameObject);

                                // Draw the bitmap
                                canvas.drawBitmap(levelManager.bitmapArray[levelManager.getBitmapIndex(
                                        gameObject.getBlockType())],
                                                  bitmapTemplate.left,
                                                  bitmapTemplate.top,
                                                  paint);
                            }

                   /*if(i == levelManager.playerIndex) {
                       paint.setColor(Color.argb(255, 255, 0, 0));
                   }*/

                            if(!inputController.bitmapsOn) {
                                WorldLocation leftHitbox = new WorldLocation(levelManager.player.leftHitbox.left,
                                                                             levelManager.player.leftHitbox.top);
                                leftHitbox.width = 1f;
                                leftHitbox.height = levelManager.player.leftHitbox.bottom - levelManager.player.leftHitbox.top;
                                RectF rectLeft = new RectF();
                                rectLeft.set(viewport.worldToScreen(leftHitbox));

                                paint.setColor(Color.GREEN);
                                canvas.drawRect(rectLeft, paint);


                                WorldLocation rightHitbox = new WorldLocation(levelManager.player.rightHitbox.left - 1f,
                                                                              levelManager.player.rightHitbox.top);
                                rightHitbox.width = 1f;
                                rightHitbox.height = levelManager.player.rightHitbox.bottom - levelManager.player.rightHitbox.top;
                                RectF rectRight = new RectF();
                                rectRight.set(viewport.worldToScreen(rightHitbox));

                                paint.setColor(Color.BLUE);
                                canvas.drawRect(rectRight, paint);


                                WorldLocation topHitbox = new WorldLocation(levelManager.player.topHitbox.left,
                                                                            levelManager.player.topHitbox.top);
                                topHitbox.width = levelManager.player.topHitbox.right - levelManager.player.topHitbox.left;
                                topHitbox.height = 1f;
                                RectF rectTop = new RectF();
                                rectTop.set(viewport.worldToScreen(topHitbox));

                                paint.setColor(Color.RED);
                                canvas.drawRect(rectTop, paint);


                                WorldLocation bottomHitbox = new WorldLocation(levelManager.player.bottomHitbox.left,
                                                                               levelManager.player.bottomHitbox.top - 1f);
                                bottomHitbox.width = levelManager.player.bottomHitbox.right - levelManager.player.bottomHitbox.left;
                                bottomHitbox.height = 1f;
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
                                Paint text = new Paint();
                                text.setColor(Color.WHITE);
                                text.setTextSize(15);
                                canvas.drawText(i + ", " + j, bitmapTemplate.left, bitmapTemplate.bottom, text);

                        /*canvas.drawLine(0, 0, inputController.leftArea.left, inputController.leftArea.bottom, paint);
                        canvas.drawLine(inputController.rightArea.left, 0, inputController.rightArea.left, inputController.rightArea.bottom, paint);
                        canvas.drawLine(inputController.rightArea.right, 0, inputController.rightArea.right, inputController.rightArea.bottom, paint);

                        canvas.drawLine(inputController.upArea.left, inputController.upArea.top, inputController.upArea.left, inputController.upArea.bottom, paint);*/
                            }
                        }
                    }
                }
            }
        }
    }

    private void drawButtons() {
        paint.setColor(Color.argb(80, 255, 255, 255));
        // Debugging: set i = 4 to i = 0
        for(int i = 4; i < inputController.getButtonList().size(); i++) {
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


    public boolean onTouchEvent(MotionEvent motionEvent) {
        // Verify that levelManager is initialized
        if(levelManager != null) {
            inputController.handleInput(motionEvent);
        }
        return true;
    }
}
