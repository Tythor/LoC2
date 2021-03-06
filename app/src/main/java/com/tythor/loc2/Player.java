package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.RectF;

public class Player extends GameObject {
    final int X_VELOCITY = 120;
    final int X_AXIS = 1;
    final int Y_AXIS = 2;
    // In milliseconds
    final long maxJumpTime = 1000;
    float upGravity = 180f; // 10.6f
    float downGravity;
    boolean pressingRight = false;
    boolean pressingLeft = false;
    final WorldLocation spawnLocation;
    RectF leftHitbox;
    RectF topHitbox;
    RectF rightHitbox;
    RectF bottomHitbox;
    boolean falling;
    int count;
    boolean jumping = false;
    long timeOfJump;
    float locationOfJump;

    float addedGravity;

    Player(WorldLocation playerLocation) {
        // Players are 0.40 x 0.55
        final float WIDTH = 8;
        final float HEIGHT = 11;

        spawnLocation = new WorldLocation(playerLocation.x, playerLocation.y);

        playerLocation.z = 2;
        playerLocation.width = WIDTH;
        playerLocation.height = HEIGHT;

        setupGameObject("p", "playerleft1", playerLocation, LEFT, true, true, true);

        // Hitboxes
        leftHitbox = new RectF();
        topHitbox = new RectF();
        rightHitbox = new RectF();
        bottomHitbox = new RectF();
    }

    @Override
    public void update(int FPS) {
        // Update player's X location and direction
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

        // Update player's Y location
        /*if(jumping) {
            long timeJumping = System.currentTimeMillis() - timeOfJump;
            if(Math.abs(getWorldLocation().y - locationOfJump) < 3.5) {
                setYVelocity(-(upGravity + addedGravity));
            }
            else {
                setYVelocity(upGravity);
                System.out.println("capped");
                System.out.println(addedGravity + " " + timeJumping / 1000.00);
                addedGravity = 0;
                jumping = false;
            }
            addedGravity += 10f;
        }
        else {
            setYVelocity(upGravity);
        }*/
        if(jumping) {
            long timeJumping = System.currentTimeMillis() - timeOfJump;
            if(Math.abs(getWorldLocation().y - locationOfJump) < 75) {
                setYVelocity(-(upGravity + addedGravity));
            }
            else {
                setYVelocity(upGravity);
                /*System.out.println("capped");
                System.out.println(addedGravity + " " + timeJumping / 1000.00);*/
                addedGravity = 0;
                jumping = false;
            }
            addedGravity += .116 * 60;
        }
        else {
            setYVelocity(upGravity);
        }

        move(FPS);
    }

    public void startJump() {
        if(!falling) {
            if(!jumping) {
                jumping = true;
                timeOfJump = System.currentTimeMillis();
                locationOfJump = getWorldLocation().y;
            }
        }
    }

    public void checkForCollisions(GameObject gameObject, boolean setHitbox) {
        count++;
        int collisionLocation = 0;

        RectF objectHitbox = gameObject.objectHitbox;

        if(leftHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom)) {
            this.setWorldLocationX(objectHitbox.right);

            collisionLocation = X_AXIS;
            handleCollisions(gameObject, collisionLocation);

            System.out.println("LEFT");
        }

        if(topHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom)) {
            this.setWorldLocationY(objectHitbox.bottom);

            collisionLocation = Y_AXIS;
            handleCollisions(gameObject, collisionLocation);

            System.out.println("TOP");
        }

        if(rightHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom)) {
            this.setWorldLocationX(objectHitbox.left - getWidth());

            collisionLocation = X_AXIS;
            handleCollisions(gameObject, collisionLocation);

            System.out.println("RIGHT");
        }

        // Feet
        if(bottomHitbox.intersects(objectHitbox.left, objectHitbox.top, objectHitbox.right, objectHitbox.bottom)) {
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
        //setPlayerHitboxes();

        if(setHitbox) {

        }
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
                addedGravity = 0;
                collided = true;
                break;
        }
        if(collided && gameObject.isDeadly()) {
            this.setWorldLocationX(spawnLocation.x);
            this.setWorldLocationY(spawnLocation.y);
        }
    }

    public void setPressingRight(boolean pressingRight) {
        this.pressingRight = pressingRight;
    }

    public void setPressingLeft(boolean pressingLeft) {
        this.pressingLeft = pressingLeft;
    }
}
