package work.Android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;



//import work.Android.selectevent.OnReadyListener;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class selectclub  extends Activity {
	String urlString = "organisations";
	private ArrayList<String> oforbund;
	private ArrayList<String> oforbundid;
	private ArrayList<String> oklubbar;
	private ArrayList<String> oklubbarid;
    private int mYear;
    private int mMonth;
    private int mDay;
	private ArrayAdapter<String> mySpinnerArrayAdapter1;
	private ArrayAdapter<String> mySpinnerArrayAdapter2;
	private Spinner  mySpinner1;
	private Spinner  mySpinner2;
	private Button myDateFrom; 
	private Button myDateTo; 
	private String mySelDateFrom;
	private String mySelDateTo;
	private String mySelSearchLength;
	private String mySelForbundId;
	private String mySelClub;
	private String mySelClubId;

	private int mySelForbundIndx;
	private int mySelKlubbIndx;
	
	private String myBuffSelectedForbundId;
	private String myBuffSelectedClub;
	private String myBuffSearchLength;

	private String mySelClassificationIds;								
//	private String mySelDisciplineIds;			
	private int openstate;
	private int myKlubbIdCreated;
	
    static final int DATE_DIALOG_ID1 = 0;
    static final int DATE_DIALOG_ID2 = 2;

    static final String ORGTYPE_SOFT = "1";
    static final String ORGTYPE_FORB = "2";
    static final String ORGTYPE_KLUBB = "3";
    static final String ORG_STOCKHOLM = "18";
	private Context cont;

    private boolean[] eventtypes; 
    private boolean[] eventdisciplines; 
    private String[] types; 
//    private String[] disciplines; 
    
	///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.selectclub);

		Serializable s = this.getIntent().getSerializableExtra("arguments");
		Object[] o = (Object[]) s;

		if (o != null) {
			myBuffSearchLength = o[1].toString();
			myBuffSelectedForbundId = o[3].toString();
			myBuffSelectedClub = o[5].toString();
		}
		mySelSearchLength = myBuffSearchLength;
		mySelForbundId = myBuffSelectedForbundId;
		mySelClubId = myBuffSelectedClub;

		openstate = 0;
		eventtypes = new boolean[]{true,true,true,true,true,true,true,true,true,true};
		types = new String[]{"1","2","3","4","5","6"};
//		disciplines = new String[]{"1","2","3","4"};
		mySelClassificationIds	= "1,2,3,4,5,6";
