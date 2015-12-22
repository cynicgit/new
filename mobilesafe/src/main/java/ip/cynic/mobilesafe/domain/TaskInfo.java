package ip.cynic.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2015/12/22.
 */
public class TaskInfo {

    private String taskName;
    private Drawable icon;
    private String taskPackage;

    //是否是用户进程
    private boolean userTask;
    //内存占用
    private long memorySize;

    //是否选中
    private boolean checked;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTaskPackage() {
        return taskPackage;
    }

    public void setTaskPackage(String taskPackage) {
        this.taskPackage = taskPackage;
    }

    public boolean isUserTask() {
        return userTask;
    }

    public void setUserTask(boolean userTask) {
        this.userTask = userTask;
    }

    public long getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(long memorySize) {
        this.memorySize = memorySize;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public TaskInfo(String taskName, Drawable icon, String taskPackage, boolean userTask, long memorySize, boolean checked) {
        this.taskName = taskName;
        this.icon = icon;
        this.taskPackage = taskPackage;
        this.userTask = userTask;
        this.memorySize = memorySize;
        this.checked = checked;
    }

    public TaskInfo() {
    }
}
