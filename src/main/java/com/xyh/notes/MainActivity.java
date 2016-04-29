package com.xyh.notes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
                intent.putExtra(NotesDB.TIME, cursor.getString(cursor.getColumnIndex(NotesDB.TIME)));
                intent.putExtra(NotesDB.PATH, cursor.getString(cursor.getColumnIndex(NotesDB.PATH)));
                intent.putExtra(NotesDB.VIDEO, cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO)));
                startActivity(intent);
            }
        });
    }

    private void initView() {
        text_btn = (Button) findViewById(R.id.text_id);
        img_btn = (Button) findViewById(R.id.img_id);
        video_btn = (Button) findViewById(R.id.video_id);
        mListView = (ListView) findViewById(R.id.listview_id);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, EditContent.class);
        switch (v.getId()) {
            case R.id.text_id:
                intent.putExtra(TYPE, 0);
                startActivity(intent);
                break;
            case R.id.img_id:
                intent.putExtra(TYPE, 1);
                startActivity(intent);
                break;
            case R.id.video_id:
                intent.putExtra(TYPE, 2);
                startActivity(intent);
                break;
        }
    }
}
