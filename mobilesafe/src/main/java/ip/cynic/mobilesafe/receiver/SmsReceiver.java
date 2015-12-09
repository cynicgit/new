package ip.cynic.mobilesafe.receiver;

import ip.cynic.mobilesafe.utils.SMSUtil;
import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.service.LocationService;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * @author cynic
 *
 * 2015-12-2
 */
public class SmsReceiver extends BroadcastReceiver{

    private DevicePolicyManager mDPM;
    private ComponentName mDeviceAdminSample;


    @Override
    public void onReceive(Context context, Intent intent) {

        //获取短信
        Bundle bundle = intent.getExtras();
        Object[] objects = (Object[]) bundle.get("pdus");

        for (Object object : objects) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])object);
            String originatingAddress = smsMessage.getOriginatingAddress();//手机号码
            String messageBody = smsMessage.getMessageBody();
            if("#*alarm*#".equals(messageBody)){
                //播放音乐
                MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
                player.setLooping(true);//循环
                player.setVolume(1f, 1f);
                player.start();

                abortBroadcast();
            }else if("#*location*#".equals(messageBody)){
                Intent service = new Intent(context, LocationService.class);
                context.startService(service);
                SharedPreferences mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                String x = mPref.getString("locationX", "");
                String y = mPref.getString("locationY", "");
                System.out.println(x+y+"位置");
                SMSUtil.sendSMS(originatingAddress, "x:"+x+"  Y:"+y);

                abortBroadcast();
            }else if("#*wipedata*#".equals(messageBody)){
                initAdmin(context);
                mDPM.wipeData(0);//清除数据
                abortBroadcast();
            }else if("#*lockscreen*#".equals(messageBody)){
                initAdmin(context);
                mDPM.lockNow();//锁屏
                abortBroadcast();
            }
        }

    }

    public void initAdmin(Context ctx){
        //获取设备策略服务
        mDPM = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //获取设备管理组件
        mDeviceAdminSample = new ComponentName(ctx, MyDeviceAdminReceiver.class);
        //openAdmin(ctx);
    }

    public void openAdmin(Context ctx){//激活设备
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "哈哈哈, 我们有了超级设备管理器, 好NB!");
        ctx.startActivity(intent);
    }

}
