package work.Android;

//import android.database.Cursor;
import android.database.Cursor;
import android.database.SQLException;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteQueryBuilder;
import android.content.ContentValues;
import android.content.Context;

import android.util.Log;

//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;

///////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////
public class DataBaseHelper extends SQLiteOpenHelper{

	//The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/work.Android/databases/";
	private static String DB_NAME = "organisations";
	private static String TABLE_NAME = "Organisation";	
	private static String ORGANISATIONID = "OrganisationId";
	private static String NAME = "Name";
	private static String SHORTNAME = "ShortName";
	private static String ORGANISATIONTYPEID = "OrganisationTypeId";
	private static String PARENTORGANISATIONID = "ParentOrganisationId";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	long rowId;

	/**
	 * Constructor
	 * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
	 * @param context
	 */
	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public DataBaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
//		Log.e("XTRACTOR","DataBaseHelper : Constructor: DB Name:" + DB_NAME);	
		this.myContext = context;
	}	

	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 * */
	public void createDataBase() throws IOException{

		Log.e("XTRACTOR","DataBaseHelper : Create Database:");	
		boolean dbExist = checkDataBase();

		
		if(dbExist){
			Log.e("XTRACTOR","DataBaseHelper : database exists");	
			//do nothing - database already exist
		}else{
			Log.e("XTRACTOR","DataBaseHelper : database does not exist:");	

			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();
			Log.e("XTRACTOR","DataBaseHelper : after getReadableDatabase");

			try {
				Log.e("XTRACTOR","DataBaseHelper : copy database:");
				copyDataBase();

			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	/**
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			Log.e("XTRACTOR","DataBaseHelper : checkdatabase: open database:");

			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		}catch(SQLiteException e){
//	}catch(SQLException e){

			//database does't exist yet.

		}

		if(checkDB != null){

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException{
	
		Log.e("XTRACTOR","DataBaseHelper : copyDatabase: database:" + DB_NAME);
		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		Log.e("XTRACTOR","DataBaseHelper : copyDatabase: after inputStream");

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		Log.e("XTRACTOR","DataBaseHelper : copyDatabase: after outputStream");

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;

		while ((length = myInput.read(buffer))>0){
			Log.e("XTRACTOR","DataBaseHelper : copyDatabase: loop writing data" + buffer);
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public boolean openDataBase(int openstate) throws SQLException{

		//Open the database
		String myPath = DB_PATH + DB_NAME;

		if ((openstate != SQLiteDatabase.OPEN_READWRITE) && (openstate != SQLiteDatabase.OPEN_READONLY)) {
			Log.e("XTRACTOR","DataBaseHelper : openDataBase: " + myPath + " ERROR: wrong state : " + openstate);		
			return false;
		}
//		Log.e("XTRACTOR","DataBaseHelper : openDataBase: " + myPath + " state : " + openstate + "0=readwrite, 1=readonly");		
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, openstate);
		return true;
	}

	///////////////////////////////////////////////////////////
	//
	//
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
	//
	//
	///////////////////////////////////////////////////////////
	@Override
	public void onCreate(SQLiteDatabase db) {

	}
	
	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public void createTable() {		

		myDataBase.execSQL("DROP TABLE Organisation");
		String CREATE_TABLE_ORG = "CREATE TABLE Organisation (_id INTEGER PRIMARY KEY, OrganisationId TEXT, Name TEXT, ShortName TEXT, OrganisationTypeId TEXT, ParentOrganisationId TEXT);";
		myDataBase.execSQL(CREATE_TABLE_ORG);
	}
	
	///////////////////////////////////////////////////////////
	//
	//
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
	//
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
	//
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
	//
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
	//
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
		Integer ind = new Integer(0);

		// looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	            String klubb = new String();
				klubb = cursor.getString(cursor.getColumnIndex("Name"));

//	            klubbNameList.add(klubb);
				if (ind < 4) {
					klubbNameList.add("kalle"+ind.toString());					
					Log.d("Klubb: ", klubb);		
				}
	            ind++;
	        } while (cursor.moveToNext());
	    }
	 
	    // return contact list
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
	            Log.d("KlubbId: ", klubbid);		

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
	        if (rowId > 0) {
	        	Log.e("XTRACTOR","DataBaseHelper : addRecord: new row added to db : id:" + rowId);
	        } else {
	        	// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
	        	throw new SQLException("Failed to insert row into organisations");
	        }
	}		

	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	///////////////////////////////////////////////////////////
	//
	//
	//
	///////////////////////////////////////////////////////////
	public void createConfigTable() {		

		// FIX Not good to just drop the table !!!
		myDataBase.execSQL("DROP TABLE Config");
		String CREATE_TABLE_CONFIG = "CREATE TABLE Config (_id INTEGER PRIMARY KEY, SearchIntervall INTEGER, SelectedOrg INTEGER, SelectedClub INTEGER);";
		myDataBase.execSQL(CREATE_TABLE_CONFIG);
	}
	
	///////////////////////////////////////////////////////////
	//
	//
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
	//
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
	        if (rowId > 0) {
	        	Log.e("XTRACTOR","DataBaseHelper : addRecord: new row added to db : id:" + rowId);
	        } else {
	        	// If the insert didn't succeed, then the rowID is <= 0. Throws an exception.
	        	throw new SQLException("Failed to insert row into organisations");
	        }
	}		

	// Add your public helper methods to access and get content from the database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
	// to you to create adapters for your views.
}