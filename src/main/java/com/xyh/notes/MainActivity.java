package com.xyh.notes;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button text_btn, img_btn, video_btn;
    private NotesDB notesDB;
    private SQLiteDatabase dbReader;
    private MyBaseAdapter myBaseAdapter;
    private ListView mListView;
    public static final String TYPE = "type";
    private Cursor cursor;
    private float mFirstY, mCurrentY;
    private int direction;
    private boolean mShow = true;
    private ObjectAnimator mAnimator;
    private View header;
    private int mTouchSlop;
    private Toolbar mTollbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text_btn.setOnClickListener(this);
        img_btn.setOnClickListener(this);
        video_btn.setOnClickListener(this);
        notesDB = new NotesDB(this);
        dbReader = notesDB.getReadableDatabase();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SelectActivity.class);
                cursor.moveToPosition(position);
                intent.putExtra(NotesDB.ID, cursor.getInt(cursor.getColumnIndex(NotesDB.ID)));
                intent.putExtra(NotesDB.CONTENT, cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
                intent.putExtra(NotesDB.TXT_COLOR, cursor.getInt(cursor.getColumnIndex(NotesDB.TXT_COLOR)));
                intent.putExtra(NotesDB.TXT_FONT, cursor.getInt(cursor.getColumnIndex(NotesDB.TXT_FONT)));
                intent.putExtra(NotesDB.TIME, cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                intent.putExtra(NotesDB.PATH, cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                intent.putExtra(NotesDB.VIDEO, cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(intent);
            }
        });

        //向上滑动actionBar不变,向下滑动actionBar隐藏.功能失败
//        judgeListViewSlip();
    }

    private void judgeListViewSlip() {
        header = new View(this);
        header.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                (int) getResources().getDimension(R.dimen.abc_action_bar_default_height_material)));
        mListView.addHeaderView(header);
        mTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        View.OnTouchListener mTouchListener = new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mFirstY =  event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mCurrentY = event.getY();
                        if (mCurrentY - mFirstY > mTouchSlop) {
                            direction = 0;  //向下滑动
                        } else if(mFirstY - mCurrentY > mTouchSlop) {
                            direction = 1;  //向上滑动
                        }

                        if (direction == 1) {
                            if (mShow) {
                                toolbarAnim(0);   //隐藏
                                mShow = !mShow;
                            }
                        } else if (direction == 0) {
                            if (!mShow) {
                                toolbarAnim(1);  //show
                                mShow = !mShow;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        };
    }

    private void toolbarAnim(int flag) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (flag == 0) {
            mAnimator = ObjectAnimator.ofFloat(mTollbar, "translationY", mTollbar.getTranslationY(), 0);
        } else {
            mAnimator = ObjectAnimator.ofFloat(mTollbar, "translationY", mTollbar.getTranslationY(), -mTollbar.getHeight());
        }
        mAnimator.start();
    }

    private void initView() {
        text_btn = (Button) findViewById(R.id.text_id);
        img_btn = (Button) findViewById(R.id.img_id);
        video_btn = (Button) findViewById(R.id.video_id);
        mListView = (ListView) findViewById(R.id.listview_id);
        mTollbar = (Toolbar) findViewById(R.id.toolbar);
    }


    private void selectData() {
        cursor = dbReader.query(NotesDB.TABLE_NAME, null, null, null, null, null, null);
        myBaseAdapter = new MyBaseAdapter(this, cursor);
        mListView.setAdapter(myBaseAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, EditContent.class);
        switch (v.getId()) {
            //添加文字笔记
            case R.id.text_id:
                intent.putExtra(TYPE, 0);
                startActivity(intent);
                break;
            //添加图片笔记
            case R.id.img_id:
                intent.putExtra(TYPE, 1);
                startActivity(intent);
                break;
            //添加视频笔记
            case R.id.video_id:
                intent.putExtra(TYPE, 2);
                startActivity(intent);
                break;
        }
    }
}
