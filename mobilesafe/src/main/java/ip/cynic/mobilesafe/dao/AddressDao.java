package ip.cynic.mobilesafe.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author cynic
 *
 * 2015-12-3
 */
public class AddressDao {


    private static final String PATH = "data/data/ip.cynic.mobilesafe/files/address.db";

    public static String queryAddress(String phone){

        String address = "未知号码";

        SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        String sql = null;
        if(phone.matches("^1[3-8]\\d{9}$")){
            sql = "select location from data2 where id = (select outkey from data1 where id=?)";
            Cursor cursor = db.rawQuery(sql, new String[]{phone.substring(0, 7)});
            if(cursor.moveToNext()){
                address = cursor.getString(0);
            }
            cursor.close();
        }

        db.close();

        return address;
    }
}
