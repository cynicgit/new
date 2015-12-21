package ip.cynic.sms.cia;

import android.app.Application;

import cn.ciaapp.sdk.CIAService;

/**
 * Created by baoyz on 15/5/21.
 */
public class ExampleApplication extends Application {

    // TODO
    protected static final String appId = "67c3594c9ad347c6abf41d2b17e588bc";
    protected static final String authKey = "30f6b5d8b8224070831e22470adcbfd7";

    private int state;

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO 初始化CIA服务，填写申请的appId和authKey
        CIAService.init(this, appId, authKey);

    }
}
