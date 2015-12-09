package ip.cynic.mobilesafe.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Cynic on 2015/12/9.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter{

    List<T> lists;

    public MyBaseAdapter(List<T> lists) {
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
