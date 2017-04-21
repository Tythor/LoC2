package com.tythor.loc2;

// Created by Tythor on 8/25/2016

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Arrays;

import static com.tythor.loc2.GameActivity.context;

public abstract class GameObject {
    // Facing
    final int LEFT = 1;
    final int RIGHT = 2;
    public boolean canPassX = true;
    public boolean canPassY = true;
    private WorldLocation worldLocation;
    private boolean active = true;
    private boolean visible = true;
    private String blockType;
    private String bitmapName;
    private float xVelocity = 0;
    private float yVelocity = 0;
    private int facing;
    private boolean movable = false;
    private boolean deadly = false;
    public RectF objectHitbox = new RectF();
    public boolean checkLeftBounds = false;
    public boolean checkRightBounds = false;

    public boolean runningInstruction = false;
    public float moveSpeed;
    public ArrayList<Instruction> instructionList = new ArrayList<>();
    public Instruction currentInstruction;

    public int indexX;
    public int indexY;

    // Force the gameObjects to update themselves
    public abstract void update(int FPS);

    // Bitmap optimization
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both height and width larger than the requested height and width
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // Label, fetch, scale, then return the bitmap
    public Bitmap prepareBitmap(String bitmapName, float scaleFactor) {
        long timeStart = System.currentTimeMillis();

        // Label the bitmap
        int resID = context.getResources().getIdentifier(bitmapName,
                                                         "drawable",
                                                         context.getPackageName());

        // Optimization
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resID, options);
        options.inSampleSize = calculateInSampleSize(options, (int) (worldLocation.width * Viewport.pixelsPerMeter * scaleFactor), (int) (worldLocation.height * Viewport.pixelsPerMeter * scaleFactor));
        options.inJustDecodeBounds = false;

        // Fetch the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID, options);

        // Scale the bitmap; divided by block size
        bitmap = Bitmap.createScaledBitmap(bitmap,
                                           (int) (worldLocation.width * Viewport.pixelsPerMeter * scaleFactor),
                                           (int) (worldLocation.height * Viewport.pixelsPerMeter * scaleFactor),
                                           false);
        System.out.println((worldLocation.width * Viewport.pixelsPerMeter * scaleFactor));
        System.out.println((worldLocation.height * Viewport.pixelsPerMeter * scaleFactor));

        System.out.println(bitmap.getWidth() + " x " + bitmap.getHeight());
        System.out.println((System.currentTimeMillis() - timeStart) / 1000.0 + "s");
        /*if(bitmapName.equals("playerleft1")) {
            System.out.println("resetPlayer");
            // Scale the bitmap; divided by block size
            int width = (int) (8 * Viewport.pixelsPerMeter * scaleFactor);
            int difference = width % 8;
            if(difference <= 4 && difference != 0)
                width -= difference;
            else
                width += 8 - difference;
            System.out.println(difference + "I AM THE DIFFERENCE");
            System.out.println(width + "I AM THE WIDTH");
            bitmap = Bitmap.createScaledBitmap(bitmap,
                                               width,
                                               (int) (width * 1.375),
                                               false);
            System.out.println(bitmap.getWidth() + " x " + bitmap.getHeight() + "\n\n\n\n\n");
        }*/

