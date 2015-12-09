package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class AddressLocationActivity extends Activity {

    private ImageView ivDrag;
    private int startX;
    private int startY;
    private Editor editor;
    long[] mHits = new long[2];// 数组长度表示要点击的次数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_location);

        SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
        editor = mPref.edit();
        ivDrag = (ImageView) findViewById(R.id.iv_drag);

        final TextView tvTop = (TextView) findViewById(R.id.tv_top);
        final TextView tvBottom = (TextView) findViewById(R.id.tv_bottom);

        //获取图片保存的坐标
        int x = mPref.getInt("lastX", 0);
        int y = mPref.getInt("lastY", 0);
        //初始化图片位置
        LayoutParams params = (LayoutParams) ivDrag.getLayoutParams();
        params.leftMargin = x;
        params.topMargin = y;
        ivDrag.setLayoutParams(params);

        //获取屏幕的宽高
        final int winWidth = getWindowManager().getDefaultDisplay().getWidth();
        final int winHeight = getWindowManager().getDefaultDisplay().getHeight();

        if(y>winHeight/2){
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.INVISIBLE);
        }else{
            tvTop.setVisibility(View.INVISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        }


        ivDrag.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    ivDrag.layout(winWidth/2-ivDrag.getWidth()/2, ivDrag.getTop(),
                            winWidth/2+ivDrag.getWidth()/2, ivDrag.getBottom());
                }
            }
        });

        //移动
        ivDrag.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://获取起始坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();//获取移动后的xy轴坐标
                        int endY = (int) event.getRawY();

                        //计算偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        //获取四个边框距离屏幕的margin值
                        int left = ivDrag.getLeft() + dx;
                        int right = ivDrag.getRight() + dx;
                        int top = ivDrag.getTop() + dy;
                        int bottom = ivDrag.getBottom() + dy;

                        if(left<0||right>winWidth||top<0||bottom>winHeight-20){
                            break;
                        }


                        if(top>winHeight/2){
                            tvTop.setVisibility(View.VISIBLE);
                            tvBottom.setVisibility(View.INVISIBLE);
                        }else{
                            tvTop.setVisibility(View.INVISIBLE);
                            tvBottom.setVisibility(View.VISIBLE);
                        }
                        //更新图片坐标
                        ivDrag.layout(left, top, right, bottom);

                        //更新起始坐标
                        startX = (int) event.getRawX();//获取移动后的xy轴坐标
                        startY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        editor.putInt("lastX", ivDrag.getLeft());
                        editor.putInt("lastY", ivDrag.getTop());
                        editor.commit();
                        break;

                    default:
                        break;
                }

                return false;//事件向后传递
            }
        });
    }
}
