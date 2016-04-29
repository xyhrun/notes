package com.xyh.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 向阳湖 on 2016/4/28.
 */
public class NotesDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "notes2";
    public static final String ID = "_id";
    public static final String CONTENT= "content";
    //path当做图片
    public static final String PATH= "path";
    public static final String VIDEO= "video";
    public static final String TIME = "time";


    public NotesDB(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table notes2 ("
                             +" _id integer primary key autoincrement,"
                             +" content text,"
                             +" path text,"
                             +" video text,"
                             +" time text)";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
