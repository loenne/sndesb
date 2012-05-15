package work.Android;

//import android.app.Activity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import work.Android.selectklubb.MyOnItemSelectedListener1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
// import android.os.Parcelable;
//import android.content.SharedPreferences;
//import android.preference.ListPreference;
//import android.preference.PreferenceManager;
//import android.preference.Preference;
//import android.preference.PreferenceActivity;
import android.app.Activity;
//import android.preference.Preference.OnPreferenceClickListener;
//import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class configApp extends Activity {
//public class configApp extends PreferenceActivity { 
	final int MAX_FORBUND = 24;
	private ArrayList<String> organisationId;
	private ArrayList<String> organisationTypeId;
	private ArrayList<String> name;
	private ArrayList<String> shortName;
	private ArrayList<String> parentOrganisationId;
	private List<Organisation> organisations;
	private ArrayAdapter<String> mySpinnerForbundArrayAdapter;
	private ArrayAdapter<String> mySpinnerKlubbArrayAdapter;	
	private ArrayAdapter<String> mySpinnerKlubbArrayAdapter2;	
	private TextView mySearchLength;
	private Spinner  myForbundSpinner;
	private Spinner  myKlubbSpinner;
	private Spinner  myKlubbSpinner2;
	private Button myCreateDatabase; 
	private Button myUpdateEventor;
	private ArrayList<String> oforbund;
	private ArrayList<String> oforbundid;
	private ArrayList<String> oklubbar;
	private ArrayList<String> oklubbarid;
	private String mySelKlubb;
	private String mySelKlubbId;	
	private String mySelKlubbIndx;	
	private String mySelForbund;
	private String mySelForbundId;
	private int mySelForbundIndx;
	private List <Organisation> allaOrg;
	private Config cfg;
	private int openstate;
	
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configapp);

		oforbund = new ArrayList<String>();
		oforbundid = new ArrayList<String>();
		oklubbar = new ArrayList<String>();
		oklubbarid = new ArrayList<String>();
		oforbund.clear();
		oforbundid.clear();
		oklubbar.clear();
		oklubbarid.clear();
		mySelForbundIndx = 0;
		
		mySearchLength = (TextView)findViewById(R.id.configSearchLengthField);
   		myForbundSpinner = (Spinner)findViewById(R.id.configSearchForbundField);
		myKlubbSpinner = (Spinner)findViewById(R.id.configSearchKlubbField);
		myCreateDatabase = (Button)findViewById(R.id.configcreatedatabase);
   		myUpdateEventor = (Button)findViewById(R.id.configupdateeventor);
   		
//   	//createConfigTable();
   		fetchConfig();

   		mySearchLength.setText(String.valueOf(cfg.getSearchIntervall()));
   		
		Log.d("XTRACTOR","configApp: Fetch all forbund records from config database");
		fetchAllOrganisationNamesAndIds();
//		fetchOrgClubs(1);
		Log.d("XTRACTOR","configApp: Fetch all clubs belonging to configured forbund: " + cfg.getSelectedOrg() + " from config database");
		fetchOrgClubNamesAndIds(cfg.getSelectedOrg());
   		
		mySpinnerForbundArrayAdapter =  
	   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oforbund);
		   		mySpinnerForbundArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);		   		
		   		myForbundSpinner.setAdapter(mySpinnerForbundArrayAdapter);
		   		myForbundSpinner.setOnItemSelectedListener(new MyOnForbundItemSelectedListener()); 	   	 	   		 	   		 	   		 	   		

		mySpinnerKlubbArrayAdapter =  
	   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oforbund);
				mySpinnerKlubbArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);
				myKlubbSpinner.setAdapter(mySpinnerKlubbArrayAdapter);
				myKlubbSpinner.setOnItemSelectedListener(new MyOnKlubbItemSelectedListener());		
  		
//		addPreferencesFromResource(R.layout.configapp);

