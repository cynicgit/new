package ip.cynic.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;

import ip.cynic.mobilesafe.dao.BlackNumberDao;

public class CallSafeService extends Service {

    private BlackNumberDao dao;
    private Receiver receiver;

    public CallSafeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(receiver, filter);
        dao = new BlackNumberDao(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class Receiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取短信
            Bundle bundle = intent.getExtras();
            Object[] objects = (Object[]) bundle.get("pdus");
            System.out.println("短信来了");
            for (Object object : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                String originatingAddress = smsMessage.getOriginatingAddress();//手机号码
                String messageBody = smsMessage.getMessageBody();

                String mode = dao.findByNumber(originatingAddress);
                System.out.println("mode"+mode);
                if("1".equals(mode)||"2".equals(mode)){
                    abortBroadcast();
                }
            }
        }
    }
}
