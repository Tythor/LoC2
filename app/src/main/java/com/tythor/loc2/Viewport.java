package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.graphics.RectF;

public class Viewport {
    // Determine size of the viewport; Scale to 840 x 480
    public int METERSTOSHOWX = 30;
    public int METERSTOSHOWY = 30;

    private WorldLocation viewportLocation;
    private RectF screenLocation;
    public int pixelsPerMeter;
    private int screenWidth;
    private int screenHeight;
    public int screenCenterX;
    public int screenCenterY;

    // Debugging
    private int numberOfClippedObjects;

    Viewport(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Divide screenResolution by 2 to determine screenCenter
        screenCenterX = screenWidth / 2;
        screenCenterY = screenHeight / 2;

        // Set smaller than viewport
        pixelsPerMeter = screenWidth / (METERSTOSHOWX - 2);

        screenLocation = new RectF();
        viewportLocation = new WorldLocation();
    }

    public RectF worldToScreen(WorldLocation worldLocation) {
        float left = (screenCenterX - ((viewportLocation.x - worldLocation.x) * pixelsPerMeter));

        float top = (screenCenterY - ((viewportLocation.y - worldLocation.y) * pixelsPerMeter));

        float right = (left + worldLocation.width * pixelsPerMeter);

        float bottom = (top + worldLocation.height * pixelsPerMeter);

        screenLocation.set(left, top, right, bottom);

        return screenLocation;
    }

    public boolean renderObject(WorldLocation worldLocation) {
        boolean rendered = false;

        // Determine if the object is in the viewport
        if(worldLocation.x - worldLocation.width < viewportLocation.x + METERSTOSHOWX / 2) {
            if(worldLocation.x + worldLocation.width > viewportLocation.x - METERSTOSHOWX / 2) {
                if(worldLocation.y - worldLocation.height < viewportLocation.y + METERSTOSHOWY / 2) {
                    if(worldLocation.y + worldLocation.height > viewportLocation.y - METERSTOSHOWY / 2) {
                        rendered = true;
                    }
                }
            }
        }

        // Debugging
        if(!rendered) {
            numberOfClippedObjects++;
        }

        return rendered;
    }

    public void setViewportLocation(WorldLocation viewportLocation) {
        this.viewportLocation = viewportLocation;
    }

    public WorldLocation getViewportLocation() {
        return viewportLocation;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getPixelsPerMeter() {
        return pixelsPerMeter;
    }

    public int getNumberOfClippedObjects() {
        return numberOfClippedObjects;
    }

    public void resetNumberOfClippedObjects() {
        numberOfClippedObjects = 0;
    }
}
