package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.ArrayList;

public class LevelManager {
    int mapWidth;
    int mapHeight;
    Player player;
    LevelData levelData;
    ArrayList<GameObject> gameObjects;
    ArrayList<Rect> currentButtons;
    Bitmap[] bitmapArray;
    private String level;
    private boolean playing;

    LevelManager(Context context, int pixelsPerMeter, int screenWidth, InputController inputController, String level, WorldLocation playerLocation) {
        this.level = level;

        switch(level) {
            case "LevelOne":
                levelData = new LevelOne();
                break;
        }

        gameObjects = new ArrayList<>();

        // All the bitmaps
        // 0 is nothing; 1, 2 are player; 3-9 are block colors; 10-13 are spike directions
        final int TOTALBITMAPS = 25;
        bitmapArray = new Bitmap[TOTALBITMAPS];

        // Load all GameObjects and Bitmaps
        loadGameObjects(context, pixelsPerMeter, playerLocation);
    }

    private void loadGameObjects(Context context, int pixelsPerMeter, WorldLocation playerLocation) {
        char blockType;

        int currentIndex = 0;

        // Width is the size of the first object in the ArrayList; Height is the size of the ArrayList
        mapWidth = levelData.blocks.get(0).length();
        mapHeight = levelData.blocks.size();

        // Add blocks from levelData
        for(int y = 0; y < levelData.blocks.size(); y++) {
            for(int x = 0; x < levelData.blocks.get(y).length(); x++) {
                blockType = levelData.blocks.get(y).charAt(x);

                // Ignore empty blocks
                if(blockType != '.') {
                    switch(blockType) {
                        // Add player
                        case 'p':
                            gameObjects.add(new Player(context, playerLocation, pixelsPerMeter));

                            player = (Player) gameObjects.get(currentIndex);
                            break;

                        // Add block
                        case '1':
                            gameObjects.add(new Block(x, y, blockType));
                            break;

                        case 'l':
                            gameObjects.add(new Spike(x, y, blockType));
                            break;

                        case 'u':
                            gameObjects.add(new Spike(x, y, blockType));
                            break;

                        case 'r':
                            gameObjects.add(new Spike(x, y, blockType));
                            break;

                        case 'd':
                            gameObjects.add(new Spike(x, y, blockType));
                            break;
                    }

                    // Check if bitmap has already been prepared
                    if(bitmapArray[getBitmapIndex(blockType)] == null) {
                        // Add block's bitmap to bitmapArray
                        bitmapArray[getBitmapIndex(blockType)] = gameObjects.get(currentIndex).prepareBitmap(
                                context,
                                gameObjects.get(currentIndex).getBitmapName(),
                                pixelsPerMeter);
                    }

                    currentIndex++;
                }
            }
        }
    }

    public int getBitmapIndex(char blockType) {
        int index = 0;

        switch(blockType) {
            case '.':
                index = 0;
                break;

            case 'p':
                index = 1;
                break;

            case '1':
                index = 3;
                break;

            case 'l':
                index = 10;
                break;

            case 'u':
                index = 11;
                break;

            case 'r':
                index = 12;
                break;

            case 'd':
                index = 13;
                break;
        }

        return index;
    }

    public Bitmap getBitmap(char blockType) {
        int index = 0;

        switch(blockType) {
            case '.':
                index = 0;
                break;

            case 'p':
                index = 1;
                break;

            case '1':
                index = 3;
                break;
        }

        return bitmapArray[index];
    }

    public boolean isPlaying() {
        return playing;
    }
}