/*		organisationId = new ArrayList<String>();
		organisationTypeId = new ArrayList<String>();
		name= new ArrayList<String>();
		parentOrganisationId = new ArrayList<String>();
		shortName = new ArrayList<String>();
*/	    
//		openstate = 0; //0=readwrite, 1 = readonly
		
//		int rec = fetchOrganisations();
//		Log.e("XTRACTOR","configApp Fetched:" + rec + " records from eventor");

//		if (rec>0) {
//			updateDatabase(rec);
//		}

//		updateDatabase(0);
		
//		fetchOneOrganisation(1200);
		
		mySpinnerForbundArrayAdapter.notifyDataSetChanged();
		mySpinnerKlubbArrayAdapter.notifyDataSetChanged();
		myForbundSpinner.setSelection(mySelForbundIndx,true);
   		myKlubbSpinner.setSelection(1,true);
	}

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchOneOrganisation(int id) {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		// Fetch one
		Organisation org1 = myDbHelper.getOrganisation(id);
		org1.printRecord();
				
		myDbHelper.close();
	}

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchAllOrganisations() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		
		allaOrg = myDbHelper.getAllOrganisations();
		
		for (Organisation org : allaOrg) {
			oforbund.add(org.getName());
			String log = "OrgId: "+org.getOrganisationId() + "TypeId: "+org.getOrganisationTypeId()+" ,Name: " + org.getName() + " ,ShortName: " + org.getShortName() + " ,ParentOrgId: " + org.getParentOrganisationId();
            // Writing Contacts to log
            Log.d("Org: ", log);		
		}
		myDbHelper.close();
	}

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchAllOrganisationNames() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		
		oforbund = myDbHelper.getAllForbundNames();
		
		for (String oforb : oforbund) {
			String log = "Name: " + oforb;
//            // Writing Contacts to log
            Log.d("Org: ", log);		            
		}
		myDbHelper.close();
	}

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchAllOrganisationNamesAndIds() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		
		oforbund = myDbHelper.getAllForbundNames();
		oforbundid = myDbHelper.getAllForbundIds();

   		mySelForbundId = String.valueOf(cfg.getSelectedOrg());
		int i = 0;

		Log.d("XTRACTOR","Search for selected forbundid: " + mySelForbundId + " among forbunds");

		for (String oforbid : oforbundid) {
		//	Log.d("XTRACTOR","Search index: " + i + "forbundid: " + oforbid);

			if (oforbid.equals(mySelForbundId)) {
				mySelForbundIndx = i;
				mySelForbundId = oforbundid.get(i);
				Log.d("XTRACTOR","Found forbundsid: " + mySelForbundId + " in index : " + i + " name:" + oforbund.get(i));
			}
	//		String log = "ForbundId: " + oforbid;
    //        Log.d("Organisation: ", log);
            i++;
		}
		myDbHelper.close();
	}
	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchConfig() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		
		cfg = myDbHelper.getConfig();
		String log = "SearchIntervall: "+cfg.getSearchIntervall() + " SelectedOrg: "+cfg.getSelectedOrg()+" SelectedClub: " + cfg.getSelectedClub();
		Log.d("Fetched config: ", log);		
		myDbHelper.close();
	}
	
	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchOrgClubNamesAndIds(int id) {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		oklubbar.clear();
		oklubbarid.clear();
		
		oklubbar = myDbHelper.getOrgClubNames(id);
		oklubbarid = myDbHelper.getOrgClubNameIds(id);
	
		for (String klubb : oklubbar) {
            Log.d("Klubb: ", klubb);		
		}
		myDbHelper.close();
	}
	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchOrgClubs(int id) {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		
		allaOrg = myDbHelper.getOrgClubs(id);
		
		for (Organisation org : allaOrg) {
            String log = "Id: "+org.getOrganisationId() + "TypeId: "+org.getOrganisationTypeId()+" ,Name: " + org.getName() + " ,ShortName: " + org.getShortName() + " ,ParentOrgId: " + org.getParentOrganisationId();
            // Writing Contacts to log
            Log.d("Org: ", log);		
		}
		myDbHelper.close();
	}

	///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void updateDatabase(int records) {
		
		DataBaseHelper myDbHelper = new DataBaseHelper(this);
 
		// To be done once to create the database
        //try { 
        // 	myDbHelper.createDataBase(); 
        //} catch (IOException ioe) {
        //	throw new Error("Unable to create database"); 
        //}
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }

