package ip.cynic.mobilesafe.service;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.dao.AddressDao;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * 显示归属地
 */
public class AddressService extends Service {

    private MyListener listener;
    private WindowManager mWM;
    private View view;
    private CallReceiver receiver;
    private TelephonyManager tm;
    private SharedPreferences mPref;

    private int startX;
    private int startY;
    private WindowManager.LayoutParams params;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

        receiver = new CallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }


    //去电使用广播 来显示归属地   要使广播随着服务启动而启动 则使用服务手动注册广播
    class CallReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData();
            String address = AddressDao.queryAddress(phone);
            toastShow(address);
        }

    }


    //来电监听 显示手机号码
    class MyListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:// 响铃
                    System.out.println("响铃");
                    String address = AddressDao.queryAddress(incomingNumber);
                    toastShow(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:// 挂机
                    if(mWM!=null){
                        mWM.removeView(view);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:// 接听

                    break;

                default:
                    break;
            }
        }
    }


    //显示浮窗
    public void toastShow(String address) {
        int[] ids = new int[] { R.drawable.call_locate_blue,
                R.drawable.call_locate_orange ,R.drawable.call_locate_gray,
                R.drawable.call_locate_white,R.drawable.call_locate_green};
        int id = getSharedPreferences("config", MODE_PRIVATE).getInt("address_style", 0);
        mWM = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        final int winWidth = mWM.getDefaultDisplay().getWidth();
        final int winHeight = mWM.getDefaultDisplay().getHeight();
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");

        //设置浮窗以屏幕左上角为原点
        params.gravity = Gravity.LEFT + Gravity.TOP;
        int x = mPref.getInt("lastX", 0);
        int y = mPref.getInt("lastY", 0);
        params.x = x;
        params.y = y;

        view = View.inflate(this, R.layout.activity_address_show, null);
        TextView tvToast = (TextView) view.findViewById(R.id.tv_address_show);
        tvToast.setText(address);
        tvToast.setBackgroundResource(ids[id]);
        mWM.addView(view, params);

        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();//获取移动后的xy轴坐标
                        int endY = (int) event.getRawY();

                        //计算偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        params.x += dx;
                        params.y += dy;

                        if(params.x < 0){
                            params.x = 0;
                        }
                        if(params.y < 0){
                            params.y = 0;
                        }
                        if(params.x > winWidth){
                            params.x = winWidth;
                        }
                        if(params.y > winHeight){
                            params.y = winHeight;
                        }

                        mWM.updateViewLayout(view, params);//更新页面位置
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        Editor editor = mPref.edit();
                        editor.putInt("lastX", params.x);
                        editor.putInt("lastY", params.y);
                        editor.commit();
                        break;
                }

                return true;
            }
        });


    }

}
