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
    public static final String TXT_COLOR = "txt_color";
    public static final String TXT_FONT = "txt_font";

    //修改数据库表的字段信息,要控制好版本信息,或者直接卸载软件重新安装.否则插入信息不匹配,导致插入失败
    public NotesDB(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    //创建notes2表
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table notes2 ("
                             +" _id integer primary key autoincrement,"
                             +" content text,"
                             +" path text,"
                             +" video text,"
                             +" txt_color integer,"
                             +" txt_font integer,"
                             +" time text)";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
