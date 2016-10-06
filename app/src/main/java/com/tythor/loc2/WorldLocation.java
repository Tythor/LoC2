package com.tythor.loc2;

// Created by Tythor on 8/25/2016

public class WorldLocation {
    // Location
    float x;
    float y;
    int z;

    // Size; Blocks/Players
    float width;
    float height;

    WorldLocation() {

    }

    WorldLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }
    WorldLocation(float x, float y, int z, float width, float height) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
    }
}
