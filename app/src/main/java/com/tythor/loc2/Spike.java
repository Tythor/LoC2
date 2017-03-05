package com.tythor.loc2;

// Created by Tythor on 9/3/2016

import android.graphics.RectF;

public class Spike extends GameObject {
    private RectF triggerBounds;

    private boolean hasInstructions = false;
    private boolean hasMovementInstructions = false;

    private float moveSpeed;

    private WorldLocation moveToLocation;

    private WorldLocation triggerLocation;

    private WorldLocation boundLocation;

    private boolean hasIntersected = false;

    Spike(float startLocationX, float startLocationY, String spikeType) {
        // Blocks are 1 x 1
        final float WIDTH = 20;
        final float HEIGHT = 20;
        WorldLocation worldLocation = new WorldLocation(startLocationX,
                                                        startLocationY,
                                                        0,
                                                        WIDTH,
                                                        HEIGHT);
        setDeadly(true);
        String bitmapName = "";
        // Dummy blockType
        String blockType = " ";

        switch(spikeType) {
            case "3":
                bitmapName = "spikeleft";
                blockType = "l";
                break;

            case "1":
                bitmapName = "spikeup";
                blockType = "u";
                break;

            case "4":
                bitmapName = "spikeright";
                blockType = "r";
                break;

            case "2":
                bitmapName = "spikedown";
                blockType = "d";
                break;
        }

        setupGameObject(blockType, bitmapName, worldLocation);
    }

    public void applyInstructions() {
        setYVelocity(moveSpeed);

        triggerBounds = new RectF(triggerLocation.x, triggerLocation.y, boundLocation.x, boundLocation.y);

        // Verify that triggerBounds is correct
        if(triggerLocation.x > boundLocation.x) {
            triggerBounds.left = boundLocation.x;
            triggerBounds.top = triggerLocation.x;
        }
        if(triggerLocation.y > boundLocation.y) {
            triggerBounds.top = boundLocation.y;
            triggerBounds.bottom = triggerLocation.y;
        }
        if(getWorldLocation().x > moveToLocation.x || getWorldLocation().y > moveToLocation.y)
            moveSpeed *= -1;
        setYVelocity(moveSpeed * 40);
    }

    @Override
    public void update(int FPS) {
        if(hasInstructions) {
            if(hasMovementInstructions) {
                if(RectF.intersects(GameView.levelManager.player.objectHitbox, triggerBounds) || triggerBounds.contains(GameView.levelManager.player.getWorldLocation().x, GameView.levelManager.player.getWorldLocation().y))
                    hasIntersected = true;
                if(hasIntersected)
                        moveTo(ViewController.FPS, moveToLocation);
            }
        }
    }

    public boolean hasInstructions() {
        return hasInstructions;
    }

    public void setHasInstructions(boolean hasInstructions) {
        this.hasInstructions = hasInstructions;
    }

    public boolean hasMovementInstructions() {
        return hasMovementInstructions;
    }

    public void setHasMovementInstructions(boolean hasMovementInstructions) {
        this.hasMovementInstructions = hasMovementInstructions;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public WorldLocation getMoveToLocation() {
        return moveToLocation;
    }

    public void setMoveToLocation(WorldLocation moveToLocation) {
        this.moveToLocation = moveToLocation;
    }

    public WorldLocation getTriggerLocation() {
        return triggerLocation;
    }

    public void setTriggerLocation(WorldLocation triggerLocation) {
        this.triggerLocation = triggerLocation;
    }

    public WorldLocation getBoundLocation() {
        return boundLocation;
    }

    public void setBoundLocation(WorldLocation boundLocation) {
        this.boundLocation = boundLocation;
    }
}
