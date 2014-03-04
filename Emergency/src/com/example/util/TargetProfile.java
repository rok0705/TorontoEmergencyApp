package com.example.util;

import android.location.Location;

public abstract class TargetProfile {

	private String m_name;
	private String m_address;
	
	private float m_xcoordinate;
	private float m_ycoordinate;
	
	public TargetProfile(){
		
	}
	
	public void SetName(String name){
		m_name = name;
	}
	public void SetAddress(String address){
		m_address = address;
	}
	
	public String GetName(){
		return m_name;
	}
	
	public String GetAddress(){
		return m_address;
	}
	
	public void SetCoordinates(String coordinates){
		String[] coord = coordinates.split(",");
		m_xcoordinate = Float.parseFloat(coord[0]);
		m_ycoordinate = Float.parseFloat(coord[1]);
	}

	public float GetXcoordinate(){
		return m_xcoordinate;
	}
	
	public float GetYcoordinate(){
		return m_ycoordinate;
	}
	
	public String GetCoordinates(){
		return m_xcoordinate + "," + m_ycoordinate;
	}
	
	public Location toLocation(){
		Location loc = new Location("CA");
		loc.setLongitude(m_xcoordinate);
		loc.setLatitude(m_ycoordinate);
		return loc;
	}
	@Override
	public String toString(){
		return "Name: " + m_name + "\n" 
				+ "Address: " + m_address + "\n"
				+ "Coordinate: " + GetCoordinates();
	}
}
