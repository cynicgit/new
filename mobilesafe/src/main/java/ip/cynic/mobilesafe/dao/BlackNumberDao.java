package ip.cynic.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ip.cynic.mobilesafe.domain.BlackNumber;

/**
 * Created by Administrator on 2015/12/8.
 */
public class BlackNumberDao {

    private BlackNumberHelper helper;

    public BlackNumberDao(Context context) {
        helper = new BlackNumberHelper(context);
    }

    public boolean add(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        long l = db.insert("blacknumber", null, values);
        db.close();
        if (l > -1) {
            return true;
        }
        return false;
    }

    public void delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("blacknumber", "number = ?", new String[]{number});
        db.close();
    }

    public void updateMode(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        db.update("blacknumber", values, "number = ?", new String[]{number});
        db.close();
    }

    public String findByNumber(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor query = db.query("blacknumber", new String[]{"mode"}, "number = ?", new String[]{number}, null, null, null);
        String mode = "";
        while (query.moveToNext()) {
            mode = query.getString(0);
        }
        query.close();
        db.close();
        return mode;
    }

    public List<BlackNumber> findAll() {
        List<BlackNumber> numberList = new ArrayList<BlackNumber>();

        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{"number", "mode"}, null, null, null, null, null);


        while (cursor.moveToNext()) {
            String number = cursor.getString(0);
            String mode = cursor.getString(1);
            numberList.add(new BlackNumber(number, mode));
        }
        cursor.close();
        db.close();

        return numberList;
    }

}
