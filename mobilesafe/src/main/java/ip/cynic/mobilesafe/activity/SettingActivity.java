package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.service.CallSafeService;
import ip.cynic.mobilesafe.view.SettingItemClick;
import ip.cynic.mobilesafe.view.SettingItemView;
import ip.cynic.mobilesafe.utils.ServiceStatusUtils;
import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.service.AddressService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

    private SettingItemView siv;
    private Editor edit;
    private SharedPreferences mPref;
    private SettingItemView sivAddress;
    private SettingItemView sivCallsafe;
    String[] items = new String[] { "魅力蓝", "活力橙", "金属灰", "姨妈白", "绿帽子" };
    private SettingItemClick sivAddressStyle;
    private SettingItemClick sicAddressLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        siv = (SettingItemView) findViewById(R.id.siv_update);
        //sivCallsafe = (SettingItemView) findViewById(R.id.siv_call_safe);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        edit = mPref.edit();
        update();//自动更新
        showAddress();//显示归属地
        showSingleDailog();//归属地显示浮窗样式
        showBlackSetting();
        //归属地显示浮窗位置
        sicAddressLocation = (SettingItemClick) findViewById(R.id.siv_address_location);
        sicAddressLocation.setTitle("归属地浮窗位置设置");
        sicAddressLocation.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AddressLocationActivity.class));
            }
        });

    }

    private void update() {
        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate) {
            siv.setCheck(true);
        } else {
            siv.setCheck(false);
        }
        siv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (siv.isChecked()) {
                    edit.putBoolean("auto_update", false);
                    siv.setCheck(false);
                    // siv.setDesc("自动更新已关闭");
                } else {
                    edit.putBoolean("auto_update", true);
                    siv.setCheck(true);
                    // siv.setDesc("自动更新已开启");
                }
                edit.commit();
            }
        });
    }

    private void showAddress() {
        // boolean addressShow = mPref.getBoolean("address_show", false);
        sivAddress = (SettingItemView) findViewById(R.id.siv_address_show);
        boolean addressShow = ServiceStatusUtils.isServiceRunning(this,
                "ip.cynic.mobilesafe.service.AddressService");
        sivAddress.setCheck(addressShow);

        sivAddress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    edit.putBoolean("address_show", false);
                    stopService(new Intent(SettingActivity.this,
                            AddressService.class));
                } else {
                    edit.putBoolean("address_show", true);
                    startService(new Intent(SettingActivity.this,
                            AddressService.class));
                }
                edit.commit();
                sivAddress.setCheck(!sivAddress.isChecked());
            }
        });
    }

    private void showBlackSetting() {
        // boolean addressShow = mPref.getBoolean("address_show", false);
        sivCallsafe = (SettingItemView) findViewById(R.id.siv_call_safe);
        boolean addressShow = ServiceStatusUtils.isServiceRunning(this,
                "ip.cynic.mobilesafe.service.CallSafeService");
        sivCallsafe.setCheck(addressShow);

        sivCallsafe.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sivCallsafe.isChecked()) {
                    edit.putBoolean("call_safe", false);
                    stopService(new Intent(SettingActivity.this,
                            CallSafeService.class));
                } else {
                    edit.putBoolean("call_safe", true);
                    startService(new Intent(SettingActivity.this,
                            CallSafeService.class));
                }
                edit.commit();
                sivCallsafe.setCheck(!sivAddress.isChecked());
            }
        });
    }

    private void showSingleDailog() {
        final int id = mPref.getInt("address_style", 0);

        sivAddressStyle = (SettingItemClick) findViewById(R.id.siv_address_style);
        sivAddressStyle.setTitle("归属地选择样式设置");
        sivAddressStyle.setDesc(items[id]);
        sivAddressStyle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new Builder(SettingActivity.this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle("归属地选择样式选择");
                builder.setSingleChoiceItems(items, id, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sivAddressStyle.setDesc(items[which]);
                        edit.putInt("address_style", which).commit();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

    }

}
