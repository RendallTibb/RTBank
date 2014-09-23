package com.custom.rtbank;

import com.custom.rtbank.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;

public class RTBank extends Activity {
	int fR;
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        fR = 1;
        if (fR == 1){
        	firstRun();
        }
    }

	void firstRun() {
		Intent PreferencesIntent = new Intent().setClass(this, RTPreferences.class);
		startActivity(PreferencesIntent);
		//setContentView(R.layout.preferencesold);
		//populateSpinContacts();
		//populateSpinCarriers();
	}	

	void populateSpinContacts() {
		
		//Create variables for query, then query.
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
        		ContactsContract.Contacts._ID,
        		ContactsContract.Contacts.DISPLAY_NAME
        };
        Cursor curCon = managedQuery(uri,projection,null,null,null);
        
        //Variable for SimpCurAdap, then adapter for query.
        String[] fieldsCon = new String[] {
        		ContactsContract.Contacts.DISPLAY_NAME
        };
        SimpleCursorAdapter adapterCon = new SimpleCursorAdapter(this,
        	android.R.layout.simple_spinner_item, curCon, fieldsCon,
        	new int[] {android.R.id.text1}
        );
        
        //find xml spinner and set adapter data to it.
//        Spinner spinnerContacts = (Spinner) findViewById(R.id.spinContacts);
	    adapterCon.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	    spinnerContacts.setAdapter(adapterCon);
	}

	void populateSpinCarriers(){
//	    Spinner spinnerCarriers = (Spinner) findViewById(R.id.spinCarriers);
	    ArrayAdapter<CharSequence> adapterCar = ArrayAdapter.createFromResource(
	            this, R.array.carriers, android.R.layout.simple_spinner_item);
	    adapterCar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	    spinnerCarriers.setAdapter(adapterCar);
	}
	
}