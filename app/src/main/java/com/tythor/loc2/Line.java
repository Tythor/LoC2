package com.tythor.loc2;

import android.graphics.PointF;
import android.graphics.RectF;

import static com.tythor.loc2.GameView.viewport;
import static com.tythor.loc2.ViewController.canvas;
import static com.tythor.loc2.ViewController.paint;

/**
 * Created by Tyler on 4/17/2017.
 */

public class Line {
    PointF A;
    PointF B;
    int slopeX;
    int slopeY;

    PointF intersectionPoint;

    public Line(PointF A, PointF B, int slopeX, int slopeY) {
        this.A = A;
        this.B = B;
        this.slopeX = slopeX;
        this.slopeY = slopeY;
    }

    public boolean intersects(RectF intersectObject) {
        int steps = 0;
        boolean intersected = false;

        float Cx = A.x + steps * slopeX;
        // Because the Y-axis is flipped, each step is subtracted
        float Cy = A.y - steps * slopeY;
        while(((Cx < B.x && slopeX > 0) || (Cx > B.x && slopeX < 0)) ||
                ((Cy > B.y && slopeY > 0) || (Cy < B.y && slopeY < 0))) {
            Cx = A.x + steps * slopeX;
            // Because the Y-axis is flipped, each step is subtracted
            Cy = A.y - steps * slopeY;
            //System.out.println(Cx + ", " + Cy + "\n" + intersectObject.left + ", " + intersectObject.top);
            if(intersectObject.contains(Cx, Cy)) {
                intersectionPoint = new PointF(Cx, Cy);
                canvas.drawCircle(viewport.pointToScreen(intersectionPoint).x, viewport.pointToScreen(intersectionPoint).y, 15, paint);
                intersected = true;
            }
            steps++;
        }
        return intersected;
    }

    // For debugging
    public void draw() {
        int steps = 0;
        float Cx = A.x + steps * slopeX;
        // Because the Y-axis is flipped, each step is subtracted
        float Cy = A.y - steps * slopeY;
        while(((Cx < B.x && slopeX > 0) || (Cx > B.x && slopeX < 0)) ||
                ((Cy > B.y && slopeY > 0) || (Cy < B.y && slopeY < 0))) {
            Cx = A.x + steps * slopeX;
            // Because the Y-axis is flipped, each step is subtracted
            Cy = A.y - steps * slopeY;
            canvas.drawPoint(viewport.pointToScreen(new PointF(Cx, Cy)).x,
                             viewport.pointToScreen(new PointF(Cx, Cy)).y,
                             paint);
            steps++;
        }
    }
}
