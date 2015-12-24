package ip.cynic.mobilesafe.service;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.receiver.MyAppWidget;
import ip.cynic.mobilesafe.utils.ActivityManagerUtils;

/**
 * Created by Administrator on 2015/12/24.
 */
public class KillTaskWidgetService extends Service {

    private Timer timer;
    private AppWidgetManager appWidgetManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //桌面控件管理者
        appWidgetManager = AppWidgetManager.getInstance(this);

        timer = new Timer();
        //每5秒更新进程数，内存占用情况
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                /**
                 * 1 初始化页面显示 远程view
                 */
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.process_widget);
                long memorySize = ActivityManagerUtils.getProgessCount(getApplicationContext());
                views.setTextViewText(R.id.process_count, "正在运行的进程:" + memorySize);
                long memoryAvai = ActivityManagerUtils.getMemoryAvai(getApplicationContext());
                views.setTextViewText(R.id.process_memory,"可以内存:"+ Formatter.formatFileSize(getApplicationContext(),memoryAvai));

                //通知广播来接收点击事件 杀死进程
                Intent intent = new Intent();

                intent.setAction("ip.cynic.mobilesafe.receiver.killtask");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);

                //设置点击事件 杀死进程
                views.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);

                //第一个参数表示上下文
                //第二个参数表示当前有哪一个广播进行去处理当前的桌面小控件
                ComponentName privoders = new ComponentName(getApplicationContext(), MyAppWidget.class);

                appWidgetManager.updateAppWidget(privoders,views);
            }
        }, 0, 5);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
