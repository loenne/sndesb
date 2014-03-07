package work.Android;


import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
// import android.os.Parcelable;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

//import work.Android.selecttavling;

///////////////////////////////////////////////////////////
//
//
//
///////////////////////////////////////////////////////////
public class events extends Activity  {

	SimpleAdapter myAdapter;
	private boolean fetchOk; 
	private Runnable getComp;
	private ProgressDialog m_ProgressDialog = null;
	String urlString = "events";
	String mySelDateFrom;
	String mySelDateTo;
	String mySelForbundId;
	String mySelForbund;
	String mySelClassificationIds;
	String mySelDisciplineIds;
	private ArrayList<event> events;
	ListView list; 
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map;
	ArrayList<String> interfaceArray = new ArrayList<String>();
	//HashMap<String,String> klubbArray = new HashMap<String,String>();
	String[] from  = new String[] {"datum", "namn"};
	int[] to = new int[] { R.id.item1, R.id.item2 };
	String[] classificationId = new String[] {"Okänd","Mästerskap","Nationell","Distrikt","När","Klubb"};
	String[] lightConditions = new String[] {"Okänd","Dag","Natt","Dag/Natt"};
	
/*
	"champs" championship / "mäst" (mästerskap)
	"nat" national / "nat" nationell
	"dist" district / "dist" distrikt
	"loc" local / "när" närtävling
	"club" clubb / "klubb" klubbtävling
	
    <xs:enumeration value="reg" />     1
    <xs:enumeration value="other" />   2
    <xs:enumeration value="nat" />     3
    <xs:enumeration value="loc" />     4
    <xs:enumeration value="int" />     5
*/	
	///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.events);
				
//		Bundle b=this.getIntent().getExtras();
//		Serializable s = b.getSerializable("arguments");
		
		Serializable s = this.getIntent().getSerializableExtra("arguments");
//		Object[] o = (Object[]) s;

		interfaceArray = (ArrayList<String>) s;
//		interfaceArray = (ArrayList<String>) b.getArrayList("DATE_FROM");
		
/*		int g = interfaceArray.indexOf("DATE_FROM");
		mySelDateFrom  = interfaceArray.get(g);
		interfaceArray.remove(g);

		Log.i("SNDESB","events, onCreate: Fetched : " + mySelDateFrom + "from indx:" + g);    			


		mySelForbundId  = interfaceArray.get(interfaceArray.indexOf("FORBUNDID"));
		interfaceArray.remove(interfaceArray.indexOf("FORBUNDID"));
		mySelForbund  = interfaceArray.get(interfaceArray.indexOf("FORBUND"));
		interfaceArray.remove(interfaceArray.indexOf("FORBUND"));
*/
//		Log.i("SNDESB","events, onCreate: Fetched : " + mySelDateFrom + ":" + mySelDateTo + ":" + mySelForbundId + ":" + mySelForbund );    			
		Log.i("SNDESB","events, onCreate:");    			

		Log.i("SNDESB","events, onCreate:");    			
		
//		if (o != null) {
		//int size = interfaceArray.size();
		//int i = 10;
		
		mySelDateFrom  = interfaceArray.get(1);
		mySelDateTo    = interfaceArray.get(3);
		mySelForbundId = interfaceArray.get(5);
		mySelForbund   = interfaceArray.get(7);
		mySelClassificationIds = interfaceArray.get(9);
		//setDisciplineIds(interfageceArray.get(11));
		mySelDisciplineIds = interfaceArray.get(11);
			
		Log.i("SNDESB","events, onCreate: Received: " + mySelForbundId + ":" + mySelForbund);    			
//		while (i < size-1) {
//			klubbArray.put(interfaceArray.get(i), interfaceArray.get(i+1));
//			Log.i("SNDESB","events, onCreate: Fetched : " + interfaceArray.get(i) + ", " + interfaceArray.get(i+1) + " size:" + interfaceArray.size());    			
//			i = i + 2;
//		}

		if (mySelForbund == "Alla forbund") {
			urlString = urlString + "?fromDate=" + mySelDateFrom + "&toDate=" + mySelDateTo;
		} else {
			urlString = urlString + "?fromDate=" + mySelDateFrom + "&toDate=" + mySelDateTo + "&organisationIds=" + mySelForbundId;
		}
		urlString = urlString + "&classificationIds=" + mySelClassificationIds + "&includeEntryBreaks=true";

