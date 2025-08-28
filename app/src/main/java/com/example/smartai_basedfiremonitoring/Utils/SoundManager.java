package com.example.smartai_basedfiremonitoring.Utils;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.LinkedList;
import java.util.Queue;

public class SoundManager {
    private static SoundManager instance;
    private final Queue<Integer> soundQueue = new LinkedList<>();
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private final Context appContext; // safer

    private SoundManager(Context context) {
        this.appContext = context.getApplicationContext(); // use application context
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null && context != null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    public synchronized void playSound(int resId) {
        if (appContext == null) return; // safety check
        soundQueue.add(resId);
        if (!isPlaying) {
            playNext();
        }
    }

    private void playNext() {
        Integer nextResId = soundQueue.poll();
        if (nextResId == null) {
            isPlaying = false;
            return;
        }

        isPlaying = true;
        mediaPlayer = MediaPlayer.create(appContext, nextResId);
        if (mediaPlayer == null) {
            isPlaying = false;
            playNext();
            return;
        }

        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
            playNext();
        });
        mediaPlayer.start();
    }
}
