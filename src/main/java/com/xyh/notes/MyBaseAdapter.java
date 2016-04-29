package com.xyh.notes;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 向阳湖 on 2016/4/28.
 */
public class MyBaseAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LayoutInflater mInflater;

    public MyBaseAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item, parent, false);
            viewHolder.photo_img = (ImageView) convertView.findViewById(R.id.list_img_id);
            viewHolder.video_img = (ImageView) convertView.findViewById(R.id.list_video_id);
            viewHolder.text = (TextView) convertView.findViewById(R.id.list_text_id);
            viewHolder.time = (TextView)convertView.findViewById(R.id.list_time_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        cursor.moveToPosition(position);
        String text = cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT));
        String time = cursor.getString(cursor.getColumnIndex(NotesDB.TIME));
        String uri = cursor.getString(cursor.getColumnIndex(NotesDB.PATH));
        String videoUri = cursor.getString(cursor.getColumnIndex(NotesDB.VIDEO));
        viewHolder.text.setText(text);
        viewHolder.time.setText(time);
        viewHolder.photo_img.setImageBitmap(getImageThumbnail(uri,200, 200));
//        viewHolder.video_img.setImageBitmap(getVideoThumbnail(videoUri, 200, 200, MediaStore.Images.Thumbnails.MICRO_KIND));
        return convertView;
    }

    private Bitmap getImageThumbnail(String uri, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }

        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private Bitmap getVideoThumbnail(String uri, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    class ViewHolder {
        private ImageView photo_img, video_img;
        private TextView text, time;
    }

}
