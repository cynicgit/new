package ip.cynic.mobilesafe.utils;

import android.telephony.SmsManager;

/**
 * @author cynic
 *
 * 2015-12-2
 */
public class SMSUtil {

	public static void sendSMS(String phone,String text){
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phone, null, text, null, null);
	}
}
