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
public class selecttavling  extends Activity {
	String urlString = "organisations";
	private ArrayList<String> oforbund;
	private ArrayList<String> oforbundid;
	private  ArrayList<String> klubbarSelected;	
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

	private String myBuffSelectedForbundId;
	
	private int mySelForbundIndx;
	private int openstate;
    static final int DATE_DIALOG_ID1 = 0;
    static final int DATE_DIALOG_ID2 = 2;

    static final String ORGTYPE_SOFT = "1";
    static final String ORGTYPE_FORB = "2";
    static final String ORGTYPE_KLUBB = "3";
    static final String ORG_STOCKHOLM = "18";

    private boolean[] tavlingstyper; 
    private String[] typer; 
   
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////    
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.selecttavling);

   		Serializable s = this.getIntent().getSerializableExtra("arguments");
   		Object[] o = (Object[]) s;

   		if ((o != null) && (o[0].toString().equals("FORBUNDID"))) {
   			myBuffSelectedForbundId = o[1].toString();
   		}

   		openstate = 0;
		tavlingstyper = new boolean[]{true,true,true,true,true,true};
		typer = new String[]{"1","2","3","4","5","6"};
		mySelClassificationIds	= "1,2,3,4,5,6";
		oforbund = new ArrayList<String>();
		oforbundid = new ArrayList<String>(); 
		klubbarSelected = new ArrayList<String>();

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
        updateDisplay1();
        mMonth = c.get(Calendar.MONTH);       
		Log.e("XTRACTOR","Month1 : " + mMonth);
		// Adjust for new year
		if (mMonth > 8) { 
        	mYear = mYear+1;
        }
		mMonth=(mMonth+3)%11;
   		Log.e("XTRACTOR","Month2 : " + mMonth);
        updateDisplay2();		
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
		//	Log.d("SNDESB","Search index: " + i + "forbundid: " + oforbid);

			if (oforbid.equals(myBuffSelectedForbundId)) {
				mySelForbundIndx = i;
				mySelForbundId = oforbundid.get(i);
				mySelForbund = oforbund.get(i);
				Log.d("SNDESB","Found forbundsid: " + myBuffSelectedForbundId + " in index : " + i + " name:" + oforbund.get(i));
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
//			Log.e("XTRACTOR","Selected pos : " + pos + " som Šr : " + parent.getItemAtPosition(pos).toString() );
			mySelForbund = parent.getItemAtPosition(pos).toString();
			mySelForbundId = oforbundid.get(pos);
			mySelForbundIndx = pos;
			Log.e("XTRACTOR","Selected pos : " + pos + " som Šr : " + mySelForbund + " och id:" + oforbundid.get(pos) );
   	    }

   	    public void onNothingSelected(AdapterView<?> parent) {
   	    	mySelForbund = oforbund.get(0);
			mySelForbundId = oforbundid.get(0);
   	    }
   	}

    ///////////////////////////////////////////////////////////
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
	                .append(mYear)
	                .append("-")
   	            	.append(mZeroMonth)
   	            	.append(mMonth)
   	            	.append("-")
   	                .append(mZeroDay)
   	                .append(mDay));
   	        mySelDateFrom = myDateFrom.getText().toString();
			Log.e("XTRACTOR","update display1 : " + mySelDateFrom);
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
   	            	.append(mYear)
   	            	.append("-")
	            	.append(mZeroMonth)
	            	.append(mMonth)
	            	.append("-")
	                .append(mZeroDay)
	                .append(mDay));
   	        mySelDateTo = myDateTo.getText().toString();
			Log.e("XTRACTOR","update display2 : " + mySelDateTo);
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
                mDay = dayOfMonth;
                mMonth = monthOfYear+1;
                mYear = year;
                updateDisplay2();
    	}
    };
        
    
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
    public void konfigurera() {

    	Intent i = new Intent();
    	i.setClassName("work.Android", "work.Android.configApp");
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
    // Handle item selection
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
  	private class OnReadyListener implements tavltyper.ReadyListener {
        @Override

        public void ready(boolean typ1, boolean typ2, boolean typ3, 
        				  boolean typ4, boolean typ5, boolean typ6) {

        	tavlingstyper[0] = typ1;                
        	tavlingstyper[1] = typ2;                
        	tavlingstyper[2] = typ3;                
        	tavlingstyper[3] = typ4;                
        	tavlingstyper[4] = typ5;                
        	tavlingstyper[5] = typ6;

        	mySelClassificationIds = "";
        	int i;
        	boolean commaneeded = false;

        	for (i = 0; i<5; i++) {
        		
        		if (tavlingstyper[i]) { 
            		mySelClassificationIds = mySelClassificationIds + typer[i];        		
    				commaneeded = false;
    				
            		for (int j=i+1; j<6; j++) {

            			if (tavlingstyper[j]) {
            				commaneeded = true;
            			}
            		}
            		if (commaneeded) {
            			mySelClassificationIds = mySelClassificationIds + ",";
            		}
        		}
        		Log.e("XTRACTOR OnReadyListener"," just nu: " + mySelClassificationIds);
        	}            		

        	if (tavlingstyper[5]) { 
        		mySelClassificationIds = mySelClassificationIds + typer[5];        		
    			Log.e("XTRACTOR OnReadyListener"," slutklämm: " + mySelClassificationIds);
        	}
        	
			Log.e("XTRACTOR OnReadyListener"," resultat: " + mySelClassificationIds);        	
        }
   	}
   	
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void myTavlingSearchClickHandler(View view) {
		switch (view.getId()) {
		
			case R.id.tavlingstypval:
			{
                tavltyper ttyp = new tavltyper(this, tavlingstyper[0],tavlingstyper[1], tavlingstyper[2],
                		tavlingstyper[3], tavlingstyper[4], tavlingstyper[5], new OnReadyListener());
                ttyp.show();
                return;
			}
			case R.id.sokval:
			{
				Log.e("XTRACTOR"," myTavlingSearchClickHandler : DateFrom:" + mySelDateFrom + " DateTo:" + 
						mySelDateTo + " ForbundId: " + mySelForbundId + " Forbund: " + mySelForbund + " ClassificationIds:" + mySelClassificationIds );
				klubbarSelected.clear();
				klubbarSelected.add("DATE_FROM");
				klubbarSelected.add(mySelDateFrom);
				klubbarSelected.add("DATE_TO");
				klubbarSelected.add(mySelDateTo);
				klubbarSelected.add("FORBUNDID");
				klubbarSelected.add(mySelForbundId);
				klubbarSelected.add("FORBUND");
				klubbarSelected.add(mySelForbund);
				klubbarSelected.add("CLASSIFICATIONIDS");
				klubbarSelected.add(mySelClassificationIds);								

				Log.e("XTRACTOR"," myTavlingSearchClickHandler : array size (borde vara 10) : " + klubbarSelected.size());
				
				// Store Klubbar belonging to selected forbund
				// Since it's possible to select "allaFšrbund" we need to even store getParentOrganisationId 
//				for (organisation org : organisations){

//					if () {
						
//					}else {
						
//						if ((org.getOrganisationTypeId().equals(ORGTYPE_KLUBB)) &&
//								(org.getParentOrganisationId().equals(mySelForbundId))) {
//							klubbarSelected.add(org.getOrganisationId());
//							klubbarSelected.add(org.getShortName());
//							Log.e("XTRACTOR"," myTavlingSearchClickHandler 2 : stored org : " + org.getShortName() + ", " + org.getOrganisationId());
//						} else {
//							Log.e("XTRACTOR"," myTavlingSearchClickHandler 3 : org not part of : " + mySelForbundId + ": " + org.getShortName() + ", " + org.getOrganisationId());
//						}	   															
//					}
//				}
//				Log.e("XTRACTOR"," myTavlingSearchClickHandler 4 : array size : " + klubbarSelected.size());
				
				Intent i = new Intent();
				i.setClassName("work.Android", "work.Android.tavlingar");
                i.putExtra("arguments", klubbarSelected);
				startActivity(i);
				return;
			}
		}		
	}
}