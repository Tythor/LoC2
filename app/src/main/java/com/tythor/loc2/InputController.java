package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

public class InputController {
    Rect left;
    Rect right;
    Rect up;
    Rect zoomIn;
    Rect zoomOut;
    Rect increaseGravity;
    Rect deccreaseGravity;
    Rect toggleBitmaps;
    Rect resetLocation;

    Bitmap bitmapLeft;
    Bitmap bitmapRight;
    Bitmap bitmapUp;

    boolean bitmapsOn = true;

    InputController(Context context, int pixelsPerMeter, int screenWidth, int screenHeight) {
        int buttonWidth = 3 * pixelsPerMeter;
        int buttonHeight = 3 * pixelsPerMeter;
        int buttonPadding = pixelsPerMeter / 2;

        left = new Rect(buttonPadding,
                        screenHeight - buttonHeight - buttonPadding,
                        buttonPadding + buttonWidth,
                        screenHeight - buttonPadding);

        bitmapLeft = prepareBitmap(context, "arrowleft", pixelsPerMeter);

        right = new Rect(buttonWidth + buttonPadding * 2,
                         screenHeight - buttonHeight - buttonPadding,
                         buttonWidth * 2 + buttonPadding * 2,
                         screenHeight - buttonPadding);

        bitmapRight = prepareBitmap(context, "arrowright", pixelsPerMeter);

        up = new Rect(screenWidth - buttonWidth - buttonPadding,
                        screenHeight - buttonHeight - buttonPadding,
                        screenWidth - buttonPadding,
                        screenHeight - buttonPadding);

        bitmapUp = prepareBitmap(context, "arrowup", pixelsPerMeter);

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

        toggleBitmaps = new Rect(buttonPadding,
                                 zoomIn.top,
                                 buttonPadding + buttonWidth,
                                 zoomIn.bottom);

        resetLocation = new Rect(toggleBitmaps.left,
                                 increaseGravity.top,
                                 toggleBitmaps.right,
                                 increaseGravity.bottom);
    }

    private Bitmap prepareBitmap(Context context, String bitmapName, int pixelsPerMeter) {
        int resID = context.getResources().getIdentifier(bitmapName,
                                                         "drawable",
                                                         context.getPackageName());

        // Return the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                                           3 * pixelsPerMeter,
                                           3 * pixelsPerMeter,
                                           false);
        return bitmap;
    }

    public void handleInput(MotionEvent motionEvent, LevelManager levelManager, SoundManager soundManager, Viewport viewport) {
        int pointerCount = motionEvent.getPointerCount();

        for(int i = 0; i < pointerCount; i++) {

            int x = (int) motionEvent.getX(i);
            int y = (int) motionEvent.getY(i);

            if(right.contains(x, y)) {
                levelManager.player.setPressingRight(true);
                levelManager.player.setPressingLeft(false);
            }
            else if(left.contains(x, y)) {
                levelManager.player.setPressingLeft(true);
                levelManager.player.setPressingRight(false);
            }
            if(up.contains(x, y)) {
                levelManager.player.startJump();
            }

            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {

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
                        levelManager.player.gravity++;
                    }
                    else if(deccreaseGravity.contains(x, y)) {
                        levelManager.player.gravity--;
                    }
                    else if(toggleBitmaps.contains(x, y)) {
                        bitmapsOn = !bitmapsOn;
                    }
                    else if(resetLocation.contains(x, y)) {
                        levelManager.player.setWorldLocationX(1);
                        levelManager.player.setWorldLocationY(15);
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
            /*else {// Not playing
                //Move the viewport around to explore the map
                switch(motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        if(right.contains(x, y)) {
                            viewport.moveViewportRight(l.mapWidth);
                            //Log.w("right:", "DOWN" );
                        }
                        else if(left.contains(x, y)) {
                            viewport.moveViewportLeft();
                            //Log.w("left:", "DOWN" );
                        }
                        else if(up.contains(x, y)) {
                            viewport.moveViewportUp();
                            //Log.w("up:", "DOWN" );/
                        }
                        else if(shoot.contains(x, y)) {
                            viewport.moveViewportDown(l.mapHeight);
                            //Log.w("shoot:", "DOWN" );/
                        }
                        else if(pause.contains(x, y)) {
                            l.switchPlayingStatus();
                            System.out.println("pause:" + "DOWN");
                        }

                        break;
                }
            }*/
        }
    }

    public ArrayList<Rect> getButtonList() {
        ArrayList<Rect> buttonList = new ArrayList<>();


        buttonList.add(left);
        buttonList.add(right);
        buttonList.add(up);
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
        return bitmapList;
    }
}
