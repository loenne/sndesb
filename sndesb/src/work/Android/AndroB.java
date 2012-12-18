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
public class AndroB extends Activity {

	String urlString = "organisations";

	boolean fetchOk = false;
	private Config cfg;
	private Integer mySelectedSearchInterval;
	private Integer mySelectedForbundId;
	private Integer mySelectedClubId;
	private int openstate;
		
    static final int DATE_DIALOG_ID1 = 0;
    static final int DATE_DIALOG_ID2 = 2;

    static final String ORGTYPE_SOFT = "1";
    static final String ORGTYPE_FORB = "2";
    static final String ORGTYPE_KLUBB = "3";
    static final String ORG_STOCKHOLM = "18";
	
    ///////////////////////////////////////////////////////////
    //
    //
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
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchConfig() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		int openstate = 0;

		try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
    		Log.d("SNDESB:","Fetch config: Database does not seem to exist. Try creating it");		
    		createDatabase();
    		Log.d("SNDESB:","Fetch config: Try to create config table");		
    		createConfigTable();
    		Log.d("SNDESB:","Fetch config: Try to create organisations table");		
    		createOrganisationsTable(false);
    		
    		try {
            	myDbHelper.openDataBase(openstate);
            }catch(SQLException sqle2){ 
               	throw sqle2;
            }
        }
		Log.d("SNDESB:","Fetch config: Try to fetch config");		
		cfg = myDbHelper.getConfig();
		mySelectedSearchInterval  = cfg.getSearchIntervall();
		mySelectedForbundId = cfg.getSelectedOrg();
		mySelectedClubId   = cfg.getSelectedClub();

		Log.d("SNDESB:","Fetched config: SearchIntervall: "+mySelectedSearchInterval + " SelectedOrg: "+mySelectedForbundId+" SelectedClub: " + mySelectedClubId);		
		myDbHelper.close();
	}	    
    

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void createDatabase() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
	 
		// To be done once to create the database
		try { 
			myDbHelper.createDataBase(); 
		} catch (IOException ioe) {
			throw new Error("Unable to create database"); 
		}
		myDbHelper.close();
	}	
	
	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public void createConfigTable() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);

		try {
			myDbHelper.openDataBase(openstate);
		}catch(SQLException sqle){ 
			throw sqle;
		}

		try {
			Log.d("SNDESB", "Create config Table");		
			myDbHelper.createConfigTable(false);
		}catch(SQLException sqle){ 
			throw sqle;
		}

		try {
			Log.d("SNDESB", "Add record in config Table");		
			myDbHelper.addConfigRecord();
		}
		catch(SQLException sqle) {
			throw sqle;
		}

		myDbHelper.close();
	}	

	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public void createOrganisationsTable(boolean drop) {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);

		try {
			myDbHelper.openDataBase(openstate);
		}catch(SQLException sqle){ 
			throw sqle;
		}

		try {
			Log.d("SNDESB", "Create Organisations Table");		
			myDbHelper.createOrganisationsTable(drop);	
		}catch(SQLException sqle){ 
			throw sqle;
		}

		myDbHelper.close();
	}	

	///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void myClickHandler(View view) {

		switch (view.getId()) {
			// Tävlingar
			case R.id.tavlingsval:
			{
                Intent i = new Intent();
                i.setClassName("work.Android", "work.Android.selecttavling");
        		String sendbuff[] = new String[2];
        		sendbuff[0] = "FORBUNDID";
        		sendbuff[1] = mySelectedForbundId.toString();
      			Log.e("SNDESB","AndroB: Start selectTavling with forbundsid: " + sendbuff[1]);
        		i.putExtra("arguments", sendbuff); 
                startActivity(i);
				return;
			}
			// Klubbanmälningar
			case R.id.klubbval:
			{
                Intent i = new Intent();
                i.setClassName("work.Android", "work.Android.selectklubb");
        		String sendbuff[] = new String[6];
        		sendbuff[0] = "SEARCHLENGTH";
        		sendbuff[1] = mySelectedSearchInterval.toString();
        		sendbuff[2] = "FORBUNDID";
        		sendbuff[3] = mySelectedForbundId.toString();
        		sendbuff[4] = "CLUBID";
        		sendbuff[5] = mySelectedClubId.toString();
      			Log.e("SNDESB","AndroB: Start selectKlubb with SearchInterval: " + sendbuff[1] + " forbundsid: " + sendbuff[3] + " KlubbId:" + sendbuff[5]);
                i.putExtra("arguments", sendbuff); 
                startActivity(i);               
				return;
			} 
			// Löpare
			case R.id.loparval:
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
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void konfigurera() {

        Intent i = new Intent();
		String sendbuff[] = new String[6];
        i.setClassName("work.Android", "work.Android.configApp");
		sendbuff[0] = "SEARCH_LENGTH";
		sendbuff[1] = mySelectedSearchInterval.toString();
		sendbuff[2] = "FORBUND";
		sendbuff[3] = mySelectedForbundId.toString();
		sendbuff[4] = "KLUBB";
		sendbuff[5] = mySelectedClubId.toString();
        i.putExtra("arguments", sendbuff); 
        startActivity(i);               
		return;
	
	}
	
	///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void showAbout() {

        showabout sab = new showabout(this);
        sab.show();
		return;
	
	}

	///////////////////////////////////////////////////////////
	//
	//
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
	//
	//
	///////////////////////////////////////////////////////////
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId()) {
        case R.id.konfig:
            konfigurera();
            return true;
        case R.id.aboutme:
            showAbout();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }    
}		