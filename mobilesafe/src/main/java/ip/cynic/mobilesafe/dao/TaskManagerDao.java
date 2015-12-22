package ip.cynic.mobilesafe.dao;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import java.util.ArrayList;
import java.util.List;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.domain.TaskInfo;

/**
 * Created by Administrator on 2015/12/22.
 */
public class TaskManagerDao {

    public static List<TaskInfo> getTaskList(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        PackageManager packageManager = context.getPackageManager();

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        List<TaskInfo> lists = new ArrayList<TaskInfo>();

        for (int i = 0; i < runningAppProcesses.size(); i++) {
            Drawable icon = null;
            String processName = "";
            String processPackage = "";
            String appName = "";
            int privateDirty = 0;
            boolean isUserApp = false;
            try {
                ActivityManager.RunningAppProcessInfo processInfo = runningAppProcesses.get(i);

                processName = processInfo.processName;

                Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(new int[]{processInfo.pid});

                //获取占用内存
                privateDirty = memoryInfo[0].getTotalPrivateDirty();

                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(processName, 0);

                int flags = applicationInfo.flags;
                if((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
                    isUserApp = false;
                }else {
                    isUserApp = true;
                }

                processPackage = applicationInfo.packageName;
                appName = applicationInfo.loadLabel(packageManager).toString();
                icon = applicationInfo.loadIcon(packageManager);


            } catch (Exception e) {
                e.printStackTrace();
                icon = context.getResources().getDrawable(R.drawable.ic_launcher);
            }

            lists.add(new TaskInfo(processName,icon,processPackage,isUserApp,privateDirty,false,appName));
        }


        return null;
    }
}
