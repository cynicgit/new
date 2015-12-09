package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.adapter.MyBaseAdapter;
import ip.cynic.mobilesafe.dao.BlackNumberDao;
import ip.cynic.mobilesafe.domain.BlackNumber;

/**
 * Created by Administrator on 2015/12/8.
 */
public class BlackNumberActivity extends Activity {

    private ListView lvBlack;
    private BlackNumberDao dao;
    private List<BlackNumber> numberList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacknumber);
        initDate();
        lvBlack = (ListView) findViewById(R.id.lv_black);
        lvBlack.setAdapter(new MyAdpter(numberList));
    }

    //获取黑名单
    private void initDate() {
        this.dao = new BlackNumberDao(this);
        numberList = dao.findAll();
    }

    class MyAdpter extends MyBaseAdapter<BlackNumber> {

        public MyAdpter(List lists) {
            super(lists);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder;
            if (view == null) {
                view = View.inflate(BlackNumberActivity.this, R.layout.blacknumber_list_item, null);
                holder = new ViewHolder();
                holder.tvNumber = (TextView) view.findViewById(R.id.tv_blacknumber);
                holder.tvMode = (TextView) view.findViewById(R.id.tv_black_mode);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            BlackNumber blackNumber = numberList.get(i);
            holder.tvNumber.setText(blackNumber.getNumber());
            holder.tvMode.setText(blackNumber.getMode());

            return view;
        }

        class ViewHolder {
            TextView tvNumber;
            TextView tvMode;
        }
    }

}
