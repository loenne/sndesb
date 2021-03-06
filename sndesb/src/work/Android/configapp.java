package work.Android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
//import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class configapp extends Activity {
    final int MAX_FORBUND = 24;

	private ArrayList<String> organisationId;
	private ArrayList<String> organisationTypeId;
	private ArrayList<String> name;
	private ArrayList<String> shortName;
	private ArrayList<String> parentOrganisationId;
	
	private Runnable getEntr;
	SimpleAdapter myAdapter;
	private ProgressDialog m_ProgressDialog = null;

	private List<Organisation> organisations;
	private ArrayAdapter<String> mySpinnerForbundArrayAdapter;
	private ArrayAdapter<String> mySpinnerKlubbArrayAdapter;
	private TextView mySearchLength;
	private Spinner  myForbundSpinner;
	private Spinner  myKlubbSpinner;

	//	private Button myCreateDatabase; 
	//	private Button myUpdateEventor;
	//	private Button mySaveConfig;
	private ArrayList<String> oforbund;
	private ArrayList<String> oforbundid;
	private ArrayList<String> oklubbar;
	private ArrayList<String> oklubbarid;
	//	private String mySelKlubb;
	//	private String mySelForbund;
	
	// Selected config values
	private int mySelSearchLength;
	private int mySelForbundId;
	private int mySelKlubbId;	

	private int mySelForbundIndx;
	private int mySelKlubbIndx;	
	private int myKlubbIdCreated;

	private String myBuffSearchLength;
	private String myBuffSelectedForbund;
	private String myBuffSelectedKlubb;	
	private String myBuffConnectionState;
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
	    myKlubbSpinner = (Spinner)findViewById(R.id.configsearchclubbfield);
	    //		myCreateDatabase = (Button)findViewById(R.id.configcreatedatabase);
	    //   		mySaveConfig = (Button)findViewById(R.id.configsave);
   		
	    Serializable s = this.getIntent().getSerializableExtra("arguments");
	    Object[] o = (Object[]) s;

	    if (o != null) {
	        myBuffSearchLength = o[1].toString();
	        myBuffSelectedForbund = o[3].toString();
	        myBuffSelectedKlubb = o[5].toString();		 
	        myBuffConnectionState = o[7].toString();

		 }else {
			 Log.e("SNDESB","configApp start. Passed arguments from main app to config not correct");			 
		 }

   		mySearchLength.setText(String.valueOf(myBuffSearchLength));

   		mySelSearchLength = Integer.parseInt(myBuffSearchLength);
   		mySelForbundId = Integer.parseInt(myBuffSelectedForbund);
   		mySelKlubbId = Integer.parseInt(myBuffSelectedKlubb);
   		
   		// TODO: Add check for if the database have been fetched from Eventor. Should be part of
   		// TODO: installation ? and then it should be able to refresh (update )
   		// TODO: As it is working now, we don't fetch data before this and app crashes !!
   		Log.d("SNDESB","configApp: Fetch all forbund records from config database");
		fetchAllOrganisationNamesAndIds();
		Log.d("SNDESB","configApp: Fetch all clubs belonging to configured forbund: " + myBuffSelectedForbund + " from config database");
		int forbId = Integer.parseInt(myBuffSelectedForbund);
		fetchOrgClubNamesAndIds(forbId);
   		  		
		cont = this;

		TextWatcher myTextWatcher = new TextWatcher() {
			public void afterTextChanged(Editable s) {
				Log.d("SNDESB","configApp: mySearchLength changed to: " + s.toString() + " ");
				if (s.toString().length() >0) {
					mySelSearchLength = Integer.parseInt(s.toString());
				}
		    }

		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		    }

		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    }
		};

		mySearchLength.addTextChangedListener(myTextWatcher);
				
		mySpinnerForbundArrayAdapter =  
	   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oforbund);
	   	mySpinnerForbundArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);		   		
		myForbundSpinner.setAdapter(mySpinnerForbundArrayAdapter);
		myForbundSpinner.setOnItemSelectedListener(new MyOnForbundItemSelectedListener()); 	   	 	   		 	   		 	   		 	   		
		myForbundSpinner.setSelection(mySelForbundIndx,true);

		mySpinnerKlubbArrayAdapter =  
			   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oklubbar);
		mySpinnerKlubbArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);
		myKlubbSpinner.setAdapter(mySpinnerKlubbArrayAdapter);
		myKlubbSpinner.setOnItemSelectedListener(new MyOnKlubbItemSelectedListener());
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
//			Log.d("SNDESB","Search index: " + i + " forbundid: " + oforbid + " after " + mySelForbundId);
						
			if (Integer.parseInt(oforbid) == mySelForbundId) {
				mySelForbundIndx = i;
				mySelForbundId = Integer.parseInt(oforbundid.get(i));
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
		
		Config cfg = new Config();
   		cfg.setSearchIntervall(mySelSearchLength);
   		cfg.setSelectedOrg(mySelForbundId);
   		cfg.setSelectedClub(mySelKlubbId);

   		myDbHelper.updateConfig(cfg);
		String log = "SearchIntervall: "+cfg.getSearchIntervall() + " SelectedOrg: "+cfg.getSelectedOrg()+" SelectedClub: " + cfg.getSelectedClub();
		Log.d("Config updated with: ", log);		
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

			if (Integer.parseInt(klubbid) == mySelKlubbId) {
				mySelKlubbIndx = i;
				mySelKlubbId = Integer.parseInt(oklubbarid.get(i));
				Log.d("SNDESB","Found klubbid: " + mySelKlubbId + " in index : " + i + " name:" + oklubbar.get(i));
				break;
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
	public void storeOrganisationsToDatabase() {
		
		DataBaseHelper myDbHelper = new DataBaseHelper(this);
 
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }

    	Log.d("SNDESB","storeOrganisationsToDatabase: delete all old organisation records.");
        myDbHelper.deleteAllOrgRecords();        
    	Log.d("SNDESB","storeOrganisationsToDatabase: delete finished.");

        Organisation org = new Organisation();
        int ind = 0;
        int records = organisationId.size();

        while (ind < records) {
//        	Log.d("SNDESB","configApp: OrganisationId       " + organisationId.get(ind));
//        	Log.d("SNDESB","configApp: OrganisationTypeId   " + organisationTypeId.get(ind));
//        	Log.d("SNDESB","configApp: Name                 " + name.get(ind));
//        	Log.d("SNDESB","configApp: ParentOrganisationId " + parentOrganisationId.get(ind));
//        	Log.d("SNDESB","configApp: ShortName            " + shortName.get(ind));

            org.setOrganisationId(organisationId.get(ind));
            org.setOrganisationTypeId(organisationTypeId.get(ind));
            org.setName(name.get(ind));
            org.setParentOrganisationId(parentOrganisationId.get(ind));
            org.setShortName(shortName.get(ind));

           	try {
           		myDbHelper.addRecord(org);
//            	Log.d("SNDESB","configApp:-------Record stored ----");
           	}
           	catch(SQLException sqle) {
           		throw sqle;
           	}
           	ind++;
        }
        Log.d("SNDESB","configApp: Added: " + ind + " organisations to database");
        
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
	public void fetchOrganisationsFromEventor() {

		 getEntr = new Runnable() {
				@Override
				public void run() {
					fetchFromEventor();
				}
			};
			Thread thread = new Thread(null, getEntr, "MagentoBackground");
			thread.start();
			m_ProgressDialog = ProgressDialog.show(configapp.this,"Please wait...", "Retreiving data...",true);		 
	}		
		
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void fetchFromEventor() {
		
		//int count = 0;
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

				// 1 = Orienteringsf�rbundet
				// 2 = Forbund
				// 3 = Klubb

				for (Organisation org : organisations){
					
					organisationId.add(org.getOrganisationId());
					organisationTypeId.add(org.getOrganisationTypeId());
					name.add(org.getName());
					shortName.add(org.getShortName());
					parentOrganisationId.add(org.getParentOrganisationId());	   				
//					Log.d("SNDESB","configApp: loop index:" + count + " name: " + org.getName());

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
					//count++;
				}
				Log.d("SNDESB","myCreateDatabaseClickHandler: Store organisations to config database");
				storeOrganisationsToDatabase();				
			} else {
				Toast.makeText(this, "No network connection. Check mobile network",
						Toast.LENGTH_LONG).show();
			}
		} catch (Throwable t){
			Log.e("SNDESB","fetchOrganisationsFromEventor: Failing to fetch organisations: " + t.getMessage(), t);
		}
  		runOnUiThread(returnRes);
		
//		Log.d("SNDESB","configApp: fetched:" + count + " forbund from Eventor");
//		return count;
	}
	
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
//			myAdapter.notifyDataSetChanged();
//			mySelectedClub.setText(clubName);
			m_ProgressDialog.dismiss();
		}
	};
	
    ///////////////////////////////////////////////////////////
    //
    // myCreateDatabaseClickHandler
    //
    ///////////////////////////////////////////////////////////
  	public void myCreateDatabaseClickHandler(View view) {

   		switch (view.getId()) {
   			case R.id.configcreatedatabase:
   			{	   				
   				Log.d("SNDESB","myCreateDatabaseClickHandler: Fetch Organisations from Eventor");
   				fetchOrganisationsFromEventor();
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
   				Log.d("SNDESB","myCreateDatabaseClickHandler: Save the configuration to database");
   				updateConfig();
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
			Log.d("SNDESB","MyOnForbundItemSelectedListener: Selected pos : " + pos + " is: " + parent.getItemAtPosition(pos).toString() + " och id:" + oforbundid.get(pos) );
//			mySelForbund = parent.getItemAtPosition(pos).toString();
			mySelForbundId = Integer.parseInt(oforbundid.get(pos));
			mySelForbundIndx = pos;
//			Integer val = new Integer(mySelForbundId);
			Integer val = Integer.valueOf(mySelForbundId);
			Log.d("SNDESB","MyOnForbundItemSelectedListener: Before fetching clubs for " + val);
			fetchOrgClubNamesAndIds(val);

			mySpinnerKlubbArrayAdapter = new ArrayAdapter<String>(cont, R.layout.spinnerlayout, oklubbar);
			mySpinnerKlubbArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);
			myKlubbSpinner.setAdapter(mySpinnerKlubbArrayAdapter);
	   		myKlubbSpinner.setSelection(mySelKlubbIndx,true);
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
  			Log.d("SNDESB","MyOnKlubbItemSelectedListener: Selected pos : " + pos + " is : " + parent.getItemAtPosition(pos).toString() );
//  			mySelKlubb = parent.getItemAtPosition(pos).toString();

  			if (myKlubbIdCreated == 1) {
  				mySelKlubbId = Integer.parseInt(oklubbarid.get(pos));
  	  			Log.d("SNDESB","MyOnKlubbItemSelectedListener: Selected clubb id : " + mySelKlubbId );
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
