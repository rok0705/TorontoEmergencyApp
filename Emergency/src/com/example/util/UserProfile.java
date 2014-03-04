package com.example.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserProfile extends TargetProfile{
	
	public enum BLOODTYPE{
		A,B,O,AB
	}
	
	private String m_lastName;
	private Date m_bDay;

	
//	private BLOODTYPE m_bloodType;
	private String m_bloodType;
	private String m_ISN;
	
	private List<EmergencyContact> m_econtactList = new ArrayList<EmergencyContact>();
	//temp
	private String m_ems_1;
	
	
	public UserProfile(){
		super();
	}
	
//	public void SetLastName(String lname){
//		m_lastName = lname;
//	}
//	public void SetBirthDay(Date bday){
//		m_bDay = bday;
//	}
	public void SetBloodType(String bloodtype){
		m_bloodType = bloodtype;
	}
	
	public void SetInsuranceNumber(String isn){
		m_ISN = isn;
	}
	
//	public void SetEmergencyNumber(String phoneNum){
//		m_ems_1 = phoneNum;
//	}
//	public String GetLastName(){
//		return m_lastName;
//	}
//	public String GetBirthDay(){
//		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//		return df.format(m_bDay);
//	}
	
	public String GetBloodType(){
		return m_bloodType.toString();
	}
	
	public String GetInsuranceNumber(){
		return m_ISN;
	}
//	public List<EmergencyContact> GetEmergencyNumber(){
//		return m_econtactList;
//	}
	
	public void AddEmergencyContact(EmergencyContact newEContact){
		m_econtactList.add(newEContact);
	}
	
	public List<EmergencyContact> GetEmergencyContacts(){
		return m_econtactList;
	}
	
}