		//			urlString = urlString + "?fromDate=" + mySelDateFrom + "&toDate=" + mySelDateTo + "&organisationIds=" + mySelForbundId;
		Log.e("SNDESB","tavling.onCreate : urlString sent to Eventor : "+urlString);
//		}		 

		list = (ListView) findViewById(R.id.listview);

		getComp = new Runnable() {
			@Override
			public void run() {
				loadEvents();
			}
		};
		
		myAdapter = new SimpleAdapter(this, mylist, R.layout.eventrow, from, to);
		list.setAdapter(myAdapter);

		Thread thread = new Thread(null, getComp, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(events.this,"Please wait...", "Retreiving competitions...",true);
		
		list.setTextFilterEnabled(true);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Log.i("SNDESB","loadEvents : Selected : " + list.getItemAtPosition(position));    			
//				Log.i("SNDESB","loadEvents : Selected : view :" + v + "pos: "+ position + " id : " +id);

				event valdTavl = new event();
				valdTavl = events.get(position);
//				String name; 				
//				name = selecttavling.getKlubbarFetched(valdTavl.getOrganisationId());

//				Log.i("SNDESB","setOnItemClickListener : organisationid : " + valdTavl.getOrganisationId() + "club: "+ klubbArray.get(valdTavl.getOrganisationId()));
				Log.i("SNDESB","setOnItemClickListener : organisationid : " + valdTavl.getOrganisationId());
				
				String interfaceArray[] = new String[26];
//				interfaceArray[0] = "EVENTID";
//				interfaceArray[1] = valdTavl.getEventId();
				interfaceArray[2] = "EVENTFORM";
				String tmp = new String();
				tmp = valdTavl.getEventForm();

				Log.i("SNDESB","setOnItemClickListener : tmp: " + tmp);
				
				if ((tmp.compareTo("IndSingleDay") == 0) ||
					(tmp.compareTo("IndMultiDay") == 0)) {
					interfaceArray[3] = "individuell";
				} else if ((tmp.compareTo("TeamSingleDay") == 0) ||
					  	   (tmp.compareTo("TeamMultiDay") == 0)) {
						interfaceArray[3] = "Lag";
				} else if ((tmp.compareTo("RelaySingleDay") == 0) ||
					  	   (tmp.compareTo("RelayMultiDay") == 0)) {
						interfaceArray[3] = "Stafett";
				} else if ((tmp.compareTo("PatrolSingleDay") == 0) ||
					  	   (tmp.compareTo("PatrolMultiDay") == 0)) {
						interfaceArray[3] = "Patrul";
				} else {
					interfaceArray[3] = "Okänd";					
				}			

				interfaceArray[4] = "EVENTCLASSIFICATIONID";
				
				String tmp2 = new String();
				tmp2 = valdTavl.getEventClassificationId();
				
				if (tmp2.compareTo("1") == 0)  {
					interfaceArray[5] = classificationId[1];
				} else if (tmp2.compareTo("2") == 0)  {
					interfaceArray[5] = classificationId[2];
				} else if (tmp2.compareTo("3") == 0)  {
					interfaceArray[5] = classificationId[3];
				} else if (tmp2.compareTo("4") == 0)  {
					interfaceArray[5] = classificationId[4];
				} else if (tmp2.compareTo("5") == 0)  {
					interfaceArray[5] = classificationId[5];
				} else {
					interfaceArray[5] = classificationId[0];					
				}			

					
//				interfaceArray[6] = "EVENTSTATUSID";
//				interfaceArray[7] = valdTavl.getEventStatusId();

				interfaceArray[8] = "NAME";
				interfaceArray[9] = valdTavl.getName();
				interfaceArray[10] = "STARTDATE";
				interfaceArray[11] = valdTavl.getStartDate();
//				interfaceArray[10] = "ORANISATIONID";
//				interfaceArray[11] = valdTavl.getOrganisationId();
				interfaceArray[12] = "ORGANISATIONNAME";				
				interfaceArray[13] = "kalle"; // klubbArray.get(valdTavl.getOrganisationId());
				interfaceArray[14] = "RACEDISTANCE";

				tmp = valdTavl.getRaceDistance();
		
				if (tmp.compareTo("Sprint") == 0) {
					interfaceArray[15] = "Sprint";
				} else if (tmp.compareTo("Middle") == 0) {
					interfaceArray[15] = "Medel";
				} else if (tmp.compareTo("Long") == 0) {
					interfaceArray[15] = "Lång";
				} else if (tmp.compareTo("UltraLong") == 0) {
					interfaceArray[15] = "UltraLång";
				} else {
					interfaceArray[15] = "Okänd";
				}				
				interfaceArray[16] = "RACELIGHTCONDITION";

				tmp = valdTavl.getRaceLightCondition();
				
				if (tmp.compareTo("Day") == 0) {
					interfaceArray[17] = lightConditions[1];
				} else if (tmp.compareTo("Night") == 0) {
					interfaceArray[17] = lightConditions[2];
				} else if (tmp.compareTo("DayAndNight") == 0) {
					interfaceArray[17] = lightConditions[3];
				} else {
					interfaceArray[17] = lightConditions[0];
				}				
				interfaceArray[18] = "GPSXCOORDINATES";
				interfaceArray[19] = valdTavl.getGPSXCoordinates();
				interfaceArray[20] = "GPSYCOORDINATES";
				interfaceArray[21] = valdTavl.getGPSYCoordinates();
				interfaceArray[22] = "WEBURL";
				interfaceArray[23] = valdTavl.getWebURL();
				interfaceArray[24] = "COMMENTS";
				interfaceArray[25] = valdTavl.getComment();

				Intent i = new Intent();
				i.setClassName("work.Android", "work.Android.eventinfo");
				//                i.putExtra("arguments", valdTavl);
				i.putExtra("arguments", interfaceArray);
				startActivity(i);
			}
		});		 
	}

	// Fetch events with selection (Date from/to and forbund)
	///////////////////////////////////////////////////////////
    //
    // loadEvents
    //
	// Fetches events from EVENTOR through REST-API
	//
    ///////////////////////////////////////////////////////////
	private void loadEvents() 
	{
		try 
		{
			RestAPI andRest = new RestAPI();
			fetchOk = andRest.queryRESTurl(urlString);

			if (!fetchOk) {			 
				Log.i("SNDESB","loadEvents : Nothing fetched from EVENTOR");
			} else {
//				events = andRest.parseEvents(getDisciplineIds());    		
				events = andRest.parseEvents(mySelDisciplineIds);    		
				
				for (event tavl : events){
					map = new HashMap<String, String>();
					map.put("datum", tavl.getStartDate());
					map.put("namn", tavl.getName());
					Log.i("SNDESB","loadEvents : Fetched : " + tavl.getStartDate() + " " + tavl.getName() +"from EVENTOR");    			
					mylist.add(map);
				}
			}		 
		} catch (Throwable t)
		{
			Log.e("SNDESB",t.getMessage(),t);
		}
		runOnUiThread(returnRes);
	}   			 

    @Override
	///////////////////////////////////////////////////////////
    //
    // onCreateOptionsMenu
    //
    //
    ///////////////////////////////////////////////////////////
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;		
	}

	///////////////////////////////////////////////////////////
    //
    // Runnable
    //
    ///////////////////////////////////////////////////////////
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			Log.i("SNDESB","Runnable : run started"); 
			myAdapter.notifyDataSetChanged();
/*
		 if (m_orders != null && ) {


		 }
*/
			Log.i("SNDESB","Runnable : run finished"); 
			m_ProgressDialog.dismiss();
//			m_adapter.notify....
		}
	};
	
	///////////////////////////////////////////////////////////
    //
    // setDisciplineIds
    //
    ///////////////////////////////////////////////////////////
	void setDisciplineIds(String values) {
		mySelDisciplineIds = values;
	}
	
	///////////////////////////////////////////////////////////
    //
    // getDisciplineIds
    //
    ///////////////////////////////////////////////////////////
	String getDisciplineIds() {
		return mySelDisciplineIds;
	}
}

/*
AlertDialog.Builder adb=new AlertDialog.Builder(events.this);
adb.setTitle("Tavling");
adb.setMessage("Selected Item is = "+list.getItemAtPosition(position));
adb.setPositiveButton("Ok", null);
	 Log.i("SNDESB","loadEvents : C");    			
adb.show();
 */