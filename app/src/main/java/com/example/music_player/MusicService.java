package com.example.music_player;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.util.Random;
import android.app.Notification;
import android.app.PendingIntent;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

    public void onCreate(){
        super.onCreate();
        musicPosn=0;
        player = new MediaPlayer();

        rand = new Random();
    }

    private MediaPlayer player;
    private ArrayList<Music> musics;
    private int musicPosn;

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    private String musicTitle=";";;
    private static final int NOTIFY_ID=1;

    private boolean shuffle=false;
    private Random rand;

    @Override
    public IBinder onBind(Intent arg0) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

//--------------------------------------------------------------------------------------//

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);

        initMusicPlayer();
    }

    public void setList(ArrayList<Music> theMusics) {
        musics=theMusics;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    private final IBinder musicBind = new MusicBinder();

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    public void playMusic() {
        player.reset();

        Music playMusic = musics.get(musicPosn);
        musicTitle=playMusic.getTitle();
        long currMusic = playMusic.getID();
        Uri tarckUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currMusic);

        try{
            player.setDataSource(getApplicationContext(), tarckUri);
        }
        catch (Exception e) {
            Log.e("Music SERVICE","Error setting data source", e);
        }
        player.prepareAsync();
    }


    public void setMusic(int musicIndex) {
        musicPosn=musicIndex;
    }


    public int getPosn() {
        return player.getCurrentPosition();
    }

    public int getDur() {
        return player.getDuration();
    }

    public boolean isPng () {
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void go() {
        player.start();

        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0, notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.ic_play)
                .setTicker(musicTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(musicTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);

    }

    public void playPrev() {
        musicPosn--;
        if (musicPosn <0) musicPosn=musics.size()-1;
        playMusic();
    }

    public void setShuffle() {
        if (shuffle) shuffle=false;
        else shuffle=true;
    }

    public void playNext() {
        if (shuffle) {
            int newMusic = musicPosn;
            while (newMusic == musicPosn) {
                newMusic = rand.nextInt(musics.size());
            }
            musicPosn = newMusic;
        } else {
            musicPosn++;
            if ( musicPosn>=musics.size()) musicPosn=0;
        }
        playMusic();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }
}