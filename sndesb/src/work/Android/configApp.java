package work.Android;

//import android.app.Activity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
// import android.os.Parcelable;
//import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
//import android.preference.Preference;
import android.preference.PreferenceActivity;
//import android.preference.Preference.OnPreferenceClickListener;
//import android.widget.Toast;
import android.util.Log;
import android.widget.Toast;

//public class configApp extends Activity {
public class configApp extends PreferenceActivity { 
	final int MAX_FORBUND = 24;
	private ArrayList<String> organisationId;
	private ArrayList<String> organisationTypeId;
	private ArrayList<String> name;
	private ArrayList<String> shortName;
	private ArrayList<String> parentOrganisationId;
	private List<Organisation> organisations;	
	private Config cfg;
	int openstate;
	
	@Override	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		addPreferencesFromResource(R.layout.configapp);

/*		organisationId = new ArrayList<String>();
		organisationTypeId = new ArrayList<String>();
		name= new ArrayList<String>();
		parentOrganisationId = new ArrayList<String>();
		shortName = new ArrayList<String>();
*/	    
		openstate = 0; //0=readwrite, 1 = readonly
		
//		int rec = fetchOrganisations();
//		Log.e("XTRACTOR","configApp Fetched:" + rec + " records from eventor");

//		if (rec>0) {
//			updateDatabase(rec);
//		}

//		updateDatabase(0);
		
//		fetchOneOrganisation(1200);
//		fetchAllOrganisations();
//		fetchOrgClubs(1);
//		fetchOrgClubs(18);
		fetchConfig();
		
		
	}

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

	public void fetchAllOrganisations() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }

		// Fetch all
		List <Organisation> allaOrg;
		
		allaOrg = myDbHelper.getAllOrganisations();
		
		for (Organisation org : allaOrg) {
            String log = "OrgId: "+org.getOrganisationId() + "TypeId: "+org.getOrganisationTypeId()+" ,Name: " + org.getName() + " ,ShortName: " + org.getShortName() + " ,ParentOrgId: " + org.getParentOrganisationId();
            // Writing Contacts to log
            Log.d("Org: ", log);		
		}
		myDbHelper.close();
	}

	public void fetchConfig() {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }
		
		cfg = myDbHelper.getConfig();
		String log = "SearchIntervall: "+cfg.getSearchIntervall() + "SelectedOrg: "+cfg.getSelectedOrg()+" ,SelectedClub: " + cfg.getSelectedClub();
		// Writing Contacts to log
		Log.d("Org: ", log);		
		myDbHelper.close();
	}
	
	
	public void fetchOrgClubs(int id) {

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		
        try {
        	myDbHelper.openDataBase(openstate);
        }catch(SQLException sqle){ 
        	throw sqle;
        }

		List <Organisation> allaOrg;
		
		allaOrg = myDbHelper.getOrgClubs(id);
		
		for (Organisation org : allaOrg) {
            String log = "Id: "+org.getOrganisationId() + "TypeId: "+org.getOrganisationTypeId()+" ,Name: " + org.getName() + " ,ShortName: " + org.getShortName() + " ,ParentOrgId: " + org.getParentOrganisationId();
            // Writing Contacts to log
            Log.d("Org: ", log);		
		}
		myDbHelper.close();
	}
	
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

/* create main table
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
	
	public int fetchOrganisations() {
	
		int count = 0;
		String urlString = "organisations";

		boolean fetchOk = false;

		Log.e("XTRACTOR","configApp: fetchOrganisations");

		organisationId.clear();
		organisationTypeId.clear();
		name.clear();
		parentOrganisationId.clear();
		shortName.clear();

		try{
			RestAPI andRest = new RestAPI();
			fetchOk = andRest.queryRESTurl(urlString);

			if (fetchOk) {
				Log.e("XTRACTOR","configApp: fetchOK");
				organisations = andRest.parseOrganisations();    		

				// 1 = Orienteringsfšrbundet
				// 2 = Forbund
				// 3 = Klubb

				for (Organisation org : organisations){
					Log.e("XTRACTOR","configApp: loop index:" + count);
					
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

		/*		ListPreference ForbundList = (ListPreference) findPreference("forbund");

		CharSequence[] entries = new CharSequence[MAX_FORBUND];
		CharSequence[] entryValues = new CharSequence[MAX_FORBUND];
        entries[0] = "Svenska Orienteringsfšrbundet";
        entries[1] = "SmŒland";
        entries[2] = "VŠstergštland";	    
        entries[3] = "Uppland";
        entries[4] = "ngermanland";
        entries[5] = "BohuslŠn-Dal";
        entries[6] = "Blekinge";
        entries[7] = "Dalarna";
        entries[8] = "Gotland";	    
        entries[9] = "GŠstrikland";	    
        entries[10] = "HŠlsingland";	    
        entries[11] = "HŠlsingland";	    
        entries[12] = "Halland";	    
        entries[13] = "Gšteborg";	    
   		entries[14] = "Medelpad";
   		entries[15] = "Norrbotten";	    
   		entries[16] = "SkŒne";	    
   		entries[17] = "Stockholm";
    	entries[18] = "JŠmtland-HŠrjedalen";	    
        entries[19] = "Sšdermanland";	    
        entries[20] = "VŠrmland";	    
        entries[21] = "VŠsterbotten";	    
        entries[22] = "VŠstmanland";	    
        entries[23] = "…stergštland";	    

        entryValues[0] = "1";
        entryValues[1] = "2";
        entryValues[2] = "3";
        entryValues[3] = "4";
        entryValues[4] = "5";
        entryValues[5] = "6";
        entryValues[6] = "7";
        entryValues[7] = "8";
        entryValues[8] = "9";
        entryValues[9] = "10";
        entryValues[10] = "11";
        entryValues[11] = "12";
        entryValues[12] = "13";
        entryValues[13] = "14";
        entryValues[14] = "15";
        entryValues[15] = "16";
        entryValues[16] = "17";
        entryValues[17] = "18";
        entryValues[18] = "19";
        entryValues[19] = "20";
        entryValues[20] = "21";
        entryValues[21] = "22";
        entryValues[22] = "23";
        entryValues[23] = "24";
        ForbundList.setEntries(entries);
		ForbundList.setEntryValues(entryValues);

		 */
		return count;
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