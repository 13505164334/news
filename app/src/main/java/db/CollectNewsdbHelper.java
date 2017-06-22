package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangzixiong on 2017/5/18.
 */

public class CollectNewsdbHelper extends SQLiteOpenHelper {
    public CollectNewsdbHelper(Context context) {
        super(context, "collectnews.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table news("
                + "id integer primary key autoincrement,"
                + "nid integer,"
                + "stamp text,"
                + "icon text,"
                + "title text,"
                + "summary text,"
                + "link text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
