package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import static com.tythor.loc2.GameView.levelManager;

public class Block extends GameObject {
    WorldLocation worldLocation;

    RectF leftHitbox = new RectF();
    RectF topHitbox = new RectF();
    RectF rightHitbox = new RectF();
    RectF bottomHitbox = new RectF();

    Block(float startLocationX, float startLocationY, String blockType) {
        // Blocks are 1 x 1
        final float WIDTH = 20;
        final float HEIGHT = 20;
        worldLocation = new WorldLocation(startLocationX, startLocationY, 1, WIDTH, HEIGHT);
        String bitmapName = "";

        switch(blockType) {
            case "B":
                bitmapName = "blockblue";
                break;

            case "G":
                bitmapName = "blockgreen";
                break;

            case "O":
                bitmapName = "blockorange";
                break;

            case "Pi":
                bitmapName = "blockpink";
                break;

            case "Pu":
                bitmapName = "blockpurple";
                break;
            case "R":
                bitmapName = "blockred";
                break;
        }

        setupGameObject(blockType, bitmapName, worldLocation);
    }

    int count;
    @Override
    public void update(long fps) {
        count++;
        // Check for collision
        /*setPlayerHitboxes();
        if(levelManager.player.leftHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom) && checkRightBounds) {
            levelManager.player.setWorldLocationX(objectHitbox.right);
            System.out.println("LEFT");
            GameView.updateCount++;
            setPlayerHitboxes();
            setObjectHitboxes();
        }
        if(levelManager.player.rightHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom) && checkLeftBounds) {
            levelManager.player.setWorldLocationX(objectHitbox.left - levelManager.player.getWidth());
            System.out.println("RIGHT");
            GameView.updateCount++;
            setPlayerHitboxes();
            setObjectHitboxes();
        }
        if(levelManager.player.bottomHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom)) {
            levelManager.player.setWorldLocationY(objectHitbox.top - levelManager.player.getHeight());
            System.out.println("BOTTOM");
            GameView.updateCount++;
            setPlayerHitboxes();
            setObjectHitboxes();
        }
        if(levelManager.player.bottomHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom) && levelManager.player.leftHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom)) {
            System.out.println("LEFT AND BOTTOM");
        }*/

        setPlayerHitboxes();
        setObjectHitboxes();

        if(levelManager.player.leftHitbox.intersects(rightHitbox.left, rightHitbox.top, rightHitbox.right, rightHitbox.bottom) && checkRightBounds) {
            System.out.println("RIGHT");
            levelManager.player.setWorldLocationX(objectHitbox.right);
        }
        setPlayerHitboxes();
        setObjectHitboxes();

        if(levelManager.player.rightHitbox.intersects(leftHitbox.left, leftHitbox.top, leftHitbox.right, leftHitbox.bottom) && checkLeftBounds) {
            System.out.println("LEFT");
            levelManager.player.setWorldLocationX(objectHitbox.left - levelManager.player.getWidth());
        }
        setPlayerHitboxes();
        setObjectHitboxes();


        if(levelManager.player.bottomHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom)) {
            levelManager.player.setWorldLocationY(objectHitbox.top - levelManager.player.getHeight());
            if(count % 100 == 0) {
                System.out.println("BOTTOM");
            }
        }
        setPlayerHitboxes();
        setObjectHitboxes();

        if(levelManager.player.topHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom)) {
            System.out.println("TOP");
            levelManager.player.setWorldLocationY(objectHitbox.bottom);
            levelManager.player.jumping = false;
            levelManager.player.addedGravity = 0;
        }

    }

    public void setObjectHitboxes() {
        objectHitbox.left = worldLocation.x;
        objectHitbox.top = worldLocation.y;
        objectHitbox.right = worldLocation.x + worldLocation.width;
        objectHitbox.bottom = worldLocation.y + worldLocation.height;

        leftHitbox.left = worldLocation.x;
        leftHitbox.top = worldLocation.y;
        leftHitbox.right = worldLocation.x + 3;
        leftHitbox.bottom = worldLocation.y + worldLocation.height;

        topHitbox.left = worldLocation.x;
        topHitbox.top = worldLocation.y;
        topHitbox.right = worldLocation.x + worldLocation.width;
        topHitbox.bottom = worldLocation.y + 10;

        rightHitbox.left = worldLocation.x + worldLocation.width - 3;
        rightHitbox.top = worldLocation.y;
        rightHitbox.right = worldLocation.x + worldLocation.width;
        rightHitbox.bottom = worldLocation.y + worldLocation.height;

        bottomHitbox.left = worldLocation.x;
        bottomHitbox.top = worldLocation.y + worldLocation.height - 10;
        bottomHitbox.right = worldLocation.x + worldLocation.width;
        bottomHitbox.bottom = worldLocation.y + worldLocation.height;
    }

    public void setPlayerHitboxes() {
        WorldLocation playerLocation = levelManager.player.getWorldLocation();
        // Set hitboxes
        levelManager.player.leftHitbox.left = playerLocation.x;
        levelManager.player.leftHitbox.top = playerLocation.y;
        levelManager.player.leftHitbox.right = levelManager.player.leftHitbox.left;
        levelManager.player.leftHitbox.bottom = playerLocation.y + playerLocation.height;

        levelManager.player.topHitbox.left = playerLocation.x;
        levelManager.player.topHitbox.top = playerLocation.y;
        levelManager.player.topHitbox.right = levelManager.player.topHitbox.left + playerLocation.width;
        levelManager.player.topHitbox.bottom = levelManager.player.topHitbox.top;

        levelManager.player.rightHitbox.left = playerLocation.x + playerLocation.width;
        levelManager.player.rightHitbox.top = playerLocation.y;
        levelManager.player.rightHitbox.right = levelManager.player.rightHitbox.left;
        levelManager.player.rightHitbox.bottom = playerLocation.y + playerLocation.height;

        levelManager.player.bottomHitbox.left = playerLocation.x;
        levelManager.player.bottomHitbox.top = playerLocation.y + playerLocation.height;
        levelManager.player.bottomHitbox.right = levelManager.player.bottomHitbox.left + playerLocation.width;
        levelManager.player.bottomHitbox.bottom = levelManager.player.bottomHitbox.top;
    }

}