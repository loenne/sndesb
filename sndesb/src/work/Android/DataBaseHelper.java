package work.Android;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteQueryBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

//import CSVReader;
//import libs.opencsv-2.3.src.au.com.bytecode.opencsv.CSVWriter;












import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
import java.util.StringTokenizer;

///////////////////////////////////////////////////////////
//
// DataBaseHelper
//
///////////////////////////////////////////////////////////
public class DataBaseHelper extends SQLiteOpenHelper{

	//The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/work.Android/databases/";
//	private static String DB_PATH = "/storage/sdcard0/sndesb/";
	private static String DB_NAME = "organisations";
	private static String TABLE_NAME = "Organisation";	
	private static String ORGANISATIONID = "OrganisationId";
	private static String NAME = "Name";
	private static String SHORTNAME = "ShortName";
	private static String ORGANISATIONTYPEID = "OrganisationTypeId";
	private static String PARENTORGANISATIONID = "ParentOrganisationId";
	private SQLiteDatabase myDataBase;
//	private final Context myContext;
	long rowId;

    private ArrayList<Organisation> objList= new ArrayList<Organisation>();

//    private ArrayList<String> defOrganisationId;
//	private ArrayList<String> defName;
//	private ArrayList<String> defShortName;
//	private ArrayList<String> defOrganisationTypeId;
//	private ArrayList<String> defParentOrganisationId;	
	
	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */
	///////////////////////////////////////////////////////////
	//
	// DataBaseHelper constructor
	//
	///////////////////////////////////////////////////////////
	public DataBaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
//		Log.e("SNDESB","DataBaseHelper : Constructor: DB Name:" + DB_NAME);	
//		this.myContext = context;
/*
        defOrganisationId = new ArrayList<String>();
		defOrganisationTypeId = new ArrayList<String>();
		defName= new ArrayList<String>();
		defParentOrganisationId = new ArrayList<String>();
		defShortName = new ArrayList<String>();

		defOrganisationId.clear();
		defOrganisationTypeId.clear();
		defName.clear();
		defParentOrganisationId.clear();
		defShortName.clear();
*/		
/*		int ind = 0;
		
		defOrganisationId.set(ind, "");
    	defOrganisationTypeId.set(ind, "");
    	defName.set(ind,"");
    	defParentOrganisationId.set(ind,"");
    	defShortName.set(ind,"");
*/
		
	}	
	
	///////////////////////////////////////////////////////////
	//
	// createDataBase
	//
	///////////////////////////////////////////////////////////
	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 * */
	public void createDataBase() throws IOException{

		Log.d("SNDESB","DataBaseHelper : Create Database:");	
		boolean dbExist = checkDataBase();
		
		if(dbExist){
			Log.e("SNDESB","DataBaseHelper : database exists");	
			//do nothing - database already exist
		}else{
			Log.d("SNDESB","DataBaseHelper : Creating empty database in default system path");	

			//By calling this method an empty database will be created into the default system path
			//of your application so we are going to be able to overwrite that database with our database.
			this.getReadableDatabase();
			Log.d("SNDESB","DataBaseHelper : after empty database creation (getReadableDatabase)");

/*			try {
				Log.e("SNDESB","DataBaseHelper : copy database:");
				copyDataBase();

			} catch (IOException e) {
				throw new Error("DataBaseHelper: Error copying database");
			}
*/		}
	}

	///////////////////////////////////////////////////////////
	//
	// checkDataBase
	//
	// Check if the database already exist to avoid re-copying
	// the file each time you open the application.
	// @return true if it exists, false if it doesn't
	//
	///////////////////////////////////////////////////////////
	
	public boolean checkDataBase(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			Log.d("SNDESB","DataBaseHelper: checkdatabase: Try to open database");

			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		}catch(SQLiteException e){
            Log.d("SNDESB","DataBaseHelper: checkdatabase: Database could not be opened. Does not exist");
		}

		if(checkDB != null){
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	///////////////////////////////////////////////////////////
	//
	// copyDataBase
	//
	///////////////////////////////////////////////////////////
	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
/*	private void copyDataBase() throws IOException{
	
		Log.e("SNDESB","DataBaseHelper : copyDatabase: database:" + DB_NAME);
		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		Log.e("SNDESB","DataBaseHelper : copyDatabase: after inputStream");

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		Log.e("SNDESB","DataBaseHelper : copyDatabase: after outputStream");

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;

		while ((length = myInput.read(buffer))>0){
			Log.e("SNDESB","DataBaseHelper : copyDatabase: loop writing data" + buffer);
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}
*/
	///////////////////////////////////////////////////////////
	//
	// openDataBase
	//
	///////////////////////////////////////////////////////////
	public boolean openDataBase(int openstate) throws SQLException{

		//Open the database
		String myPath = DB_PATH + DB_NAME;

		if ((openstate != SQLiteDatabase.OPEN_READWRITE) && (openstate != SQLiteDatabase.OPEN_READONLY)) {
			Log.e("SNDESB","DataBaseHelper : openDataBase: " + myPath + " ERROR: wrong state : " + openstate);		
			return false;
		}
//		Log.e("SNDESB","DataBaseHelper : openDataBase: " + myPath + " state : " + openstate + "0=readwrite, 1=readonly");		
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, openstate);
		return true;
	}

	///////////////////////////////////////////////////////////
	//
	// close
	//
	///////////////////////////////////////////////////////////
	@Override
	public synchronized void close() {

		if(myDataBase != null)
			myDataBase.close();
		super.close();
	}

	///////////////////////////////////////////////////////////
	//
	// onCreate
	//
	///////////////////////////////////////////////////////////
	@Override
	public void onCreate(SQLiteDatabase db) {

	}
	
	///////////////////////////////////////////////////////////
	//
	// createOrganisationsTable
	//
	///////////////////////////////////////////////////////////
	public void createOrganisationsTable(boolean drop) {		

		if (drop) {
			myDataBase.execSQL("DROP TABLE Organisation");			
		}
		String CREATE_TABLE_ORG = "CREATE TABLE Organisation (_id INTEGER PRIMARY KEY, OrganisationId TEXT, Name TEXT, ShortName TEXT, OrganisationTypeId TEXT, ParentOrganisationId TEXT);";
		myDataBase.execSQL(CREATE_TABLE_ORG);
	}
	
	///////////////////////////////////////////////////////////
	//
	// getOrganisation
	//
	///////////////////////////////////////////////////////////
	public Organisation getOrganisation(int id) {		

		//	    SQLiteDatabase db = this.getReadableDatabase();
//		String kalle = new String("1001");
		
		Cursor cursor = myDataBase.query ("Organisation",  
				new String[] { "_id", "OrganisationId", "Name", "ShortName", "OrganisationTypeId", "ParentOrganisationId"},
				"_id" + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
		Organisation org = new Organisation();

		if (cursor != null) {  
			cursor.moveToFirst();  
//			activity.startManagingCursor(cursor);  
//			org.setId(cursor.getInt(cursor.getColumnIndex("_id")));  


 			org.setOrganisationId(cursor.getString(cursor.getColumnIndex("OrganisationId")));  
			org.setName(cursor.getString(cursor.getColumnIndex("Name")));  
			org.setShortName(cursor.getString(cursor.getColumnIndex("ShortName")));  
			org.setOrganisationTypeId(cursor.getString(cursor.getColumnIndex("OrganisationTypeId")));  
			org.setParentOrganisationId(cursor.getString(cursor.getColumnIndex("ParentOrganisationId")));  

/* 			org.setOrganisationId(cursor.getString(1));  
			org.setName(cursor.getString(2));  
			org.setShortName(cursor.getString(3));  
			org.setOrganisationTypeId(cursor.getString(4));
			org.setParentOrganisationId(cursor.getString(5));
*/		}
		return org;
	}

	///////////////////////////////////////////////////////////
	//
	// getAllOrganisations
	//
	///////////////////////////////////////////////////////////
	public List <Organisation> getAllOrganisations() {		

		List<Organisation> organisationList = new ArrayList<Organisation>();

		// Select All Query
	    String selectQuery = "SELECT  * FROM " + "Organisation";
	 
//	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Organisation org = new Organisation();
				org.setOrganisationId(cursor.getString(cursor.getColumnIndex("OrganisationId")));  
				org.setName(cursor.getString(cursor.getColumnIndex("Name")));  
				org.setShortName(cursor.getString(cursor.getColumnIndex("ShortName")));  
				org.setOrganisationTypeId(cursor.getString(cursor.getColumnIndex("OrganisationTypeId")));  
				org.setParentOrganisationId(cursor.getString(cursor.getColumnIndex("ParentOrganisationId")));  

	            // Adding organisation to list
	            organisationList.add(org);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return organisationList;
	}

	///////////////////////////////////////////////////////////
	//
	// getAllForbundNames
	//
	///////////////////////////////////////////////////////////
	public ArrayList <String> getAllForbundNames() {		

		ArrayList<String> forbundNameList = new ArrayList<String>();

		// Select All Query
	    String selectQuery = "SELECT  * FROM " + "Organisation" + " WHERE organisationTypeId = 2";
//	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            String forb = new String();
				forb = cursor.getString(cursor.getColumnIndex("Name"));  

	            // Adding forbundsname to list
	            forbundNameList.add(forb);
//	            Log.d("Forb: ", forb);		

	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return forbundNameList;
	}
	
	///////////////////////////////////////////////////////////
	//
	// getAllForbundIds
	//
	///////////////////////////////////////////////////////////
	public ArrayList <String> getAllForbundIds() {		

		ArrayList<String> forbundIdsList = new ArrayList<String>();

		// Select All Query
	    String selectQuery = "SELECT  * FROM " + "Organisation" + " WHERE organisationTypeId = 2";
//	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            String forb = new String();
				forb = cursor.getString(cursor.getColumnIndex("OrganisationId"));  

	            // Adding forbundsIds to list
	            forbundIdsList.add(forb);
//	            Log.d("ForbIds: ", forb);		

	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return forbundIdsList;
	}

	///////////////////////////////////////////////////////////
	//
	// getOrgClubs
	//
	///////////////////////////////////////////////////////////
	public List <Organisation> getOrgClubs(int id) {		

		List<Organisation> organisationList = new ArrayList<Organisation>();

		Cursor cursor = myDataBase.query ("Organisation",  
				new String[] { "_id", "OrganisationId", "Name", "ShortName", "OrganisationTypeId", "ParentOrganisationId"},
				"ParentOrganisationId" + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
				
		// Select All Query
//	    String selectQuery = "SELECT  * FROM " + "Organisation WHERE organisationId "+;
//	    Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            Organisation org = new Organisation();
				org.setOrganisationId(cursor.getString(cursor.getColumnIndex("OrganisationId")));  
				org.setName(cursor.getString(cursor.getColumnIndex("Name")));  
				org.setShortName(cursor.getString(cursor.getColumnIndex("ShortName")));  
				org.setOrganisationTypeId(cursor.getString(cursor.getColumnIndex("OrganisationTypeId")));  
				org.setParentOrganisationId(cursor.getString(cursor.getColumnIndex("ParentOrganisationId")));  

	            // Adding organisation to list
	            organisationList.add(org);
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return organisationList;
	}
	
	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public ArrayList <String> getOrgClubNames(int id) {		

		ArrayList<String> klubbNameList = new ArrayList<String>();

		Cursor cursor = myDataBase.query ("Organisation",  
				new String[] { "_id", "OrganisationId", "Name", "ShortName", "OrganisationTypeId", "ParentOrganisationId"},
				"ParentOrganisationId" + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
				
		// Select All Query
//	    String selectQuery = "SELECT  * FROM " + "Organisation WHERE organisationId "+;
//	    Cursor cursor = myDataBase.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            String klubb = new String();
				klubb = cursor.getString(cursor.getColumnIndex("Name"));

	            klubbNameList.add(klubb);
//	            Log.d("SNDESB", "getOrgClubKlubbNames: Klubb: " + klubb);		
	        } while (cursor.moveToNext());
	    }
	    return klubbNameList;
	}

	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public ArrayList <String> getOrgClubNameIds(int id) {		

		ArrayList<String> klubbNameIdList = new ArrayList<String>();

		Cursor cursor = myDataBase.query ("Organisation",  
				new String[] { "_id", "OrganisationId", "Name", "ShortName", "OrganisationTypeId", "ParentOrganisationId"},
				"ParentOrganisationId" + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
				
		// Select All Query
//	    String selectQuery = "SELECT  * FROM " + "Organisation WHERE organisationId "+;
//	    Cursor cursor = myDataBase.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            String klubbid = new String();
				klubbid = cursor.getString(cursor.getColumnIndex("OrganisationId"));
	            klubbNameIdList.add(klubbid);
//	            Log.d("SNDESB", "getOrgClubKlubbIdNames: Add KlubbId: " + klubbid);		
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
	    return klubbNameIdList;
	}
	
	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public void addRecord(Organisation org) {
		
//		select OrganisationId, Name, ShortName, OrganisationTypeId, ParentOrganisationId from organizations;

//		INSERT INTO organisations 
//		(OrganisationId, Name, ShortName,OrganisationTypeId, ParentOrganisationId) 
//		VALUES(val1, val2, val3, val4, val5);		
		
	        // A map to hold the new record's values.
	        ContentValues values = new ContentValues();

	    	values.put(ORGANISATIONID, org.getOrganisationId());
	    	values.put(NAME, org.getName());
	    	values.put(SHORTNAME, org.getShortName());
	    	values.put(ORGANISATIONTYPEID, org.getOrganisationTypeId());
	    	values.put(PARENTORGANISATIONID, org.getParentOrganisationId());

	    	rowId = myDataBase.insert(TABLE_NAME, null, values);

	    	// Opens the database object in "write" mode.
	//        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

	        // If the insert succeeded, the row ID exists.
	        if (rowId <= 0) {
	        	// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
	        	throw new SQLException("Failed to insert row into organisations");
	        }
	}		

	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public void addOrganisationsRecords(AssetManager assetManager) {
		
       	Log.d("SNDESB","addOrganisationsRecords: delete all old organisation records.");
		deleteAllOrgRecords(); 

		try {
		    readOrganisationsFromFile(assetManager);
		} catch (IOException e) {
		    
		    
		}
        objList= new ArrayList<Organisation>();
		
        // A map to hold the new record's values.
        ContentValues values = new ContentValues(); 
        
        int ind = 0;
        int records = objList.size();
        Organisation org = new Organisation();
        
        while (ind < records) {
            org = objList.get(ind);
            values.put("OrganisationId", org.getOrganisationId());
        	values.put("OrganisationTypeId", org.getOrganisationTypeId());
        	values.put("Name", org.getName());
            values.put("ShortName", org.getShortName());
        	values.put("ParentOrganisationId", org.getParentOrganisationId());

        	Log.d("SNDESB","addOrganisationsRecords: Fill default Organisations data started.");
        	rowId = myDataBase.insert("Organisations", null, values);
        	Log.d("SNDESB","addOrganisationsRecords: Fill default Organisations data completed.");
           	ind++;
        }

        // If the insert succeeded, the row ID exists.
    	if (rowId <= 0) {
        	// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
        	throw new SQLException("Failed to insert row into organisations");
        }
        Log.d("SNDESB","configApp: Added: " + ind + " organisations to database");
	}
	
	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public void deleteAllOrgRecords() {
		
		rowId = myDataBase.delete(TABLE_NAME, null, null);

	        // If the insert succeeded, the row ID exists.
//	        if (rowId <= 0) {
//	        	// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
//	        	throw new SQLException("Failed to insert row into organisations");
//	        }
	}		
	
	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	private void readOrganisationsFromFile(AssetManager assetManager) throws UnsupportedEncodingException {

	    InputStream is = null;

	    try {
	        is = assetManager.open("organisations.txt");
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	    BufferedReader reader = null;
	    reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

	    String line = "";
	    StringTokenizer st = null;

	    try {

	        while ((line = reader.readLine()) != null) {
	            st = new StringTokenizer(line, ",");
	            Organisation obj= new Organisation();
	            obj.setOrganisationId(st.nextToken());
	            obj.setOrganisationTypeId(st.nextToken());
	            obj.setName(st.nextToken());
	            obj.setShortName(st.nextToken());
	            obj.setParentOrganisationId(st.nextToken());
	            objList.add(obj);
	        }
	    } catch (IOException e) {

	        e.printStackTrace();
	    }
	}
	

	///////////////////////////////////////////////////////////
	//
	// onUpgrade
	//
	///////////////////////////////////////////////////////////
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	///////////////////////////////////////////////////////////
	//
	// createConfigTable
	//
	///////////////////////////////////////////////////////////
	public void createConfigTable(boolean drop) {		

		// FIX Not good to just drop the table !!!
		if (drop) {
			myDataBase.execSQL("DROP TABLE Config");
		}
		String CREATE_TABLE_CONFIG = "CREATE TABLE Config (_id INTEGER PRIMARY KEY, SearchIntervall INTEGER, SelectedOrg INTEGER, SelectedClub INTEGER);";
		myDataBase.execSQL(CREATE_TABLE_CONFIG);
	}
	
	///////////////////////////////////////////////////////////
	//
	// getConfig
	//
	///////////////////////////////////////////////////////////
	public Config getConfig() {		

		Config conf = new Config();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + "Config";

		//	    SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			conf.setSearchIntervall(cursor.getInt(cursor.getColumnIndex("SearchIntervall")));  
			conf.setSelectedOrg(cursor.getInt(cursor.getColumnIndex("SelectedOrg")));  
			conf.setSelectedClub(cursor.getInt(cursor.getColumnIndex("SelectedClub")));  
		}

		// return contact list
		return conf;
	}

	///////////////////////////////////////////////////////////
	//
	// updateConfig
	//
	///////////////////////////////////////////////////////////
	public void updateConfig(Config cfg) {		

        ContentValues values = new ContentValues();
        
    	values.put("SearchIntervall", cfg.getSearchIntervall() );
    	values.put("SelectedOrg", cfg.getSelectedOrg());
    	values.put("SelectedClub", cfg.getSelectedClub());

    	int count = myDataBase.update("Config", values, null, null);

		Log.d("SNDESB","UpdateConfig: " + count + " Rows updated");
}

	
	///////////////////////////////////////////////////////////
	//
	// addConfigRecord
	//
	///////////////////////////////////////////////////////////
	public void addConfigRecord() {
				
	        // A map to hold the new record's values.
	        ContentValues values = new ContentValues();

	    	values.put("SearchIntervall", 2);
	    	values.put("SelectedOrg", 18);
	    	values.put("SelectedClub", 335);

	    	rowId = myDataBase.insert("Config", null, values);

	    	// Opens the database object in "write" mode.
	//        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

	        // If the insert succeeded, the row ID exists.
	    	if (rowId <= 0) {
	        	// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
	        	throw new SQLException("Failed to insert row into organisations");
	        }
	}		

	// Add your public helper methods to access and get content from the database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
	// to you to create adapters for your views.
}