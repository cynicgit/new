package ip.cynic.mobilesafe.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2015/12/12.
 */
public class AppInfo {

    private Drawable icon;//图片
    private String appPackage;//包名
    private String appName;//应用名
    private long appSize;//大小
    private boolean isRom;//是否安装在手机内存
    private boolean isSystem;//是否是系统应用

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    public boolean isRom() {
        return isRom;
    }

    public void setIsRom(boolean isRom) {
        this.isRom = isRom;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setIsSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }


    public AppInfo(Drawable icon, String appPackage, String appName, long appSize, boolean isRom, boolean isSystem) {
        this.icon = icon;
        this.appPackage = appPackage;
        this.appName = appName;
        this.appSize = appSize;
        this.isRom = isRom;
        this.isSystem = isSystem;
    }

    public AppInfo() {
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", appPackage='" + appPackage + '\'' +
                ", appName='" + appName + '\'' +
                ", appSize='" + appSize + '\'' +
                ", isRom=" + isRom +
                ", isSystem=" + isSystem +
                '}';
    }
}
