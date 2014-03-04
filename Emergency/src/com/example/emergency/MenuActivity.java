package com.example.emergency;

import java.util.ArrayList;
import java.util.List;

import com.example.util.EmergencyContact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity {

	final Context context = this;

	private Button btn_call;
	private Button btn_map;
	private Button btn_sms;
	private Button btn_userInfo;
	private Button btn_faq;
	
	private List<EmergencyContact> m_ContactList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		init_ids();
		
		init_listeners();
	}
	
	private void init_ids() {
		btn_call = (Button)findViewById(R.id.btn_button1);
		btn_sms  = (Button)findViewById(R.id.btn_sms);
		btn_map = (Button)findViewById(R.id.btn_map);
		btn_userInfo = (Button)findViewById(R.id.btn_userInfo);
		btn_faq = (Button)findViewById(R.id.btn_faq);
		
		m_ContactList = new ArrayList<EmergencyContact>();
		
	}
	
	private void addEmergencyContact(String firstName, String lastName, String phoneNumber){
		
		EmergencyContact newContact = new EmergencyContact(firstName, lastName, phoneNumber);
		
		m_ContactList.add(newContact);
	}
	private void init_listeners() {
		// add PhoneStateListener
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
			.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
 		
		//settings
		btn_userInfo.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MenuActivity.this, UserInformation.class);
				startActivity(intent);
			}
		});
		
		//call
		btn_call.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, SpeedDialActivity.class);
				
				startActivity(intent);				
				//Intent intent = new Intent(MenuActivity.this, SpeedDialActivity.class);
				//startActivity(intent);				
			}
		});
		
		//sms
		btn_sms.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MenuActivity.this, SmsActivity.class);
				startActivity(intent);
			}
		});
		
		//map
		btn_map.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MenuActivity.this, MapActivity.class);
				Bundle dataType = new Bundle();
				dataType.putString("fireStation","true");
				dataType.putString("Ambulance", "true");
				intent.putExtras(dataType);
				startActivity(intent);
				 
			}
		});
		btn_faq.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				//Uri uri = Uri.parse("http://www.google.com");
				 //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				 Intent intent = new Intent(MenuActivity.this, InfoActivity.class);
				//Intent intent = new Intent(MenuActivity.this, SmsActivity.class);
				startActivity(intent);
			}
		});
		
		
	}

	//monitor phone call activities
	private class PhoneCallListener extends PhoneStateListener {
 
		private boolean isPhoneCalling = false;
 
		String LOG_TAG = "LOGGING 123";
 
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
 
			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}
 
			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(LOG_TAG, "OFFHOOK");
 
				isPhoneCalling = true;
			}
 
			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// run when class initial and phone call ended, 
				// need detect flag from CALL_STATE_OFFHOOK
				Log.i(LOG_TAG, "IDLE");
 
				if (isPhoneCalling) {
 
					Log.i(LOG_TAG, "restart app");
 
					// restart app
					Intent i = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(
							getBaseContext().getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
 
					isPhoneCalling = false;
				}
 
			}
		}
	}
 	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
