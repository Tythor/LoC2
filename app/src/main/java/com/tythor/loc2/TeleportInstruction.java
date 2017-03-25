package com.tythor.loc2;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Tyler on 3/6/2017.
 */

public class TeleportInstruction extends Instruction {
    public PointF destinationLocation;
    public RectF triggerBounds;
    public GameObject intersectObject;

    TeleportInstruction(PointF destinationLocation, GameObject intersectObject, RectF triggerBounds) {
        this.destinationLocation = destinationLocation;
        this.intersectObject = intersectObject;
        this.triggerBounds = triggerBounds;
    }

    public static TeleportInstruction createTeleportInstruction(String[] instructions) {
        // Start at 1 to ignore "teleport"
        String[] sPointF = instructions[1].split("; ");
        PointF destinationLocation = new PointF(Float.parseFloat(sPointF[0]), Float.parseFloat(sPointF[1]));

        GameObject intersectObject;
        if(instructions[2].equals("player")) {
            intersectObject = LevelManager.player;
        } else {
            String[] index = instructions[2].split("; ");
            intersectObject = LevelManager.gameObjects[Integer.parseInt(index[0])][Integer.parseInt(index[1])];
        }

        sPointF = instructions[3].split("; ");

        PointF triggerLocation = new PointF(Float.parseFloat(sPointF[0]), Float.parseFloat(sPointF[1]));
        PointF boundLocation = new PointF(triggerLocation.x + Float.parseFloat(sPointF[2]), triggerLocation.y + Float.parseFloat(sPointF[3]));
        RectF triggerBounds = Instruction.createTriggerBounds(triggerLocation, boundLocation);

        return new TeleportInstruction(destinationLocation, intersectObject, triggerBounds);
    }
}
