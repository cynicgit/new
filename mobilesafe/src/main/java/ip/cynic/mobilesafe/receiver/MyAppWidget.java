package ip.cynic.mobilesafe.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import ip.cynic.mobilesafe.service.KillTaskWidgetService;

/**
 * Created by Administrator on 2015/12/24.
 */
public class MyAppWidget extends AppWidgetProvider{


    /**
     * 桌面上所有窗口删除时调用，相当于activity的ondestory
     *
     * 关闭服务
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, KillTaskWidgetService.class);
        context.stopService(intent);
    }

    /**
     * 第一次创建桌面控件的时候调用
     *
     * 开启服务 在服务里 对桌面布局显示的初始化
     *
     * 广播的生命周期是10秒 当业务复杂是需要在其他组件中进行
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Intent intent = new Intent(context, KillTaskWidgetService.class);
        context.startService(intent);


    }
}
