package com.example.emergency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.example.util.CoordsUtil;
import com.example.util.FacilityProfile;
import com.example.util.XMLParseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapActivity extends FragmentActivity 
			implements LocationListener,
			OnMyLocationChangeListener,
			OnMyLocationButtonClickListener,
			OnMarkerClickListener{

	private static Marker m_myMarker;
	private static Polyline m_line;
	private static List<Marker> markedFacilities = new ArrayList<Marker>();
	
	static final int M_DEFAULT_ZOOM_LEVEL = 13;
	static final int M_MIN_FACILITY_NUMBER = 10;
	static final int M_MIN_MIN_RANGE = 1000;
	public GoogleMap mGoogleMap;
	public SupportMapFragment mySupportMapFragment;
	public LocationManager locationManager;
	
	private List<String> m_dataType = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		Bundle bundle = this.getIntent().getExtras();
		
		for(String key: bundle.keySet()){
			
			String bvalue = bundle.getString(key);
			
			//if value is true, insert key which is name of file
			if(Boolean.parseBoolean(bvalue)){
				
				m_dataType.add(key);
				System.out.println("Bundle found: "+key);
			}
		}
	
		init_locationManager();
		initMap();
	}
	
	private void drawPath(LatLng start_LatLng, LatLng end_LatLng){
		
		if(m_line==null){
			System.out.println("Line is Null or invisible Draw a new line");
			m_line = mGoogleMap.addPolyline(new PolylineOptions()
		     .add(start_LatLng, end_LatLng)
		     .width(5)
		     .color(Color.BLUE)
		     .geodesic(true));
			System.out.println("AfterDrawing geo :"+m_line.isGeodesic());
			System.out.println("AfterDrawing visible :"+m_line.isVisible());
		}else{
			//remove and draw
			
		}
	}
	
	private void removePath(Polyline oldLine){
		oldLine.remove();
		m_line=null;
	}
	
	private void insertFacilities(Location location){
		
		if(!markedFacilities.isEmpty()){
			for(Iterator<Marker> mf = markedFacilities.iterator(); mf.hasNext();){
				RemovePin(mf.next());
			}
			markedFacilities.clear();
		}
		
		List<FacilityProfile> facilities = new ArrayList<FacilityProfile>();
		
		try{
			XMLParseHandler parser = new XMLParseHandler();
			for(String fileName : m_dataType){
				facilities.addAll(parser.parse(getAssets().open(fileName+".xml")));
				System.out.println("Loading File ["+fileName+"] Success Total: "+ facilities.size());
			}
			
		} catch (IOException e) {
            e.printStackTrace();
        }
		
		List<FacilityProfile> inRangeFacilities = new ArrayList<FacilityProfile>();
		
		if(facilities!=null){
			
			int rangeVal = 1000;
			
			while(inRangeFacilities.size()<=M_MIN_FACILITY_NUMBER&&facilities.size()>0){
				
				System.out.println("Searching for Facilities within Range: "+rangeVal);
				
				for(Iterator<FacilityProfile> fp = facilities.iterator(); fp.hasNext();){
					
					FacilityProfile fpItem = fp.next();
					
					boolean inRange = CoordsUtil.CoordsInRange(location, fpItem.toLocation(), rangeVal);
					
					if(inRange){
						
						inRangeFacilities.add(fpItem);
					}
				}
				
				System.out.println("Facility Found: " + inRangeFacilities.size() + ", Total: " +  facilities.size());
				
				for(Iterator<FacilityProfile>fp_found = inRangeFacilities.iterator(); fp_found.hasNext();){
					
					facilities.remove(fp_found.next());
				
				}
				
				rangeVal = rangeVal + M_MIN_MIN_RANGE;
			}
		}
		
		 
		//Facilities in Range is available, now we can insert pin
		for(Iterator<FacilityProfile> fp = inRangeFacilities.iterator(); fp.hasNext();){
			
			FacilityProfile fpItem = fp.next();
			int iconVal = 0;
			if(fpItem.GetName().contains("Emergency")||fpItem.GetName().contains("EMS")){
				iconVal = R.drawable.map_hospital;
			}else{
				iconVal = R.drawable.map_fire;
			}
			BitmapDescriptor fpIcon = BitmapDescriptorFactory.fromResource(iconVal);
			
			markedFacilities.add(AddPin(fpItem.toLocation(), fpItem.GetName(), fpItem.GetAddress(), fpIcon));
		
		}
		
	}
	
	private void init_locationManager() {		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, 5, this);
	}

	private void initMap(){
		// Do a null check to confirm that we have not already instantiated the map.
        if (mGoogleMap == null) {

        	mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        	// Check if we were successful in obtaining the map.
            if (mGoogleMap != null) {
            	
            	mGoogleMap.setMyLocationEnabled(true);
            	mGoogleMap.setOnMyLocationChangeListener(this);
            	mGoogleMap.setOnMyLocationButtonClickListener(this);
            	mGoogleMap.setOnMarkerClickListener(this);
            
            }
        }
	}
	
	private void ZoomView(int zoomLevel){
		
		mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel));

	}
	
	private void MoveView(Location location){

		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(ConvertToLatLng(location)));
		
	}
	private LatLng ConvertToLatLng(Location location){
		
		return new LatLng(location.getLatitude(), location.getLongitude());
		
	}
	
	private Marker AddPin(Location location, String locationTitle){
		
		Marker newMarker = mGoogleMap.addMarker(new MarkerOptions().position(ConvertToLatLng(location)).title(locationTitle));
		newMarker.showInfoWindow();
		return newMarker;
	}
	
	private Marker AddPin(Location location, String locationTitle, String description, BitmapDescriptor icon){
		
		Marker newMarker = mGoogleMap.addMarker(new MarkerOptions().position(ConvertToLatLng(location))
																	.title(locationTitle)
																	.icon(icon)
																	.snippet(description));
		newMarker.showInfoWindow();
		return newMarker;
	}
	
	private void RemovePin(Marker oldMarker){
		
		oldMarker.remove();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onMyLocationButtonClick() {
		Log.i("Location button clicked","Location button clicked!!");
		// TODO Auto-generated method stub
		Location myLocation = mGoogleMap.getMyLocation();
		if(myLocation !=null){
			Log.i("myLocation is not null!!","moving camera view, changing zoom view and inserting facils!!");
			
			MoveView(myLocation);
			ZoomView(M_DEFAULT_ZOOM_LEVEL);
			insertFacilities(myLocation);
		}
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.i("Location Changed", "new location");
		
	}
	
	@Override
	public void onMyLocationChange(Location location) {
		// TODO Auto-generated method stub
		Log.i("MyLocation Changed", "User moved to Location");
		
		CameraPosition curCameraPo =  mGoogleMap.getCameraPosition();
		Location cameraLocation = new Location (location.getProvider());
		cameraLocation.setLatitude(curCameraPo.target.latitude); 
		cameraLocation.setLongitude(curCameraPo.target.longitude); 
		boolean inScreen = CoordsUtil.CoordsInRange(location, cameraLocation, 100);
		
		if(m_myMarker==null){
			
			ZoomView(M_DEFAULT_ZOOM_LEVEL);
			//insert Facilities at initial Load
			insertFacilities(location);
			
		}else if(inScreen){
			
			RemovePin(m_myMarker);
		}
		
		
		if(m_myMarker==null || inScreen){
			MoveView(location);
			m_myMarker = AddPin(location, "My Location");
		}
		
	}

	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		System.out.println("Marker Clicked");
		if(!marker.equals(m_myMarker)){
			System.out.println("Different Marker Clicked");
			if(m_line!=null){
				System.out.println("Remove Line : " +m_line.toString());
				System.out.println("Line is Geodesic : " +m_line.isGeodesic());
				System.out.println("Line is visible : " +m_line.isVisible());
				removePath(m_line);
			}
			drawPath(m_myMarker.getPosition(), marker.getPosition());
			marker.showInfoWindow();
			return true;
		}
			
		
		return false;
	}


}
