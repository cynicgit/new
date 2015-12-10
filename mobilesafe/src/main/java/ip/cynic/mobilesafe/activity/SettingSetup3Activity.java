package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.utils.ToastUtil;
import ip.cynic.mobilesafe.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class SettingSetup3Activity extends BaseSetupActivity {

    private EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        etPhone = (EditText) findViewById(R.id.et_phone);
        String phone = mPref.getString("phone", "");
        etPhone.setText(phone);
    }

    @Override
    public void showNextPage() {
        String phone = etPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToast(this, "安全号码不能为空");
            return ;
        }
        mPref.edit().putString("phone", phone).commit();
        startActivity(new Intent(this, SettingSetup4Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);// 进入动画和退出动画
    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(this, SettingSetup2Activity.class));
        finish();
        overridePendingTransition(R.anim.tran_previous_in,
                R.anim.tran_previous_out);// 进入动画和退出动画
    }

    /**
     * 点击从数据库中获取联系人(内容提供者中获取)
     *
     * @param v
     */
    public void selectContact(View v) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("resultCode:"+resultCode);
        if(resultCode==10){
            String phone = data.getStringExtra("phone");
            phone = phone.replaceAll(" ", "").replaceAll("-", "");
            etPhone.setText(phone);
        }
    }


}
