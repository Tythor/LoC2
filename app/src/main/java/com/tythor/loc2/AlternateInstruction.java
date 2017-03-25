package com.tythor.loc2;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Tyler on 3/17/2017.
 */

public class AlternateInstruction extends Instruction {
    public ArrayList<PointF> destinationLocations;
    public ArrayList<String> spikeDirections;
    public float time;
    public RectF triggerBounds;
    public GameObject intersectObject;

    AlternateInstruction(ArrayList<PointF> destinationLocations, float time, GameObject intersectObject, RectF triggerBounds) {
        this.destinationLocations = destinationLocations;
        this.time = time;
        this.intersectObject = intersectObject;
        this.triggerBounds = triggerBounds;
    }
    AlternateInstruction(ArrayList<PointF> destinationLocations, ArrayList<String> spikeDirections, float time, GameObject intersectObject, RectF triggerBounds) {
        this.destinationLocations = destinationLocations;
        this.spikeDirections = spikeDirections;
        this.time = time;
        this.intersectObject = intersectObject;
        this.triggerBounds = triggerBounds;
    }

    public static AlternateInstruction createAlternateInstruction(String[] instructions, GameObject gameObject) {
        float time = 0;
        ArrayList<PointF> destinationLocations = new ArrayList<>();
        ArrayList<String> spikeDirections = new ArrayList<>();
        GameObject intersectObject = null;
        RectF triggerBounds = null;

        int currentIndex = 0;
            String[] sPointF;
            // Start at 1 to ignore "alternate"
            for(int i = 1; i < instructions.length; i++) {
                String commaSeparated = instructions[i].split(",")[0];
                if(commaSeparated.contains(";")) {
                     sPointF = commaSeparated.split("; ");
                    destinationLocations.add(new PointF(Float.parseFloat(sPointF[0]),
                                                        Float.parseFloat(sPointF[1])));
                    if(gameObject instanceof Spike) {
                        spikeDirections.add(sPointF[2]);
                    }
                }
                else {
                    currentIndex = i;
                    break;
                }
            }

            time = Float.parseFloat(instructions[currentIndex++]);

            if(instructions[currentIndex].equals("player")) {
                intersectObject = LevelManager.player;
            } else {
                String[] index = instructions[currentIndex].split("; ");
                intersectObject = LevelManager.gameObjects[Integer.parseInt(index[0])][Integer.parseInt(index[1])];
            }

            sPointF = instructions[++currentIndex].split("; ");

            PointF triggerLocation = new PointF(Float.parseFloat(sPointF[0]), Float.parseFloat(sPointF[1]));
            PointF boundLocation = new PointF(triggerLocation.x + Float.parseFloat(sPointF[2]), triggerLocation.y + Float.parseFloat(sPointF[3]));
            triggerBounds = Instruction.createTriggerBounds(triggerLocation, boundLocation);

        System.out.println(Arrays.toString(Arrays.asList(destinationLocations).toArray()) + " destinationLocations");
        System.out.println(Arrays.toString(Arrays.asList(spikeDirections).toArray()) + " spikeDirections");

        return new AlternateInstruction(destinationLocations, spikeDirections, time, intersectObject, triggerBounds);
    }

}
