package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.utils.SMSUtil;
import ip.cynic.mobilesafe.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author cynic
 *
 * 2015-12-3
 */
public class AdvanceToolActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }


    //开启查询归属地页面
    public void openQuery(View v){
        startActivity(new Intent(this, AddressActivity.class));
    }


    public void backUpSms(View v){
        boolean backUpSms = SMSUtil.backUpSms(this);
        if (backUpSms){
            ToastUtil.showToast(this,"成功");
        }else {
            ToastUtil.showToast(this,"失败");

        }
    }
}