//		mySelDisciplineIds	= "1,2,3,4";
		
		myDateFrom = (Button)findViewById(R.id.selectFDate);
   		myDateTo = (Button)findViewById(R.id.selectTDate);
   		mySpinner1 = (Spinner)findViewById(R.id.spinner1);
   		mySpinner2 = (Spinner)findViewById(R.id.spinner2);

		oforbund = new ArrayList<String>();
		oforbundid = new ArrayList<String>(); 
		oklubbar = new ArrayList<String>();
		oklubbarid = new ArrayList<String>(); 
   		
   		fetchAllOrganisationNamesAndIds();
		int forbId = Integer.parseInt(myBuffSelectedForbundId);
		fetchOrgClubNamesAndIds(forbId);
   		  		
		cont = this;

		mySpinnerArrayAdapter1 =  
   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oforbund);
		mySpinnerArrayAdapter1.setDropDownViewResource(R.layout.spinnerlayout);
		mySpinner1.setAdapter(mySpinnerArrayAdapter1);
		mySpinner1.setOnItemSelectedListener(new MyOnItemSelectedListener1()); 	   	 	   		 	   		 	   		 	   		
			
		mySpinnerArrayAdapter2 =  
   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oklubbar);
		mySpinnerArrayAdapter2.setDropDownViewResource(R.layout.spinnerlayout);
		mySpinner2.setAdapter(mySpinnerArrayAdapter2);
		mySpinner2.setOnItemSelectedListener(new MyOnItemSelectedListener2()); 	   	 	   		 	   		 	   		 	   						

		Log.d("SNDESB","Forbund that is stored as selected: " + mySelForbundIndx + " " + oforbund.get(mySelForbundIndx));
		Log.d("SNDESB","Club that is stored as selected: " + mySelKlubbIndx + " " + oklubbar.get(mySelKlubbIndx));
		mySpinner1.setSelection(mySelForbundIndx,true);
		mySpinner2.setSelection(mySelKlubbIndx,true);

		// get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
		mMonth = mMonth + 1;        
        mDay = c.get(Calendar.DAY_OF_MONTH);       
        updateDisplay1();
        mMonth = c.get(Calendar.MONTH);       
		mMonth = mMonth + 3;
        updateDisplay2();
        myKlubbIdCreated = 0;
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

		Log.d("SNDESB","Search for selected forbundid: " + myBuffSelectedForbundId + " among forbunds");

		for (String oforbid : oforbundid) {
//			Log.d("SNDESB","selectclub: oforbid: " + oforbid);

			if (oforbid.equals(myBuffSelectedForbundId)) {
				mySelForbundIndx = i;
//				mySelectedForbundId = oforbundid.get(i);
//				mySelectedForbund = oforbund.get(i);
				Log.d("SNDESB","Found forbundsid: " + myBuffSelectedForbundId + " in index : " + i + " name:" + oforbund.get(i));
				break;
			}
            i++;
		}
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

			if (klubbid.equals(mySelClubId)) {
				mySelKlubbIndx = i;
				mySelClubId = oklubbarid.get(i);
				Log.d("SNDESB","Found klubbid: " + mySelClubId + " in index : " + i + " name:" + oklubbar.get(i));
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
  	public class MyOnItemSelectedListener1 implements OnItemSelectedListener 
   	{
   	    public void onItemSelected(AdapterView<?> parent,
   	        View view, int pos, long id) {
			mySelForbundId = oforbundid.get(pos);
			Log.d("SNDESB","MyOnItemSelectedListener1: Selected pos : " + pos + " which is : " + parent.getItemAtPosition(pos).toString() + " och id:" + oforbundid.get(pos) );
			mySelForbundIndx = pos;
			Integer val = new Integer(mySelForbundId);		
			fetchOrgClubNamesAndIds(val);

			mySpinnerArrayAdapter2 = new ArrayAdapter<String>(cont, R.layout.spinnerlayout, oklubbar);
			mySpinnerArrayAdapter2.setDropDownViewResource(R.layout.spinnerlayout);
			mySpinner2.setAdapter(mySpinnerArrayAdapter2);
	   		mySpinner2.setSelection(mySelKlubbIndx,true);
   	    }

   	    public void onNothingSelected(AdapterView<?> parent) {
			mySelForbundId = oforbundid.get(0);
   	    }
   	}
	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
   	public class MyOnItemSelectedListener2 implements OnItemSelectedListener 
   	{
   	    public void onItemSelected(AdapterView<?> parent,
   	        View view, int pos, long id) {
  			mySelClub = parent.getItemAtPosition(pos).toString();

  			if (myKlubbIdCreated == 1) {
  				mySelClubId = oklubbarid.get(pos);
  	  			Log.d("SNDESB","MyOnKlubbItemSelectedListener: Selected klubb id : " + mySelClubId );
  			}
   	    }

   	    public void onNothingSelected(AdapterView<?> parent) {
   	      // Do nothing.
   	    }
   	}
	
    ///////////////////////////////////////////////////////////
    //
    //
    // updates the date in the TextView
    //
    ///////////////////////////////////////////////////////////
  	private void updateDisplay1() {
   		String mZeroMonth = "";
   		String mZeroDay = "";
   		
		if (mMonth < 10) {
			mZeroMonth = "0";
		}
		if (mDay < 10) {
			mZeroDay = "0";
		}  		
   		
		myDateFrom.setText(
            new StringBuilder()
                // Month is 0 based so add 1
            	.append(mYear)
            	.append("-")
            	.append(mZeroMonth)
            	.append(mMonth)
            	.append("-")
            	.append(mZeroDay)
                .append(mDay));
        mySelDateFrom = myDateFrom.getText().toString();
    }

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
    private void updateDisplay2() {
   		String mZeroMonth = "";
   		String mZeroDay = "";
   	   		
		if (mMonth < 10) {
			mZeroMonth = "0";
		}
		if (mDay < 10) {
			mZeroDay = "0";
		}  		
        myDateTo.setText(
            new StringBuilder()
                // Month is 0 based so add 1
         	.append(mYear)
         	.append("-")
          	.append(mZeroMonth)
           	.append(mMonth)
           	.append("-")
           	.append(mZeroDay)
            .append(mDay));
        mySelDateTo = myDateTo.getText().toString();
    }

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
    private DatePickerDialog.OnDateSetListener mDateSetListener1 =
        new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, 
                                  int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear+1;
                mDay = dayOfMonth;
                updateDisplay1();
            }
    };

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
    private DatePickerDialog.OnDateSetListener mDateSetListener2 =
        new DatePickerDialog.OnDateSetListener() {
    	public void onDateSet(DatePicker view, int year, 
    			int monthOfYear, int dayOfMonth) {
    		mYear = year;
    		mMonth = monthOfYear+1;
    		mDay = dayOfMonth;
    		updateDisplay2();
    	}
    };
        
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
    @Override
    protected Dialog onCreateDialog(int id) {
    	switch (id) {
    	case DATE_DIALOG_ID1:
    		return new DatePickerDialog(this,
    				mDateSetListener1,
    				mYear, mMonth, mDay);
    	case DATE_DIALOG_ID2:
    		return new DatePickerDialog(this,
    				mDateSetListener2,
    				mYear, mMonth, mDay);
    	}
    	return null;
    }

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
    @SuppressWarnings("deprecation")
	public void mySelectFrDateClickHandler(View view) {

    	switch (view.getId()) {
    	case R.id.selectFDate:
    	{	   				
    		showDialog(DATE_DIALOG_ID1);
    		return;
    	}
    	}		
    }

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
    @SuppressWarnings("deprecation")
	public void mySelectToDateClickHandler(View view) {
    	switch (view.getId()) {
    	case R.id.selectTDate:
    	{	
    		showDialog(DATE_DIALOG_ID2);
    		return;
    	}
    	}
    }

    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
    private class OnReadyListener implements eventtypes.ReadyListener {
    	@Override

    	public void ready(boolean typ1, boolean typ2, boolean typ3, 
				  boolean typ4, boolean typ5, boolean typ6,
				  boolean disc1, boolean disc2, boolean disc3, boolean disc4) {

    		eventtypes[0] = typ1;                
    		eventtypes[1] = typ2;                
    		eventtypes[2] = typ3;                
    		eventtypes[3] = typ4;                
    		eventtypes[4] = typ5;                
    		eventtypes[5] = typ6;
    		eventdisciplines[0] = disc1;
    		eventdisciplines[1] = disc2;
    		eventdisciplines[2] = disc3;
    		eventdisciplines[3] = disc4;
	
    		mySelClassificationIds = "";
//    		mySelDisciplineIds = "";

    		int i;
    		boolean commaneeded = false;

    		for (i = 0; i<5; i++) {

    			if (eventtypes[i]) { 
    				mySelClassificationIds = mySelClassificationIds + types[i];        		
    				commaneeded = false;

    				for (int j=i+1; j<6; j++) {

    					if (eventtypes[j]) {
    						commaneeded = true;
    					}
    				}
    				if (commaneeded) {
    					mySelClassificationIds = mySelClassificationIds + ",";
    				}
    			}
    			Log.d("SNDESB OnReadyListener"," just nu: " + mySelClassificationIds);
    		}            		

    		if (eventtypes[5]) { 
    			mySelClassificationIds = mySelClassificationIds + types[5];        		
    			Log.d("SNDESB OnReadyListener"," slutklämm: " + mySelClassificationIds);
    		}

    		Log.d("SNDESB OnReadyListener"," resultat: " + mySelClassificationIds);        	
    	}
    }

    ///////////////////////////////////////////////////////////
    //
    // This method is called at button click because we assigned the name to the
    // "On Click property" of the button
    //
    ///////////////////////////////////////////////////////////
    public void myKlubbSearchClickHandler(View view) {
    	switch (view.getId()) {
    	case R.id.eventtypechoice:
    	{
    		eventtypes ttyp = new eventtypes(this, eventtypes[0],eventtypes[1], eventtypes[2],
    		eventtypes[3], eventtypes[4], eventtypes[5], eventdisciplines[0], 
    		eventdisciplines[1],eventdisciplines[2], eventdisciplines[3], new OnReadyListener());
    		ttyp.show();
    		return;
    	}
    	case R.id.searchchoice:
    	{	
    		Log.d("SNDESB","myKlubbSearchClickHandler klubbid" + mySelClubId + " som Šr : " + mySelClub );
    		String kalle[] = new String[8];
    		kalle[0] = "DATE_FROM";
    		kalle[1] = mySelDateFrom;
    		kalle[2] = "DATE_TO";
    		kalle[3] = mySelDateTo;
    		kalle[4] = "KLUBBID";
    		kalle[5] = mySelClubId;
    		kalle[6] = "KLUBB";
    		kalle[7] = mySelClub;
    		//				kalle[8] = "CLASSIFICATIONIDS";
    		//				kalle[9] = mySelClassificationIds;

    		Intent i = new Intent();
    		i.setClassName("work.Android", "work.Android.clubentry");
    		i.putExtra("arguments", kalle);
    		startActivity(i);
    		return;
    	}
    	}		
    }
    
       
    ///////////////////////////////////////////////////////////
    // GENERAL MENU HANDLING
    ///////////////////////////////////////////////////////////

    
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
    // Handle item selection
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
    //
    //
    ///////////////////////////////////////////////////////////
    public void konfigurera() {

    	Intent i = new Intent();
    	String sendbuff[] = new String[6];
    	i.setClassName("work.Android", "work.Android.configapp");
    	sendbuff[0] = "SEARCHLENGTH";
    	sendbuff[1] = mySelSearchLength.toString();
    	sendbuff[2] = "FORBUNDID";
    	sendbuff[3] = mySelForbundId.toString();
    	sendbuff[4] = "CLUBID";
    	sendbuff[5] = mySelClubId.toString();
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