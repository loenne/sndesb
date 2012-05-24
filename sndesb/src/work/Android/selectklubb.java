package work.Android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// import work.Android.selecttavling.OnReadyListener;
// import work.Android.tavltyper.ReadyListener;

import android.app.ProgressDialog;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ListView;
import android.widget.Spinner;
// import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class selectklubb  extends Activity {
	String urlString = "organisations";
	private boolean fetchOk; 
	private Runnable getOrg;
	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<String> oforbund;
	private ArrayList<String> oforbundid;
	private List<Organisation> organisations;
	private ArrayList<String> klubbar;
	private ArrayList<String> klubbid;
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
	private String mySelForbundId;
	private String mySelKlubb;
	private String mySelKlubbId;
	private String mySearchLength;
	private String mySelectedForbund;
	private String mySelectedKlubb;
	
	private String mySelClassificationIds;								
	private int defForbundIndex;
	private int defKlubbIndex;
	int klubbIdCreated;	
	
    static final int DATE_DIALOG_ID1 = 0;
    static final int DATE_DIALOG_ID2 = 2;

    static final String ORGTYPE_SOFT = "1";
    static final String ORGTYPE_FORB = "2";
    static final String ORGTYPE_KLUBB = "3";
    static final String ORG_STOCKHOLM = "18";

    private boolean[] tavlingstyper; 
    private String[] typer; 
    
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.selectklubb);

		 Serializable s = this.getIntent().getSerializableExtra("arguments");
		 Object[] o = (Object[]) s;

		 if (o != null) {
	       		mySearchLength = o[1].toString();
	       		mySelectedForbund = o[3].toString();
	       		mySelectedKlubb = o[5].toString();
		 }

		 /*		
		getOrg = new Runnable() {
			@Override
			public void run() {
				loadForbund();
			}
		};
*/		
		tavlingstyper = new boolean[]{true,true,true,true,true,true};
		typer = new String[]{"1","2","3","4","5","6"};
		mySelClassificationIds	= "1,2,3,4,5,6";

		myDateFrom = (Button)findViewById(R.id.selectFDate);
   		myDateTo = (Button)findViewById(R.id.selectTDate);
   		mySpinner1 = (Spinner)findViewById(R.id.spinner1);
   		mySpinner2 = (Spinner)findViewById(R.id.spinner2);

		oforbund = new ArrayList<String>();
		oforbundid = new ArrayList<String>(); 
		klubbar = new ArrayList<String>();
		klubbid = new ArrayList<String>(); 

//		Thread thread = new Thread(null, getOrg, "MagentoBackground");
//		thread.start();
		
//		m_ProgressDialog = ProgressDialog.show(selectklubb.this,"Please wait...", "Retreiving data...",true);

		mySpinnerArrayAdapter1 =  
   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, oforbund);
	   		mySpinnerArrayAdapter1.setDropDownViewResource(R.layout.spinnerlayout);
	   		mySpinner1.setAdapter(mySpinnerArrayAdapter1);
	   		mySpinner1.setOnItemSelectedListener(new MyOnItemSelectedListener1()); 	   	 	   		 	   		 	   		 	   		
			
		mySpinnerArrayAdapter2 =  
   			new ArrayAdapter<String>(this, R.layout.spinnerlayout, klubbar);
	   		mySpinnerArrayAdapter2.setDropDownViewResource(R.layout.spinnerlayout);
	   		mySpinner2.setAdapter(mySpinnerArrayAdapter2);
	   		mySpinner2.setOnItemSelectedListener(new MyOnItemSelectedListener2()); 	   	 	   		 	   		 	   		 	   						
		defForbundIndex = 0;
		defKlubbIndex = 0;

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
        klubbIdCreated = 0;
   	}

	private void loadForbund() 
	{
		oforbund.clear();
		oforbundid.clear();

		// Specialläsning start. Eftersom man inte får hämta andra klubbar !!
//		oforbund.add("[Alla]");
//		oforbundid.add("0");
/*
 		oforbund.add("Stockholms Orienteringsförbund");		
		oforbundid.add("18");
		defForbundIndex = 0;
		mySelForbundId = "18";
*/
		// Speciallösning stop. Eftersom man inte får hämta andra klubbar !!
		
		try{
			RestAPI andRest = new RestAPI();
			fetchOk = andRest.queryRESTurl(urlString);

			if (fetchOk) {
				organisations = andRest.parseOrganisations();    		

				// 1 = Orienteringsförbundet
				// 2 = Forbund
				// 3 = Klubb
				int i = 0;

				for (Organisation org : organisations){

					if (org.getOrganisationTypeId().equals("2")) {
						// Log.e("SNDESB","Stored : " + org.getShortName() + " id: " + org.getOrganisationId() + " in pos : " + i);
						oforbund.add(org.getShortName());
						oforbundid.add(org.getOrganisationId());

						String stof = "Stockholms Orienteringsfˆrbund";

						if (org.getShortName().equals(stof)) {
							defForbundIndex = i;
							Log.e("SNDESB","Found " + stof + " in index : " + i + " id:" + oforbundid.get(i));
							//mySelForbund = oforbund.get(i);
							mySelForbundId = oforbundid.get(i);  	  						
						}
						i++;
					}
				}
			} else {
/*	
 				Toast.makeText(this, "No network connection. Check mobile network",
						Toast.LENGTH_LONG).show();
*/				oforbund.add("Inga förbund hittade");
				oforbundid.add("1");
			}  			
		} catch (Throwable t){
			Log.e("SNDESB","loadForbund : Failing to fetch forbund \n" + t.getMessage(),t);
		}
//		runOnUiThread(returnRes);
	}
