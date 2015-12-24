package ip.cynic.mobilesafe.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;

public class KillTaskService extends Service {

    private MyScreenOffRecriver recriver;

    public KillTaskService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        recriver = new MyScreenOffRecriver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(recriver,filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(recriver);
    }


    class MyScreenOffRecriver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
            for (RunningAppProcessInfo runningAppProcesse : runningAppProcesses) {
                activityManager.killBackgroundProcesses(runningAppProcesse.processName);
            }
        }
    }
}
