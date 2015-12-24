package ip.cynic.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import ip.cynic.mobilesafe.utils.ToastUtil;

/**
 * Created by Administrator on 2015/12/24.
 */
public class KillTaskReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo  app: runningAppProcesses) {
            activityManager.killBackgroundProcesses(app.processName);
        }

        ToastUtil.showToast(context,"清理完毕");
    }
}
