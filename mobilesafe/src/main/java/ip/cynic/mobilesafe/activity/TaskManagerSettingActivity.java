package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.service.KillTaskService;
import ip.cynic.mobilesafe.utils.MPrefUtils;

/**
 * Created by Administrator on 2015/12/23.
 */
public class TaskManagerSettingActivity extends Activity{

    @ViewInject(R.id.cb_show_sys_task)
    private CheckBox cbShowSysTasK;
    @ViewInject(R.id.cb_timer_clear)
    private CheckBox cbTimerClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();

    }


    private void initUI(){
        setContentView(R.layout.activity_task_setting);
        ViewUtils.inject(this);
        boolean showSysTask = MPrefUtils.getBoolean(this, "show_sys_task");
        cbShowSysTasK.setChecked(showSysTask);
    }

    public void showSysTask(View v){
        MPrefUtils.saveBoolean(this, "show_sys_task", cbShowSysTasK.isChecked());
    }

    public void timerClear(View v){
        Intent intent = new Intent(this, KillTaskService.class);
        if(cbTimerClear.isChecked()){
            startService(intent);
        }else{
            stopService(intent);
        }
    }

}
