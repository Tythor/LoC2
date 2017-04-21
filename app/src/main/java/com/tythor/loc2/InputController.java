package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

import static com.tythor.loc2.GameActivity.context;
import static com.tythor.loc2.GameView.levelManager;
import static com.tythor.loc2.GameView.viewport;

public class InputController {
    float pixelsPerMeter = viewport.pixelsPerMeter;

    RectF left;
    RectF right;
    RectF up;
    RectF zoomIn;
    RectF zoomOut;
    RectF increaseGravity;
    RectF deccreaseGravity;
    RectF toggleBitmaps;
    RectF resetLocation;
    RectF pauseButton;

    Bitmap bitmapLeft;
    Bitmap bitmapRight;
    Bitmap bitmapUp;
    Bitmap bitmapPauseButton;

    RectF pauseArea;
    RectF leftArea;
    RectF rightArea;
    RectF upArea;

    boolean bitmapsOn = true;

    InputController() {
        int screenWidth = viewport.getScreenWidth();
        int screenHeight = viewport.getScreenHeight();

        // Buttons are 3 x 3
        float buttonWidth = pixelsPerMeter * 60;
        float buttonHeight = pixelsPerMeter * 60;
        float buttonPadding = pixelsPerMeter * 2;

        left = new RectF(buttonPadding,
                        screenHeight - buttonHeight - buttonPadding,
                        buttonPadding + buttonWidth,
                        screenHeight - buttonPadding);
        bitmapLeft = prepareBitmap("arrowleft");

        right = new RectF(buttonWidth + buttonPadding * 3,
                         screenHeight - buttonHeight - buttonPadding,
                         buttonWidth * 2 + buttonPadding * 3,
                         screenHeight - buttonPadding);
        bitmapRight = prepareBitmap("arrowright");

        up = new RectF(screenWidth - buttonWidth - buttonPadding,
                      screenHeight - buttonHeight - buttonPadding,
                      screenWidth - buttonPadding,
                      screenHeight - buttonPadding);
        bitmapUp = prepareBitmap("arrowup");

        zoomIn = new RectF(screenWidth - buttonPadding - buttonWidth,
                          buttonPadding,
                          screenWidth - buttonPadding,
                          buttonPadding + buttonHeight);

        zoomOut = new RectF(screenWidth - buttonWidth * 2 - buttonPadding * 2,
                           zoomIn.top,
                           screenWidth - buttonWidth - buttonPadding * 2,
                           zoomIn.bottom);

        increaseGravity = new RectF(zoomIn.left,
                                   buttonPadding * 2 + buttonHeight,
                                   zoomIn.right,
                                   buttonPadding * 2 + buttonHeight * 2);

        deccreaseGravity = new RectF(zoomOut.left,
                                    increaseGravity.top,
                                    zoomOut.right,
                                    increaseGravity.bottom);

        resetLocation = new RectF(buttonPadding * 2 + buttonWidth,
                zoomIn.top,
                buttonPadding * 2 + buttonWidth * 2,
                zoomIn.bottom);

        toggleBitmaps = new RectF(resetLocation.right + buttonPadding,
                resetLocation.top,
                resetLocation.right + buttonPadding + buttonWidth,
                resetLocation.bottom);

        pauseButton = new RectF((int) (buttonWidth / 7.5), (int) (buttonHeight / 7.5), (int) (buttonWidth / 7.5) + (int) ((pixelsPerMeter * 60) / 1.875), (int) (buttonWidth / 7.5) + (int) ((pixelsPerMeter * 60) / 1.875));
        System.out.println(pauseButton);
        bitmapPauseButton = prepareBitmap("pausebutton");
        pauseArea = new RectF(0, 0, pauseButton.left + bitmapPauseButton.getWidth(), pauseButton.top + bitmapPauseButton.getHeight());

        leftArea = new RectF(0, 0, left.right + pixelsPerMeter / 2, screenHeight);
        rightArea = new RectF(right.left - pixelsPerMeter / 2, 0, right.right, screenHeight);
        upArea = new RectF(up.left, 0, screenWidth, screenHeight);
    }

