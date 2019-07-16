package com.example.mediaplayer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;


public class Mp3view extends AppCompatActivity {

    Button playBtn;
    Button prevBtn;
    Button nxtBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;
    public int Id;
    public int firstSongId;
    public int lastSongId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3view);
        prevBtn = findViewById(R.id.prevBtn);
        nxtBtn = findViewById(R.id.nxtBtn);
        playBtn =  findViewById(R.id.playBtn);
        elapsedTimeLabel =  findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel =  findViewById(R.id.remainingTimeLabel);



    Intent intent =getIntent();
  Id = intent.getIntExtra(MainActivity.SONG,0);

  firstSongId = intent.getIntExtra(MainActivity.FIRST_SONG,0);
  lastSongId = intent.getIntExtra(MainActivity.LAST_SONG,0);

        mp =  MediaPlayer.create(this, Id);
       // mp.start();


        mp.setVolume(0.5f, 0.5f);

        totalTime = mp.getDuration();


        positionBar =findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);

        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );



        //volume adjustments
        volumeBar = findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum= progress/100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp != null){
                    try{
                        Message message = new Message();
                        message.what = mp.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    }catch(InterruptedException e){}
                }
            }
        }).start();

    }



    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            int currentPosition = message.what;
            positionBar.setProgress(currentPosition);

            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime - currentPosition);
            remainingTimeLabel.setText(remainingTime);
        }
    };

    public String createTimeLabel(int time){
        String timeLabel = "";
        int min = time /1000/60;
        int sec = time / 1000%60;
        timeLabel = min + ":";
        if(sec < 10){
            timeLabel += "0";
        }
        timeLabel += sec;

        return timeLabel;
    }

    public void playBtnClick(View view){

        if(!mp.isPlaying()){
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);
        }
        else{
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }


    }

    public void nxtBtnClick(View view){
    mp.pause();
    mp.release();
    if(Id == lastSongId){
        Id = firstSongId;
        mp = MediaPlayer.create(this, firstSongId);
    }
    else {
        Id += 1;
        mp = MediaPlayer.create(this, Id);
    }
        totalTime = mp.getDuration();
        positionBar =findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        volumeBar = findViewById(R.id.volumeBar);
         mp.start();
        playBtn.setBackgroundResource(R.drawable.stop);


    }
    public void prevBtnClick (View view){
        mp.pause();
        mp.release();
        if(Id == firstSongId){
            Id = lastSongId;
            mp = MediaPlayer.create(this, Id);
        }
        else {
            Id -= 1;
            mp = MediaPlayer.create(this, Id);
        }
        totalTime = mp.getDuration();
        positionBar =findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        volumeBar = findViewById(R.id.volumeBar);
        mp.start();
        playBtn.setBackgroundResource(R.drawable.stop);
    }

    public void viewClick(View view){
        mp.pause();
        finish();
    }

    @Override
    public void onBackPressed() {
        mp.pause();
      finish();
    }





}


