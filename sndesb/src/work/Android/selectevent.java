package work.Android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.database.SQLException;

///////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////    
public class selectevent  extends Activity {
	String urlString = "organisations";
	private ArrayList<String> oforbund;
	private ArrayList<String> oforbundid;
	private  ArrayList<String> selectedClubs;	
    private int mYear;
    private int mMonth;
    private int mDay;
	private ArrayAdapter<String> mySpinnerArrayAdapter;
	private Spinner  mySpinner;
	private TextView myDateFrom; 
	private TextView myDateTo; 
	private String mySelDateFrom;
	private String mySelDateTo;
	private String mySelForbund;
	private String mySelForbundId;
	private String mySelClassificationIds;			
	private String mySelDisciplineIds;			
	private String mySelSearchLength;
	//private String mySelClub;
	private String mySelClubId;

	private String myBuffSelectedForbundId;
	private String myBuffSelectedKlubb;
	private String myBuffSearchLength;
	
	private int mySelForbundIndx;
	private int openstate;
    static final int DATE_DIALOG_ID1 = 0;
    static final int DATE_DIALOG_ID2 = 2;

    static final String ORGTYPE_SOFT = "1";
    static final String ORGTYPE_FORB = "2";
    static final String ORGTYPE_KLUBB = "3";
    static final String ORG_STOCKHOLM = "18";

