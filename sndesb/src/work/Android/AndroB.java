package work.Android;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;

///////////////////////////////////////////////////////////
//
//	Class AndroB
//
///////////////////////////////////////////////////////////
public class AndroB extends Activity {

//	String urlString = "https://eventor.orientering.se/api/";
	String urlString = "organisations";

	boolean fetchOk = false;
	private Runnable getOrg;
	private List<Organisation> organisations;
	private ArrayList<String> oforbund;
	private ArrayList<String> oforbundid;
	private Hashtable orforbund;
	private String mySelForbund;
	private String mySelForbundId;
		
	private int activeIndex;
    static final int DATE_DIALOG_ID1 = 0;
    static final int DATE_DIALOG_ID2 = 2;

    static final String ORGTYPE_SOFT = "1";
    static final String ORGTYPE_FORB = "2";
    static final String ORGTYPE_KLUBB = "3";
    static final String ORG_STOCKHOLM = "18";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		oforbund = new ArrayList<String>();
		oforbundid = new ArrayList<String>(); 
		orforbund = new Hashtable();

//		Log.e("XTRACTOR","AndroB: Create new DownloadForbund task");
//    	DownloadForbundTask task = new DownloadForbundTask();
//    	task.execute();
/*		getOrg = new Runnable() {
			@Override
			public void run() {
				loadForbund();
			}
		};

		Thread thread = new Thread(null, getOrg, "MagentoBackground");
		thread.start();
*/
	}

    private class DownloadForbundTask extends AsyncTask<String, Void, String> {
    	@Override
    	protected String doInBackground(String... urls) {
    		Log.e("XTRACTOR","AndroB: doInBackground started");
    		
    		String response = "";
    		loadForbund();
    		Log.e("XTRACTOR","AndroB: doInBackground ended");
	/*
				for (String url : urls) {
					DefaultHttpClient client = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(url);
					try {
						HttpResponse execute = client.execute(httpGet);
						InputStream content = execute.getEntity().getContent();

						BufferedReader buffer = new BufferedReader(
								new InputStreamReader(content));
						String s = "";
						while ((s = buffer.readLine()) != null) {
							response += s;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
    		 */
    		return response;
    	}

    	@Override
    	protected void onPostExecute(String result) {
    		
    		Log.e("XTRACTOR","AndroB: onPostExecute");
    		//textView.setText(result);
    	}
    }

//    public void readWebpage(View view) {
//    	DownloadWebPageTask task = new DownloadWebPageTask();
//    	task.execute(new String[] { "http://www.vogella.com" });
//    }

    
    private void loadForbund() 
   	{
		Log.e("XTRACTOR","AndroB: Start of loadForbund");

		oforbund.clear();
		oforbundid.clear();
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		StringBuilder builder = new StringBuilder();
//		builder.append("\n" + sharedPrefs.getString("Forbund", "NULL"));
//		oforbund.add(builder.toString());

//		oforbund.add(sharedPrefs.getString("forbund", "NULL"));
//		oforbundid.add("18");
		activeIndex = 0;

		try{
   			RestAPI andRest = new RestAPI();
   			fetchOk = andRest.queryRESTurl(urlString);

   			if (fetchOk) {
   				Log.e("XTRACTOR","AndroB: fetchOK");
   				organisations = andRest.parseOrganisations();    		
   				
   				// 1 = Orienteringsförbundet
   				// 2 = Forbund
   				// 3 = Klubb
   				int i = 1;

				oforbund.add("Alla förbund");
				oforbundid.add("1");
   				   				
   				for (Organisation org : organisations){
   					
  					if ((org.getOrganisationTypeId().equals(ORGTYPE_SOFT)) ||
  						(org.getOrganisationTypeId().equals(ORGTYPE_FORB))) {
  						oforbund.add(org.getShortName());
  						oforbundid.add(org.getOrganisationId());
  						Log.e("XTRACTOR","Stored : " + org.getShortName() + " id: " + org.getOrganisationId() + " in pos : " + i);
  						
  						if (org.getOrganisationId().equals(ORG_STOCKHOLM)) {
  							activeIndex = i;
  	  						Log.e("XTRACTOR","Found Stockholm as 18 in index : " + i);
  	  						mySelForbund = oforbund.get(i);
  	  	   	    			mySelForbundId = oforbundid.get(i);  	  						
  						}  						
  						i++;
  					}
   				}
   			} else {
				Toast.makeText(this, "No network connection. Check mobile newtwok",
						Toast.LENGTH_LONG).show();
				oforbund.add("Inga förbund hittade");
				oforbundid.add("0");
  			}
   		} catch (Throwable t){
  			Log.e("XTRACTOR","loadForbund : Failing to fetch forbund : " + t.getMessage(),t);
  		}

		/* END OF COMMENTED OUT */  		
//		runOnUiThread(returnRes);
	}

/*	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
//			mySpinnerArrayAdapter.notifyDataSetChanged();
//	   		mySpinner.setSelection(activeIndex,true);			
//			m_ProgressDialog.dismiss();
		}
	};
*/    
    ///////////////////////////////////////////////////////////
    //
    //
    //
    ///////////////////////////////////////////////////////////
	public void myClickHandler(View view) {
		switch (view.getId()) {
			// Tävlingar
			case R.id.tavlingsval:
			{
                Intent i = new Intent();
                i.setClassName("work.Android", "work.Android.selecttavling");
                startActivity(i);
				return;
			}
			// Klubbanmälningar
			case R.id.klubbval:
			{
                Intent i = new Intent();
                i.setClassName("work.Android", "work.Android.selectklubb");
                startActivity(i);               
				return;
			} 
			// Löpare
			case R.id.loparval:
			{
				Toast.makeText(this, "Du vill hämta alla anmälningar för en person. Inte Implementerat änn !!",
						Toast.LENGTH_LONG).show();
				return;
			}
			default:
			{
				Toast.makeText(this, "Mjukvarufel....!!",
						Toast.LENGTH_LONG).show();
				return;
			}
		}
	}

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



}		