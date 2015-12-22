package ip.cynic.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
public class ActivityManagerUtils {


    /**
     * 获取内存总容量
     * @param context
     * @return
     */
    public static long getMemorySize(Context context){

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        activityManager.getMemoryInfo(memoryInfo);

        return memoryInfo.totalMem;
    }

    /**
     * 获取内存可用容量
     * @param context
     * @return
     */
    public static long getMemoryAvai(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static int getProgessCount(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();


        return list.size();
    }

}
