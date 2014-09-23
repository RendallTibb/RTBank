package com.custom.rtbank;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.custom.rtbank.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FilePicker extends ListActivity {
    private List<String> directoryEntries = new ArrayList<String>();
    private File currentDirectory = new File("/sdcard");
      
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        // setContentView() gets called within the next line,
        // so we do not need it here.
        browseTo(new File("/sdcard"));
    }
               
    /**
    * This function browses up one level
    * according to the field: currentDirectory
    */
    private void upOneLevel(){
        if(this.currentDirectory.getParent() != null)
        this.browseTo(this.currentDirectory.getParentFile());
    }
               
    private void browseTo(final File aDirectory){
       	if (aDirectory.isDirectory()){  //If it's a directory, browse to it.
       		this.currentDirectory = aDirectory;
       		fill(aDirectory.listFiles());
        }else{  //If not, pass path of file to the text box in preferences and close this class
        	setResult(RESULT_OK, (new Intent()).setAction(aDirectory.getAbsolutePath()));
            finish();
        }
    }    

    //Fill ListActivity display with formated output of selected directory
    private void fill(File[] files) {
    	this.directoryEntries.clear();
                       
    	// Add the "." and the ".." == 'Up one level'
    	try {
        	Thread.sleep(10);
    	} catch (InterruptedException e1) {
        	// TODO Auto-generated catch block
        	e1.printStackTrace();
    	}
    	this.directoryEntries.add(".");
    	if(!this.currentDirectory.getPath().equalsIgnoreCase("/sdcard")){
    		this.directoryEntries.add(".."); //no browsing above sd directory
    	}
    	int currentPathStringLenght = this.currentDirectory.getAbsolutePath().length();
        // TODO Add code here to grab path subtracted by currentPathStringLenght and display path at top
        for (File file : files){
        	if(file.isDirectory()){
        		this.directoryEntries.add(file.getAbsolutePath().substring(currentPathStringLenght + 1) + "/");
        	}else{
        		this.directoryEntries.add(file.getAbsolutePath().substring(currentPathStringLenght + 1));
        	}
        }
        ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, R.layout.filepickerrow, this.directoryEntries);
                      
    	this.setListAdapter(directoryList);
    }
    
    //when file/directory is selected, pass it's path to browseTo
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
    	int selectionRowID = (int) position;
    	String selectedFileString = this.directoryEntries.get(selectionRowID);
    	File clickedFile = null;
    	if (selectedFileString.equals(".")) {
        	// Refresh
        	this.browseTo(this.currentDirectory);
    	} else if(selectedFileString.equals("..")){
        	this.upOneLevel();
    	} else {  //Current Directory path + file name...or directory name removing the "/" at the end
           	clickedFile = new File(this.currentDirectory.getAbsolutePath() + "/" + this.directoryEntries.get(selectionRowID));
        }
        if(clickedFile != null){
            this.browseTo(clickedFile);
        }
	}
}
