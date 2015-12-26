package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.dao.AntivirusDao;
import ip.cynic.mobilesafe.utils.Md5Utils;

/**
 * Created by cynic on 2015/12/25.
 */
public class AntivirusActivity extends Activity {

    private static final int BEGINING = 1;
    private static final int SCANING = 2;
    private static final int ENDING = 3;
    @ViewInject(R.id.iv_antivirus_anim)
    private ImageView ivAntivirusAnim;
    @ViewInject(R.id.ll_app)
    private LinearLayout ll_app;
    @ViewInject(R.id.tv_antivirus_info)
    private TextView tvAntivirusInfo;
    @ViewInject(R.id.pb_antivirus_progress)
    private ProgressBar pbAntivirus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_antivirus);
        ViewUtils.inject(this);

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //设置时间
        rotate.setDuration(4000);
        //-1表示无限循环
        rotate.setRepeatCount(-1);
        ivAntivirusAnim.startAnimation(rotate);
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case BEGINING:
                    tvAntivirusInfo.setText("初始化8核引擎");
                    break;
                case SCANING :
                    tvAntivirusInfo.setText("扫描中");
                    TextView textView = new TextView(AntivirusActivity.this);
                    textView.setText((String)msg.obj);
                    ll_app.addView(textView);
                    break;
                case ENDING :
                    tvAntivirusInfo.setText("扫描完成");
                    ivAntivirusAnim.clearAnimation();
                    break;
            }
        }
    };

    private void initData() {

        new Thread() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = BEGINING;
                handler.sendMessage(message);

                //获取所有应用对应的MD5值
                PackageManager packageManager = getPackageManager();
                List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

                pbAntivirus.setMax(installedPackages.size());

                for (int i = 0; i < installedPackages.size(); i++) {
                    PackageInfo info = installedPackages.get(i);
                    String sourceDir = info.applicationInfo.sourceDir;
                    String md5 = Md5Utils.getFileMd5(sourceDir);
                    String result = AntivirusDao.queryAntivirus(md5);

                    String appInfo = info.applicationInfo.loadLabel(packageManager).toString();
                    if (result != null) {
                        appInfo += " 扫描不安全";
                    } else {
                        appInfo += " 扫描安全";
                    }

                    message = Message.obtain();
                    message.what = SCANING;
                    message.obj = appInfo;
                    handler.sendMessage(message);
                    pbAntivirus.setProgress(i+1);
                }

                message = Message.obtain();
                message.what = ENDING;
                handler.sendMessage(message);
            }
        }.start();
    }
}
