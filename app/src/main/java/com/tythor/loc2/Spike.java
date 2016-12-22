package com.tythor.loc2;

// Created by Tythor on 9/3/2016

public class Spike extends GameObject {
    Spike(float startLocationX, float startLocationY, String spikeType) {
        // Blocks are 1 x 1
        final float WIDTH = 20;
        final float HEIGHT = 20;
        WorldLocation worldLocation = new WorldLocation(startLocationX, startLocationY, 0, WIDTH, HEIGHT);
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

    @Override
    public void update(long fps) {

    }
}
