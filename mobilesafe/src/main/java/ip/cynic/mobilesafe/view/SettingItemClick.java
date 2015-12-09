package ip.cynic.mobilesafe.view;

import ip.cynic.mobilesafe.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemClick extends RelativeLayout{

	private TextView tv_desc;
	private TextView tv_title;

	public SettingItemClick(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 
		init();
	}

	public SettingItemClick(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SettingItemClick(Context context) {
		super(context);
		init();
	}
	
	
	public void init(){
		View.inflate(getContext(), R.layout.activity_setting_click, this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
	}
	
	public void setTitle(String text){
		tv_title.setText(text);
	}
	
	public void setDesc(String desc){
		tv_desc.setText(desc);
	}
	
}
