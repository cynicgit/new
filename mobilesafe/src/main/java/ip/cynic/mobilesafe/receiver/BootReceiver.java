package ip.cynic.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * @author cynic
 *
 * 2015-12-2
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean lock = mPref.getBoolean("lock", false);
        if(lock){
            String phoneNumber = mPref.getString("phoneNumber", "");
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String number = manager.getLine1Number();
            if(!phoneNumber.equals(number)){
                SmsManager sms = SmsManager.getDefault();
                String safePhone = mPref.getString("phone", "");
                sms.sendTextMessage(safePhone, null, "sim card change", null, null);
                System.out.println("手机不安全");
            }else {
                System.out.println("手机安全");
            }
        }
    }

}