    private boolean[] eventtypes; 
    private boolean[] eventdisciplines; 
    private String[] types; 
    private String[] disciplines; 
   
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////    
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.selectevent);

   		Serializable s = this.getIntent().getSerializableExtra("arguments");
   		Object[] o = (Object[]) s;

   		// && (o[0].toString().equals("FORBUNDID")))
   		if (o != null) {
   			myBuffSearchLength = o[1].toString();
   			myBuffSelectedForbundId = o[3].toString();
   			myBuffSelectedKlubb = o[5].toString();
   		}

   		mySelSearchLength = myBuffSearchLength;
   		mySelForbundId = myBuffSelectedForbundId;
   		mySelClubId = myBuffSelectedKlubb;
   		
   		openstate = 0;
		eventtypes = new boolean[]{true,true,true,true,true,true};
		eventdisciplines = new boolean[]{true,true,true,true,true};
		types = new String[]{"1","2","3","4","5","6"};
		disciplines = new String[]{"1","2","3","4"};
		mySelClassificationIds	= "1,2,3,4,5,6";
		mySelDisciplineIds	= "1,2,3,4,5,6";
		oforbund = new ArrayList<String>();
		oforbundid = new ArrayList<String>(); 
		selectedClubs = new ArrayList<String>();

		myDateFrom = (TextView)findViewById(R.id.selectFDate);
   		myDateTo = (TextView)findViewById(R.id.selectTDate);
   		mySpinner = (Spinner)findViewById(R.id.spinner1);
   		
   		fetchAllOrganisationNamesAndIds();

		mySpinnerArrayAdapter =  
   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oforbund);
	   		mySpinnerArrayAdapter.setDropDownViewResource(R.layout.spinnerlayout);
	   		mySpinner.setAdapter(mySpinnerArrayAdapter);
	   		mySpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
			mySpinner.setSelection(mySelForbundIndx,true);
		
   		// get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;  // January is 0 but we want to see it as 1
        mDay = c.get(Calendar.DAY_OF_MONTH);       
        updateDisplayFromDate();
        mMonth = c.get(Calendar.MONTH);       
		Log.e("SNDESB","Month1 : " + mMonth);
		// Adjust for new year
		if (mMonth > 8) { 
        	mYear = mYear+1;
        }
		mMonth=(mMonth+3)%11;
   		Log.e("SNDESB","Month2 : " + mMonth);
        updateDisplayToDate();		
   	}

	///////////////////////////////////////////////////////////
    //
    //  fetchAllOrganisationNamesAndIds
    //
	//  Fetch all "Forbunds" from local database
	//  Remember the selected forbundsid/name
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

		Log.d("SNDESB","selectevent: Search for selected forbundid: " + myBuffSelectedForbundId + " among forbundsId");

		for (String oforbid : oforbundid) {
			Log.d("SNDESB","selectevent: Search index: " + i + "forbundid: " + oforbid + " " + oforbund.get(i));

			if (oforbid.equals(myBuffSelectedForbundId)) {
				mySelForbundIndx = i;
				mySelForbundId = oforbundid.get(i);
				mySelForbund = oforbund.get(i);
				Log.d("SNDESB","selectevent: Found forbundsid: " + myBuffSelectedForbundId + " in index : " + i + " name:" + oforbund.get(i));
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
   	public class MyOnItemSelectedListener implements OnItemSelectedListener 
   	{
   	    public void onItemSelected(AdapterView<?> parent,
   	        View view, int pos, long id) {
//			Log.e("SNDESB","selectevent: Selected pos : " + pos + " beeing : " + parent.getItemAtPosition(pos).toString() );
			mySelForbund = parent.getItemAtPosition(pos).toString();
			mySelForbundId = oforbundid.get(pos);
			mySelForbundIndx = pos;
			Log.e("SNDESB","Selected pos : " + pos + " beeing : " + mySelForbund + " and id:" + oforbundid.get(pos) );
   	    }

   	    public void onNothingSelected(AdapterView<?> parent) {
   	    	mySelForbund = oforbund.get(0);
			mySelForbundId = oforbundid.get(0);
   	    }
   	}

    ///////////////////////////////////////////////////////////
    //
   	// updateDisplay1
   	//
   	// updates the fromdate in the selectevent screen
    //
    ///////////////////////////////////////////////////////////
   	private void updateDisplayFromDate() {
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
	                .append(mYear)
	                .append("-")
   	            	.append(mZeroMonth)
   	            	.append(mMonth)
   	            	.append("-")
   	                .append(mZeroDay)
   	                .append(mDay));
   	        mySelDateFrom = myDateFrom.getText().toString();
			Log.e("SNDESB","selectevent: update fromdate : " + mySelDateFrom);
    }

    ///////////////////////////////////////////////////////////
    //
   	// updateDisplay2
   	//
   	// updates the todate in the selectevent screen
    //
    ///////////////////////////////////////////////////////////
    private void updateDisplayToDate() {
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
   	            	.append(mYear)
   	            	.append("-")
	            	.append(mZeroMonth)
	            	.append(mMonth)
	            	.append("-")
	                .append(mZeroDay)
	                .append(mDay));
   	        mySelDateTo = myDateTo.getText().toString();
			Log.e("SNDESB","update display2 : " + mySelDateTo);
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
                mDay = dayOfMonth;
                mMonth = monthOfYear+1;
                mYear = year;
                updateDisplayFromDate();
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
                mDay = dayOfMonth;
                mMonth = monthOfYear+1;
                mYear = year;
                updateDisplayToDate();
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
                       mYear, mMonth-1, mDay);
        case DATE_DIALOG_ID2:
            return new DatePickerDialog(this,
            		mDateSetListener2,
                    mYear, mMonth-1, mDay);
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
        	mySelDisciplineIds = "";
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
        		Log.e("SNDESB OnReadyListener"," just nu: " + mySelClassificationIds);
        	}            		

        	for (i = 0; i<3; i++) {
        		
        		if (eventdisciplines[i]) { 
            		mySelDisciplineIds = mySelDisciplineIds + disciplines[i];        		
    				commaneeded = false;
    				
            		for (int j=i+1; j<4; j++) {

            			if (eventdisciplines[j]) {
            				commaneeded = true;
            			}
            		}
            		if (commaneeded) {
            			mySelDisciplineIds = mySelDisciplineIds + ",";
            		}
        		}
        		Log.e("SNDESB OnReadyListener"," just nu: " + mySelDisciplineIds);
        	}            		
        	
        	if (eventtypes[5]) { 
        		mySelClassificationIds = mySelClassificationIds + types[5];        		
    			Log.e("SNDESB OnReadyListener"," slutklŠm: " + mySelClassificationIds);
        	}
        	if (eventdisciplines[3]) { 
        		mySelDisciplineIds = mySelDisciplineIds + disciplines[3];        		
    			Log.e("SNDESB OnReadyListener"," slutklŠm: " + mySelDisciplineIds);
        	}
        	
			Log.e("SNDESB OnReadyListener"," resultat: " + mySelClassificationIds + "," + mySelDisciplineIds);        	
        }
   	}
   	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void myEventSearchClickHandler(View view) {
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
				Log.e("SNDESB"," myEventSearchClickHandler : DateFrom:" + mySelDateFrom + " DateTo:" + 
						mySelDateTo + " ForbundId: " + mySelForbundId + " Forbund: " + mySelForbund +
						" ClassificationIds:" + mySelClassificationIds + "Disciplins:"  + mySelDisciplineIds );
				selectedClubs.clear();
				selectedClubs.add("DATE_FROM");
				selectedClubs.add(mySelDateFrom);
				selectedClubs.add("DATE_TO");
				selectedClubs.add(mySelDateTo);
				selectedClubs.add("FORBUNDID");
				selectedClubs.add(mySelForbundId);
				selectedClubs.add("FORBUND");
				selectedClubs.add(mySelForbund);
				selectedClubs.add("CLASSIFICATIONIDS");
				selectedClubs.add(mySelClassificationIds);								
				selectedClubs.add("DISCIPLINIDS");
				selectedClubs.add(mySelDisciplineIds);								

				Log.e("SNDESB"," myEventSearchClickHandler : array size (borde vara 12) : " + selectedClubs.size());
				
				// Store Clubs belonging to selected forbund
				// Since it's possible to select "allaFšrbund" we need to even store getParentOrganisationId 
//				for (organisation org : organisations){

//					if () {
						
//					}else {
						
//						if ((org.getOrganisationTypeId().equals(ORGTYPE_KLUBB)) &&
//								(org.getParentOrganisationId().equals(mySelForbundId))) {
//							selectedClubs.add(org.getOrganisationId());
//							selectedClubs.add(org.getShortName());
//							Log.e("SNDESB"," myEventSearchClickHandler 2 : stored org : " + org.getShortName() + ", " + org.getOrganisationId());
//						} else {
//							Log.e("SNDESB"," myEventSearchClickHandler 3 : org not part of : " + mySelForbundId + ": " + org.getShortName() + ", " + org.getOrganisationId());
//						}	   															
//					}
//				}
//				Log.e("SNDESB"," myEventSearchClickHandler 4 : array size : " + selectedClubs.size());
				
				Intent i = new Intent();
				i.setClassName("work.Android", "work.Android.events");
                i.putExtra("arguments", selectedClubs);
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