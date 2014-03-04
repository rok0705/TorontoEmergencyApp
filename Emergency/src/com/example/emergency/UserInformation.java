package com.example.emergency;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.util.EmergencyContact;
import com.example.util.UserProfile;

public class UserInformation extends Activity {

	public static final String PREFS_NAME = "userProfile";

	EditText m_etxt_userName;
	EditText m_txtEdit_emergency1, m_txtEdit_emergency2, m_txtEdit_emergency3;
	EditText m_txtEdit_ohip;
	EditText m_txtEdit_blood;
	Button m_setting_save;
	private UserProfile m_userProfile;
	private Typeface tf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information);
		tf = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeue.ttf");
		//findViewById(R.id.test).requestFocus();
		m_etxt_userName = (EditText)this.findViewById(R.id.txtEdit_userName);
		m_txtEdit_emergency1 = (EditText)this.findViewById(R.id.txtEdit_emergency1);
		m_txtEdit_emergency2 = (EditText)this.findViewById(R.id.txtEdit_emergency2);
		m_txtEdit_emergency3 = (EditText)this.findViewById(R.id.txtEdit_emergency3);

		m_txtEdit_ohip = (EditText)this.findViewById(R.id.txtEdit_ohip);
		m_txtEdit_blood = (EditText)this.findViewById(R.id.txtEdit_blood);
		m_setting_save = (Button)this.findViewById(R.id.setting_save);

		m_etxt_userName.setTypeface(tf);
		m_txtEdit_emergency1.setTypeface(tf);
		m_txtEdit_emergency2.setTypeface(tf);
		m_txtEdit_emergency3.setTypeface(tf);
		m_txtEdit_ohip.setTypeface(tf);
		m_txtEdit_blood.setTypeface(tf);

		m_userProfile = new UserProfile();
		LoadUserData();
	
		m_etxt_userName.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				m_etxt_userName.setSelectAllOnFocus(true);
			}
		});
		
		m_setting_save.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_CALL);
				
				SaveUserData();
				finish();
								
			}
		});
	}

    private boolean validInt(String str){
		System.out.println("parsing s = " + str);
		try  
		  {  
		    double deo = Double.parseDouble(str);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true; 
	}
    
	private UserProfile LoadUserData(){
		SharedPreferences savedData = getSharedPreferences(PREFS_NAME, 0);
		
		m_userProfile.SetName(savedData.getString("name","First Name"));
	    
		List<String> emsNumList = new ArrayList<String>();
		emsNumList.add(savedData.getString("ems1", "Emergency Contact #"));
		m_txtEdit_emergency1.setText(emsNumList.get(0));
		emsNumList.add(savedData.getString("ems2", "Emergency Contact #"));
		m_txtEdit_emergency2.setText(emsNumList.get(1));
		emsNumList.add(savedData.getString("ems3", "Emergency Contact #"));
		m_txtEdit_emergency3.setText(emsNumList.get(2));
		
		for(String enumItem : emsNumList){
			if(validInt(enumItem)){
				EmergencyContact nEmergency = new EmergencyContact(enumItem);
				m_userProfile.AddEmergencyContact(nEmergency);
				
			}
		}
	    
	    m_userProfile.SetInsuranceNumber(savedData.getString("ohip", "Insurance Number"));
	    
	    m_userProfile.SetBloodType(savedData.getString("blood", "Blood Type"));
	    
	    m_etxt_userName.setText(m_userProfile.GetName());
	    m_txtEdit_ohip.setText(m_userProfile.GetInsuranceNumber());
	    m_txtEdit_blood.setText(m_userProfile.GetBloodType());
	    
	    return m_userProfile;
	    
	}
	
	private void SaveUserData(){
		SharedPreferences saveData = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = saveData.edit();
	      
	    editor.putString("name", m_etxt_userName.getText().toString());
	    
	    editor.putString("ems1", m_txtEdit_emergency1.getText().toString());
	    editor.putString("ems2", m_txtEdit_emergency2.getText().toString());
	    editor.putString("ems3", m_txtEdit_emergency3.getText().toString());
	    
	    editor.putString("ohip", m_txtEdit_ohip.getText().toString());
	    editor.putString("blood", m_txtEdit_blood.getText().toString());
	    
	    
//		editor.putString("blood", m_) 
//		    
//		    m_etxt_userName.setText(m_userProfile.GetName());
//		    m_userProfile.SetBloodType((BLOODTYPE.valueOf(savedData.getString("blood", "Blood Type"))));
//		    
//		    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//		    
//		    String bday = savedData.getString("bday", "12/31/2013");
//		    

	      // Commit the edits!
	      editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_information, menu);
		return true;
	}

	@Override
    protected void onStop(){
       super.onStop();

      // We need an Editor object to make preference changes.
      // All objects are from android.context.Context
      //SaveUserData();
    }
}
