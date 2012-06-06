package work.Android;

//import android.app.Activity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import work.Android.selectklubb.MyOnItemSelectedListener1;

import android.content.Context;
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
	private TextView mySearchLength;
	private Spinner  myForbundSpinner;
	private Spinner  myKlubbSpinner;
	private Button myCreateDatabase; 
	private Button myUpdateEventor;
	private Button mySaveConfig;
	private ArrayList<String> oforbund;
	private ArrayList<String> oforbundid;
	private ArrayList<String> oklubbar;
	private ArrayList<String> oklubbarid;
	private String mySelKlubb;
	private String mySelKlubbId;	
	private String mySelForbund;
	private String mySelForbundId;
	
	private int mySelForbundIndx;
	private int mySelKlubbIndx;	
	
	private int myKlubbIdCreated;

	private String myBuffSearchLength;
	private String myBuffSelectedForbund;
	private String myBuffSelectedKlubb;
	
	
	private List <Organisation> allaOrg;
	private Config cfg;
	private Context cont;
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
		myKlubbIdCreated = 0;
		openstate = 0;

		mySearchLength = (TextView)findViewById(R.id.configSearchLengthField);
   		myForbundSpinner = (Spinner)findViewById(R.id.configSearchForbundField);
		myKlubbSpinner = (Spinner)findViewById(R.id.configSearchKlubbField);
		myCreateDatabase = (Button)findViewById(R.id.configcreatedatabase);
   		mySaveConfig = (Button)findViewById(R.id.configsave);
   		
		 Serializable s = this.getIntent().getSerializableExtra("arguments");
		 Object[] o = (Object[]) s;

		 if (o != null) {
	       		myBuffSearchLength = o[1].toString();
	       		myBuffSelectedForbund = o[3].toString();
	       		myBuffSelectedKlubb = o[5].toString();
		 }

   		mySearchLength.setText(String.valueOf(myBuffSearchLength));
   		mySelForbundId = myBuffSelectedForbund;
   		mySelKlubbId = myBuffSelectedKlubb;
   		
		Log.d("SNDESB","configApp: Fetch all forbund records from config database");
		fetchAllOrganisationNamesAndIds();
