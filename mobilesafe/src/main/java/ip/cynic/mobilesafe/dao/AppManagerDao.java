package ip.cynic.mobilesafe.dao;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ip.cynic.mobilesafe.domain.AppInfo;

/**
 * Created by Administrator on 2015/12/12.
 */
public class AppManagerDao {

    public static List<AppInfo> getAppInfos(Context context){

        List<AppInfo> appInfoList = new ArrayList<AppInfo>();

        PackageManager packageManager = context.getPackageManager();
        //获得安装的软件信息
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo info : installedPackages) {
            Drawable icon = info.applicationInfo.loadIcon(packageManager);
            String appPackage = info.packageName;
            String appName = info.applicationInfo.loadLabel(packageManager).toString();
            //apk 资源路径
            String sourceDir = info.applicationInfo.sourceDir;

            File file = new File(sourceDir);

            long size = file.getFreeSpace();

            int flags = info.applicationInfo.flags;

            boolean isSystem = false;
            boolean isRom = false;
            //是否是系统应用
            if((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
                isSystem = true;
            }

            //是否安装在sd卡
            if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
                isRom = true;
            }
            AppInfo appInfo = new AppInfo(icon, appPackage, appName, size, isRom, isSystem);
            System.out.println(appInfo);
            appInfoList.add(appInfo);
        }


        return appInfoList;

    }

}
