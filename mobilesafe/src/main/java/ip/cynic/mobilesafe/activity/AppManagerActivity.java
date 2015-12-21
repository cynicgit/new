package ip.cynic.mobilesafe.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.dao.AppManagerDao;
import ip.cynic.mobilesafe.domain.AppInfo;
import ip.cynic.mobilesafe.utils.ToastUtil;

public class AppManagerActivity extends Activity implements View.OnClickListener {

    @ViewInject(R.id.tv_app_rom)
    private TextView tvAppRom;
    @ViewInject(R.id.tv_app_sd)
    private TextView tvAppSd;
    @ViewInject(R.id.ll_app_infos)
    private ListView lvAppInfos;
    private List<AppInfo> appInfos;
    private List<AppInfo> userApp;
    private List<AppInfo> systemApp;
    @ViewInject(R.id.tv_app)
    private TextView tvApp;
    private PopupWindow pop;
    private AppInfo appInfo;
    private View appPop;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        initUI();
        inieData();
    }

    private void initUI() {
        //使用注解注入 activity元素
        ViewUtils.inject(this);

        lvAppInfos.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            /**
             *
             * @param view
             * @param firstVisibleItem 第一个可见的条的位置
             * @param visibleItemCount 一页可以展示多少个条目
             * @param totalItemCount   总共的item的个数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                popDismss();

                if (userApp != null && systemApp != null) {
                    if (firstVisibleItem < userApp.size() + 1) {
                        tvApp.setText("用户应用(" + userApp.size() + ")");
                    } else {
                        tvApp.setText("系统应用(" + systemApp.size() + ")");

                    }
                }
            }
        });

        lvAppInfos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                Object obj = lvAppInfos.getItemAtPosition(i);

                appInfo = (AppInfo) obj;
                if (obj != null && obj instanceof AppInfo) {
                    appPop = View.inflate(AppManagerActivity.this, R.layout.app_pop, null);

                    LinearLayout llAppUninstall = (LinearLayout) appPop.findViewById(R.id.ll_app_uninstall);
                    LinearLayout llAppRun = (LinearLayout) appPop.findViewById(R.id.ll_app_run);
                    LinearLayout llAppShare = (LinearLayout) appPop.findViewById(R.id.ll_app_share);

                    llAppUninstall.setOnClickListener(AppManagerActivity.this);
                    llAppRun.setOnClickListener(AppManagerActivity.this);
                    llAppShare.setOnClickListener(AppManagerActivity.this);

                    popDismss();

                    pop = new PopupWindow(appPop, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    //窗体显示动画必须设置背景
                    pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    int[] location = new int[2];
                    //等到当前item的坐标
                    view.getLocationInWindow(location);
                    pop.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 70, location[1]);

                    //缩放
                    ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f,
                            1.0f, Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    sa.setDuration(200);

                    //透明
                    AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
                    aa.setDuration(200);
                    AnimationSet set = new AnimationSet(false);
                    set.addAnimation(aa);
                    set.addAnimation(sa);

                    appPop.setAnimation(set);
                }
            }
        });

    }

    private void popDismss() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }


    private MyAdapter myAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myAdapter = new MyAdapter();
            lvAppInfos.setAdapter(myAdapter);
        }
    };

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void inieData() {

        new Thread() {
            @Override
            public void run() {
                long freeRom = getFilesDir().getFreeSpace();
                long freeSD = Environment.getExternalStorageDirectory().getFreeSpace();
                System.out.println("内存可用:" + Formatter.formatFileSize(AppManagerActivity.this, freeRom));
                tvAppRom.setText("内存可用:" + Formatter.formatFileSize(AppManagerActivity.this, freeRom));
                tvAppSd.setText("SD卡可用:" + Formatter.formatFileSize(AppManagerActivity.this, freeSD));

                appInfos = AppManagerDao.getAppInfos(AppManagerActivity.this);
                userApp = new ArrayList<AppInfo>();
                systemApp = new ArrayList<AppInfo>();
                for (AppInfo app : appInfos) {
                    if (app.isSystem()) {
                        systemApp.add(app);
                    } else {
                        userApp.add(app);
                    }
                }
                handler.sendEmptyMessage(1);

            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        System.out.println("-----------------------------");
        popDismss();
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_app_uninstall:
                intent = new Intent();
                intent.setAction("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + appInfo.getAppPackage()));
                startActivityForResult(intent, 0);

                break;
            case R.id.ll_app_run:
                Intent start_localIntent = this.getPackageManager().getLaunchIntentForPackage(appInfo.getAppPackage());
                startActivityForResult(start_localIntent,0);
                break;
            case R.id.ll_app_share:
                intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "推荐一款软件名叫：" + appInfo.getAppName() + ",下载地址：ccc" + appInfo.getAppPackage());
                startActivity(intent);
                break;
        }

    }

    private void startApp() {
        Intent intent = new Intent();
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packInfo = pm.getPackageInfo(appInfo.getAppPackage(), 0);
            ActivityInfo[] acivityInfos = packInfo.activities;
            if(acivityInfos != null&&acivityInfos.length>0){
                ActivityInfo activityInfo = acivityInfos[0];
                intent.setClassName(appInfo.getAppPackage(), activityInfo.name);
                startActivity(intent);
            }else{
                ToastUtil.showToast(this, "这个程序没有界面");
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ToastUtil.showToast(this, "这个应用无法启动");
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return appInfos.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            if (position == 0) {
                return null;
            } else if (position == userApp.size() + 1) {
                return null;
            }
            AppInfo appInfo;

            if (position < userApp.size() + 1) {
                //把多出来的特殊的条目减掉
                appInfo = userApp.get(position - 1);

            } else {

                int location = userApp.size() + 2;

                appInfo = systemApp.get(position - location);
            }

            return appInfo;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            View v = null;
            ViewHodler hodler = null;
            if (position == 0) {
                TextView textView = new TextView(AppManagerActivity.this);
                textView.setText("用户应用(" + userApp.size() + ")");
                textView.setTextColor(Color.GRAY);
                return textView;
            } else if (position == userApp.size() + 1) {
                TextView textView = new TextView(AppManagerActivity.this);
                textView.setText("系统应用(" + systemApp.size() + ")");
                textView.setTextColor(Color.GRAY);
                return textView;
            } else {
                AppInfo appInfo = null;
                if (view != null && view instanceof LinearLayout) {
                    v = view;
                    hodler = (ViewHodler) v.getTag();
                } else {
                    v = View.inflate(AppManagerActivity.this, R.layout.app_info_item, null);
                    hodler = new ViewHodler();
                    hodler.ivAppIcon = (ImageView) v.findViewById(R.id.iv_app_icon);
                    hodler.tvAppPackage = (TextView) v.findViewById(R.id.tv_app_package);
                    hodler.tvAppName = (TextView) v.findViewById(R.id.tv_app_name);
                    hodler.tvAppSize = (TextView) v.findViewById(R.id.tv_app_size);
                    v.setTag(hodler);
                }

                if (position < userApp.size() + 1) {
                    appInfo = userApp.get(position - 1);
                } else {
                    appInfo = systemApp.get(position - (userApp.size() + 2));
                }

                hodler.ivAppIcon.setImageDrawable(appInfo.getIcon());
                hodler.tvAppPackage.setText(appInfo.getAppName());
                if (appInfo.isRom()) {
                    hodler.tvAppName.setText("手机内存");
                } else {
                    hodler.tvAppName.setText("SD卡");
                }
                hodler.tvAppSize.setText(Formatter.formatFileSize(AppManagerActivity.this, appInfo.getAppSize()) + "");

            }

            return v;
        }

        class ViewHodler {
            ImageView ivAppIcon;
            TextView tvAppPackage;
            TextView tvAppName;
            TextView tvAppSize;
        }

    }

}
