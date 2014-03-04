package com.example.emergency;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.util.EmergencyContact;
import com.example.util.UserProfile;

public class SmsActivity extends Activity {

	public static final String PREFS_NAME = "userProfile";
	
	private Button btn1, btn2, btn3;
	private PendingIntent pi;
	private SmsManager sms;
	final String broken_msg = "Hi, I'm contacting you because I need your help.\n I JUST GOT INJURED.";
	final String robbery_msg = "Hi, I'm contacting you because I need your help.\n I JUST GOT ROBBED.";
	final String illness_msg = "Hi, I'm contacting you because I need your help.\n I AM VERY SICK NOW.";
	private UserProfile m_userProfile = new UserProfile();
	private List<EmergencyContact> contacts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		
		init_ids();
		init_sms();
		LoadUserData();
		init_listeners();

	}
	
	private void init_ids() {
		btn1 = (Button)findViewById(R.id.button1);
		btn2 = (Button)findViewById(R.id.button2);
		btn3 = (Button)findViewById(R.id.button3);
	}
	
	private void init_sms() {
        pi = PendingIntent.getActivity(this, 0,
                new Intent(this, SmsActivity.class), 0);                
        sms = SmsManager.getDefault();        
	}
	
	private void init_listeners() {
		btn1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				send(broken_msg);
				Log.i("broken sent", "broken sent");
			}			
		});

		btn2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				send(robbery_msg);
				Log.i("robbery sent", "robbery sent");

			}			
		});

		btn3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				send(illness_msg);
				Log.i("illness sent", "illness sent");

			}			
		});
		
	}
	
	private void send(String message) {
		Log.i("phone num that we will send is ","preparing to send msg.. " + m_userProfile.GetEmergencyContacts().size());
		int count = 0;
		for(EmergencyContact contact : m_userProfile.GetEmergencyContacts()) {
			String phoneNo = contact.GetPhoneNumber();
			Log.i("phone num that we will send is ","phone num that we will send is "+phoneNo);
			// if we have num and msg, we send SMS
			if (phoneNo.length()>0 && message.length()>0) {                
                sendSMS(phoneNo, message);  
				Toast.makeText(getBaseContext(), "Emergency message has been sent.", Toast.LENGTH_SHORT).show();
			} else {
                Toast.makeText(getBaseContext(), 
                    "Please enter both valid phone number and message.", 
                    Toast.LENGTH_SHORT).show();
			}
			Log.i("illness sent", "total SMS msg sent: "+(int)++count);
		}			
	}

    //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {        
        sms.sendTextMessage(phoneNumber, null, message, null, null); 
        finish();
    }    
    
    private boolean validInt(String str){
		System.out.println("parsing s = " + str);
		try  
		  {  
		    double d = Double.parseDouble(str);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true; 
	}
    
    private void LoadUserData(){
        m_userProfile = new UserProfile();

    	SharedPreferences savedData = getSharedPreferences(PREFS_NAME, 0);
		
		m_userProfile.SetName(savedData.getString("name","First Name"));
	    
		List<String> emsNumList = new ArrayList<String>();
		emsNumList.add(savedData.getString("ems1", "Emergency Contact #"));
			
		emsNumList.add(savedData.getString("ems2", "Emergency Contact #"));
		
		emsNumList.add(savedData.getString("ems3", "Emergency Contact #"));
		
		
		for(String enumItem : emsNumList){
			System.out.println("checking enum list " + enumItem);
			if(validInt(enumItem)){
				System.out.println("Valid Phone Number Found " + enumItem);
				EmergencyContact nEmergency = new EmergencyContact(enumItem);
				m_userProfile.AddEmergencyContact(nEmergency);
				
			}
		}
	    
	    m_userProfile.SetInsuranceNumber(savedData.getString("ohip", "Insurance Number"));
	    
	    m_userProfile.SetBloodType(savedData.getString("blood", "Blood Type"));
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sms, menu);
		return true;
	}

}
