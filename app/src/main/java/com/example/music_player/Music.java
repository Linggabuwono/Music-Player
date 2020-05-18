package com.example.music_player;

public class Music {

    private long id;
    private String title;
    private String artist;

    public Music(long musicID, String musicTitle, String musicArtist) {
        id = musicID;
        title = musicTitle;
        artist = musicArtist;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}