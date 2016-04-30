package com.xyh.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 向阳湖 on 2016/4/29.
 */
public class SelectActivity extends Activity {
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private ImageView select_img;
    private VideoView select_videoView;
    private TextView select_content, select_time;
    private Button selectDel, selectSave;
    private ToggleButton selectUpdate;
    private static final String TAG = "SelectActivity";
    private String imgPath, videoPath, content, time;
    private int id;
    private MediaController mController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_layout);
        initView();
        select_content.setEnabled(false);
        notesDB = new NotesDB(this);
        mController = new MediaController(this);
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
        if (!imgPath.equals("null")) {
            select_img.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            select_img.setImageBitmap(bitmap);
        }

        if (!videoPath.equals("null")) {
            select_videoView.setVisibility(View.VISIBLE);
            //第一种方式,点击详情查看视频界面是黑屏的,点击有声音,可控制进度.推荐这种
            select_videoView.setVideoPath(videoPath.toString());
            select_videoView.setMediaController(mController);
            mController.setMediaPlayer(select_videoView);
            select_videoView.requestFocus();

            //第二种方式,点击详情查看时直接播放视频,播放无声音,无控制进度
//            select_videoView.setVideoURI(Uri.parse(videoPath));
//            select_videoView.start();
        }

        selectDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectActivity.this);
                builder.setIcon(R.mipmap.warning).setTitle("删除后不可恢复,你确定?")
                        .setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbWriter.delete(NotesDB.TABLE_NAME, NotesDB.ID + "=" + id, null);
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                }).show();
            }
        });

        selectUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    select_content.setEnabled(true);
                } else {
                    select_content.setEnabled(false);
                }
            }
        });

        selectSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = getTime();
                String content = select_content.getText().toString();
                ContentValues values = new ContentValues();
                values.put(NotesDB.CONTENT, content);
                values.put(NotesDB.TIME, time);
                dbWriter.update(NotesDB.TABLE_NAME, values, NotesDB.ID + "=" + id, null);
                finish();
            }
        });
    }

    public String getTime() {
        //时间格式若是设置为HH:mm:ss  则无法播放时视频
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH-mm-ss");
        Date date = new Date();
        String mTime = sdf.format(date);
        return mTime;
    }

    private void initView() {
        select_img = (ImageView) findViewById(R.id.select_img_id);
        select_videoView = (VideoView) findViewById(R.id.select_video_id);
        select_content = (TextView) findViewById(R.id.select_et_id);
        select_time = (TextView) findViewById(R.id.select_time_id);
        selectDel = (Button) findViewById(R.id.select_delete_id);
        selectSave = (Button) findViewById(R.id.select_save_id);
        selectUpdate = (ToggleButton) findViewById(R.id.select_update_id);
    }
}
