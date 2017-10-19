package com.example.nitishprasad.musicplayer;

import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class controlScreen extends AppCompatActivity implements Runnable {

    static ImageView play,repeat,albumArt;
    static SeekBar seek;
    boolean isVisible = false;
    static TextView sTime,eTime;
    TextView title,singer;
    Handler handler;
    static ImageButton favButton;
    SQLiteDatabase db;
    static  Bitmap defaultBitmap,currBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper helper = new databaseHelper(this);
        db = helper.getWritableDatabase();

        play = (ImageView) findViewById(R.id.play);
        seek = (SeekBar) findViewById(R.id.seekbar);
        sTime = (TextView) findViewById(R.id.sTime);
        eTime = (TextView)findViewById(R.id.eTime);
        repeat = (ImageView)findViewById(R.id.repeat);
        favButton = (ImageButton)findViewById(R.id.fav);
        albumArt = (ImageView)findViewById(R.id.albumArt);
        title = (TextView)findViewById(R.id.title);
        singer = (TextView)findViewById(R.id.singer);

        defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logo);
        if(music.mediaPlayer!= null) seek.setMax(music.mediaPlayer.getDuration());

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(b){
                    music.seek(i);
                    if(!music.mediaPlayer.isPlaying()) music.mediaPlayer.start();
                    play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
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


            int albumId = song.getAlbum_id();
            Uri album = Uri.parse("content://media/external/audio/albumart");
            Uri path = ContentUris.withAppendedId(album,albumId);


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                currBitmap = bitmap;
                if(bitmap != null)
                albumArt.setImageBitmap(bitmap);
                else
                albumArt.setImageBitmap(defaultBitmap);

                Log.e("setting bitmap","yes");
            }catch (Exception e){
                Log.e("error",e.getMessage());
                albumArt.setImageBitmap(defaultBitmap);
            }
        }
    }

    public void pauseOrPlay(View view){
        music.reset();
        if (music.mediaPlayer.isPlaying()) play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        else  play.setImageDrawable(getResources().getDrawable(R.drawable.play));



    }

    public void next(View view){

        music.next(this);

        setUpInitial();

    }

    private void updateStauts(){

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

        if (music.mediaPlayer.isPlaying()) play.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        else  play.setImageDrawable(getResources().getDrawable(R.drawable.play));

    }
    public void previous(View view){
        music.previous(this);

       setUpInitial();
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

        ImageButton b = (ImageButton)view;
        int id = music.songs.get(MainActivity.index).getId();

        if(music.favList.contains(id)) {
            String query = "delete from favList where id = " + id + "";
            db.execSQL(query);

            Log.e("before ", music.favList.toString());
            music.favList.remove((Object) id);
            Log.e("after", music.favList.toString());
            b.setImageDrawable(getResources().getDrawable(R.drawable.nfav));
        }

        else{

            String query = "insert into favList (id) values("+id+")";
            db.execSQL(query);
            music.favList.add(id);
            b.setImageDrawable(getResources().getDrawable(R.drawable.fav));

        }

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

            Song s = music.songs.get(MainActivity.index);
            String t = s.getTitle();
            String ar= s.getArtist();

            title.setText(t);
            singer.setText(ar);

            int min,sec;
            sec = (pos/1000)%60;
            min = ((pos/1000)/60)%60;

            String time = String.format("%02d:%02d",min,sec);
            sTime.setText(time);
          }

          handler.postDelayed(this,1000);
    }
}
