package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.graphics.RectF;

public class Viewport {
    // Determine size of the viewport; Scale to 840 x 480 28 x 30
    public int METERSTOSHOWX = 28;
    public int METERSTOSHOWY = 30;

    private WorldLocation viewportLocation;
    private RectF screenLocation;
    public int pixelsPerMeter;
    private int screenWidth;
    private int screenHeight;
    public int screenCenterX;
    public int screenCenterY;

    // Debugging
    public int numberOfRenderedObjects;
    public int numberOfObjects;

    Viewport(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Divide screenResolution by 2 to determine screenCenter
        screenCenterX = screenWidth / 2;
        screenCenterY = screenHeight / 2;

        pixelsPerMeter = screenWidth / (METERSTOSHOWX);
        System.out.println(screenWidth + "/" + (METERSTOSHOWX));

        screenLocation = new RectF();
        viewportLocation = new WorldLocation();
    }

    public RectF worldToScreen(WorldLocation worldLocation) {
        float left = (screenCenterX - ((viewportLocation.x - worldLocation.x) * pixelsPerMeter / worldLocation.width));

        float top = (screenCenterY - ((viewportLocation.y - worldLocation.y) * pixelsPerMeter / worldLocation.height));

        float right = (left + worldLocation.width * pixelsPerMeter / worldLocation.width);

        float bottom = (top + worldLocation.height * pixelsPerMeter / worldLocation.height);

        screenLocation.set(left, top, right, bottom);

        return screenLocation;
    }

    public boolean renderObject(WorldLocation worldLocation) {
        boolean rendered = false;

        // Determine if the object is in the viewport by checking if left/right is within width and top/bottom is within height
        if((worldToScreen(worldLocation).left > 0 && worldToScreen(worldLocation).left < screenWidth) || (worldToScreen(
                worldLocation).right > 0 && worldToScreen(worldLocation).right < screenWidth)) {
            if(worldToScreen(worldLocation).top > 0 && worldToScreen(worldLocation).top < screenHeight || (worldToScreen(
                    worldLocation).bottom > 0 && worldToScreen(worldLocation).bottom < screenHeight)) {
                rendered = true;
            }
        }

        // Debugging
        if(rendered) {
            numberOfRenderedObjects++;
        }
        numberOfObjects++;

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

    public int getNumberOfRenderedObjects() {
        return numberOfRenderedObjects;
    }

    public void resetNumberOfClippedObjects() {
        numberOfRenderedObjects = 0;
        numberOfObjects = 0;
    }
}
