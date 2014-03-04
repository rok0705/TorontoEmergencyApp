package com.example.emergency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.util.FacilityProfile;
import com.example.util.XMLParseHandler;

public class FacilityActivity extends Activity {

	ListView facilityListView;
	private String _dataType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facility);
		facilityListView = (ListView) findViewById(R.id.facilityListView);
		List<FacilityProfile> facilities = null;
		
		Bundle dataType = this.getIntent().getExtras();
		
		_dataType = dataType.getString("key");
		
		try{
			XMLParseHandler parser = new XMLParseHandler();
			facilities = parser.parse(getAssets().open(_dataType+".xml"));
			ArrayAdapter<FacilityProfile> adapter =
	                new ArrayAdapter<FacilityProfile>(this,R.layout.facility_item, facilities);
			facilityListView.setAdapter(adapter);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facility, menu);
		return true;
	}

}