    private Bitmap prepareBitmap(String bitmapName) {
        int resID = context.getResources().getIdentifier(bitmapName,
                                                         "drawable",
                                                         context.getPackageName());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        // Return the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID, options);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                                           (int) (60 * pixelsPerMeter),
                                           (int) (60 * pixelsPerMeter),
                                           false);

        if(bitmapName.equals("pausebutton")) {
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) ((pixelsPerMeter * 60) / 1.875), (int) ((pixelsPerMeter * 60) / 1.875), false);
        }
        return bitmap;
    }

    public void handleInput(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();

        for(int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);

            // Extend the controls' touch area
            if(leftArea.contains(x, y) && !pauseArea.contains(x, y)) {
                levelManager.player.setPressingLeft(true);
                levelManager.player.setPressingRight(false);
            }
            else if(rightArea.contains(x, y)) {
                levelManager.player.setPressingLeft(false);
                levelManager.player.setPressingRight(true);
            }

            if(upArea.contains(x, y)) {
                levelManager.player.startJump();
            }
            else if(!(leftArea.contains(x, y) || rightArea.contains(x, y))) {
                levelManager.player.setPressingLeft(false);
                levelManager.player.setPressingRight(false);
            }

            switch(motionEvent.getActionMasked() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if(zoomIn.contains(x, y)) {
                        viewport.METERSTOSHOWX *= 2;
                        viewport.METERSTOSHOWY *= 2;
                        viewport.pixelsPerMeter *= 2;
                        levelManager.refreshBitmaps();
                    }
                    else if(zoomOut.contains(x, y)) {
                        viewport.METERSTOSHOWX /= 2;
                        viewport.METERSTOSHOWY /= 2;
                        viewport.pixelsPerMeter /= 2;
                        levelManager.refreshBitmaps();
                    }
                    else if(increaseGravity.contains(x, y)) {
                        levelManager.player.upGravity += 10;
                    }
                    else if(deccreaseGravity.contains(x, y)) {
                        levelManager.player.upGravity -= 10;
                    }
                    else if(toggleBitmaps.contains(x, y)) {
                        bitmapsOn = !bitmapsOn;
                    }
                    else if(resetLocation.contains(x, y)) {
                        levelManager.player.setWorldLocationX(levelManager.player.spawnLocation.x);
                        levelManager.player.setWorldLocationY(levelManager.player.spawnLocation.y);
                    }
                    else if(pauseArea.contains(x, y)) {
                        ViewController.changeToTitleView();
                        System.out.println("TOUCHED PAUSE");
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    levelManager.player.setPressingRight(false);
                    levelManager.player.setPressingLeft(false);
                    break;

                    /*case MotionEvent.ACTION_POINTER_DOWN:
                        if (right.contains(x, y)) {
                            l.player.setPressingRight(true);
                            l.player.setPressingLeft(false);
                            System.out.println("RD");
                        } else if (left.contains(x, y)) {
                            l.player.setPressingLeft(true);
                            l.player.setPressingRight(false);
                            System.out.println("LD");
                        } else if (up.contains(x, y)) {
                            l.player.startJump(sound);
                        } else if (shoot.contains(x, y)) {
                            if (l.player.pullTrigger()) {
                                sound.playSound("shoot");
                            }
                        }
                        System.out.println("DOWN");

                        break;*/


                /*case MotionEvent.ACTION_POINTER_UP:
                    levelManager.player.setPressingRight(false);
                    levelManager.player.setPressingLeft(false);
                    System.out.println("UP");
                    break;*/
            }
        }
    }

    public ArrayList<RectF> getButtonList() {
        ArrayList<RectF> buttonList = new ArrayList<>();

        buttonList.add(left);
        buttonList.add(right);
        buttonList.add(up);
        buttonList.add(pauseButton);
        buttonList.add(zoomIn);
        buttonList.add(zoomOut);
        buttonList.add(increaseGravity);
        buttonList.add(deccreaseGravity);
        buttonList.add(toggleBitmaps);
        buttonList.add(resetLocation);

        return buttonList;
    }

    public ArrayList<Bitmap> getBitmapList() {
        ArrayList<Bitmap> bitmapList = new ArrayList<>();

        bitmapList.add(bitmapLeft);
        bitmapList.add(bitmapRight);
        bitmapList.add(bitmapUp);
        bitmapList.add(bitmapPauseButton);

        return bitmapList;
    }
}
