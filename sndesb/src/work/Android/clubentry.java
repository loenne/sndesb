package work.Android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.SimpleAdapter;

public class clubentry extends Activity 
{
	SimpleAdapter myAdapter;
	private Runnable getEntr;
	private ProgressDialog m_ProgressDialog = null;
	private String urlString = "entries";
	private String urlStringClass = "eventclasses";
	private boolean fetchOk; 
//	private List<event> events;
//	private List<String> titles;
	private TextView mySelectedClub;
	private String myDateFrom;
	private String myDateTo;
	private String myClubId;
	private String myClub;
	private ArrayList<evententry> evententries;
	private ArrayList<eventclass> eventclasses;
	private ListView list; 
	private ArrayList<HashMap<String, String>> mylist;
	private HashMap<String, String> map;
	private HashMap<String, String> tClasses;
	private String clubName;
	
	@Override
	public void onCreate(Bundle icicle)
	{
		String[] from = new String[] {"datum", "namn"};
		int[] to = new int[] { R.id.item1, R.id.item2 };
		clubName = new String("");
		super.onCreate(icicle);
//		Log.e("SNDESB","In clubentry OnCreate 1");
		setContentView(R.layout.clubentry);

		 Serializable s = this.getIntent().getSerializableExtra("arguments");
		 Object[] o = (Object[]) s;

		 if (o != null) {
	       		myDateFrom = o[1].toString();
	       		myDateTo = o[3].toString();
	       		myClubId = o[5].toString();
	       		myClub = o[7].toString();

//				kalle[8] = "CLASSIFICATIONIDS";
//				kalle[9] = mySelClassificationIds;
	       		
	       		if (myClub.compareTo("[Alla]") == 0) {
	       			urlString = urlString + "?fromEventDate=" + myDateFrom + "&toEventDate=" + myDateTo;
	       		} else {
	       			urlString = urlString + "?organisationIds=" + myClubId + "&fromEventDate=" + myDateFrom + "&toEventDate=" + myDateTo;
	       		}
      			urlString = urlString + "&includePersonElement=true" + "&includeOrganisationElement=true" + "&includeEventElement=true";
//	       		Log.i("SNDESB","clubentry.onCreate : urlString : " + urlString);	       		
	     }		 

		 getEntr = new Runnable() {
				@Override
				public void run() {
					loadEntries();
				}
			};

		 mySelectedClub = (TextView)findViewById(R.id.clubname);		 
		 Thread thread = new Thread(null, getEntr, "MagentoBackground");
		 thread.start();
		 m_ProgressDialog = ProgressDialog.show(clubentry.this,"Please wait...", "Retreiving data...",true);		 
		 list = (ListView) findViewById(R.id.listview);
		 myAdapter = new SimpleAdapter(this, mylist, R.layout.clubentryrow, from, to);
		 list.setAdapter(myAdapter);
//		 Log.e("SNDESB","In clubEntry OnCreate 3");   		
   	}

	private void loadEntries() 
   	{
		int heading = 0;
		String event = "";
		String eventdate = "";
		mylist = new ArrayList<HashMap<String, String>>();			 

		try{
			RestAPI andRest = new RestAPI();
			
			fetchOk = andRest.queryRESTurl(urlString);
//			fetchOk = andRest.execute(urlString);

   			if (fetchOk) {
   				evententries = andRest.parseEventEntries();
//   				titles = new ArrayList<String>(evententries.size());
//   				Log.e("SNDESB","loadEntries : List titles created, size :" + evententries.size());
  				
   				for (evententry tavlAnm : evententries){

					if (heading == 0) {

						if (tavlAnm.getShortName() != null) {
//							Log.e("SNDESB","loadEntries : Heading updated with : " + tavlAnm.getShortName());					
							clubName = tavlAnm.getShortName();
							heading = 1;
						}
					}
						// New event  event / date
					if (tavlAnm.getEventName() != null) {

						if ((event.compareTo(tavlAnm.getEventName()) != 0) ||
						    (eventdate.compareTo(tavlAnm.getEventDate()) != 0)){
//							Log.e("SNDESB","loadEntries: New event, fetch all classes for id " + tavlAnm.getEventId());

							fetchEventClasses(tavlAnm.getEventId());							

							// fetchTavlingsStartlista							
							map = new HashMap<String, String>();
//							Log.e("SNDESB","loadAnmalningar : Ny tävling : " + tavlAnm.getEventName());
							event = tavlAnm.getEventName();
							eventdate = tavlAnm.getEventDate();
							map.put("datum", tavlAnm.getEventDate());
							map.put("namn", tavlAnm.getEventName());
//							Log.i("SNDESB","loadEntries : Fetched : " + tavlAnm.getEventDate() + " " +tavlAnm.getEventName() +"from EVENTOR");    			
							mylist.add(map);
							map = new HashMap<String, String>();
							map.put("datum", "---------------");
							map.put("namn", "--------------------");
							mylist.add(map);
						}
					}						
					// Class / Name
					map = new HashMap<String, String>();
//	   				Log.e("SNDESB","loadEntries : New event entry for : " + tavlAnm.getGiven() + " " + tavlAnm.getFamily() + "Form:" + tavlAnm.getEventForm());
					map.put("datum", tClasses.get(tavlAnm.getEventClassId()));
					if ((tavlAnm.getEventForm().compareTo("IndSingleDay") == 0) ||
						(tavlAnm.getEventForm().compareTo("IndMultiDay") == 0)) {
						map.put("namn", tavlAnm.getGiven() + " " + tavlAnm.getFamily());
					}else {
						map.put("namn", clubName);						
					}
//					Log.i("SNDESB","loadAnmalningar : Fetched : klassid:" + tavlAnm.getEventClassId() + " Klass: " + tKlasser.get(tavlAnm.getEventClassId()) +" from EVENTOR");    			
					mylist.add(map);
   				}
  			} else {
  				Log.e("SNDESB","loadEntries : Nothing fetched from EVENTOR");    			
  			}
  		} catch (Throwable t){
  			Log.e("SNDESB",t.getMessage(),t);
  		}
  		runOnUiThread(returnRes);
  	}   		

	private void fetchEventClasses(String id) {
//		Log.e("SNDESB","fetchEventClasses : tavlId: " + id);

		try{
   			urlString = urlStringClass + "?eventId=" + id;
			
			RestAPI andRest = new RestAPI();
			fetchOk = andRest.queryRESTurl(urlString);

			if (fetchOk) {
				tClasses = new HashMap<String,String>();
				eventclasses = andRest.parseEventClasses();

   				for (eventclass tavlKl : eventclasses){
//   		  			Log.e("SNDESB","fetchEventClasses : ClassId: " + tavlKl.getEventClassId() + "ClassShortName: " + tavlKl.getClassShortName() );
   					tClasses.put(tavlKl.getEventClassId(), tavlKl.getClassShortName());
   				}				
			}
		} catch (Throwable t){
			Log.e("SNDESB",t.getMessage(),t);
		}
	}

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			myAdapter.notifyDataSetChanged();
			mySelectedClub.setText(clubName);
			m_ProgressDialog.dismiss();
		}
	};
}
