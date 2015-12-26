package ip.cynic.mobilesafe.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by cynic on 2015/12/25.
 */
public class AntivirusDao {

    private static final String PATH = "data/data/ip.cynic.mobilesafe/files/antivirus.db";

    public static String queryAntivirus(String md5){

        String address = "未知号码";

        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);

        Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});

        String result = null;
        while (cursor.moveToNext()){
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();

        return result;
    }
}
