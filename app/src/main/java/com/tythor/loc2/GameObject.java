package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public abstract class GameObject {
    private Context context = GameView.context;

    // Facing
    final int LEFT = 1;
    final int RIGHT = 2;
    public boolean canPassX = true;
    public boolean canPassY = true;
    private WorldLocation worldLocation;
    private boolean active = true;
    private boolean visible = true;
    private String blockType;
    private String bitmapName;
    private float xVelocity = 0;
    private float yVelocity = 0;
    private int facing;
    private boolean movable = false;
    private boolean deadly = false;
    public RectF objectHitbox = new RectF();
    public boolean checkLeftBounds = false;
    public boolean checkRightBounds = false;

    // Force the gameObjects to update themselves
    public abstract void update(long fps);

    // Bitmap optimization
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both height and width larger than the requested height and width
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // Label, fetch, scale, then return the bitmap
    public Bitmap prepareBitmap(String bitmapName, float scaleFactor) {
        long timeStart = System.currentTimeMillis();

        // Label the bitmap
        int resID = context.getResources().getIdentifier(bitmapName,
                                                         "drawable",
                                                         context.getPackageName());

        // Optimization
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resID, options);
        options.inSampleSize = calculateInSampleSize(options, (int) (worldLocation.width * Viewport.pixelsPerMeter * scaleFactor), (int) (worldLocation.height * Viewport.pixelsPerMeter * scaleFactor));
        options.inJustDecodeBounds = false;

        // Fetch the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID, options);

        // Scale the bitmap; divided by block size
        bitmap = Bitmap.createScaledBitmap(bitmap,
                                           (int) (worldLocation.width * Viewport.pixelsPerMeter * scaleFactor),
                                           (int) (worldLocation.height * Viewport.pixelsPerMeter * scaleFactor),
                                           false);
        System.out.println((worldLocation.width * Viewport.pixelsPerMeter * scaleFactor));
        System.out.println((worldLocation.height * Viewport.pixelsPerMeter * scaleFactor));

        System.out.println(bitmap.getWidth() + " x " + bitmap.getHeight());
        System.out.println((System.currentTimeMillis() - timeStart) / 1000.0 + "s");
        /*if(bitmapName.equals("playerleft1")) {
            System.out.println("resetPlayer");
            // Scale the bitmap; divided by block size
            int width = (int) (8 * Viewport.pixelsPerMeter * scaleFactor);
            int difference = width % 8;
            if(difference <= 4 && difference != 0)
                width -= difference;
            else
                width += 8 - difference;
            System.out.println(difference + "I AM THE DIFFERENCE");
            System.out.println(width + "I AM THE WIDTH");
            bitmap = Bitmap.createScaledBitmap(bitmap,
                                               width,
                                               (int) (width * 1.375),
                                               false);
            System.out.println(bitmap.getWidth() + " x " + bitmap.getHeight() + "\n\n\n\n\n");
        }*/

        return bitmap;
    }

    public void setupGameObject(String blockType, String bitmapName, WorldLocation worldLocation) {
        this.blockType = blockType;
        this.bitmapName = bitmapName;
        this.worldLocation = worldLocation;

        objectHitbox.left = worldLocation.x;
        objectHitbox.top = worldLocation.y;
        objectHitbox.right = worldLocation.x + worldLocation.width;
        objectHitbox.bottom = worldLocation.y + worldLocation.height;
    }

    public void setupGameObject(String blockType, String bitmapName, WorldLocation worldLocation, int facing, boolean movable, boolean active, boolean visible) {
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
        worldLocation.x += xVelocity / fps;
        worldLocation.y += yVelocity / fps;
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

    public String getBlockType() {
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
