package com.tythor.loc2;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.logging.Level;

/**
 * Created by Tyler on 3/6/2017.
 */

public class MoveInstruction extends Instruction {
    public float moveSpeed;
    public PointF destinationLocation;
    public RectF triggerBounds;
    public GameObject intersectObject;

    MoveInstruction(float moveSpeed, PointF destinationLocation, GameObject intersectObject, RectF triggerBounds) {
        this.moveSpeed = moveSpeed;
        this.destinationLocation = destinationLocation;
        this.intersectObject = intersectObject;
        this.triggerBounds = triggerBounds;
    }

    public static MoveInstruction createMoveInstruction(String[] instructions) {
        // Start at 1 to ignore "move"
        Float moveSpeed = Float.parseFloat(instructions[1]);

        String[] sPointF = instructions[2].split("; ");
        PointF destinationLocation = new PointF(Float.parseFloat(sPointF[0]), Float.parseFloat(sPointF[1]));

        GameObject intersectObject;
        if(instructions[3].equals("player")) {
            intersectObject = LevelManager.player;
        } else {
            String[] index = instructions[3].split("; ");
            intersectObject = LevelManager.gameObjects[Integer.parseInt(index[0])][Integer.parseInt(index[1])];
        }

        sPointF = instructions[4].split("; ");

        PointF triggerLocation = new PointF(Float.parseFloat(sPointF[0]), Float.parseFloat(sPointF[1]));
        PointF boundLocation = new PointF(triggerLocation.x + Float.parseFloat(sPointF[2]), triggerLocation.y + Float.parseFloat(sPointF[3]));
        RectF triggerBounds = Instruction.createTriggerBounds(triggerLocation, boundLocation);

        return new MoveInstruction(moveSpeed, destinationLocation, intersectObject, triggerBounds);
    }

}
