package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.RectF;

public class Player extends GameObject {
    final int X_VELOCITY = 6;
    int gravity = 8;

    final int X_AXIS = 1;
    final int Y_AXIS = 2;

    boolean pressingRight = false;
    boolean pressingLeft = false;

    RectF leftHitbox;
    RectF topHitbox;
    RectF rightHitbox;
    RectF bottomHitbox;

    boolean falling;
    int count;
    boolean jumping;
    long timeOfJump;
    // In milliseconds
    final long maxJumpTime = 1000;

    Player(Context context, WorldLocation playerLocation, int pixelsPerMeter) {
        // Players are 0.5 x 0.75
        final float WIDTH = 0.40f;
        final float HEIGHT = 0.55f;

        playerLocation.z = 0;
        playerLocation.width = WIDTH;
        playerLocation.height = HEIGHT;

        setupGameObject('p', "playerleft1", playerLocation, LEFT, true, true, true);

        // Hitboxes
        leftHitbox = new RectF();
        topHitbox = new RectF();
        rightHitbox = new RectF();
        bottomHitbox = new RectF();
    }

    @Override
    public void update(long fps) {
        // Update player's location and direction
        if(pressingRight) {
            setXVelocity(X_VELOCITY);
            setFacing(RIGHT);

        }
        else if(pressingLeft) {
            setXVelocity(-X_VELOCITY);
            setFacing(LEFT);
        }
        else {
            setXVelocity(0);
        }

        if(jumping) {
            long timeJumping = System.currentTimeMillis() - timeOfJump;
            if(timeJumping < maxJumpTime) {
                if(timeJumping < maxJumpTime / 2) {
                    setYVelocity(-gravity);
                }
                else if(timeJumping > maxJumpTime / 2) {
                    setYVelocity(gravity);
                }
            }
            else {
                jumping = false;
            }
        }
        else {
            setYVelocity(gravity);
        }

        move(fps);

        WorldLocation playerLocation = getWorldLocation();

        // Set hitboxes
        leftHitbox.left = playerLocation.x;
        leftHitbox.top = playerLocation.y + 0.15f;
        leftHitbox.right = leftHitbox.left;
        leftHitbox.bottom = leftHitbox.top;

        topHitbox.left = playerLocation.x + 0.175f;
        topHitbox.top = playerLocation.y;
        topHitbox.right = topHitbox.left;
        topHitbox.bottom = topHitbox.top;

        rightHitbox.left = playerLocation.x + getWidth() - 0.05f;
        rightHitbox.top = playerLocation.y + 0.15f;
        rightHitbox.right = rightHitbox.left;
        rightHitbox.bottom = rightHitbox.top;

        bottomHitbox.left = playerLocation.x + 0.175f;
        bottomHitbox.top = playerLocation.y + getHeight() - 0.05f;
        bottomHitbox.right = bottomHitbox.left;
        bottomHitbox.bottom = bottomHitbox.top;
    }

    public void checkForCollisions(GameObject gameObject) {
        count++;
        int collisionLocation = 0;

        RectF objectHitbox = gameObject.objectHitbox;

        if(leftHitbox.intersect(objectHitbox)) {
            this.setWorldLocationX(objectHitbox.right);

            collisionLocation = X_AXIS;
            handleCollisions(gameObject, collisionLocation);

            System.out.println("LEFT");
        }

        if(topHitbox.intersect(objectHitbox)) {
            this.setWorldLocationY(objectHitbox.bottom);

            collisionLocation = Y_AXIS;
            handleCollisions(gameObject, collisionLocation);

            System.out.println("TOP");
        }

        if(rightHitbox.intersect(objectHitbox)) {
            this.setWorldLocationX(objectHitbox.left - getWidth());

            collisionLocation = X_AXIS;
            handleCollisions(gameObject, collisionLocation);

            System.out.println("RIGHT");

        }

        // Feet
        if(bottomHitbox.intersect(objectHitbox)) {
            this.setWorldLocationY(objectHitbox.top - getHeight());

            collisionLocation = Y_AXIS;
            handleCollisions(gameObject, collisionLocation);

            if(count % 100 == 0) {
                System.out.println("BOTTOM");
            }
        }
        else {
            if(count % 10000 == 0) {
                System.out.println("NOT BOTTOM");
            }
        }

        handleCollisions(gameObject, collisionLocation);
    }

    private void handleCollisions(GameObject gameObject, int collisionLocation) {
        boolean collided = false;
        switch(collisionLocation) {
            case 0:
                canPassX = true;
                canPassY = true;
                break;

            case 1:
                setXVelocity(0);
                canPassX = false;
                collided = true;
                break;

            case 2:
                setYVelocity(0);
                canPassY = false;
                jumping = false;
                collided = true;
                break;
        }
        if(collided && gameObject.isDeadly()) {
            this.setWorldLocationX(1);
            this.setWorldLocationY(15);
        }
    }

    public void setPressingRight(boolean pressingRight) {
        this.pressingRight = pressingRight;
    }

    public void setPressingLeft(boolean pressingLeft) {
        this.pressingLeft = pressingLeft;
    }

    public void startJump() {
        if(!falling) {
            if(!jumping) {
                jumping = true;
                timeOfJump = System.currentTimeMillis();
            }
        }
    }
}