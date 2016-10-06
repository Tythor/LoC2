package com.tythor.loc2;

// Created by Tythor on 8/25/2016

public class Block extends GameObject {
    Block(int startLocationX, int startLocationY, char blockType) {
        // Blocks are 1 x 1
        final float WIDTH = 1;
        final float HEIGHT = 1;
        WorldLocation worldLocation = new WorldLocation(startLocationX, startLocationY, 0, WIDTH, HEIGHT);

        setupGameObject(blockType, "greenblock", worldLocation);
    }

    @Override
    public void update(long fps) {

    }
}
