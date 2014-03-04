package com.example.util;

import android.location.Location;

public class CoordsUtil {

	// in metres
    final static double EARTH_CIRCUMFERENCE = 40075040.00;
    final static double FULL_DEGREES = 360.00;
    final static double NEARBY_ESTIMATE= 100.00;
    
    static int counter = 0;
    static enum _Direction {
            North,
            South,
            East,
            West
    }

	public static boolean CoordsInRange(Location _curLocation, Location targetLocation, int range) {
        //calculate values
        Double curLatitude, curLongitude, targetLatitude, targetLongitude, SouthLat, NorthLat, EastLong, WestLong;
		double adjVal = FULL_DEGREES * range/ EARTH_CIRCUMFERENCE;
        curLatitude = _curLocation.getLatitude();
        curLongitude = _curLocation.getLongitude();
        targetLatitude = targetLocation.getLatitude();
        targetLongitude = targetLocation.getLongitude();

        NorthLat = new Double(curLatitude + adjVal);
        //System.out.println("North test:c:"+targetLatitude+","+NorthLat.toString());
        
        SouthLat = new Double(curLatitude - adjVal);
        //System.out.println("South test:c:"+targetLatitude+","+SouthLat.toString());
        
        adjVal /= Math.cos(curLatitude);
        WestLong = new Double(curLongitude - adjVal);
        //System.out.println("West test:c:"+targetLongitude+","+WestLong.toString());

        EastLong = new Double(curLongitude + adjVal);
        //System.out.println("East test:c:"+targetLongitude+","+EastLong.toString());

        // check if given latitude is between north and south
        if((targetLatitude >= SouthLat.doubleValue() && targetLatitude <= NorthLat.doubleValue()) && 
           (targetLongitude >= WestLong.doubleValue() && targetLongitude <= EastLong.doubleValue())) {
        	//System.out.println("inRangeLocation Found count: "+ ++counter);
        	return true;
        } 		
		return false;		
	}
}