// create main table
/*
          try {
        	myDbHelper.createTable();
        }catch(SQLException sqle){ 
        	throw sqle;
        }
*/       

// create config table
/*        try {
       	myDbHelper.createConfigTable();
       }catch(SQLException sqle){ 
       	throw sqle;
       }
 */       
       	try {
       		myDbHelper.addConfigRecord();
       	}
       	catch(SQLException sqle) {
       		throw sqle;
       	}
 //       organisation org = new organisation();
 //       int ind = 0;

/*
         while (ind < records) {
 
        	Log.e("XTRACTOR","configApp: OrganisationId       " + organisationId.get(ind));
        	Log.e("XTRACTOR","configApp: OrganisationTypeId   " + organisationTypeId.get(ind));
        	Log.e("XTRACTOR","configApp: Name                 " + name.get(ind));
        	Log.e("XTRACTOR","configApp: ParentOrganisationId " + parentOrganisationId.get(ind));
        	Log.e("XTRACTOR","configApp: ShortName            " + shortName.get(ind));
        	ind++;
        }
 */
        // To be done each time the organisations have changed
/*        while (ind < records) {
            org.setOrganisationId(organisationId.get(ind));
            org.setOrganisationTypeId(organisationTypeId.get(ind));
            org.setName(name.get(ind));
            org.setParentOrganisationId(parentOrganisationId.get(ind));
            org.setShortName(shortName.get(ind));

           	try {
           		myDbHelper.addRecord(org);
           	}
           	catch(SQLException sqle) {
           		throw sqle;
           		
           	}
           	ind++;
        }       
*/
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

		//create config table
		try {
			myDbHelper.createConfigTable();
		}catch(SQLException sqle){ 
			throw sqle;
		}

		try {
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
	public int fetchOrganisations() {
	
		int count = 0;
		String urlString = "organisations";

		boolean fetchOk = false;

		Log.d("XTRACTOR","configApp: fetchOrganisations");

		organisationId.clear();
		organisationTypeId.clear();
		name.clear();
		parentOrganisationId.clear();
		shortName.clear();

		try{
			RestAPI andRest = new RestAPI();
			fetchOk = andRest.queryRESTurl(urlString);

			if (fetchOk) {
				Log.d("XTRACTOR","configApp: fetchOK");
				organisations = andRest.parseOrganisations();    		

				// 1 = Orienteringsf�rbundet
				// 2 = Forbund
				// 3 = Klubb

				for (Organisation org : organisations){
					Log.d("XTRACTOR","configApp: loop index:" + count);
					
					organisationId.add(org.getOrganisationId());
					organisationTypeId.add(org.getOrganisationTypeId());
					name.add(org.getName());
					shortName.add(org.getShortName());
					parentOrganisationId.add(org.getParentOrganisationId());	   				

					/*	  					if ((org.getOrganisationTypeId().equals(ORGTYPE_SOFT)) ||
	  						(org.getOrganisationTypeId().equals(ORGTYPE_FORB))) {
	  						oforbund.add(org.getShortName());
	  						oforbundid.add(org.getOrganisationId());
	  						Log.e("XTRACTOR","Stored : " + org.getShortName() + " id: " + org.getOrganisationId() + " in pos : " + i);

	  						if (org.getOrganisationId().equals(ORG_STOCKHOLM)) {
	  							activeIndex = i;
	  	  						Log.e("XTRACTOR","Found Stockholm as 18 in index : " + i);
	  	  						mySelForbund = oforbund.get(i);
	  	  	   	    			mySelForbundId = oforbundid.get(i);  	  						
	  						}  						
						}
					 */
					count++;
				}
			} else {
				Toast.makeText(this, "No network connection. Check mobile newtwok",
						Toast.LENGTH_LONG).show();
			}
		} catch (Throwable t){
			Log.e("XTRACTOR","loadForbund : Failing to fetch forbund : " + t.getMessage(),t);
		}

		return count;
	}
	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
  	public void myCreateDatabaseClickHandler(View view) {

   		switch (view.getId()) {
   			case R.id.configcreatedatabase:
   			{	   				
   				Log.d("XTRACTOR","myCreateDatabaseClickHandler Want to create Config database");
   	            return;
   			}
   			case R.id.configupdateeventor:
   			{	   				
   				Log.d("XTRACTOR","myCreateDatabaseClickHandler Want to fill database from Eventor");
   	            return;
   			}
   		}		
   	}
   	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
  	public class MyOnForbundItemSelectedListener implements OnItemSelectedListener 
   	{
   	    public void onItemSelected(AdapterView<?> parent,
   	        View view, int pos, long id) {
			Log.d("XTRACTOR","MyOnForbundItemSelectedListener: Selected pos : " + pos + " som �r : " + parent.getItemAtPosition(pos).toString() + " och id:" + oforbundid.get(pos) );
			mySelForbund = parent.getItemAtPosition(pos).toString();
			mySelForbundId = oforbundid.get(pos);
			mySelForbundIndx = pos;
			Integer val = new Integer(mySelForbundId);
			Log.d("XTRACTOR","MyOnForbundItemSelectedListener: Before" );
			fetchOrgClubNamesAndIds(val);
			mySpinnerKlubbArrayAdapter.notifyDataSetChanged();
//	   		myKlubbSpinner.setSelection(0,true);	
			Log.d("XTRACTOR","MyOnForbundItemSelectedListener: After" );
   	    }

   	    public void onNothingSelected(AdapterView<?> parent) {
   	    	//mySelForbund = oforbund.get(0);
			//mySelForbundId = oforbundid.get(0);
			Log.d("XTRACTOR","MyOnForbundItemSelectedListener: Nothing selected" );
   	    }
   	}
	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
  	public class MyOnKlubbItemSelectedListener implements OnItemSelectedListener 
   	{
   	    public void onItemSelected(AdapterView<?> parent,
   	        View view, int pos, long id) {
			Log.d("XTRACTOR","MyOnKlubbItemSelectedListener: Selected pos : " + pos + "som �r : " + parent.getItemAtPosition(pos).toString() );
			mySelKlubb = parent.getItemAtPosition(pos).toString();
			
//			if (klubbIdCreated == 1) {
//				mySelKlubbId = klubbid.get(pos);			}
//   	    }
   	    }
   	    public void onNothingSelected(AdapterView<?> parent) {
			Log.d("XTRACTOR","MyOnKlubbItemSelectedListener: Nothing selected" );
  	      // Do nothing.
   	    }
   	}  	
  	
	// Get the custom preference
	//        Preference customPref = (Preference) findPreference("customPref");
	//        customPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

	//        	public boolean onPreferenceClick(Preference preference) {
	//        		Toast.makeText(getBaseContext(),
	//        				"The custom preference has been clicked",
	//        				Toast.LENGTH_LONG).show();
	//        		SharedPreferences customSharedPreference = getSharedPreferences(
	//        				"myCustomSharedPrefs", Activity.MODE_PRIVATE);
	//				SharedPreferences.Editor editor = customSharedPreference
	//        				.edit();
	//        		editor.putString("myCustomPref",
	//        				"The preference has been clicked");
	//        		editor.commit();
	//        		return true;
	//        	}

	//       });

	//		Bundle b=this.getIntent().getExtras();
	//		Serializable s = b.getSerializable("arguments");

	//		Serializable s = this.getIntent().getSerializableExtra("arguments");
	//	
	//	}
}