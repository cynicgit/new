package cn.ciaapp.cia_example;

import android.app.Application;

import cn.ciaapp.sdk.CIAService;

/**
 * Created by baoyz on 15/5/21.
 */
public class ExampleApplication extends Application {

    // TODO
    protected static final String appId = "your appId";
    protected static final String authKey = "your authKey";

    private int state;

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO 初始化CIA服务，填写申请的appId和authKey
        CIAService.init(this, appId, authKey);

    }
}
