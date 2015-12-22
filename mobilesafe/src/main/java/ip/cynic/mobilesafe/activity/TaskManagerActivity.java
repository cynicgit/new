package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.utils.ActivityManagerUtils;

/**
 * Created by Administrator on 2015/12/22.
 */
public class TaskManagerActivity extends Activity{

    @ViewInject(R.id.tv_memory)
    private TextView tvMemory;
    @ViewInject(R.id.tv_task_size)
    private TextView tvTaskSize;
    @ViewInject(R.id.lv_task_info)
    private ListView lvTaskInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();

    }

    private void initData() {
        long memoryAvai = ActivityManagerUtils.getMemoryAvai(this);
        long memorySize = ActivityManagerUtils.getMemorySize(this);
        int progessCount = ActivityManagerUtils.getProgessCount(this);

        tvTaskSize.setText("当前运行进程数:"+progessCount);
        tvMemory.setText("内存:"+ Formatter.formatFileSize(this,memoryAvai)+
                "/"+Formatter.formatFileSize(this,memorySize));
    }

    private void initUI(){
        setContentView(R.layout.activity_task_manager);
        //使用注解注入 activity元素
        ViewUtils.inject(this);
    }
}
