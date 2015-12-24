package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.dao.TaskManagerDao;
import ip.cynic.mobilesafe.domain.TaskInfo;
import ip.cynic.mobilesafe.utils.ActivityManagerUtils;
import ip.cynic.mobilesafe.utils.MPrefUtils;
import ip.cynic.mobilesafe.utils.ToastUtil;

/**
 * Created by Administrator on 2015/12/22.
 */
public class TaskManagerActivity extends Activity {

    @ViewInject(R.id.tv_memory)
    private TextView tvMemory;
    @ViewInject(R.id.tv_task_size)
    private TextView tvTaskSize;
    @ViewInject(R.id.lv_task_info)
    private ListView lvTaskInfo;
    private List<TaskInfo> taskInfos;
    private List<TaskInfo> userTasks;
    private ArrayList<TaskInfo> systemTasks;
    private MyAdpater myAdpater;
    @ViewInject(R.id.tv_task_status_info)
    private TextView tvTaskStatusInfo;
    private int progessCount;
    private long memoryAvai;
    private long memorySize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
        listener();
    }

    private void initData() {
        memoryAvai = ActivityManagerUtils.getMemoryAvai(this);
        memorySize = ActivityManagerUtils.getMemorySize(this);
        progessCount = ActivityManagerUtils.getProgessCount(this);

        tvTaskSize.setText("当前运行进程数:" + progessCount);
        tvMemory.setText("内存:" + Formatter.formatFileSize(this, memoryAvai) +
                "/" + Formatter.formatFileSize(this, memorySize));

        taskInfos = TaskManagerDao.getTaskList(this);
        userTasks = new ArrayList<TaskInfo>();
        systemTasks = new ArrayList<TaskInfo>();
        for (TaskInfo info : taskInfos) {
            if (info.isUserTask()) {
                userTasks.add(info);
            } else {
                systemTasks.add(info);
            }
        }

        myAdpater = new MyAdpater();
        lvTaskInfo.setAdapter(myAdpater);
    }

    private void initUI() {
        setContentView(R.layout.activity_task_manager);
        //使用注解注入 activity元素
        ViewUtils.inject(this);
    }


    private void listener() {
        lvTaskInfo.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userTasks != null && systemTasks != null) {
                    if (firstVisibleItem < userTasks.size() + 1) {
                        tvTaskStatusInfo.setText("用户进程(" + userTasks.size() + ")");
                    } else {
                        tvTaskStatusInfo.setText("系统进程(" + systemTasks.size() + ")");
                    }
                }
            }
        });

        lvTaskInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = lvTaskInfo.getItemAtPosition(position);
                if (obj != null && obj instanceof TaskInfo) {
                    TaskInfo taskInfo = (TaskInfo) obj;
                    if (taskInfo.getTaskPackage().equals(getPackageName())) {
                        return;
                    }
                    taskInfo.setChecked(!taskInfo.isChecked());
                }

                myAdpater.notifyDataSetChanged();
            }
        });
    }

    class MyAdpater extends BaseAdapter {


        @Override
        public int getCount() {

            if(MPrefUtils.getBoolean(TaskManagerActivity.this,"show_sys_task")){
                return userTasks.size() + 1;
            }

            return userTasks.size() + 1 + systemTasks.size() + 1;
        }

        @Override
        public Object getItem(int position) {

            if (position == 0 || position == userTasks.size() + 1) {
                return null;
            }

            if (position < userTasks.size() + 1) {
                return userTasks.get(position - 1);
            } else {
                int value = position - userTasks.size() - 2;
                return systemTasks.get(value);
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            if (position == 0) {
                TextView textView = new TextView(TaskManagerActivity.this);
                textView.setText("用户进程(" + userTasks.size() + ")");
                textView.setTextColor(Color.GRAY);
                return textView;
            } else if (position == userTasks.size() + 1) {
                TextView textView = new TextView(TaskManagerActivity.this);
                textView.setText("系统进程(" + systemTasks.size() + ")");
                textView.setTextColor(Color.GRAY);
                return textView;
            }

            View v = null;
            ViewHodler hodler = null;
            if (convertView != null && convertView instanceof LinearLayout) {
                v = convertView;
                hodler = (ViewHodler) v.getTag();
            } else {
                v = View.inflate(TaskManagerActivity.this, R.layout.item_task, null);
                hodler = new ViewHodler();
                hodler.ivTaskIcon = (ImageView) v.findViewById(R.id.iv_task_icon);
                hodler.tvTaskName = (TextView) v.findViewById(R.id.tv_task_name);
                hodler.tvTaskMenory = (TextView) v.findViewById(R.id.tv_task_memory);
                hodler.cbTaskStatus = (CheckBox) v.findViewById(R.id.cb_task_status);
                v.setTag(hodler);
            }

            System.out.println(systemTasks.size()+"----------"+userTasks.size());
            TaskInfo taskInfo = null;
            if (position < userTasks.size() + 1) {
                taskInfo = userTasks.get(position - 1);
            } else {
                int value = position - userTasks.size() - 2;
                taskInfo = systemTasks.get(value);
            }
            hodler.ivTaskIcon.setImageDrawable(taskInfo.getIcon());
            hodler.tvTaskName.setText(taskInfo.getAppName());
            hodler.tvTaskMenory.setText(Formatter.formatFileSize(TaskManagerActivity.this, taskInfo.getMemorySize()));

            System.out.println(getPackageName() + "------------------");
            //判断如果是自己的应用则将 选择框隐藏
            if (taskInfo.getTaskPackage().equals(getPackageName())) {
                hodler.cbTaskStatus.setVisibility(View.INVISIBLE);
            } else {
                hodler.cbTaskStatus.setVisibility(View.VISIBLE);
            }

            hodler.cbTaskStatus.setChecked(taskInfo.isChecked());

            return v;
        }

    }

    static class ViewHodler {
        ImageView ivTaskIcon;
        TextView tvTaskName;
        TextView tvTaskMenory;
        CheckBox cbTaskStatus;
    }

    /**
     * 全选
     *
     * @param v
     */
    public void allCheck(View v) {

        for (TaskInfo taskinfo : userTasks) {
            //如果是自己的应用则结束当前循环，继续下一个循环
            if (taskinfo.getTaskPackage().equals(getPackageName())) {
                continue;
            }
            taskinfo.setChecked(true);
        }

        for (TaskInfo taskinfo : systemTasks) {
            taskinfo.setChecked(true);
        }
        //刷新listview
        myAdpater.notifyDataSetChanged();
    }

    /**
     * 反选
     *
     * @param v
     */
    public void inverseCheck(View v) {
        for (TaskInfo taskinfo : userTasks) {
            //如果是自己的应用则结束当前循环，继续下一个循环
            if (taskinfo.getTaskPackage().equals(getPackageName())) {
                continue;
            }
            taskinfo.setChecked(!taskinfo.isChecked());
        }

        for (TaskInfo taskinfo : systemTasks) {
            taskinfo.setChecked(!taskinfo.isChecked());
        }

        //刷新listview
        myAdpater.notifyDataSetChanged();
    }

    /**
     * 清理进程
     *
     * @param v
     */
    public void taskClear(View v) {

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        int clearTask = 0;
        int clearMemory = 0;

        List<TaskInfo> killList = new ArrayList<TaskInfo>();
        for (TaskInfo taskInfo : userTasks) {
            if (taskInfo.isChecked()) {
                //for循环时不能改变 集合的大小
                //activityManager.killBackgroundProcesses(taskInfo.getTaskPackage());
                killList.add(taskInfo);
                clearTask++;
                clearMemory += taskInfo.getMemorySize();
            }
        }

        for (TaskInfo taskInfo : systemTasks) {
            if (taskInfo.isChecked()) {
                // activityManager.killBackgroundProcesses(taskInfo.getTaskPackage());
                killList.add(taskInfo);
                clearTask++;
                clearMemory += taskInfo.getMemorySize();
            }
        }

        ToastUtil.showToast(TaskManagerActivity.this,"共清理"+clearTask+"个进程,释放"
                +Formatter.formatFileSize(TaskManagerActivity.this,clearMemory)+"内存");
        progessCount -= clearTask;
        tvTaskSize.setText("当前运行进程数:" + progessCount);
        memoryAvai += clearMemory;
        tvMemory.setText("内存:" + Formatter.formatFileSize(this, memoryAvai) +
                "/" + Formatter.formatFileSize(this, memorySize));

        for (TaskInfo taskInfo : killList) {
            if (taskInfo.isUserTask()) {
                userTasks.remove(taskInfo);
            } else {
                systemTasks.remove(taskInfo);
            }
            activityManager.killBackgroundProcesses(taskInfo.getTaskPackage());
        }

        myAdpater.notifyDataSetChanged();

    }


    /**
     * 跳到设置界面
     * @param v
     */
    public void taskSetting(View v){
        Intent intent = new Intent(this,TaskManagerSettingActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(myAdpater!=null){
            myAdpater.notifyDataSetChanged();
        }
    }
}
