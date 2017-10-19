package com.example.nitishprasad.musicplayer;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper {



    public databaseHelper(Context context) {
        super(context,"fav",null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "create table favList (_id integer primary key autoincrement,id int unique)";
        sqLiteDatabase.execSQL(query);

        query = "create table playlist(_id integer primary key autoincrement,name varchar(20) unique)";
        sqLiteDatabase.execSQL(query);

        query = "create table playlistSongs(_id integer primary key autoincrement,name varchar(20),id int)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String query ;
        query = "create table playlist(_id integer primary key autoincrement,name varchar(20) unique)";
        sqLiteDatabase.execSQL(query);

        query = "create table playlistSongs(_id integer primary key autoincrement, name varchar(20),id int)";
        sqLiteDatabase.execSQL(query);

    }
}
