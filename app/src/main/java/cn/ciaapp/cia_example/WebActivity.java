package cn.ciaapp.cia_example;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import cn.ciaapp.sdk.CIAService;
import cn.ciaapp.sdk.VerificationListener;


public class WebActivity extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/index.html");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new VerifyInterface(), "cia");
    }

    class VerifyInterface {

        @JavascriptInterface
        public void verify(final String phoneNumber){
            CIAService.startVerification(phoneNumber, new VerificationListener() {
                @Override
                public void onStateChange(int status, String msg, String transId) {
                    // 回调js的函数
                    webView.loadUrl(String.format("javascript:afterVerification('%s', %d, '%s')", phoneNumber, status, msg));
                }
            });
        }

        @JavascriptInterface
        public void verifyCode(String code){
            CIAService.verifySecurityCode(code, new VerificationListener() {
                @Override
                public void onStateChange(int status, String msg, String transId) {
                    webView.loadUrl(String.format("javascript:afterVerification(%d, '%s')", status, msg));
                }
            });
        }

        @JavascriptInterface
        public void showToast(String text) {
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }
}
