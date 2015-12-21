package cn.ciaapp.cia_example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.ciaapp.sdk.CIAService;
import cn.ciaapp.sdk.VerificationListener;


public class SecurityCodeActivity extends Activity implements View.OnClickListener {

    private EditText mCodeEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_code);

        // 设置下一步按钮的点击事件
        findViewById(R.id.bt_next).setOnClickListener(this);

        // 验证码的输入框
        mCodeEt = (EditText) findViewById(R.id.et_code);

        TextView tv_hint = (TextView) findViewById(R.id.tv_hint);
        tv_hint.setText("验证码已发送到您输入的手机号码上，隐藏在手机未接来电" + CIAService.getSecurityCode() + "的后四位，请查询并输入验证码。");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 页面关闭的时候取消验证，如果有当前有验证请求就取消验证
        CIAService.cancelVerification();
    }

    @Override
    public void onClick(View v) {
        // 点击下一步，校验验证码的正确性
        // 获取用户输入的验证码
        String code = mCodeEt.getText().toString();
        // 校验验证码
        CIAService.verifySecurityCode(code, new VerificationListener() {
            /**
             * 校验结果回调
             * @param status        验证状态码
             * @param msg           验证状态的描述
             * @param transId       当前验证请求的流水号，服务器可以根据流水号查询验证的状态
             */
            @Override
            public void onStateChange(int status, String msg, String transId) {

                /**
                 * status 是返回的状态码，CIAService包含了一些常量
                 * @see CIAService.VERIFICATION_SUCCESS    验证成功
                 * @see CIAService.SECURITY_CODE_WRONG 验证码输入错误
                 * @see CIAService.SECURITY_CODE_EXPIRED   验证码失效，需要重新验证
                 * @see CIAService.SECURITY_CODE_EXPIRED_INPUT_OVERRUN  验证码输入错误次数超限(3次)，需要重新验证
                 */

                switch (status) {
                    case CIAService.VERIFICATION_SUCCESS: // 验证成功
                        // TODO 进入下一步业务逻辑
                        showToast("验证成功");
                        finish();
                        break;
                    case CIAService.SECURITY_CODE_WRONG: // 验证码输入错误
                        showToast("验证码错误");
                        break;
                    case CIAService.SECURITY_CODE_EXPIRED:  // 验证码失效，需要重新验证
                        showToast("验证码失效，请重新验证");
                        finish();
                        break;
                    case CIAService.SECURITY_CODE_EXPIRED_INPUT_OVERRUN:    // 验证码输入错误次数过多(3次)，需要重新验证
                        showToast("验证码输入错误超过3次，请重新验证");
                        finish();
                        break;
                }
            }
        });
    }

    /**
     * Toast提示
     *
     * @param txt
     */
    private void showToast(String txt) {

        Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
    }
}
