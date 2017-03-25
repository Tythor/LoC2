package com.tythor.loc2;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Tyler on 3/6/2017.
 */

public abstract class Instruction {
    public static RectF createTriggerBounds(PointF triggerLocation, PointF boundLocation) {
        RectF triggerBounds = new RectF(triggerLocation.x, triggerLocation.y, boundLocation.x, boundLocation.y);

        // Verify that triggerBounds are correct
        if(triggerLocation.x > boundLocation.x) {
            triggerBounds.left = boundLocation.x;
            triggerBounds.top = triggerLocation.x;
        }
        if(triggerLocation.y > boundLocation.y) {
            triggerBounds.top = boundLocation.y;
            triggerBounds.bottom = triggerLocation.y;
        }
        return triggerBounds;
    }
}
