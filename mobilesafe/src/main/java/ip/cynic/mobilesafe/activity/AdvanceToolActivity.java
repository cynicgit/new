package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.utils.SMSUtil;
import ip.cynic.mobilesafe.utils.ToastUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author cynic
 *
 * 2015-12-3
 */
public class AdvanceToolActivity extends Activity{

    private ProgressDialog progressDialog;

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("短信备份");
        progressDialog.setMessage("正在备份，请稍候...");
        //横向
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();

        new Thread(){
            @Override
            public void run() {
                boolean backUpSms = SMSUtil.backUpSms(AdvanceToolActivity.this, new SMSUtil.BackUpSms() {
                    @Override
                    public void setMax(int count) {
                        progressDialog.setMax(count);
                    }

                    @Override
                    public void setProgess(int progess) {
                        progressDialog.setProgress(progess);
                    }
                });

                if (backUpSms){
                    ToastUtil.showToast(AdvanceToolActivity.this,"成功");
                }else {
                    ToastUtil.showToast(AdvanceToolActivity.this,"失败");

                }

                progressDialog.dismiss();
            }
        }.start();

    }
}
