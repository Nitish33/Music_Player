package com.example.nitishprasad.musicplayer;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class controlScreen extends AppCompatActivity implements Runnable {

    ImageView play,repeat;
    SeekBar seek;
    boolean isVisible = false;
    TextView sTime,eTime;
    Handler handler;
    ImageButton favButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        play = (ImageView) findViewById(R.id.play);
        seek = (SeekBar) findViewById(R.id.seekbar);
        sTime = (TextView) findViewById(R.id.sTime);
        eTime = (TextView)findViewById(R.id.eTime);
        repeat = (ImageView)findViewById(R.id.repeat);
        favButton = (ImageButton)findViewById(R.id.fav);

        if(music.mediaPlayer!= null) seek.setMax(music.mediaPlayer.getDuration());

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(b){
                    music.seek(i);
                    if(!music.mediaPlayer.isPlaying()) music.mediaPlayer.start();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setUpInitial();
        handler = new Handler();
        handler.post(this);
    }

    private void setUpInitial() {

        if(music.mediaPlayer!=null) {
            int max = music.mediaPlayer.getDuration();
            seek.setMax(max);
            int sec = (max / 1000) % 60;
            int min = (max / 60000) % 60;
            String time = String.format("%02d:%02d", min, sec);
            eTime.setText(time);

            int pos = music.mediaPlayer.getCurrentPosition();
            seek.setProgress(pos);
            sec = (pos/1000)%60;
            min = (pos/60000)%60;
            time = String.format("%02d:%02d",min,sec);
            sTime.setText(time);

            if (music.mediaPlayer.isPlaying()) play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
            else  play.setImageDrawable(getResources().getDrawable(R.drawable.play));

            Song song = music.songs.get(MainActivity.index);
            int id = song.getId();

            if(music.favList.contains(id)) favButton.setImageDrawable(getResources().getDrawable(R.drawable.fav));
            else favButton.setImageDrawable(getResources().getDrawable(R.drawable.nfav));
        }
    }

    public void pauseOrPlay(View view){
        Toast.makeText(this, "pause or Play", Toast.LENGTH_SHORT).show();
        if (music.mediaPlayer.isPlaying()) play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        else  play.setImageDrawable(getResources().getDrawable(R.drawable.play));
        music.reset();


    }

    public void next(View view){

        music.next(this);

        int max = music.mediaPlayer.getDuration();
        seek.setMax(max);
        int sec = (max/1000)%60;
        int min = (max/60000)%60;
        String time = String.format("%02d:%02d",min,sec);
        eTime.setText(time);

        Song song = music.songs.get(MainActivity.index);
        int id = song.getId();

        if(music.favList.contains(id)) favButton.setImageDrawable(getResources().getDrawable(R.drawable.fav));
        else favButton.setImageDrawable(getResources().getDrawable(R.drawable.nfav));
    }
    public void previous(View view){
        music.previous(this);
        int max = music.mediaPlayer.getDuration();
        seek.setMax(max);
        int sec = (max/1000)%60;
        int min = (max/60000)%60;
        String time = String.format("%02d:%02d",min,sec);
        eTime.setText(time);

        Song song = music.songs.get(MainActivity.index);
        int id = song.getId();

        if(music.favList.contains(id)) favButton.setImageDrawable(getResources().getDrawable(R.drawable.fav));
        else favButton.setImageDrawable(getResources().getDrawable(R.drawable.nfav));
    }
    public void repeat(View view){

        int currentStatus = music.repeat;

        if(currentStatus == 1){
            music.repeat = 2;
            repeat.setImageDrawable(getResources().getDrawable(R.drawable.repeatone));
        }

        else if(currentStatus == 2){

            music.repeat = 3;
            repeat.setImageDrawable(getResources().getDrawable(R.drawable.suffle));
        }

        else  if(currentStatus == 3){

            music.repeat = 1;
            repeat.setImageDrawable(getResources().getDrawable(R.drawable.repeat));
        }


    }
    public void fav(View view){


    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isVisible = true;
    }

    protected void onStop(){
        super.onStop();
        isVisible = false;
    }
    protected void onRestart(){
        super.onRestart();
        isVisible = false;
    }

    @Override
    public void run() {

        if(isVisible && music.mediaPlayer != null){

            int pos = music.mediaPlayer.getCurrentPosition();
            seek.setProgress(pos);

            int min,sec;
            sec = (pos/1000)%60;
            min = ((pos/1000)/60)%60;

            String time = String.format("%02d:%02d",min,sec);
            sTime.setText(time);
          }

          handler.postDelayed(this,1000);
    }
}
