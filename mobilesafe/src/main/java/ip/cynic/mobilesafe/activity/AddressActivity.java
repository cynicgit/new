package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.dao.AddressDao;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author cynic
 *
 * 2015-12-3
 */
public class AddressActivity extends Activity {

	private EditText etPhoneNumber;  
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
		tvResult = (TextView) findViewById(R.id.tv_result);
		
		etPhoneNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String result = AddressDao.queryAddress(s.toString());
				tvResult.setText(result);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	public void queryAddress(View v){
		String phone = etPhoneNumber.getText().toString();
		if(TextUtils.isEmpty(phone)){
			Animation animation = AnimationUtils.loadAnimation(this, R.anim.tran_shake);
			etPhoneNumber.setAnimation(animation);
		}else{
			String result = AddressDao.queryAddress(phone);
			tvResult.setText(result);
		}
	}
}
