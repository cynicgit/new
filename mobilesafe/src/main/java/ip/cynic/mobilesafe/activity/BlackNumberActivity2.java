package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
public class BlackNumberActivity2 extends Activity {

    private ListView lvBlack;
    private BlackNumberDao dao = new BlackNumberDao(BlackNumberActivity2.this);
    private List<BlackNumber> numberList;

    private MyAdpter myAdpter;

    private int startIndex = 0;
    private int numIndex = 20;
    private int numCount = 0;

    //消息队列
    private Handler handlder = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    llProg.setVisibility(View.INVISIBLE);
                    if (myAdpter == null) {
                        myAdpter = new MyAdpter(numberList);
                        lvBlack.setAdapter(myAdpter);
                    } else {
                        myAdpter.notifyDataSetChanged();
                    }
                    break;
            }
        }

    };
    private LinearLayout llProg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacknumber2);
        llProg = (LinearLayout) findViewById(R.id.ll_prog);

        lvBlack = (ListView) findViewById(R.id.lv_black);//listview
        Button btAddBlack = (Button) findViewById(R.id.bt_add_black);
        int height = llProg.getHeight();
        System.out.println("height:" + height);
        initDate();


        //监听listview滑动
        lvBlack.setOnScrollListener(new AbsListView.OnScrollListener() {

            //滚动的状态
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态 判断是否是最后一个
                        int lastItem = lvBlack.getLastVisiblePosition();
                        if (lastItem == numberList.size() - 1) {
                            startIndex += numIndex;
                            if (startIndex > numCount) {
                                ToastUtil.showToast(BlackNumberActivity2.this, "没有更多数据了");
                                return;
                            }
                            initDate();
                        }

                        break;
                }
            }

            //滚动的时候调用
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        btAddBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAddDialog();
            }
        });
    }

    private void showAddDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View addBlackView = View.inflate(this, R.layout.blacknumber_add_dialog, null);


        final EditText etBlackNumber = (EditText) addBlackView.findViewById(R.id.et_black_number);
        final CheckBox cbSms = (CheckBox) addBlackView.findViewById(R.id.cb_sms);
        final CheckBox cbPone = (CheckBox) addBlackView.findViewById(R.id.cb_phone);

        Button btAddConfirm = (Button) addBlackView.findViewById(R.id.bt_add);
        Button btCancel = (Button) addBlackView.findViewById(R.id.bt_black_cancel);

        btAddConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String number = etBlackNumber.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    ToastUtil.showToast(BlackNumberActivity2.this, "请输入正确的号码");
                    return;
                }

                if (!cbSms.isChecked() && !cbPone.isChecked()) {
                    ToastUtil.showToast(BlackNumberActivity2.this, "请选择拦截模式");
                    return;
                }

                String mode = "1";
                if (cbSms.isChecked() && cbPone.isChecked()) {
                    mode = "1";
                } else if (cbSms.isChecked()) {
                    mode = "2";
                } else if (cbPone.isChecked()) {
                    mode = "3";
                }

                dao.add(number, mode);
                numberList.add(0, new BlackNumber(number, mode));
                if (myAdpter == null) {//黑名单中没有号码时 需初始化 adpter  listview
                    myAdpter = new MyAdpter(numberList);
                    lvBlack.setAdapter(myAdpter);
                } else {
                    myAdpter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setView(addBlackView);
        dialog.show();

    }


    //获取黑名单
    private void initDate() {
        //子线程加载数据 使用消息队列
        System.out.println(startIndex + "," + numIndex);
        Thread t = new Thread() {
            @Override
            public void run() {
                numCount = dao.count();
                if (numberList == null) {
                    numberList = dao.findBlackNumberPage(startIndex, numIndex);
                } else {
                    List<BlackNumber> blackNumberPage = dao.findBlackNumberPage2(startIndex, numIndex);
                    numberList.addAll(blackNumberPage);
                }
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

            ViewHolder holder;
            if (view == null) {
                view = View.inflate(BlackNumberActivity2.this, R.layout.blacknumber_list_item, null);
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
                    dao.delete(number);
                    initDate();
                    myAdpter.notifyDataSetChanged();//刷新界面

                }
            });


            holder.tvNumber.setText(blackNumber.getNumber());
            holder.tvMode.setText(blackNumber.getMode());

            return view;
        }

        class ViewHolder {
            TextView tvNumber;
            TextView tvMode;
            ImageView ivDeleteList;
        }
    }

}
