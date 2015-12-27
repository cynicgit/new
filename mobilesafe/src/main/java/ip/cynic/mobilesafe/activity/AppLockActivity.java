package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.fragment.LockFragment;
import ip.cynic.mobilesafe.fragment.UnlockFragment;

/**
 * Created by Cynic on 2015/12/27.
 */
public class AppLockActivity extends FragmentActivity implements View.OnClickListener{

    @ViewInject(R.id.tv_lock)
    private TextView tvLock;
    @ViewInject(R.id.tv_unlock)
    private TextView tvUnlock;
    @ViewInject(R.id.tv_applock_title_info)
    private TextView tvTittleInfo;
    @ViewInject(R.id.fl_content)
    private FrameLayout flContent;


    private FragmentManager fragmentManager;
    private UnlockFragment unlockFragment;
    private LockFragment lockFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applock);
        initUI();
    }

    private void initUI() {
        ViewUtils.inject(this);

        tvLock.setOnClickListener(this);
        tvUnlock.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();

        lockFragment = new LockFragment();
        unlockFragment = new UnlockFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        /**
         * 替换界面
         * 1 需要替换的界面的id
         * 2具体指某一个fragment的对象
         */
        transaction.replace(R.id.fl_content,lockFragment).commit();

    }


    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.tv_lock:
                tvLock.setBackgroundResource(R.drawable.tab_left_pressed);
                tvUnlock.setBackgroundResource(R.drawable.tab_right_default);
                transaction.replace(R.id.fl_content,lockFragment).commit();
                tvTittleInfo.setText("已加锁应用");
                break;
            case R.id.tv_unlock:
                tvLock.setBackgroundResource(R.drawable.tab_left_default);
                tvUnlock.setBackgroundResource(R.drawable.tab_right_pressed);
                transaction.replace(R.id.fl_content,unlockFragment).commit();
                tvTittleInfo.setText("未加锁应用");
                break;
        }
    }
}
