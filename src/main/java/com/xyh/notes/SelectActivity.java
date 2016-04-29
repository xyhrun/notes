package com.xyh.notes;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Created by 向阳湖 on 2016/4/29.
 */
public class SelectActivity extends Activity {
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private ImageView select_img;
    private VideoView select_videoView;
    private TextView select_content, select_time;
    private Button selectDel, selectReturn;
    private static final String TAG = "SelectActivity";
    private String imgPath, videoPath, content, time;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_layout);
        initView();
        notesDB = new NotesDB(this);
        dbWriter = notesDB.getWritableDatabase();
        imgPath = getIntent().getStringExtra(NotesDB.PATH);
        videoPath = getIntent().getStringExtra(NotesDB.VIDEO);
        id = getIntent().getIntExtra(NotesDB.ID, -1);
        content = getIntent().getStringExtra(NotesDB.CONTENT);
        time = getIntent().getStringExtra(NotesDB.TIME);
//        Log.d(TAG, "------onCreate: id = "+getIntent().getIntExtra(NotesDB.ID,-1));
        select_content.setText(content);
        select_time.setText(time);
        Log.d(TAG, "-----onCreate: imgPath = "+imgPath);
        if (!TextUtils.isEmpty(imgPath)) {
            select_img.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            select_img.setImageBitmap(bitmap);
        }

//        if (!(videoPath.equals("null"))) {
//            select_videoView.setVisibility(View.VISIBLE);
//            select_videoView.setVideoURI(Uri.parse(videoPath));
//            select_videoView.start();
//        }

        selectDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbWriter.delete(NotesDB.TABLE_NAME, NotesDB.ID + "=" + id, null);
                finish();
            }
        });

        selectReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        select_img = (ImageView) findViewById(R.id.select_img_id);
        select_videoView = (VideoView) findViewById(R.id.select_video_id);
        select_content = (TextView) findViewById(R.id.select_et_id);
        select_time = (TextView) findViewById(R.id.select_time_id);
        selectDel = (Button) findViewById(R.id.select_delete_id);
        selectReturn = (Button) findViewById(R.id.select_return_id);
    }
}
