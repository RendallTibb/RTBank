package com.custom.rtbank;

import java.io.File;
import java.util.List;

import com.custom.rtbank.R;
import com.custom.rtbank.DialogPreferenceEx.OnClickListener;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class RTPreferences extends PreferenceActivity implements OnClickListener {

	static final private int ACTIVITY_ID = 5; //For FilePicker. Doesn't matter for this program. Must be positive. 
	String savedPath = "";
	static final private String KEY_FILE = "key_file"; //Connects with DialogPreferenceEx class in XML.
	
	//Launching, Puts users selected path into text box
	private final class FilePreferenceOnClickListener implements OnPreferenceClickListener {

		@Override
	    public boolean onPreferenceClick(Preference preference) {
	        DialogPreferenceEx dPEx = (DialogPreferenceEx) preference;
	    	EditText etPath = (EditText)dPEx.getDialog().findViewById(R.id.etxtPath);  //Connect to EditText in Dialog's layout
    		etPath.setText(savedPath);
			return true;
		}
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences);
        populateContactsPreference();
        populateAppsPreference();
    	DialogPreferenceEx dPExClick = (DialogPreferenceEx)getPreferenceScreen().findPreference(KEY_FILE);
        dPExClick.setOnPreferenceClickListener(new FilePreferenceOnClickListener());
	}

	@Override
	public void onClick (DialogInterface dialog, int which){
		DialogPreferenceEx dPEx = (DialogPreferenceEx)getPreferenceScreen().findPreference(KEY_FILE);
		EditText etPath = (EditText)dPEx.getDialog().findViewById(R.id.etxtPath);  //Connect to EditText in Dialog's layout
        File fileCheck = new File(etPath.getText().toString());
    	dPEx.getDialog();
		if((fileCheck.isDirectory() || fileCheck.isFile()) && (which == DialogInterface.BUTTON_POSITIVE)) { //File has been selected
    		savedPath = etPath.getText().toString();
    	}else{			
    	    savedPath = "Bad Path";
    	}
	}
	
	//Browse button located in the File Preference. Launches file picker
    public void btnBrowse(View view) {
		Intent PreferencesIntent = new Intent().setClass(this, FilePicker.class);
		startActivityForResult(PreferencesIntent, ACTIVITY_ID);
    }
    
    //Puts result of file picker in path text box.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       	if(resultCode == RESULT_OK){
        	DialogPreferenceEx dPEx = (DialogPreferenceEx)getPreferenceScreen().findPreference(KEY_FILE);
        	EditText etPath = (EditText)dPEx.getDialog().findViewById(R.id.etxtPath);  //Connect to EditText in Dialog's layout
       		File fileCheck = new File(data.getAction());
        	if(fileCheck.isDirectory() || fileCheck.isFile()) { //File has been selected
        		etPath.setText(data.getAction());
        	}else{ //Something went wrong
        		etPath.setText("Error: Result - " + data.getAction());
        	}
        }
    }

	void populateContactsPreference() {
		//Create variables for query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
        		ContactsContract.Contacts._ID,
        		ContactsContract.Contacts.DISPLAY_NAME
        };
        Cursor curCon = managedQuery(uri,projection,null,null,null);  //query
        int contactsCount = curCon.getCount();  //array count
        String[] arrayContacts = new String[contactsCount];  //contacts array
        int nameColumn = curCon.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);  //picks name column
        for(int i = 0; !(curCon.isLast()); i++){  //fill array
          	curCon.moveToNext();  //cursor starts at -1, first record starts at 0 so move nextToNext first.
        	arrayContacts[i] = curCon.getString(nameColumn);  //reads records from name column
        }
       	String keyCon = "key_contact";  //key for ListPreference from xml
        ListPreference fillContacts = (ListPreference)getPreferenceScreen().findPreference(keyCon);  //connect to ListPreference from xml.
        fillContacts.setEntries(arrayContacts);  //set filled array to Listpreference in xlm
        fillContacts.setEntryValues(arrayContacts);
	}

	void populateAppsPreference() {
		PackageManager manager = getPackageManager();
        List<PackageInfo> apps = manager.getInstalledPackages(0);
        int appsCount = apps.size();  //array count
        String[] arrayApps = new String[appsCount];
        for (int i = 0; i < appsCount; i++) {
            PackageInfo info = apps.get(i);
            arrayApps[i] = info.packageName;
        }
       	String keyCon = "key_app";  //key for ListPreference from xml
        ListPreference fillApps = (ListPreference)getPreferenceScreen().findPreference(keyCon);  //connect to ListPreference from xml.
        fillApps.setEntries(arrayApps);  //set filled array to Listpreference in xlm
        fillApps.setEntryValues(arrayApps);
	}

	//Old, Not used. Saved for reference. Delete in finished product.
	void populateSpinCarriers(){
//	    Spinner spinnerCarriers = (Spinner) findViewById(R.id.spinCarriers);
	    ArrayAdapter<CharSequence> adapterCar = ArrayAdapter.createFromResource(
	            this, R.array.carriers, android.R.layout.simple_spinner_item);
	    adapterCar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	    spinnerCarriers.setAdapter(adapterCar);
	}
}
