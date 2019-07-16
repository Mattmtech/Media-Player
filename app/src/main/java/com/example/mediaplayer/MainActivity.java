package com.example.mediaplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ListView actualListView;
    ArrayList<String> songList;

    ArrayAdapter songListAdapter;
    //MediaPlayer songPlayer;
    public int Id;
    public static String SONG = "mediaplayer.application.SONG";
    public int lastSongId;
    public static String LAST_SONG = "mediaplayer.application.LAST_SONG";
    public int firstSongId;
    public static String FIRST_SONG = "mediaplayer.application.FIRST_SONG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actualListView = findViewById(R.id.actualListView);
        songList = new ArrayList<String>();
        Field[] fields = R.raw.class.getFields();
        for(int i = 0; i < fields.length; i++){
            songList.add(fields[i].getName());
        }

        firstSongId = getResources().getIdentifier(songList.get(0),"raw",getPackageName());
        lastSongId = getResources().getIdentifier(songList.get(songList.size()-1),"raw",getPackageName());

        songListAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,songList);
        actualListView .setAdapter(songListAdapter);

        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Id = getResources().getIdentifier(songList.get(position), "raw",getPackageName());
                openActivityMp3();


            }
        });

    }

    public void openActivityMp3(){
        Intent intent = new Intent(this, Mp3view.class);
        intent.putExtra(SONG,Id);
        intent.putExtra(LAST_SONG, lastSongId);
        intent.putExtra(FIRST_SONG,firstSongId);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {

        finish();
    }


}
