package ip.cynic.sms.cia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.ciaapp.sdk.CIAService;
import cn.ciaapp.sdk.VerificationListener;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText mPhoneEt;
    private ProgressDialog progressDialog;
    protected static final String appId = "67c3594c9ad347c6abf41d2b17e588bc";
    protected static final String authKey = "30f6b5d8b8224070831e22470adcbfd7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CIAService.init(this, appId, authKey);
        // 获取手机号码输入框
        mPhoneEt = (EditText) findViewById(R.id.et_phone);

        // 设置注册按钮的点击事件
        findViewById(R.id.bt_register).setOnClickListener(this);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 页面关闭的时候取消验证，如果有当前有验证请求就取消验证
        CIAService.cancelVerification();
    }

    @Override
    public void onClick(View v) {
        // 获取用户输入的手机号
        String phoneNumber = mPhoneEt.getText().toString();

        // TODO 判断网络状态

        // TODO 校验手机号码格式

        showProgress();

        // 调用CIAService发起验证请求
        CIAService.startVerification(phoneNumber, new VerificationListener() {
            /**
             * 校验结果回调
             * @param status        验证状态码
             * @param msg           验证状态的描述
             * @param transId       当前验证请求的流水号，服务器可以根据流水号查询验证的状态
             */
            @Override
            public void onStateChange(int status, String msg, String transId) {
                dismissProgress();

                /**
                 * status 是返回的状态码，CIAService包含了一些常量
                 * @see CIAService.VERIFICATION_SUCCESS 验证成功
                 * @see CIAService.VERIFICATION_FAIL 验证失败，请查看 msg 参数描述，例如手机号码格式错误，手机号格式一般需要开发者先校验
                 * @see CIAService.SECURITY_CODE_MODE   验证码模式
                 *      验证码模式：需要提示用户输入验证码，调用
                 *      @see CIAService.getSecurityCode()    获取当前的验证码，格式类似05311234****，需要提示用户****部分是输入的验证码内容
                 * @see CIAService.REQUEST_EXCEPTION    发生异常，msg 是异常描述，例如没有网络连接，网络连接状况一般需要开发者先判断
                 *
                 * 其他情况，status不在上述常量中，是服务器返回的错误，查看 msg 描述，例如 appId 和 authKey 错误。
                 */

                switch (status) {
                    case CIAService.VERIFICATION_SUCCESS: // 验证成功
                        // TODO 进入下一步业务逻辑
                        showToast("验证成功");
                        break;
                    case CIAService.SECURITY_CODE_MODE: // 验证码模式
                        // 进入输入验证码的页面，并提示用户输入验证码
                        startActivity(new Intent(getApplicationContext(), SecurityCodeActivity.class));
                        break;
                    case CIAService.VERIFICATION_FAIL:
                        showToast("验证失败：" + msg);
                        break;
                    case CIAService.REQUEST_EXCEPTION:
                        showToast("请求异常：" + msg);
                        break;
                    default:
                        // 服务器返回的错误
                        showToast(msg);
                }
            }
        });
    }

    /**
     * 显示进度条对话框
     */
    private void showProgress(){
        dismissProgress();
        progressDialog = ProgressDialog.show(this, "正在验证", "请稍后...");
    }

    /**
     * 关闭进度条对话框
     */
    private void dismissProgress(){
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
