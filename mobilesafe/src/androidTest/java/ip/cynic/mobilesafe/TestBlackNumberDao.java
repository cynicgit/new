package ip.cynic.mobilesafe;

import android.content.Context;
import android.test.AndroidTestCase;

import java.util.Random;

import ip.cynic.mobilesafe.dao.BlackNumberDao;

/**
 * Created by Administrator on 2015/12/8.
 */
public class TestBlackNumberDao extends AndroidTestCase{


    private Context context;

    @Override
    protected void setUp() throws Exception {
        context = getContext();
        super.setUp();
    }

    /**
     * 黑名单添加
     */
    public void testAdd(){
        BlackNumberDao dao = new BlackNumberDao(context);

        for (int i=0;i<100;i++){
            dao.add("135052500"+i,new Random().nextInt(3)+1+"");
        }
    }

}
