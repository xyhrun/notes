package com.xyh.notes;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.widget.ListView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity{
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //设置菜单显示图标失败
//        setIconEnable(menu,true);
//        MenuItem item1 = menu.add(0, 1, 0, "文本");
//        item1.setIcon(R.mipmap.text);
//
//        MenuItem item2 = menu.add(0, 1, 0, "图片");
//        item2.setIcon(R.mipmap.carema1);
//
//        MenuItem item3 = menu.add(0, 1, 0,"视频");
//        item3.setIcon(R.mipmap.video);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, EditContent.class);
        //设置跳转指定网页
        Intent intent1 = new Intent();
        intent1.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse("https://www.zhihu.com/people/cloud-10");
        intent1.setData(uri);
        switch (item.getItemId()) {
            //添加文本
            case R.id.add_txt:
                intent.putExtra(TYPE, 0);
                startActivity(intent);
                break;
            //添加图片笔记
            case R.id.add_carema:
                intent.putExtra(TYPE, 1);
                startActivity(intent);
                break;
            //添加视频笔记
            case R.id.add_video:
                intent.putExtra(TYPE, 2);
                startActivity(intent);
                break;
            case R.id.about_me:
                startActivity(intent1);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //利用反射机制设置菜单弹出图标,但是无效
    private void setIconEnable(Menu menu, boolean enable)
    {
        try
        {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);

            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

