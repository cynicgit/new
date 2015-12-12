package ip.cynic.mobilesafe.activity;

import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.text.format.Formatter;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.dao.AppManagerDao;
import ip.cynic.mobilesafe.domain.AppInfo;

public class AppManagerActivity extends Activity {

    @ViewInject(R.id.tv_app_rom)
    private TextView tvAppRom;
    @ViewInject(R.id.tv_app_sd)
    private TextView tvAppSd;
    @ViewInject(R.id.ll_app_infos)
    private ListView lvAppInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        initUI();
        inieData();
    }

    private void initUI(){
        //使用注解注入 activity元素
        ViewUtils.inject(this);
        
    }
    
    private void inieData(){

        long freeRom = getFilesDir().getFreeSpace();
        long freeSD = Environment.getExternalStorageDirectory().getFreeSpace();
        System.out.println("内存可用:"+Formatter.formatFileSize(this,freeRom));
        tvAppRom.setText("内存可用:" + Formatter.formatFileSize(this, freeRom));
        tvAppSd.setText("SD卡可用:" + Formatter.formatFileSize(this, freeSD));

        List<AppInfo> appInfos = AppManagerDao.getAppInfos(this);


    }

}
