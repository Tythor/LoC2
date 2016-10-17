package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class InputController {
    int pixelsPerMeter;
    int screenWidth;
    int screenHeight;

    Rect left;
    Rect right;
    Rect up;
    Rect zoomIn;
    Rect zoomOut;
    Rect increaseGravity;
    Rect deccreaseGravity;
    Rect toggleBitmaps;
    Rect resetLocation;
    Rect pauseButton;

    Bitmap bitmapLeft;
    Bitmap bitmapRight;
    Bitmap bitmapUp;
    Bitmap bitmapPauseButton;

    Rect leftArea;
    Rect rightArea;
    Rect upArea;

    boolean bitmapsOn = true;

    InputController(Context context, int pixelsPerMeter, int screenWidth, int screenHeight) {
        this.pixelsPerMeter = pixelsPerMeter;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Buttons are 3 x 3
        int buttonWidth = pixelsPerMeter * 3;
        int buttonHeight = pixelsPerMeter * 3;
        int buttonPadding = pixelsPerMeter / 2;

        left = new Rect(buttonPadding,
                        screenHeight - buttonHeight - buttonPadding,
                        buttonPadding + buttonWidth,
                        screenHeight - buttonPadding);
        bitmapLeft = prepareBitmap(context, "arrowleft");

        right = new Rect(buttonWidth + buttonPadding * 3,
                         screenHeight - buttonHeight - buttonPadding,
                         buttonWidth * 2 + buttonPadding * 3,
                         screenHeight - buttonPadding);
        bitmapRight = prepareBitmap(context, "arrowright");

        up = new Rect(screenWidth - buttonWidth - buttonPadding,
                      screenHeight - buttonHeight - buttonPadding,
                      screenWidth - buttonPadding,
                      screenHeight - buttonPadding);
        bitmapUp = prepareBitmap(context, "arrowup");

        zoomIn = new Rect(screenWidth - buttonPadding - buttonWidth,
                          buttonPadding,
                          screenWidth - buttonPadding,
                          buttonPadding + buttonHeight);

        zoomOut = new Rect(screenWidth - buttonWidth * 2 - buttonPadding * 2,
                           zoomIn.top,
                           screenWidth - buttonWidth - buttonPadding * 2,
                           zoomIn.bottom);

        increaseGravity = new Rect(zoomIn.left,
                                   buttonPadding * 2 + buttonHeight,
                                   zoomIn.right,
                                   buttonPadding * 2 + buttonHeight * 2);

        deccreaseGravity = new Rect(zoomOut.left,
                                    increaseGravity.top,
                                    zoomOut.right,
                                    increaseGravity.bottom);

        resetLocation = new Rect(buttonPadding * 2 + buttonWidth,
                zoomIn.top,
                buttonPadding * 2 + buttonWidth * 2,
                zoomIn.bottom);

        toggleBitmaps = new Rect(resetLocation.right + buttonPadding,
                resetLocation.top,
                resetLocation.right + buttonPadding + buttonWidth,
                resetLocation.bottom);

        pauseButton = new Rect((int) (buttonWidth / 7.5), (int) (buttonHeight / 7.5), (int) (buttonWidth / 2.5), (int) (buttonHeight / 2.5));
        System.out.println(pauseButton.width() + ", " + pauseButton.height());
        bitmapPauseButton = prepareBitmap(context, "pausebutton");

        leftArea = new Rect(0, 0, left.right + pixelsPerMeter / 2, screenHeight);
        rightArea = new Rect(right.left - pixelsPerMeter / 2, 0, right.right, screenHeight);
        upArea = new Rect(up.left, 0, screenWidth, screenHeight);
    }

    private Bitmap prepareBitmap(Context context, String bitmapName) {
        int resID = context.getResources().getIdentifier(bitmapName,
                                                         "drawable",
                                                         context.getPackageName());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        // Return the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID, options);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                                           3 * pixelsPerMeter,
                                           3 * pixelsPerMeter,
                                           false);

        if(bitmapName.equals("pausebutton")) {
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) ((pixelsPerMeter * 3) / 1.875), (int) ((pixelsPerMeter * 3) / 1.875), false);
        }
        return bitmap;
    }

    public void handleInput(MotionEvent motionEvent, LevelManager levelManager, SoundManager soundManager, Viewport viewport) {
        int pointerCount = motionEvent.getPointerCount();

        for(int i = 0; i < pointerCount; i++) {
            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);

            // Extend the controls' touch area
            if(leftArea.contains(x, y)) {
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

            System.out.println(pointerCount + " " + motionEvent.getPointerId(i) + " " + (leftArea.contains(x, y) || rightArea.contains(x, y)) + " " + upArea.contains(x, y));

            switch(motionEvent.getActionMasked() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    if(zoomIn.contains(x, y)) {
                        if(viewport.METERSTOSHOWY % 2 == 0) {
                            viewport.METERSTOSHOWX /= 2;
                            viewport.METERSTOSHOWY /= 2;
                            viewport.pixelsPerMeter = viewport.getScreenWidth() / (viewport.METERSTOSHOWX - 2);
                        }
                    }
                    else if(zoomOut.contains(x, y)) {
                        viewport.METERSTOSHOWX *= 2;
                        viewport.METERSTOSHOWY *= 2;
                        viewport.pixelsPerMeter = viewport.getScreenWidth() / (viewport.METERSTOSHOWX - 2);

                    }
                    else if(increaseGravity.contains(x, y)) {
                        levelManager.player.upGravity++;
                    }
                    else if(deccreaseGravity.contains(x, y)) {
                        levelManager.player.upGravity--;
                    }
                    else if(toggleBitmaps.contains(x, y)) {
                        bitmapsOn = !bitmapsOn;
                    }
                    else if(resetLocation.contains(x, y)) {
                        levelManager.player.setWorldLocationX(1);
                        levelManager.player.setWorldLocationY(17);
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

    public ArrayList<Rect> getButtonList() {
        ArrayList<Rect> buttonList = new ArrayList<>();

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
