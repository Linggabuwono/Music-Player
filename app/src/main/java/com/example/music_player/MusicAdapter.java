package com.example.music_player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicAdapter extends BaseAdapter {

    private ArrayList<Music> musics;
    private LayoutInflater musicInf;

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout musicLay = (LinearLayout) musicInf.inflate
                (R.layout.music, parent, false);

        TextView musicView = (TextView)musicLay.findViewById(R.id.music_title);
        TextView artistView = (TextView)musicLay.findViewById(R.id.music_artist);

        Music currMusic = musics.get(position);

        musicView.setText(currMusic.getTitle());
        artistView.setText(currMusic.getArtist());

        musicLay.getTag(position);
        return musicLay;
    }

    public MusicAdapter(Context c, ArrayList<Music> theMusic) {
        musics = theMusic;
        musicInf = LayoutInflater.from(c);
    }
}