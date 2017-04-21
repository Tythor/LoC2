package com.tythor.loc2;

// Created by Tythor on 8/26/2016

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

import static com.tythor.loc2.GameActivity.context;

public class SoundManager {
    private SoundPool soundPool;

    int sound = -1;

    public void loadSound() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor assetFileDescriptor;

            assetFileDescriptor = assetManager.openFd("sound.ogg");
            sound = soundPool.load(assetFileDescriptor, 0);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