/*
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			mySpinnerArrayAdapter1.notifyDataSetChanged();
			mySpinnerArrayAdapter2.notifyDataSetChanged();
	   		mySpinner1.setSelection(defForbundIndex,true);			
	   		mySpinner2.setSelection(defKlubbIndex,true);			
			m_ProgressDialog.dismiss();
		}
	};
*/	
	private void loadKlubbar(String forbundid) 
   	{
		klubbar.clear();
		klubbid.clear();
//		Log.e("SNDESB","loadKlubbar : antal organis :" + organisations.size() + " forbund : " + forbundid);
		klubbIdCreated = 1;

		// Specialläsning start. Eftersom man inte får hämta andra klubbar !!
//		klubbar.add("[Alla]");
//		klubbid.add("1");
//		klubbar.add("Skarpn‰cks OL");
//		klubbid.add("335");
//		defKlubbIndex = 0;
		// Speciallˆsning stop. Eftersom man inte fÂr h‰mta andra klubbar !!

		// 1 = Orienteringsfˆrbundet
		// 2 = Forbund
		// 3 = Klubb
		
 		int i = 0;
				
		for (Organisation org : organisations){

			if ((org.getOrganisationTypeId().equals("3")) &&
				(org.getParentOrganisationId().equals(forbundid))) {
				klubbar.add(org.getShortName());
				klubbid.add(org.getOrganisationId());
				// Log.e("SNDESB","Load klubbar : Added : " + org.getShortName() + " :" + org.getOrganisationId());    			

				String sol = "Skarpnäcks OL";

				if (org.getShortName().equals(sol)) {
					defKlubbIndex = i;
					// Log.e("SNDESB","Found Skarpnäck as 335 in index : " + i);
					mySelKlubb = klubbar.get(i);
   	    			mySelKlubbId = klubbid.get(i);  	  						
				}			
				i++;
			}
		}
		mySpinnerArrayAdapter2.notifyDataSetChanged();		
   	}		

   	public class MyOnItemSelectedListener1 implements OnItemSelectedListener 
   	{
   	    public void onItemSelected(AdapterView<?> parent,
   	        View view, int pos, long id) {
			// Log.e("SNDESB","onItemSelected1 Selected pos : " + pos + "som ‰r : " + parent.getItemAtPosition(pos).toString() + " och id:" + oforbundid.get(pos) );
			// mySelForbund = parent.getItemAtPosition(pos).toString();
			mySelForbundId = oforbundid.get(pos);
			loadKlubbar(mySelForbundId);
   	    }

   	    public void onNothingSelected(AdapterView<?> parent) {
   	    	// mySelForbund = oforbund.get(0);
			mySelForbundId = oforbundid.get(0);
   	    }
   	}
	
   	public class MyOnItemSelectedListener2 implements OnItemSelectedListener 
   	{
   	    public void onItemSelected(AdapterView<?> parent,
   	        View view, int pos, long id) {
			// Log.e("SNDESB","onItemSelected2 Selected pos : " + pos + "som ‰r : " + parent.getItemAtPosition(pos).toString() );
			mySelKlubb = parent.getItemAtPosition(pos).toString();

			if (klubbIdCreated == 1) {
				mySelKlubbId = klubbid.get(pos);
			}
   	    }

   	    public void onNothingSelected(AdapterView<?> parent) {
   	      // Do nothing.
   	    }
   	}
	
    // updates the date in the TextView
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



   	public void mySelectFrDateClickHandler(View view) {

   		switch (view.getId()) {
   			case R.id.selectFDate:
   			{	   				
   	            showDialog(DATE_DIALOG_ID1);
   	            return;
   			}
   		}		
   	}
	
   	public void mySelectToDateClickHandler(View view) {
   		switch (view.getId()) {
   			case R.id.selectTDate:
   			{	
   	            showDialog(DATE_DIALOG_ID2);
   	            return;
   			}
   		}		
   	}
   	  	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;		
	}

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
 //       	boolean commaplaced = false;

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
        		Log.e("SNDESB OnReadyListener"," just nu: " + mySelClassificationIds);
        	}            		

        	if (tavlingstyper[5]) { 
        		mySelClassificationIds = mySelClassificationIds + typer[5];        		
    			Log.e("SNDESB OnReadyListener"," slutkl‰mm: " + mySelClassificationIds);
        	}
        	
			Log.e("SNDESB OnReadyListener"," resultat: " + mySelClassificationIds);        	
        }
   	}
        
   	// This method is called at button click because we assigned the name to the
	// "On Click property" of the button
	public void myKlubbSearchClickHandler(View view) {
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
				Log.e("SNDESB","myKlubbSearchClickHandler klubbid" + mySelKlubbId + " som ‰r : " + mySelKlubb );
				String kalle[] = new String[8];
				kalle[0] = "DATE_FROM";
				kalle[1] = mySelDateFrom;
				kalle[2] = "DATE_TO";
				kalle[3] = mySelDateTo;
				kalle[4] = "KLUBBID";
				kalle[5] = mySelKlubbId;
				kalle[6] = "KLUBB";
				kalle[7] = mySelKlubb;
//				kalle[8] = "CLASSIFICATIONIDS";
//				kalle[9] = mySelClassificationIds;

				Intent i = new Intent();
				i.setClassName("work.Android", "work.Android.klubbanmalning");
                i.putExtra("arguments", kalle);
				startActivity(i);
				return;
			}
		}		
	}
}