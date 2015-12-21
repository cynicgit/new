package ip.cynic.mobilesafe.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import android.os.SystemClock;
import android.telephony.SmsManager;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ip.cynic.mobilesafe.domain.Message;

/**
 * @author cynic
 *         <p/>
 *         2015-12-2
 */
public class SMSUtil {

    public interface BackUpSms{
        public void setMax(int count);
        public void setProgess(int progess);

    }


    public static void sendSMS(String phone, String text) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, text, null, null);
    }

    public static boolean backUpSms(Context context,BackUpSms backUpSms) {
        boolean xml = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ContentResolver cr = context.getContentResolver();

            Uri uri = Uri.parse("content://sms");

            Cursor cursor = cr.query(uri, new String[]{"body", "date", "address", "type"}, null, null, null);
            List<Message> msgList = new ArrayList<Message>();

            int size = cursor.getCount();

            backUpSms.setMax(size);

            while (cursor.moveToNext()) {

                String body = cursor.getString(0);
                String date = cursor.getString(1);
                String address = cursor.getString(2);
                String type = cursor.getString(3);
                xml = msgList.add(new Message(address,date,type,body));
            }

            xmlSerializer(msgList,backUpSms);

            return xml;
        }
        return false;
    }

    private static boolean xmlSerializer(List<Message> msg,BackUpSms backUpSms){
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath()+"/sms.xml");
            FileOutputStream fos = new FileOutputStream(file);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            //enconding:指定头结点中的enconding属性的值
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "message");

            String encrypt = "";
            for (int i=0;i<msg.size();i++){
                serializer.startTag(null, "date");
                serializer.text(msg.get(i).getDate());
                serializer.endTag(null, "date");
                serializer.startTag(null, "type");
                serializer.text(msg.get(i).getType());
                serializer.endTag(null, "type");
                serializer.startTag(null, "address");
                serializer.text(msg.get(i).getAddress());
                serializer.endTag(null, "address");
                serializer.startTag(null, "body");
                try {
                    encrypt = Crypto.encrypt("message", msg.get(i).getBody());
                } catch (Exception e) {
                    encrypt = msg.get(i).getBody();
                    e.printStackTrace();
                }
                serializer.text(encrypt);
                serializer.endTag(null, "body");
                SystemClock.sleep(200);
                backUpSms.setProgess(i);
            }


            serializer.endTag(null,"message");

            //告诉序列化器，文件生成完毕
            serializer.endDocument();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
