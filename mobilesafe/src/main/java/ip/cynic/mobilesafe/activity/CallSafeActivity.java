package ip.cynic.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ip.cynic.mobilesafe.R;

/**
 * Created by Administrator on 2015/12/8.
 */
public class CallSafeActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);
    }

    public void managerBlackNumber(View view){
        startActivity(new Intent(this, BlackNumberActivity2.class));
    }
}
