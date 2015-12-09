package ip.cynic.mobilesafe.view;

import ip.cynic.mobilesafe.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout{

    private static final String NAMESPACE = "http://schemas.android.com/apk/res/ip.cynic.mobilesafe";
    private CheckBox cbStatus;
    private TextView tv_desc;
    private TextView tv_title;
    private String mTitle;
    private String mDescOn;
    private String mDescOff;

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int attributeCount = attrs.getAttributeCount();
		
		/*for (int i = 0; i < attributeCount; i++) {
			String attributeName = attrs.getAttributeName(i);
			String attributeValue = attrs.getAttributeValue(i);
			
			System.out.println(attributeName + "=" + attributeValue);
		}*/
        mTitle = attrs.getAttributeValue(NAMESPACE, "title");
        mDescOn = attrs.getAttributeValue(NAMESPACE, "desc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "desc_off");
        init();
    }

    public SettingItemView(Context context) {
        super(context);
        init();
    }


    public void init(){
        View.inflate(getContext(), R.layout.activity_setting_item, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        cbStatus = (CheckBox) findViewById(R.id.cb_status);
        setTitle(mTitle);
    }

    public void setTitle(String text){
        tv_title.setText(text);
    }

    public void setDesc(String desc){
        tv_desc.setText(desc);
    }

    /**
     * 获取单选框状态
     * @return
     */
    public boolean isChecked(){
        return cbStatus.isChecked();
    }

    public void setCheck(boolean check){
        cbStatus.setChecked(check);
        if(check){
            setDesc(mDescOn);
        }else{
            setDesc(mDescOff);
        }
    }

}
