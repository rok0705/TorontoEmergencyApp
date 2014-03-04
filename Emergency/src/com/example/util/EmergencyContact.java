package com.example.util;

public class EmergencyContact {
	
	private String firstName;
	private String lastName;
	
	private String phoneNumber;
	private String relation;
	
	public EmergencyContact(){
		
	}

	public EmergencyContact(String PhoneNumber){
		this.phoneNumber = PhoneNumber;
	}

	public EmergencyContact(String FirstName, String LastName, String PhoneNumber){
		firstName = FirstName;
		lastName = LastName;
		phoneNumber = PhoneNumber;
	}

	public String GetName(){
		return firstName +" ' "+ lastName;
	}
	
	public String GetPhoneNumber(){
		return phoneNumber;
	}
}
