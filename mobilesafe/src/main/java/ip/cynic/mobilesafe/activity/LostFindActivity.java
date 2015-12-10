package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.utils.ToastUtil;
import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.receiver.MyDeviceAdminReceiver;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {

    private SharedPreferences mPref;
    private DevicePolicyManager mDPM;
    private ComponentName componentName;
    private CheckBox cbAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_find);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        // 获取设备策略服务
        mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
        if(mDPM.isAdminActive(componentName)){
            cbAdmin = (CheckBox) findViewById(R.id.cb_admin);
            cbAdmin.setChecked(true);
        }

        TextView tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
        ImageView ivLock = (ImageView) findViewById(R.id.iv_lock);
        boolean b = mPref.getBoolean("lock", false);
        if (b) {
            ivLock.setImageResource(R.drawable.lock);
        } else {
            ivLock.setImageResource(R.drawable.unlock);
        }

        if (!"".equals(mPref.getString("phone", ""))) {
            tvSafePhone.setText(mPref.getString("phone", ""));
        }

        // 如果没有配置过则进入向导页
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = mPref.getBoolean("configed", false);
        if (!configed) {
            startActivity(new Intent(this, SettingSetup1Activity.class));
        }
    }

    public void openAdmin(View v){
        if(!mDPM.isAdminActive(componentName)){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"获取系统管权限");
            startActivityForResult(intent, 110);
            cbAdmin.setChecked(true);
        }else{
            mDPM.removeActiveAdmin(componentName);
            cbAdmin.setChecked(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==110&&resultCode!=0){
            ToastUtil.showToast(this, "如要卸载请先关闭设备管理器");
        }
    }

    public void reEnter(View v) {
        startActivity(new Intent(this, SettingSetup1Activity.class));
        finish();
    }

}
