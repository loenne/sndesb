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

public class klubbanmalning extends Activity 
{
	SimpleAdapter myAdapter;
	private Runnable getAnm;
	private ProgressDialog m_ProgressDialog = null;
	private String urlString = "entries";
	private String urlStringKlass = "eventclasses";
	private boolean fetchOk; 
//	private List<tavling> tavlingar;
//	private List<String> titles;
	private TextView mySelectedKlubb;
	private String myDateFrom;
	private String myDateTo;
	private String myKlubbId;
	private String myKlubb;
	private ArrayList<tavlingsanmalning> tavlingsanmalningar;
	private ArrayList<tavlingsklass> tavlingsklasser;
	private ListView list; 
	private ArrayList<HashMap<String, String>> mylist;
	private HashMap<String, String> map;
	private HashMap<String, String> tKlasser;
	private String KlubbNamn;
	
	@Override
	public void onCreate(Bundle icicle)
	{
		String[] from = new String[] {"datum", "namn"};
		int[] to = new int[] { R.id.item1, R.id.item2 };
		KlubbNamn = new String("");
		super.onCreate(icicle);
//		Log.e("SNDESB","In klubbanmalningar OnCreate 1");
		setContentView(R.layout.klubbanmalning);

		 Serializable s = this.getIntent().getSerializableExtra("arguments");
		 Object[] o = (Object[]) s;

		 if (o != null) {
	       		myDateFrom = o[1].toString();
	       		myDateTo = o[3].toString();
	       		myKlubbId = o[5].toString();
	       		myKlubb = o[7].toString();

//				kalle[8] = "CLASSIFICATIONIDS";
//				kalle[9] = mySelClassificationIds;
	       		
	       		if (myKlubb.compareTo("[Alla]") == 0) {
	       			urlString = urlString + "?fromEventDate=" + myDateFrom + "&toEventDate=" + myDateTo;
	       		} else {
	       			urlString = urlString + "?organisationIds=" + myKlubbId + "&fromEventDate=" + myDateFrom + "&toEventDate=" + myDateTo;
	       		}
      			urlString = urlString + "&includePersonElement=true" + "&includeOrganisationElement=true" + "&includeEventElement=true";
//	       		Log.i("SNDESB","klubbanmalning.onCreate : urlString : " + urlString);	       		
	     }		 

		 getAnm = new Runnable() {
				public void run() {
					loadAnmalningar();
				}
			};

		 mySelectedKlubb = (TextView)findViewById(R.id.klubbnamn);		 
		 Thread thread = new Thread(null, getAnm, "MagentoBackground");
		 thread.start();
		 m_ProgressDialog = ProgressDialog.show(klubbanmalning.this,"Please wait...", "Retreiving data...",true);		 
		 list = (ListView) findViewById(R.id.listview);
		 myAdapter = new SimpleAdapter(this, mylist, R.layout.klubbanmrad, from, to);
		 list.setAdapter(myAdapter);
//		 Log.e("SNDESB","In klubbanmalningar OnCreate 3");   		
   	}

	private void loadAnmalningar() 
   	{
		int heading = 0;
		String tavling = "";
		String tavlingsdatum = "";
		mylist = new ArrayList<HashMap<String, String>>();			 

		try{
			RestAPI andRest = new RestAPI();
   			fetchOk = andRest.queryRESTurl(urlString);

   			if (fetchOk) {
   				tavlingsanmalningar = andRest.parseTavlAnm();
//   				titles = new ArrayList<String>(tavlingsanmalningar.size());
//   				Log.e("SNDESB","loadAnmalningar : List titles created, size :" + tavlingsanmalningar.size());
  				
   				for (tavlingsanmalning tavlAnm : tavlingsanmalningar){

					if (heading == 0) {

						if (tavlAnm.getShortName() != null) {
//							Log.e("SNDESB","loadAnmalningar : Heading updated with : " + tavlAnm.getShortName());					
							KlubbNamn = tavlAnm.getShortName();
							heading = 1;
						}
					}
						// Ny tävling  tävling / datum
					if (tavlAnm.getEventName() != null) {

						if ((tavling.compareTo(tavlAnm.getEventName()) != 0) ||
						    (tavlingsdatum.compareTo(tavlAnm.getEventDate()) != 0)){
//							Log.e("SNDESB","loadAnmalningar : Ny tävling, hämta alla klasser för id " + tavlAnm.getEventId());

							fetchTavlingsKlasser(tavlAnm.getEventId());							

							// fetchTavlingsStartlista							
							map = new HashMap<String, String>();
//							Log.e("SNDESB","loadAnmalningar : Ny tävling : " + tavlAnm.getEventName());
							tavling = tavlAnm.getEventName();
							tavlingsdatum = tavlAnm.getEventDate();
							map.put("datum", tavlAnm.getEventDate());
							map.put("namn", tavlAnm.getEventName());
//							Log.i("SNDESB","loadAnmalningar : Fetched : " + tavlAnm.getEventDate() + " " +tavlAnm.getEventName() +"from EVENTOR");    			
							mylist.add(map);
							map = new HashMap<String, String>();
							map.put("datum", "---------------");
							map.put("namn", "--------------------");
							mylist.add(map);
						}
					}						
					// Klass / Namn
					map = new HashMap<String, String>();
//	   				Log.e("SNDESB","loadAnmalningar : Ny tävlingsanmälning för : " + tavlAnm.getGiven() + " " + tavlAnm.getFamily() + "Form:" + tavlAnm.getEventForm());
					map.put("datum", tKlasser.get(tavlAnm.getEventClassId()));
					if ((tavlAnm.getEventForm().compareTo("IndSingleDay") == 0) ||
						(tavlAnm.getEventForm().compareTo("IndMultiDay") == 0)) {
						map.put("namn", tavlAnm.getGiven() + " " + tavlAnm.getFamily());
					}else {
						map.put("namn", KlubbNamn);						
					}
//					Log.i("SNDESB","loadAnmalningar : Fetched : klassid:" + tavlAnm.getEventClassId() + " Klass: " + tKlasser.get(tavlAnm.getEventClassId()) +" from EVENTOR");    			
					mylist.add(map);
   				}
  			} else {
  				Log.e("SNDESB","loadAnmalningar : Nothing fetched from EVENTOR");    			
  			}
  		} catch (Throwable t){
  			Log.e("SNDESB",t.getMessage(),t);
  		}
  		runOnUiThread(returnRes);
  	}   		

	private void fetchTavlingsKlasser(String id) {
//		Log.e("SNDESB","fetchTavlingsKlasser : tavlId: " + id);

		try{
   			urlString = urlStringKlass + "?eventId=" + id;
			
			RestAPI andRest = new RestAPI();
			fetchOk = andRest.queryRESTurl(urlString);

			if (fetchOk) {
				tKlasser = new HashMap<String,String>();
				tavlingsklasser = andRest.parseTavlingsKlasser();

   				for (tavlingsklass tavlKl : tavlingsklasser){
//   		  			Log.e("SNDESB","fetchTavlingsKlasser : ClassId: " + tavlKl.getEventClassId() + "KlassShortName: " + tavlKl.getClassShortName() );
   					tKlasser.put(tavlKl.getEventClassId(), tavlKl.getClassShortName());
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

		public void run() {
			myAdapter.notifyDataSetChanged();
			mySelectedKlubb.setText(KlubbNamn);
			m_ProgressDialog.dismiss();
		}
	};
}
