package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import bean.NewsInfo;

/**
 * Created by wangzixiong on 2017/5/18.
 */

public class CollectNewsdb {
    public static boolean insertNews(Context context, NewsInfo info) {
        CollectNewsdbHelper helper = new CollectNewsdbHelper(context);
//        创建并返回数据库对象
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from news where nid="+info.getNid(), null);
        if (cursor.moveToFirst()){
            cursor.close();
            return false;
        }
        ContentValues values = new ContentValues();

//        数据值
        values.put("nid", info.getNid());
        values.put("stamp", info.getStamp());
        values.put("icon", info.getIcon());
        values.put("title", info.getTitle());
        values.put("summary", info.getSummary());
        values.put("link", info.getLink());
//        插入一条数据
        db.insert("news", null, values);
        return true;
    }

    public static List<NewsInfo> quary(Context context) {
        ArrayList<NewsInfo> list = new ArrayList<>();
        CollectNewsdbHelper helper = new CollectNewsdbHelper(context);
//        创建并返回数据库对象
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from news", null);
        if (cursor.moveToFirst()) {
            do {
                NewsInfo info = new NewsInfo();
                info.setStamp(cursor.getString(cursor.getColumnIndex("stamp")));
                info.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
                info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                info.setSummary(cursor.getString(cursor.getColumnIndex("summary")));
                info.setLink(cursor.getString(cursor.getColumnIndex("link")));
                info.setNid(cursor.getInt(cursor.getColumnIndex("nid")));
                list.add(info);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public static void delete(Context context,NewsInfo info){
        CollectNewsdbHelper helper = new CollectNewsdbHelper(context);
//        创建并返回数据库对象
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from news where nid="+info.getNid());
    }
    public static void deleteall(Context context){
        CollectNewsdbHelper helper = new CollectNewsdbHelper(context);
//        创建并返回数据库对象
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from news");
    }
}
