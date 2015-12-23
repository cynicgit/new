package ip.cynic.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MPrefUtils {

    private static final String NAME = "config";


    public static void saveBoolean(Context context,String key,boolean value){

        SharedPreferences mPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Editor edit = mPref.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }

    /**
     * 根据key获取boolean值 默认为false
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context,String key){
        SharedPreferences mPref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return mPref.getBoolean(key, false);
    }
}
