package com.tythor.loc2;

// Created by Tythor on 9/3/2016

import android.graphics.Point;
import android.graphics.PointF;

import static com.tythor.loc2.GameView.levelManager;
import static com.tythor.loc2.ViewController.paint;

public class Spike extends GameObject {
    public PointF A;
    public PointF B;
    public PointF C;
    
    public PointF slope;
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
        
        switch(spikeType) {
            case "1":
                A = new PointF(startLocationX, startLocationY + HEIGHT);
                B = new PointF(startLocationX + WIDTH / 2, startLocationY);
                C = new PointF(startLocationX + WIDTH, startLocationY + HEIGHT);
                break;
            case "2":
                A = new PointF(startLocationX + WIDTH, startLocationY);
                B = new PointF(startLocationX + WIDTH / 2, startLocationY + HEIGHT);
                C = new PointF(startLocationX, startLocationY);
                break;
            case "3":
                A = new PointF(startLocationX + WIDTH, startLocationY + HEIGHT);
                B = new PointF(startLocationX, startLocationY + HEIGHT / 2);
                C = new PointF(startLocationX + WIDTH, startLocationY);
                break;
            case "4":
                A = new PointF(startLocationX, startLocationY);
                B = new PointF(startLocationX + WIDTH, startLocationY + HEIGHT / 2);
                C = new PointF(startLocationX, startLocationY + HEIGHT);
                break;
        }
    }

    @Override
    public void update(int FPS) {
        executeInstruction();

        Point[] slope = new Point[3];
        switch(getBlockType()) {
            case "1":
                A = new PointF(getWorldLocation().x, getWorldLocation().y + getWorldLocation().height);
                B = new PointF(getWorldLocation().x + getWorldLocation().width / 2, getWorldLocation().y);
                C = new PointF(getWorldLocation().x + getWorldLocation().width, getWorldLocation().y + getWorldLocation().height);

                slope[0] = new Point(1, 2);
                slope[1] = new Point(1, -2);
                slope[2] = new Point(2, 0);
                break;
            case "2":
                A = new PointF(getWorldLocation().x + getWorldLocation().width, getWorldLocation().y);
                B = new PointF(getWorldLocation().x + getWorldLocation().width / 2, getWorldLocation().y + getWorldLocation().height);
                C = new PointF(getWorldLocation().x, getWorldLocation().y);

                slope[0] = new Point(-1, -2);
                slope[1] = new Point(-1, 2);
                slope[2] = new Point(-2, 0);
                break;
            case "3":
                A = new PointF(getWorldLocation().x + getWorldLocation().width, getWorldLocation().y + getWorldLocation().height);
                B = new PointF(getWorldLocation().x, getWorldLocation().y + getWorldLocation().height / 2);
                C = new PointF(getWorldLocation().x + getWorldLocation().width, getWorldLocation().y);

                slope[0] = new Point(-2, 1);
                slope[1] = new Point(2, 1);
                slope[2] = new Point(0, 2);
                break;
            case "4":
                A = new PointF(getWorldLocation().x, getWorldLocation().y);
                B = new PointF(getWorldLocation().x + getWorldLocation().width, getWorldLocation().y + getWorldLocation().height / 2);
                C = new PointF(getWorldLocation().x, getWorldLocation().y + getWorldLocation().height);

                slope[0] = new Point(2, -1);
                slope[1] = new Point(-2, -1);
                slope[2] = new Point(0, -2);
                break;
        }

        Line ABLine = new Line(A, B, slope[0].x, slope[0].y);
        Line BCLine = new Line(B, C, slope[1].x, slope[1].y);
        Line ACLine = new Line(A, C, slope[2].x, slope[2].y);

        if(ABLine.intersects(levelManager.player.objectHitbox) || BCLine.intersects(
                levelManager.player.objectHitbox) || ACLine.intersects(
                levelManager.player.objectHitbox)) {
            // Do intersection/die
            //System.out.println("Spike intersection");
        }
    }
}