//		fetchOrgClubs(1);
		Log.d("SNDESB","configApp: Fetch all clubs belonging to configured forbund: " + myBuffSelectedForbund + " from config database");
		int forbId = Integer.parseInt(myBuffSelectedForbund);
		fetchOrgClubNamesAndIds(forbId);
   		  		
		cont = this;

		mySpinnerForbundArrayAdapter =  
	   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oforbund);
	   	mySpinnerForbundArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);		   		
		myForbundSpinner.setAdapter(mySpinnerForbundArrayAdapter);
		myForbundSpinner.setOnItemSelectedListener(new MyOnForbundItemSelectedListener()); 	   	 	   		 	   		 	   		 	   		

		mySpinnerKlubbArrayAdapter =  
			   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oklubbar);
		mySpinnerKlubbArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);
		myKlubbSpinner.setAdapter(mySpinnerKlubbArrayAdapter);
		myKlubbSpinner.setOnItemSelectedListener(new MyOnKlubbItemSelectedListener());		
		
		myForbundSpinner.setSelection(mySelForbundIndx,true);
		myKlubbSpinner.setSelection(mySelKlubbIndx,true);
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

		int i = 0;

		Log.d("SNDESB","Search for selected forbundid: " + mySelForbundId + " among forbunds");

		for (String oforbid : oforbundid) {
		//	Log.d("SNDESB","Search index: " + i + "forbundid: " + oforbid);

			if (oforbid.equals(mySelForbundId)) {
				mySelForbundIndx = i;
				mySelForbundId = oforbundid.get(i);
				Log.d("SNDESB","Found forbundsid: " + mySelForbundId + " in index : " + i + " name:" + oforbund.get(i));
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
	public void updateConfig() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		
		//cfg = myDbHelper.updateConfig();
		String log = "SearchIntervall: "+cfg.getSearchIntervall() + " SelectedOrg: "+cfg.getSelectedOrg()+" SelectedClub: " + cfg.getSelectedClub();
		Log.d("Fetched config: ", log);		
		myDbHelper.close();
	}
	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchOrgClubNamesAndIds(int forbId) {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		int i = 0;
	
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		oklubbar.clear();
		oklubbarid.clear();
		oklubbar = myDbHelper.getOrgClubNames(forbId);
		oklubbarid = myDbHelper.getOrgClubNameIds(forbId);

		
		for (String klubbid : oklubbarid) {
//			Log.d("SNDESB","Klubbid: " + klubbid + " val : " + mySelKlubbId);

			if (klubbid.equals(mySelKlubbId)) {
				mySelKlubbIndx = i;
				mySelKlubbId = oklubbarid.get(i);
				Log.d("SNDESB","Found klubbid: " + mySelKlubbId + " in index : " + i + " name:" + oklubbar.get(i));
			}
			i++;
		}
		myKlubbIdCreated = 1;
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

        Organisation org = new Organisation();
        int ind = 0;

        while (ind < records) {
/*        	Log.e("SNDESB","configApp: OrganisationId       " + organisationId.get(ind));
        	Log.e("SNDESB","configApp: OrganisationTypeId   " + organisationTypeId.get(ind));
        	Log.e("SNDESB","configApp: Name                 " + name.get(ind));
        	Log.e("SNDESB","configApp: ParentOrganisationId " + parentOrganisationId.get(ind));
        	Log.e("SNDESB","configApp: ShortName            " + shortName.get(ind));
*/
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
        Log.e("SNDESB","configApp: Added: " + ind + " organisations to database");
        
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
			myDbHelper.createOrganisationsTable(drop);
		}catch (SQLException sqle){ 
			throw sqle;
		}
		
		myDbHelper.close();
	}	
	
	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public void createConfigTable(boolean drop) {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);

		try {
			myDbHelper.openDataBase(openstate);
		}catch(SQLException sqle){ 
			throw sqle;
		}

		try {
			myDbHelper.createConfigTable(true);
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
	public int fetchOrganisationsFromEventor() {
	
		int count = 0;
		String urlString = "organisations";

		boolean fetchOk = false;

		Log.d("SNDESB","configApp: fetchOrganisations");

		organisationId = new ArrayList<String>();
		organisationTypeId = new ArrayList<String>();
		name= new ArrayList<String>();
		parentOrganisationId = new ArrayList<String>();
		shortName = new ArrayList<String>();
		
		organisationId.clear();
		organisationTypeId.clear();
		name.clear();
		parentOrganisationId.clear();
		shortName.clear();

		try{
			RestAPI andRest = new RestAPI();
			fetchOk = andRest.queryRESTurl(urlString);

			if (fetchOk) {
				Log.d("SNDESB","configApp: fetchOK");
				organisations = andRest.parseOrganisations();    		

				// 1 = Orienteringsfšrbundet
				// 2 = Forbund
				// 3 = Klubb

				for (Organisation org : organisations){
//					Log.d("SNDESB","configApp: loop index:" + count);
					
					organisationId.add(org.getOrganisationId());
					organisationTypeId.add(org.getOrganisationTypeId());
					name.add(org.getName());
					shortName.add(org.getShortName());
					parentOrganisationId.add(org.getParentOrganisationId());	   				

					/*	  					if ((org.getOrganisationTypeId().equals(ORGTYPE_SOFT)) ||
	  						(org.getOrganisationTypeId().equals(ORGTYPE_FORB))) {
	  						oforbund.add(org.getShortName());
	  						oforbundid.add(org.getOrganisationId());
	  						Log.e("SNDESB","Stored : " + org.getShortName() + " id: " + org.getOrganisationId() + " in pos : " + i);

	  						if (org.getOrganisationId().equals(ORG_STOCKHOLM)) {
	  							activeIndex = i;
	  	  						Log.e("SNDESB","Found Stockholm as 18 in index : " + i);
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
			Log.e("SNDESB","loadForbund : Failing to fetch forbund : " + t.getMessage(), t);
		}
		Log.d("SNDESB","configApp: fetched:" + count + " forbund from Eventor");
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
   				Log.d("SNDESB","myCreateDatabaseClickHandler: Create Config and Fetch Organisations from Eventor");
//   				createConfigTable(true);
//   				createOrganisationsTable(true);
   				int rec = fetchOrganisationsFromEventor();
   	        	updateDatabase(rec);
   	        	mySpinnerForbundArrayAdapter =  
   	        			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oforbund);
   	        	mySpinnerForbundArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);		   		
   	        	myForbundSpinner.setAdapter(mySpinnerForbundArrayAdapter);	
   				mySpinnerKlubbArrayAdapter = new ArrayAdapter<String>(cont, R.layout.spinnerlayout, oklubbar);
   				mySpinnerKlubbArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);
   				myKlubbSpinner.setAdapter(mySpinnerKlubbArrayAdapter);	
   	            return;
   			}
   			case R.id.configsave:
   			{	   				
   				Log.d("SNDESB","myCreateDatabaseClickHandler Want to save the configuation");

   				// konfig 1 = activeConfig1;
   				// konfig 2 = activeConfig2;
   				// konfig 3 = activeConfig3;
   				//UpdateConfig
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
   	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			Log.d("SNDESB","MyOnForbundItemSelectedListener: Selected pos : " + pos + " som Šr : " + parent.getItemAtPosition(pos).toString() + " och id:" + oforbundid.get(pos) );
			mySelForbund = parent.getItemAtPosition(pos).toString();
			mySelForbundId = oforbundid.get(pos);
			mySelForbundIndx = pos;
			Integer val = new Integer(mySelForbundId);
			Log.d("SNDESB","MyOnForbundItemSelectedListener: Before fetching klubbs for " + val);
			fetchOrgClubNamesAndIds(val);

			mySpinnerKlubbArrayAdapter = new ArrayAdapter<String>(cont, R.layout.spinnerlayout, oklubbar);
			mySpinnerKlubbArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);
			myKlubbSpinner.setAdapter(mySpinnerKlubbArrayAdapter);
	   		myKlubbSpinner.setSelection(0,true);
			Log.d("SNDESB","MyOnForbundItemSelectedListener: After" );
			Log.d("SNDESB","oklubbar:" + oklubbar.get(0) + oklubbar.get(1) + oklubbar.get(2) );			
   	    }

   	    public void onNothingSelected(AdapterView<?> parent) {
   	    	//mySelForbund = oforbund.get(0);
			//mySelForbundId = oforbundid.get(0);
			Log.d("SNDESB","MyOnForbundItemSelectedListener: Nothing selected" );
   	    }
   	}

  	
	///////////////////////////////////////////////////////////
  	//
  	//
  	//
  	///////////////////////////////////////////////////////////
  	public class MyOnKlubbItemSelectedListener implements OnItemSelectedListener 
  	{
  		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
  			Log.d("SNDESB","MyOnKlubbItemSelectedListener: Selected pos : " + pos + "som Šr : " + parent.getItemAtPosition(pos).toString() );
  			mySelKlubb = parent.getItemAtPosition(pos).toString();

  			if (myKlubbIdCreated == 1) {
  				mySelKlubbId = oklubbarid.get(pos);
  	  			Log.d("SNDESB","MyOnKlubbItemSelectedListener: Selected klubb id : " + mySelKlubbId );
  			}
  		}
  		public void onNothingSelected(AdapterView<?> parent) {
  			Log.d("SNDESB","MyOnKlubbItemSelectedListener: Nothing selected" );
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