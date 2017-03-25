package com.tythor.loc2;

import android.graphics.Bitmap;

/**
 * Created by Tyler on 10/14/2016.
 */

public class Animation {
    LevelManager levelManager = GameView.levelManager;

    Bitmap playerLeft1;
    Bitmap playerLeft2;
    Bitmap playerRight1;
    Bitmap playerRight2;

    long startTime = System.currentTimeMillis();

    public Animation() {
        playerLeft1 = levelManager.player.prepareBitmap("playerleft1", 1);
        playerLeft2 = levelManager.player.prepareBitmap("playerleft2", 1);
        playerRight1 = levelManager.player.prepareBitmap("playerright1", 1);
        playerRight2 = levelManager.player.prepareBitmap("playerright2", 1);
    }

    public void animatePlayer(GameObject gameObject) {
        // Animate the player
        if((gameObject.equals(levelManager.player) && levelManager.player.getXVelocity() != 0) || (levelManager.player.pressingLeft || levelManager.player.pressingRight)) {
            if(System.currentTimeMillis() - startTime >= 80) {
                startTime = System.currentTimeMillis();

                if(gameObject.getBitmapName().equals("playerleft1")) {
                    levelManager.bitmapArray[levelManager.getBitmapIndex("p")] = playerLeft2;
                    gameObject.prepareBitmapName("playerleft2");
                }
                else if(gameObject.getBitmapName().equals("playerleft2")) {
                    levelManager.bitmapArray[levelManager.getBitmapIndex("p")] = playerLeft1;
                    gameObject.prepareBitmapName("playerleft1");
                }
                if(gameObject.getBitmapName().equals("playerright1")) {
                    levelManager.bitmapArray[levelManager.getBitmapIndex("p")] = playerRight2;
                    gameObject.prepareBitmapName("playerright2");
                }
                else if(gameObject.getBitmapName().equals("playerright2")) {
                    levelManager.bitmapArray[levelManager.getBitmapIndex("p")] = playerRight1;
                    gameObject.prepareBitmapName("playerright1");
                }
            }
        }

        // Change facing direction and set idle player to idle bitmap
        if(gameObject.equals(levelManager.player)) {
            if(gameObject.getFacing() == 1 && (gameObject.getBitmapName().equals("playerright1") || gameObject.getBitmapName().equals(
                    "playerright2") || (levelManager.player.pressingLeft == false && levelManager.player.pressingRight == false))) {
                gameObject.prepareBitmapName("playerleft1");
                levelManager.bitmapArray[levelManager.getBitmapIndex("p")] = playerLeft1;
            }
            else if(gameObject.getFacing() == 2 && (gameObject.getBitmapName().equals("playerleft1") || gameObject.getBitmapName().equals(
                    "playerleft2") || (levelManager.player.pressingLeft == false && levelManager.player.pressingRight == false))) {
                levelManager.bitmapArray[levelManager.getBitmapIndex("p")] = playerRight1;
                gameObject.prepareBitmapName("playerright1");
            }
        }
    }
}
