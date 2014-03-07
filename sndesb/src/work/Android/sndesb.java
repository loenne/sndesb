package work.Android;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.database.SQLException;

///////////////////////////////////////////////////////////
//
//	Class AndroB
//
///////////////////////////////////////////////////////////
public class sndesb extends Activity {

	String urlString = "organisations";

	boolean fetchOk = false;
	private Config cfg;
	private Integer mySelectedSearchInterval;
	private Integer mySelectedForbundId;
	private Integer mySelectedClubId;
	private int openstate;
		
    static final int DATE_DIALOG_ID1 = 0;
    static final int DATE_DIALOG_ID2 = 2;

    static final String ORGTYPE_SOFT  = "1";
    static final String ORGTYPE_FORB  = "2";
    static final String ORGTYPE_KLUBB = "3";
    static final String ORG_STOCKHOLM = "18";
	
    ///////////////////////////////////////////////////////////
    //
    // onCreate
    //
    ///////////////////////////////////////////////////////////
   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		fetchConfig();
	}

    ///////////////////////////////////////////////////////////
    //
    // fetchConfig
    //
    ///////////////////////////////////////////////////////////
	public void fetchConfig() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		int openstate = 0;

		boolean dbExist = myDbHelper.checkDataBase();

		if (dbExist) {

			try {
				myDbHelper.openDataBase(openstate);

				Log.d("SNDESB","fetchConfig: Try to fetch config");		
				cfg = myDbHelper.getConfig();
				mySelectedSearchInterval  = cfg.getSearchIntervall();
				mySelectedForbundId = cfg.getSelectedOrg();
				mySelectedClubId   = cfg.getSelectedClub();

				Log.d("SNDESB","fetchConfig: SearchIntervall: "+mySelectedSearchInterval + " SelectedOrg: "+mySelectedForbundId+" SelectedClub: " + mySelectedClubId);		
				myDbHelper.close();

			}catch(SQLException sqle){ 
				Log.d("SNDESB","fetchConfig: Database does not seem to exist. Try creating it");		
			}

		// Database seems not to exist. First time app started ? We try to create it
		} else {
			Log.d("SNDESB","fetchConfig: Database could not be opened. First time app started ? We try to create it");		
			dbSetup();
			
		}
	}	    
    
	///////////////////////////////////////////////////////////
	//
	// dbSetup
	//
	///////////////////////////////////////////////////////////
	public void dbSetup() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);

		Log.d("SNDESB","dbSetup: Create a new SQLite database");		
		createDatabase(myDbHelper);

		try {
			myDbHelper.openDataBase(openstate);
	        Log.d("SNDESB","dbSetup: Database opened");       
		}catch(SQLException sqle){ 
			throw sqle;
		}
		
		Log.d("SNDESB","dbSetup: Create a config table");		
		createConfigTable(myDbHelper);
		Log.d("SNDESB","dbSetup: Add default values to config table");		
		addConfigTableRecord(myDbHelper);
		Log.d("SNDESB","dbSetup: Try to create organisations table");		
		createOrganisationsTable(myDbHelper, false);

		
		
		myDbHelper.close();	
	}
	

    ///////////////////////////////////////////////////////////
    //
    // createDatabase
    //
    ///////////////////////////////////////////////////////////
	public void createDatabase(DataBaseHelper myDbHelper) {

		// To be done once to create the database
		try { 
			myDbHelper.createDataBase(); 
		} catch (IOException ioe) {
			throw new Error("Unable to create database"); 
		}
	}	
	
	///////////////////////////////////////////////////////////
	//
	// createConfigTable
	//
	///////////////////////////////////////////////////////////
	public void createConfigTable(DataBaseHelper myDbHelper) {

		try {
			Log.d("SNDESB", "Create config Table");		
			myDbHelper.createConfigTable(false);
		}catch(SQLException sqle){ 
			throw sqle;
		}
	}	

	///////////////////////////////////////////////////////////
	//
	// createOrganisationsTable
	//
	///////////////////////////////////////////////////////////
	public void addConfigTableRecord(DataBaseHelper myDbHelper) {
		
		try {
			Log.d("SNDESB", "Add record in config Table");		
			myDbHelper.addConfigRecord();
		}
		catch(SQLException sqle) {
			throw sqle;
		}
	}
	
	///////////////////////////////////////////////////////////
	//
	// createOrganisationsTable
	//
	///////////////////////////////////////////////////////////
	public void createOrganisationsTable(DataBaseHelper myDbHelper, boolean drop) {

		try {
			Log.d("SNDESB", "Create Organisations Table");		
			myDbHelper.createOrganisationsTable(drop);	
		}catch(SQLException sqle){ 
			throw sqle;
		}
	}	

	///////////////////////////////////////////////////////////
    //
    // myClickHandler
    //
    ///////////////////////////////////////////////////////////
	public void myClickHandler(View view) {

		switch (view.getId()) {
			// Events
			case R.id.eventchoice:
			{
                Intent i = new Intent();
                i.setClassName("work.Android", "work.Android.selectevent");
        		String sendbuff[] = new String[6];
        		sendbuff[0] = "SEARCHLENGTH";
        		sendbuff[1] = mySelectedSearchInterval.toString();
        		sendbuff[2] = "FORBUNDID";
        		sendbuff[3] = mySelectedForbundId.toString();
        		sendbuff[4] = "CLUBID";
        		sendbuff[5] = mySelectedClubId.toString();
      			Log.d("SNDESB","sndesb: Start selectevent with SearchInterval: " + sendbuff[1] + " forbundsid: " + sendbuff[3] + " KlubbId:" + sendbuff[5]);
        		i.putExtra("arguments", sendbuff); 
                startActivity(i);
				return;
			}
			// Clubchoices
			case R.id.clubchoice:
			{
                Intent i = new Intent();
                i.setClassName("work.Android", "work.Android.selectclub");
        		String sendbuff[] = new String[6];
        		sendbuff[0] = "SEARCHLENGTH";
        		sendbuff[1] = mySelectedSearchInterval.toString();
        		sendbuff[2] = "FORBUNDID";
        		sendbuff[3] = mySelectedForbundId.toString();
        		sendbuff[4] = "CLUBID";
        		sendbuff[5] = mySelectedClubId.toString();
      			Log.d("SNDESB","sndesb: Start selectKlubb with SearchInterval: " + sendbuff[1] + " forbundsid: " + sendbuff[3] + " KlubbId:" + sendbuff[5]);
                i.putExtra("arguments", sendbuff); 
                startActivity(i);               
				return;
			} 
			// Runners
			case R.id.runnerchoice:
			{
				Toast.makeText(this, "Du vill hämta alla anmälningar för en person. Inte Implementerat änn !!",
						Toast.LENGTH_LONG).show();
				return;
			}
			default:
			{
				Toast.makeText(this, "Mjukvarufel....!!",
						Toast.LENGTH_LONG).show();
				return;
			}
		}
	}


	
	///////////////////////////////////////////////////////////
    // GENERAL MENU HANDLING
    ///////////////////////////////////////////////////////////

	
	
	
	///////////////////////////////////////////////////////////
	//
	// onCreateOptionsMenu
	//
	///////////////////////////////////////////////////////////
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;		
	}
    
	///////////////////////////////////////////////////////////
	//
	// onOptionsItemSelected
	//
	///////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId()) {
        case R.id.config:
            konfigurera();
            return true;
        case R.id.aboutme:
            showAbout();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }    


    ///////////////////////////////////////////////////////////
    //
    // konfigurera
    //
    ///////////////////////////////////////////////////////////
	public void konfigurera() {

        Intent i = new Intent();
		String sendbuff[] = new String[6];
        i.setClassName("work.Android", "work.Android.configapp");
		sendbuff[0] = "SEARCHLENGTH";
		sendbuff[1] = mySelectedSearchInterval.toString();
		sendbuff[2] = "FORBUNDID";
		sendbuff[3] = mySelectedForbundId.toString();
		sendbuff[4] = "CLUBID";
		sendbuff[5] = mySelectedClubId.toString();
		Log.d("SNDESB","konfigurera: Start configapp with SearchInterval: " + sendbuff[1] + " forbundsid: " + sendbuff[3] + " KlubbId:" + sendbuff[5]);
		i.putExtra("arguments", sendbuff); 
        startActivity(i);               
		return;
	
	}
	
	///////////////////////////////////////////////////////////
    //
    // showAbout
    //
    ///////////////////////////////////////////////////////////
	public void showAbout() {

        showabout sab = new showabout(this);
        sab.show();
		return;
	
	}
}		