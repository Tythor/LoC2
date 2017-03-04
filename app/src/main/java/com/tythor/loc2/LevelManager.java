package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LevelManager {
    Context context = GameView.context;

    final int TOTALBITMAPS = 25;

    Player player;
    // ArrayList<GameObject> gameObjects;
    ArrayList<String> blocks = new ArrayList<>();
    ArrayList<String> spikes = new ArrayList<>();
    GameObject[][] gameObjects;
    ArrayList<Rect> currentButtons;
    Bitmap[] bitmapArray;
    private String levelName;
    private boolean playing;

    boolean blocksExist = false;
    boolean spikesExist = false;
    Point point;

    LevelManager(String levelName) {
        this.levelName = levelName;

        switch(levelName) {
        }

        WorldLocation spawnLocation = parseLevel(levelName);

        bitmapArray = new Bitmap[TOTALBITMAPS];

        // Load all GameObjects and Bitmaps
        loadGameObjects(spawnLocation);
    }

    private WorldLocation parseLevel(String levelName) {
        WorldLocation spawnLocation = new WorldLocation(0, 0);
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open("levels/" + levelName + ".txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            boolean areBlocks = false;
            boolean areSpikes = false;

            while((line = bufferedReader.readLine()) != null) {
                if(line.contains("size")) {
                    String[] size = line.split(", ");
                    gameObjects = new GameObject[Integer.parseInt(size[1]) / 20 + 1][Integer.parseInt(
                            size[2]) / 20 + 1];
                }
                if(line.contains("spawn")) {
                    String[] spawn = line.split(", ");
                    spawnLocation = new WorldLocation(Integer.parseInt(spawn[1]), Integer.parseInt(spawn[2]));
                    System.out.println(Integer.parseInt(spawn[1]) + " spawn " + Integer.parseInt(spawn[2]));
                    point = new Point(Integer.parseInt(spawn[1]), Integer.parseInt(spawn[2]));
                }

                if(areBlocks) {
                    if(line.startsWith("#")) {
                        areBlocks = false;
                    }
                    else {
                        blocks.add(line);
                    }
                }
                if(areSpikes) {
                    if(line.startsWith("#")) {
                        areSpikes = false;
                    }
                    else {
                        spikes.add(line);
                    }
                }

                if(line.contains("#blocks")) {
                    blocksExist = true;
                    areBlocks = true;
                }
                if(line.contains("#spikes")) {
                    spikesExist = true;
                    areSpikes = true;
                }
            }

            bufferedReader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return spawnLocation;
    }

    private void loadGameObjects(WorldLocation spawnLocation) {

        player = new Player(spawnLocation);

        if(spikesExist) {
            for(int i = 0; i < spikes.size(); i++) {
                String[] line = spikes.get(i).split(", ");
                String[] index = line[0].split("; ");
                gameObjects[Integer.parseInt(index[0])][Integer.parseInt(index[1])] = getSpikeType(
                        line);
            }
        }

        if(blocksExist) {
            for(int i = 0; i < blocks.size(); i++) {
                String[] line = blocks.get(i).split(", ");
                String[] index = line[0].split("; ");
                gameObjects[Integer.parseInt(index[0])][Integer.parseInt(index[1])] = getBlockType(
                        line);
            }
        }

        boolean set = false;

        for(int i = 0; i < gameObjects.length; i++) {
            for(int j = 0; j < gameObjects[0].length; j++) {
                if(gameObjects[i][j] == null) {
                    gameObjects[i][j] = player;
                    set = true;
                    break;
                }
            }
            if(set)
                break;
        }

        String blockType;

        // Add blocks from levelData
        for(int i = 0; i < gameObjects.length; i++) {
            for(int j = 0; j < gameObjects[i].length; j++) {
                if(gameObjects[i][j] != null) {
                    blockType = gameObjects[i][j].getBlockType();

                    // Check if bitmap has already been prepared
                    if(bitmapArray[getBitmapIndex(blockType)] == null) {
                        // Add block's bitmap to bitmapArray
                        bitmapArray[getBitmapIndex(blockType)] = gameObjects[i][j].prepareBitmap(
                                gameObjects[i][j].getBitmapName(), 1);
                    }
                }
            }
        }
    }

    public void refreshBitmaps() {
        String blockType;
        Bitmap[] mockBitmapArray = new Bitmap[TOTALBITMAPS];

        // Add blocks from levelData
        for(int i = 0; i < gameObjects.length; i++) {
            for(int j = 0; j < gameObjects[i].length; j++) {
                if(gameObjects[i][j] != null) {
                    blockType = gameObjects[i][j].getBlockType();

                    // Check if bitmap has already been prepared
                    if(mockBitmapArray[getBitmapIndex(blockType)] == null) {
                        // Add block's bitmap to bitmapArray
                        bitmapArray[getBitmapIndex(blockType)] = gameObjects[i][j].prepareBitmap(
                                gameObjects[i][j].getBitmapName(), 1);

                        mockBitmapArray[getBitmapIndex(blockType)] = bitmapArray[getBitmapIndex(blockType)];
                    }
                }
            }
        }
    }

    public int getBitmapIndex(String blockType) {
        int index = 0;

        switch(blockType) {
            case ".":
                index = 0;
                break;

            case "p":
                index = 1;
                break;

            case "B":
                index = 3;
                break;

            case "G":
                index = 4;
                break;

            case "O":
                index = 5;
                break;

            case "Pi":
                index = 6;
                break;

            case "Pu":
                index = 7;
                break;

            case "R":
                index = 8;
                break;

            case "l":
                index = 10;
                break;

            case "u":
                index = 11;
                break;

            case "r":
                index = 12;
                break;

            case "d":
                index = 13;
                break;
        }

        return index;
    }

    private GameObject getBlockType(String[] line) {
        // Dummy block
        Block block = new Block(0, 0, " ");

        String[] sLocation = line[1].split("; ");
        Float[] location = new Float[2];
        location[0] = Float.parseFloat(sLocation[0]);
        location[1] = Float.parseFloat(sLocation[1]);

        String[] blockTypes = {"B", "G", "O", "Pi", "Pu", "R"};

        for(int i = 0; i < blockTypes.length; i++) {
            if(line[2].contains(blockTypes[i])) {
                block = new Block(location[0], location[1], blockTypes[i]);
            }
        }

        return block;
    }

    private GameObject getSpikeType(String[] line) {
        // Dummy spike
        Spike spike = new Spike(0, 0, " ");

        String[] sLocation = line[1].split("; ");
        Float[] location = new Float[2];
        location[0] = Float.parseFloat(sLocation[0]);
        location[1] = Float.parseFloat(sLocation[1]);

        String[] spikeTypes = {"1", "2", "3", "4"};

        for(int i = 0; i < spikeTypes.length; i++) {
            if(line[2].contains(spikeTypes[i])) {
                spike = new Spike(location[0], location[1], spikeTypes[i]);
            }
        }

        return spike;
    }

    public Bitmap getBitmap(String blockType) {
        int index = 0;

        switch(blockType) {
            case ".":
                index = 0;
                break;

            case "p":
                index = 1;
                break;

            case "1":
                index = 3;
                break;
        }

        return bitmapArray[index];
    }

    public boolean isPlaying() {
        return playing;
    }
}
