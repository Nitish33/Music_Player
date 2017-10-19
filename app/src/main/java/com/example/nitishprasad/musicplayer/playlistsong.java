package com.example.nitishprasad.musicplayer;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class playlistsong extends AppCompatActivity {
    String name ;
    ListView list ;
    AlertDialog alert_dialog;
    ArrayList arr ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlistsong);

        list = (ListView) findViewById(R.id.list);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        getSupportActionBar().setTitle(name.toUpperCase());

        setuplist();
        setupclicklistener();
        setuplongclicklistener();

    }

    private void setuplongclicklistener() {

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(playlistsong.this);
                View alert_view = LayoutInflater.from(playlistsong.this).inflate(R.layout.playlistsong_alert_long,null);

                Button play = alert_view.findViewById(R.id.play);
                Button remove = alert_view.findViewById(R.id.remove);

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert_dialog.dismiss();
                        int id = (int)arr.get(i);
                        int pos = idToPos(id);
                        MainActivity.index = pos;
                        music.playById(playlistsong.this,id);

                    }
                });


                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alert_dialog.dismiss();

                        if(name.equals("favroite")){

                            int id = music.favList.get(i);
                            music.favList.remove(i);

                            databaseHelper helper = new databaseHelper(playlistsong.this);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            String query = "delete from favList where id = "+id;
                            db.execSQL(query);
                            db.close();
                            helper.close();
                            setuplist();
                        }

                        else{

                            int id = playListFragment.playlistSong.get(name).get(i);
                            ArrayList arr = playListFragment.playlistSong.get(name);
                            arr.remove(id);
                            playListFragment.playlistSong.put(name,arr);

                            databaseHelper helper = new databaseHelper(playlistsong.this);
                            SQLiteDatabase db = helper.getWritableDatabase();
                            String query = "delete from "+name+" where id = "+id;
                            db.execSQL(query);
                            db.close();
                            helper.close();
                            setuplist();
                        }
                    }
                });


                mBuilder.setView(alert_view);
                alert_dialog = mBuilder.create();
                alert_dialog.show();
                return true;
            }


        });



    }

    private void setupclicklistener() {

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int id = (int)arr.get(i);
                int pos = idToPos(id);
                MainActivity.index = pos;
                music.playById(playlistsong.this,id);

            }
        });
    }

    private void setuplist() {


        ArrayList nameList = new ArrayList();

        if(name.equals("favroite")){

           arr = music.favList;
        }

        else{

            arr = playListFragment.playlistSong.get(name);
            }


        for(int i =0;i<music.songs.size();++i){

           Song s = music.songs.get(i);
            int id = s.getId();

            if(arr.contains(id)){
                nameList.add(s.getTitle());
            }
        }

        Collections.sort(nameList);

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,nameList);
        list.setAdapter(adapter);
    }

    private int idToPos(int id){
        int i = 0;

        for(i = 0;i<music.songs.size();++i){

            int idd = music.songs.get(i).getId();

            if(idd == id) break;
        }

        return i;
    }
}
