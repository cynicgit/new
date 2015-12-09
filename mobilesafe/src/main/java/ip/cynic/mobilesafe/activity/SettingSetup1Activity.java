package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.R;
import android.content.Intent;
import android.os.Bundle;

public class SettingSetup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }


    @Override
    public void showNextPage() {
        startActivity(new Intent(this, SettingSetup2Activity.class));
        finish();
        // 两个界面切换的动画
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);// 进入动画和退出动画
    }

    @Override
    public void showPrevious() {
    }
}
