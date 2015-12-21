package ip.cynic.mobilesafe.utils;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtil {

	public static void showToast(final Activity context, final String str){
		if("main".equals(Thread.currentThread().getName())){
			Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
		}else{
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
