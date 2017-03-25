package com.tythor.loc2;

// Created by Tythor on 9/3/2016

public class Spike extends GameObject {
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
        String bitmapName = prepareBitmapName(spikeType);

        setupGameObject(spikeType, bitmapName, worldLocation);
    }

    @Override
    public void update(int FPS) {
        executeInstruction();
    }
}
