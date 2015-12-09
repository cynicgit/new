package ip.cynic.mobilesafe.activity;


import ip.cynic.mobilesafe.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class HomeActivity extends Activity {


    private String menuItem[] = new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
    private int menuPirc[] = { R.drawable.home_safe,
            R.drawable.home_callmsgsafe, R.drawable.home_apps,
            R.drawable.home_taskmanager, R.drawable.home_netmanager,
            R.drawable.home_trojan, R.drawable.home_sysoptimize,
            R.drawable.home_tools, R.drawable.home_settings};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GridView gridView = (GridView) findViewById(R.id.gv_menu);
        gridView.setAdapter(new HomeAdapter());
        gridView.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0://手机防盗  弹出密码框
                        showPasswordDialog();
                        break;
                    case 1://通讯卫士
                        startActivity(new Intent(HomeActivity.this, CallSafeActivity.class));
                        break;
                    case 7://高级工具
                        startActivity(new Intent(HomeActivity.this, AdvanceToolActivity.class));
                        break;
                    case 8:
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
            }

        });

    }

    class HomeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return menuItem.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = null;
            if(convertView==null){
                v = View.inflate(HomeActivity.this, R.layout.activity_home_item, null);
            }else{
                v = convertView;
            }
            //System.out.println(v);
            ImageView iv = (ImageView) v.findViewById(R.id.iv_item);
            iv.setImageResource(menuPirc[position]);
            TextView tv= (TextView) v.findViewById(R.id.tv_item);
            tv.setText(menuItem[position]);
            return v;
        }

    }


    public void showPasswordDialog(){
        SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
        System.out.println(mPref.getString("password", ""));
        if("".equals(mPref.getString("password", ""))){
            dialogSetPassword();
        }else{
            dialogLoginPassword();
        }
    }

    public void dialogSetPassword(){
        AlertDialog.Builder builder = new Builder(HomeActivity.this);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false); //点击屏幕其他地方 不让弹窗消失

        View view = View.inflate(HomeActivity.this, R.layout.activity_set_password, null);
        dialog.setView(view);
        //点击取消
        Button btCancel = (Button) view.findViewById(R.id.bt_cancel);
        btCancel.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        //点击确定 验证密码
        final EditText edPass = (EditText) view.findViewById(R.id.et_password);
        final EditText edPassConfirm = (EditText) view.findViewById(R.id.et_password_confirm);

        Button btConfirm = (Button) view.findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                String pass = edPass.getText().toString();
                String passConfirm = edPassConfirm.getText().toString();
                if(TextUtils.isEmpty(pass)||TextUtils.isEmpty(passConfirm)){
                    Toast.makeText(HomeActivity.this, "密码不能为空或空格", Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(passConfirm)){
                    Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
                    mPref.edit().putString("password", pass).commit();
                    Toast.makeText(HomeActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                }
            }

        });
        dialog.show();
    }

    public void dialogLoginPassword(){
        AlertDialog.Builder builder = new Builder(HomeActivity.this);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false); //点击屏幕其他地方 不让弹窗消失

        View view = View.inflate(HomeActivity.this, R.layout.activity_login_password, null);
        dialog.setView(view);
        //点击取消
        Button btCancel = (Button) view.findViewById(R.id.bt_cancel);
        btCancel.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        //点击确定 验证密码
        final EditText edPass = (EditText) view.findViewById(R.id.et_password);
        Button btConfirm = (Button) view.findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                String pass = edPass.getText().toString();
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(HomeActivity.this, "密码不能为空或空格", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences mPref = getSharedPreferences("config", MODE_PRIVATE);
                    String strPass = mPref.getString("password", "");
                    if(pass.equals(strPass)){
                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                    }else{
                        Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        dialog.show();
    }

}
