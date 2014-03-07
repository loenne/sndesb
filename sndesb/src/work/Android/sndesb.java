package work.Android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.SQLException;

///////////////////////////////////////////////////////////
//
//    Class AndroB
//
///////////////////////////////////////////////////////////
public class sndesb extends Activity {

    String urlString = "organisations";

    static final int CONNECTION_UNAVAILABLE = 0;
    static final int CONNECTION_MOBILE = 1;
    static final int CONNECTION_WIFI = 2;
    static final int CONNECTION_UNKNOWN = 3;

    boolean fetchOk = false;
    private Config cfg;
    private Integer mySelectedSearchInterval;
    private Integer mySelectedForbundId;
    private Integer mySelectedClubId;
    private int openstate;
    private Integer connectionState = CONNECTION_UNAVAILABLE;
    private ArrayList<String> connectionStates;
    private Map<String, String> networkDetails;
    private AssetManager assetManager;
        
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
        assetManager = this.getAssets();
        connectionState = checkNetworkConnection();    
        fetchConfig();
    }

    ///////////////////////////////////////////////////////////
    //
    // checkNetworkConnection
    //
    ///////////////////////////////////////////////////////////
    public int checkNetworkConnection() {

        int res = CONNECTION_UNKNOWN;
        
        networkDetails = new HashMap<String, String>();
        
        connectionStates = new ArrayList<String>();
        connectionStates.add("Network unavailable");
        connectionStates.add("Mobile network");
        connectionStates.add("WiFi network");
        connectionStates.add("Unknown network state");
          
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (wifiNetwork != null && wifiNetwork.isConnected()) {
                res = CONNECTION_WIFI;
                Log.d("SNDESB","checkNetworkConnection: WIFI Type:" + wifiNetwork.getTypeName() + " SubType: " + wifiNetwork.getSubtypeName() + " State: " + wifiNetwork.getState().name());
            }
   
            NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            
            if (mobileNetwork != null && mobileNetwork.isConnected()) {

//                if (mobileNetwork.isRoaming()) {
//                    networkDetails.put("Roming", "YES");
//                } else {
//                    networkDetails.put("Roming", "NO");
//                }
                res = CONNECTION_MOBILE;
                Log.d("SNDESB","checkNetworkConnection: MOBILE Type:  " + mobileNetwork.getTypeName() +     " SubType: " + mobileNetwork.getSubtypeName());
                Log.d("SNDESB","checkNetworkConnection: MOBILE State: " + mobileNetwork.getState().name() + " Roaming: " + mobileNetwork.isRoaming());
            }

            if (wifiNetwork == null && mobileNetwork == null) {
                res = CONNECTION_UNAVAILABLE;
            }
            
        } catch (Exception e) {
//            networkDetails.put("Status", e.getMessage());
            res = CONNECTION_UNAVAILABLE;
        } 
        Log.d("SNDESB","checkNetworkConnection: Network connection : " + connectionStates.get(res));
        return res;
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
        // TODO: Test if this works !. Moved dbSetup up. We want to do the fetch config always !
        
        if (!dbExist) {
            Log.d("SNDESB","fetchConfig: Database could not be opened. First time app started ? We try to create it");        
            dbSetup();
        }

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
            Log.e("SNDESB","fetchConfig: Database does not seem to exist. We should have created it !");        
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
            Log.d("SNDESB","dbSetup: Opened the new empty SQLite database");        
        }catch(SQLException sqle){ 
            throw sqle;
        }
        
        Log.d("SNDESB","dbSetup: Create config table");        
        createConfigTable(myDbHelper);
        Log.d("SNDESB","dbSetup: Add default values to config table");        
        addConfigTableRecord(myDbHelper);
        Log.d("SNDESB","dbSetup: Create organisations table");        
        createOrganisationsTable(myDbHelper, false);
        Log.d("SNDESB","dbSetup: Add default values to organisations table");        
        addOrganisationsTableRecord(myDbHelper);
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
    // addOrganisationsTableRecord
    //
    ///////////////////////////////////////////////////////////
    public void addOrganisationsTableRecord(DataBaseHelper myDbHelper) {

        try {
            Log.d("SNDESB", "Add records in Organisations Table");        
            myDbHelper.addOrganisationsRecords(assetManager);
        }
        catch(SQLException sqle) {
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
                fetchConfig();
                Intent i = new Intent();
                i.setClassName("work.Android", "work.Android.selectevent");
                String sendbuff[] = new String[6];
                sendbuff[0] = "SEARCHLENGTH";
                sendbuff[1] = mySelectedSearchInterval.toString();
                sendbuff[2] = "FORBUNDID";
                sendbuff[3] = mySelectedForbundId.toString();
                sendbuff[4] = "CLUBID";
                sendbuff[5] = mySelectedClubId.toString();
                  Log.d("SNDESB","Start selectevent with SearchInterval: " + sendbuff[1] + " forbundsid: " + sendbuff[3] + " KlubbId:" + sendbuff[5]);
                i.putExtra("arguments", sendbuff); 
                startActivity(i);
                return;
            }
            // Club choice
            case R.id.clubchoice:
            {
                fetchConfig();
                Intent i = new Intent();
                i.setClassName("work.Android", "work.Android.selectclub");
                String sendbuff[] = new String[6];
                sendbuff[0] = "SEARCHLENGTH";
                sendbuff[1] = mySelectedSearchInterval.toString();
                sendbuff[2] = "FORBUNDID";
                sendbuff[3] = mySelectedForbundId.toString();
                sendbuff[4] = "CLUBID";
                sendbuff[5] = mySelectedClubId.toString();
                  Log.d("SNDESB","Start selectclub with SearchInterval: " + sendbuff[1] + " forbundsid: " + sendbuff[3] + " KlubbId:" + sendbuff[5]);
                i.putExtra("arguments", sendbuff); 
                startActivity(i);
                return;
            } 
            // Runners
            case R.id.runnerchoice:
            {
                Toast.makeText(this, "You want to fetch all registrations for one person. Not implemented yet!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            default:
            {
                Toast.makeText(this, "Software error....!!",
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
            fetchConfig();
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
        String sendbuff[] = new String[8];
        i.setClassName("work.Android", "work.Android.configapp");
        sendbuff[0] = "SEARCHLENGTH";
        sendbuff[1] = mySelectedSearchInterval.toString();
        sendbuff[2] = "FORBUNDID";
        sendbuff[3] = mySelectedForbundId.toString();
        sendbuff[4] = "CLUBID";
        sendbuff[5] = mySelectedClubId.toString();
        sendbuff[6] = "CONNECTIONSTATE";
        sendbuff[7] = connectionState.toString();
        Log.d("SNDESB","konfigurera: Start configapp with SearchInterval: " + sendbuff[1] + " forbundsid: " + sendbuff[3] + " KlubbId: " + sendbuff[5] + "Connection: " + sendbuff[7]);
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