        return bitmap;
    }

    public void setupGameObject(String blockType, String bitmapName, WorldLocation worldLocation) {
        this.blockType = blockType;
        this.bitmapName = bitmapName;
        this.worldLocation = worldLocation;

        objectHitbox.left = worldLocation.x;
        objectHitbox.top = worldLocation.y;
        objectHitbox.right = worldLocation.x + worldLocation.width;
        objectHitbox.bottom = worldLocation.y + worldLocation.height;
    }

    public void setupGameObject(String blockType, String bitmapName, WorldLocation worldLocation, int facing, boolean movable, boolean active, boolean visible) {
        this.blockType = blockType;
        this.bitmapName = bitmapName;
        this.worldLocation = worldLocation;
        this.facing = facing;
        this.movable = movable;
        this.active = active;
        this.visible = visible;
    }

    // Update GameObject's location
    public void move(int FPS) {
        worldLocation.x += xVelocity / FPS;
        worldLocation.y += yVelocity / FPS;
    }

    boolean runningInstructionX = true;
    boolean runningInstructionY = true;
    public void moveTo(int FPS, PointF destinationLocation, float moveSpeed) {
        // If gameObject will move left or up, then make moveSpeed negative
        if(getWorldLocation().x > destinationLocation.x || getWorldLocation().y > destinationLocation.y)
            moveSpeed *= -1;
        if(getWorldLocation().x != destinationLocation.x)
            setXVelocity(moveSpeed * 75);
        if(getWorldLocation().y != destinationLocation.y)
            setYVelocity(moveSpeed * 75);

        float xSpeed = xVelocity / FPS;
        float ySpeed = yVelocity / FPS;

        // If Destination is over or equal to destinationLocation, then jump to destinationLocation
        // Hack
        // || (worldLocation.x >= destinationLocation.x - 2 && worldLocation.x <= destinationLocation.x + 2)
        if((xSpeed > 0 && worldLocation.x + xSpeed >= destinationLocation.x) || (xSpeed < 0 && worldLocation.x + xSpeed <= destinationLocation.x) || worldLocation.x == destinationLocation.x) {
            worldLocation.x = destinationLocation.x;
            runningInstructionX = false;
        }
        else {
            if(runningInstruction) {
                worldLocation.x += xSpeed;
                runningInstructionX = true;
            }
        }

        // || (worldLocation.y >= destinationLocation.y - 2 && worldLocation.y <= destinationLocation.y + 2)
        if((ySpeed > 0 && worldLocation.y + ySpeed >= destinationLocation.y) || (ySpeed < 0 && worldLocation.y + ySpeed <= destinationLocation.y) || worldLocation.y == destinationLocation.y) {
            worldLocation.y = destinationLocation.y;
            runningInstructionY = false;
        }
        else {
            if(runningInstruction) {
                worldLocation.y += ySpeed;
                runningInstructionY = true;
            }
        }
        if(!runningInstructionX && !runningInstructionY) {
            runningInstruction = false;
        }
    }

    public void teleportTo(PointF destinationLocation) {
        worldLocation.x = destinationLocation.x;
        worldLocation.y = destinationLocation.y;
    }

    long startTime = System.currentTimeMillis();
    //static boolean allGo = false;
    //static float time;
    int alternationCount = 0;
    //System.getCurrenTimeMillis() - startTime
    public void alternate(AlternateInstruction alternateInstruction) {
        if(alternationCount == 0)
            alternationCount = (int) Math.floor(GameView.differenceTime / (alternateInstruction.time * 1000));
        if(alternationCount > 0 && GameView.differenceTime / alternationCount >= alternateInstruction.time * 1000) {
            alternationCount++;
            teleportTo(alternateInstruction.destinationLocations.get(destinationLocationIndex));
            if(this instanceof Spike) {
                this.bitmapName = prepareBitmapName(alternateInstruction.spikeDirections.get(
                        destinationLocationIndex));
                blockType = alternateInstruction.spikeDirections.get(destinationLocationIndex);
            }
            GameView.resetTime = true;
            //allGo = true;
            //time = alternateInstruction.time;
            startTime = System.currentTimeMillis();
            destinationLocationIndex = (destinationLocationIndex + 1) % alternateInstruction.destinationLocations.size();
        }
    }

    public void createInstruction(String[] line) {
        String[] instructions = new String[line.length - 1];
        for(int i = 0; i < instructions.length; i++) {
            instructions[i] = line[i + 1];
            System.out.println(instructions[i]);
        }
        System.out.println(Arrays.toString(instructions));
        for(int i = 0; i < instructions.length; i++) {
            String[] instructionInfo = instructions[i].split(", ");
            if(instructionInfo[0].equals("move")) {
                instructionList.add(MoveInstruction.createMoveInstruction(instructionInfo));
            }
            if(instructionInfo[0].equals("teleport")) {
                instructionList.add(TeleportInstruction.createTeleportInstruction(instructionInfo));
            }
            if(instructionInfo[0].equals("alternate")) {
                instructionList.add(AlternateInstruction.createAlternateInstruction(instructionInfo, this));
            }
        }
    }

    int currentInstructionIndex = 0;
    int destinationLocationIndex = 0;
    private boolean hasIntersected = false;
    private boolean hasAlternated = false;

    public void executeInstruction() {
        // If instructions exist
        if(instructionList.size() > 0) {
            if(currentInstruction instanceof MoveInstruction) {
                MoveInstruction moveInstruction = (MoveInstruction) currentInstruction;

                if(moveInstruction.intersectObject != null && (moveInstruction.intersectObject.getWorldLocation() != null && RectF.intersects(moveInstruction.intersectObject.objectHitbox, moveInstruction.triggerBounds) || moveInstruction.triggerBounds.contains(
                        moveInstruction.intersectObject.getWorldLocation().x,
                        moveInstruction.intersectObject.getWorldLocation().y) || moveInstruction.intersectObject.objectHitbox.intersects(moveInstruction.triggerBounds.left, moveInstruction.triggerBounds.top, moveInstruction.triggerBounds.right, moveInstruction.triggerBounds.bottom)))
                    hasIntersected = true;
                if(hasIntersected) {
                    moveTo(ViewController.FPS,
                           moveInstruction.destinationLocation,
                           moveInstruction.moveSpeed);
                }
                /*else {
                    currentInstruction = instructionList.get(currentInstructionIndex);
                    runningInstruction = true;
                    currentInstructionIndex = (currentInstructionIndex + 1) % instructionList.size();
                }*/
            }
            else if(currentInstruction instanceof TeleportInstruction) {
                TeleportInstruction teleportInstruction = (TeleportInstruction) currentInstruction;

                if(teleportInstruction.intersectObject != null && (RectF.intersects(teleportInstruction.intersectObject.objectHitbox,
                                                                                    teleportInstruction.triggerBounds) || teleportInstruction.triggerBounds.contains(
                        teleportInstruction.intersectObject.getWorldLocation().x,
                        teleportInstruction.intersectObject.getWorldLocation().y)))
                    teleportTo(teleportInstruction.destinationLocation);
                /*else {
                    currentInstruction = instructionList.get(currentInstructionIndex);
                    runningInstruction = true;
                    currentInstructionIndex = (currentInstructionIndex + 1) % instructionList.size();
                }*/
            }
            else if(currentInstruction instanceof AlternateInstruction) {
                AlternateInstruction alternateInstruction = (AlternateInstruction) currentInstruction;

                if(alternateInstruction.intersectObject != null && (RectF.intersects(alternateInstruction.intersectObject.objectHitbox,
                                                                                    alternateInstruction.triggerBounds) || alternateInstruction.triggerBounds.contains(alternateInstruction.intersectObject.getWorldLocation().x, alternateInstruction.intersectObject.getWorldLocation().y)))
                    hasAlternated = true;
                if(hasAlternated)
                    alternate(alternateInstruction);
            }
            if(!runningInstruction) {
                currentInstruction = instructionList.get(currentInstructionIndex);
                runningInstruction = true;
                currentInstructionIndex = (currentInstructionIndex + 1) % instructionList.size();
            }
        }
    }

    public String prepareBitmapName(String blockType) {
        switch(blockType) {
            case "B":
                blockType = "blockblue";
                break;
            case "G":
                blockType = "blockgreen";
                break;
            case "O":
                blockType = "blockorange";
                break;
            case "Pi":
                blockType = "blockpink";
                break;
            case "Pu":
                blockType = "blockpurple";
                break;
            case "R":
                blockType = "blockred";
                break;
            case "3":
                blockType = "spikeleft";
                break;
            case "1":
                blockType = "spikeup";
                break;
            case "4":
                blockType = "spikeright";
                break;
            case "2":
                blockType = "spikedown";
                break;
        }
        return blockType;
    }

    public WorldLocation getWorldLocation() {
        return worldLocation;
    }

    public String getBitmapName() {
        return bitmapName;
    }

    public float getWidth() {
        return worldLocation.width;
    }

    public float getHeight() {
        return worldLocation.height;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getBlockType() {
        return blockType;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public float getXVelocity() {
        return xVelocity;
    }

    public void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }

    public float getYVelocity() {
        return yVelocity;
    }

    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public void setWorldLocationX(float worldLocationX) {
        this.worldLocation.x = worldLocationX;
    }

    public void setWorldLocationY(float worldLocationY) {
        this.worldLocation.y = worldLocationY;
    }

    public boolean isDeadly() {
        return deadly;
    }

    public void setDeadly(boolean deadly) {
        this.deadly = deadly;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
}
