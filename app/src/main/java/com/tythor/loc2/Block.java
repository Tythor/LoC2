package com.tythor.loc2;

// Created by Tythor on 8/25/2016

public class Block extends GameObject {
    Block(float startLocationX, float startLocationY, String blockType) {
        // Blocks are 1 x 1
        final float WIDTH = 20;
        final float HEIGHT = 20;
        WorldLocation worldLocation = new WorldLocation(startLocationX, startLocationY, 1, WIDTH, HEIGHT);
        String bitmapName = "";

        switch(blockType) {
            case "B":
                bitmapName = "blueblock";
                break;

            case "G":
                bitmapName = "greenblock";
                break;

            case "O":
                bitmapName = "orangeblock";
                break;

            case "Pi":
                bitmapName = "pinkblock";
                break;

            case "Pu":
                bitmapName = "purpleblock";
                break;
            case "R":
                bitmapName = "redblock";
                break;
        }

        setupGameObject(blockType, bitmapName, worldLocation);
    }

    @Override
    public void update(long fps) {

    }
}