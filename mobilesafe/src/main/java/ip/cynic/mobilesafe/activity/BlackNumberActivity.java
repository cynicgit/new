package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.adapter.MyBaseAdapter;
import ip.cynic.mobilesafe.dao.BlackNumberDao;
import ip.cynic.mobilesafe.domain.BlackNumber;
import ip.cynic.mobilesafe.utils.ToastUtil;

/**
 * Created by Administrator on 2015/12/8.
 */
public class BlackNumberActivity extends Activity {

    private ListView lvBlack;
    private BlackNumberDao dao;
    private List<BlackNumber> numberList;

    private int pageNum = 1;//默认第一页
    private int num = 5;//默认每页显示10条
    private int countPage = 0;//总页数
    private MyAdpter myAdpter;
    //消息队列
    private Handler handlder = new Handler(){



        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    llProg.setVisibility(View.INVISIBLE);
                    llBlack.setVisibility(View.VISIBLE);
                    myAdpter = new MyAdpter(numberList);
                    lvBlack.setAdapter(myAdpter);
                    tvPage.setText(pageNum+"/"+countPage);
                    break;
            }
        }

    };
    private LinearLayout llProg;
    private LinearLayout llBlack;
    private TextView tvPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacknumber);
        llProg = (LinearLayout) findViewById(R.id.ll_prog);
        llBlack = (LinearLayout) findViewById(R.id.ll_black);
        tvPage = (TextView) findViewById(R.id.tv_page);

        int height = llProg.getHeight();
        System.out.println("height:"+height);
        initDate();
        lvBlack = (ListView) findViewById(R.id.lv_black);

    }

    //获取黑名单
    private void initDate() {
        //子线程加载数据 使用消息队列
        Thread t = new Thread(){
            @Override
            public void run() {
                dao = new BlackNumberDao(BlackNumberActivity.this);
                numberList = dao.findBlackNumberPage(pageNum, num);
                countPage = (dao.count()+num-1)/num; //获得总页数
                System.out.println(dao.count());
                //SystemClock.sleep(1000);
                handlder.sendEmptyMessage(1);
            }
        };

        t.start();
    }


    /**
     * listView 适配器
     */
    class MyAdpter extends MyBaseAdapter<BlackNumber> {

        public MyAdpter(List lists) {
            super(lists);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder = null;
            if (view == null) {
                view = View.inflate(BlackNumberActivity.this, R.layout.blacknumber_list_item, null);
                holder = new ViewHolder();
                holder.tvNumber = (TextView) view.findViewById(R.id.tv_blacknumber);
                holder.tvMode = (TextView) view.findViewById(R.id.tv_black_mode);
                holder.ivDeleteList = (ImageView) view.findViewById(R.id.iv_delete_item);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final BlackNumber blackNumber = numberList.get(i);
            holder.ivDeleteList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String number = blackNumber.getNumber();
                   // numberList.remove(blackNumber);
                    dao.delete(number);
                    initDate();
                    myAdpter.notifyDataSetChanged();//刷新界面

                }
            });


            holder.tvNumber.setText(blackNumber.getNumber());
            holder.tvMode.setText(blackNumber.getMode());

            return view;
        }


    }

    static class ViewHolder {
        TextView tvNumber;
        TextView tvMode;
        ImageView ivDeleteList;
    }
    /**
     * 上一页
     * @param v
     */
    public void previous(View v){

        if(pageNum<=1){
            ToastUtil.showToast(this,"已经是第一页了");
            return;
        }
        pageNum--;
        initDate();
    }

    /**
     * 下一页
     * @param v
     */
    public void next(View v){

        if(pageNum>=countPage){
            ToastUtil.showToast(this,"已经是最后一页了");
            return;
        }
        pageNum++;
        initDate();

    }

    public void jump(View v){

    }

}
