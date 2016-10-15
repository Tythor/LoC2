package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public abstract class GameObject {
    // Facing
    final int LEFT = 1;
    final int RIGHT = 2;
    public boolean canPassX = true;
    public boolean canPassY = true;
    private WorldLocation worldLocation;
    private boolean active = true;
    private boolean visible = true;
    private char blockType;
    private String bitmapName;
    private float xVelocity = 0;
    private float yVelocity = 0;
    private int facing;
    private boolean movable = false;
    private boolean deadly = false;
    public RectF objectHitbox = new RectF();

    // Force the gameObjects to update themselves
    public abstract void update(long fps);

    // Label, fetch, scale, then return the bitmap
    public Bitmap prepareBitmap(Context context, String bitmapName, int pixelsPerMeter) {
        // Label the bitmap
        int resID = context.getResources().getIdentifier(bitmapName,
                                                         "drawable",
                                                         context.getPackageName());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        long timeStart = System.currentTimeMillis();

        // Fetch the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID, options);

        System.out.println(bitmap.getDensity() + " " + bitmap.getWidth() + " " + bitmap.getHeight() + " " + bitmap.getByteCount());

        // Scale the bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap,
                                           (int) (worldLocation.width * pixelsPerMeter),
                                           (int) (worldLocation.height * pixelsPerMeter),
                                           false);
        System.out.println(System.currentTimeMillis() - timeStart);
        return bitmap;
    }

    public void setupGameObject(char blockType, String bitmapName, WorldLocation worldLocation) {
        this.blockType = blockType;
        this.bitmapName = bitmapName;
        this.worldLocation = worldLocation;

        objectHitbox.left = worldLocation.x;
        objectHitbox.top = worldLocation.y;
        objectHitbox.right = worldLocation.x + 1;
        objectHitbox.bottom = worldLocation.y + 1;
    }

    public void setupGameObject(char blockType, String bitmapName, WorldLocation worldLocation, int facing, boolean movable, boolean active, boolean visible) {
        this.blockType = blockType;
        this.bitmapName = bitmapName;
        this.worldLocation = worldLocation;
        this.facing = facing;
        this.movable = movable;
        this.active = active;
        this.visible = visible;
    }

    // Update GameObject's location
    public void move(long fps) {
        if(canPassX) {
            worldLocation.x += xVelocity / fps;
        }
        if(canPassY) {
            worldLocation.y += yVelocity / fps;
        }
        if(!canPassX || !canPassY) {
            System.out.println("CAN'T PASS");
        }
    }

    public WorldLocation getWorldLocation() {
        return worldLocation;
    }

    public String getBitmapName() {
        return bitmapName;
    }

    public void setBitmapName(String bitmapName) {
        this.bitmapName = bitmapName;
    }

    public float getWidth() {
        return worldLocation.width;
    }

    public float getHeight() {
        return worldLocation.height;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public char getBlockType() {
        return blockType;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public float getXVelocity() {
        return xVelocity;
    }

    public void setXVelocity(float xVelocity) {
        if(movable) {
            this.xVelocity = xVelocity;
        }
    }

    public float getYVelocity() {
        return yVelocity;
    }

    public void setYVelocity(float yVelocity) {
        if(movable) {
            this.yVelocity = yVelocity;
        }
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public void setWorldLocationX(float worldLocationX) {
        this.worldLocation.x = worldLocationX;
    }

    public void setWorldLocationY(float worldLocationY) {
        this.worldLocation.y = worldLocationY;
    }

    public boolean isDeadly() {
        return deadly;
    }

    public void setDeadly(boolean deadly) {
        this.deadly = deadly;
    }
}
