package com.xyh.notes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 向阳湖 on 2016/4/28.
 */
public class EditContent extends Activity implements View.OnClickListener {
    private NotesDB notesDB;
    private SQLiteDatabase daWriter;
    private File mFile = null;
    private File vFile = null;
    private static final String TAG = "EditContent";
//    设置一个标记用于判断编辑的是文字/图片/视频
    private int type;
    private ImageView editPhoto_img, editVideo_img;
    private VideoView videoView;
    private Button save_btn, cancle_btn;
    private EditText edit_et;
    private MediaController mController;
    private Integer txt_color = 0;
    private Integer txt_font = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_view);
        mController = new MediaController(this);
        editPhoto_img = (ImageView) findViewById(R.id.edit_img_id);
        videoView = (VideoView) findViewById(R.id.edit_video_id);
        save_btn = (Button) findViewById(R.id.edit_save_id);
        cancle_btn = (Button) findViewById(R.id.edit_cancle_id);
        edit_et = (EditText) findViewById(R.id.edit_et_id);
        type = getIntent().getIntExtra(MainActivity.TYPE,-1);
        notesDB = new NotesDB(this);
        daWriter = notesDB.getWritableDatabase();
        save_btn.setOnClickListener(this);
        cancle_btn.setOnClickListener(this);
        initView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //设置字体颜色
            case R.id.txt_red:
                txt_color = Color.RED;
                edit_et.setTextColor(txt_color);
                break;
            case R.id.txt_green:
                txt_color = Color.GREEN;
                edit_et.setTextColor(txt_color);
                break;
            case R.id.txt_blue:
                txt_color = Color.BLUE;
                edit_et.setTextColor(txt_color);
                break;
            case R.id.txt_yellow:
                txt_color = Color.YELLOW;
                edit_et.setTextColor(txt_color);
                break;
            //设置字体的大小
            case R.id.font_15:
                txt_font = 15;
                edit_et.setTextSize(txt_font);
                break;
            case R.id.font_25:
                txt_font = 25;
                edit_et.setTextSize(txt_font);
                break;
            case R.id.font_35:
                txt_font = 35;
                edit_et.setTextSize(txt_font);
                break;
        }
        return true;
    }

    private void initView() {
        switch (type) {
            case 0:
                break;
            case 1:
                editPhoto_img.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/notes/" + getTime() + ".jpg");
                Log.d(TAG, "-----initView: mFile = "+mFile.toString());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));
                startActivityForResult(intent, 1);
                break;
            case 2:
                videoView.setVisibility(View.VISIBLE);
                Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                vFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/notes/" + getTime() + ".mp4");
                Log.d(TAG, "-----initView: vFile = "+vFile.toString());
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(vFile));
                startActivityForResult(intent1, 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            Bitmap bitmap = BitmapFactory.decodeFile(mFile.getAbsolutePath());
            editPhoto_img.setImageBitmap(bitmap);
        }

        if (requestCode == 2) {
            //第一种方式,录制视频后视频界面是黑屏的,点击有声音,可控制进度.推荐这种
            videoView.setVideoPath(vFile.toString());
            videoView.setMediaController(mController);
            mController.setMediaPlayer(videoView);
            videoView.requestFocus();
            //第二种方式,录制视频后直接播放视频,播放无声音,无控制进度
//            videoView.setVideoURI(Uri.parse(vFile.toString()));
//            Log.d(TAG, "*-----onActivityResult: vFile = "+ vFile.toString());
//            videoView.start();

        }
    }

    public void addData() {
        Log.d(TAG, "------addData: 运行了");
        ContentValues values = new ContentValues();
        String mTime = getTime();
        Log.d(TAG, "----addData: edit_et"+edit_et.getText().toString());
        values.put(NotesDB.CONTENT, edit_et.getText().toString());
        if (txt_color == 0) {
            txt_color = Color.BLACK;
            values.put(NotesDB.TXT_COLOR, txt_color);
        } else {
            values.put(NotesDB.TXT_COLOR, txt_color);
        }

        if (txt_font == 0) {
            txt_font = 20;
            values.put(NotesDB.TXT_FONT, txt_font);
        } else {
            values.put(NotesDB.TXT_FONT, txt_font);
        }
//        values.put(NotesDB.TXT_COLOR,txt_color);
//        values.put(NotesDB.TXT_FONT,txt_font);
        Log.d(TAG, "----addData: content values = "+values);
        values.put(NotesDB.TIME, mTime);
        Log.d(TAG, "----addData: content time values = "+values);
//        Log.d(TAG, "----addData: content time imgvalues "+mFile.toString());
        //mFile+""需要这样,利用mFile.toString()将会报空指针,因为在编辑文本时候,mFile没有被创建为空!
        values.put(NotesDB.PATH, mFile+"");
        Log.d(TAG, "----addData: content time img values  "+values);
        values.put(NotesDB.VIDEO, vFile+"");

        daWriter.insert(NotesDB.TABLE_NAME, null, values);
    }

    public String getTime() {
        //时间格式若是设置为HH:mm:ss  则无法播放时视频
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH-mm-ss");
        Date date = new Date();
        String mTime = sdf.format(date);
        return mTime;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_save_id:
                Log.d(TAG, "------onClick: 点击保存了"+R.id.edit_save_id);
                if (edit_et.getText().toString().equals("") && (mFile+"").equals("null") && (vFile+"").equals("null")) {
                    Toast.makeText(EditContent.this, "笔记不能为空!", Toast.LENGTH_SHORT).show();
                    break;
                }
                addData();
                finish();
                break;
            case R.id.edit_cancle_id:
                finish();
                break;
        }
    }
}
