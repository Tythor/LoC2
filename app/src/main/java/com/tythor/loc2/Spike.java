package com.tythor.loc2;

// Created by Tythor on 9/3/2016

public class Spike extends GameObject {
    Spike(int startLocationX, int startLocationY, char blockType) {
        // Blocks are 1 x 1
        final float WIDTH = 1;
        final float HEIGHT = 1;
        WorldLocation worldLocation = new WorldLocation(startLocationX, startLocationY, 0, WIDTH, HEIGHT);
        setDeadly(true);
        String bitmapName = "";

        switch(blockType) {
            case 'l':
                bitmapName = "spikeleft";
                break;

            case 'u':
                bitmapName = "spikeup";
                break;

            case 'r':
                bitmapName = "spikeright";
                break;

            case 'd':
                bitmapName = "spikedown";
                break;
        }

        setupGameObject(blockType, bitmapName, worldLocation);
    }

    @Override
    public void update(long fps) {

    }
}
