package work.Android;

import android.app.Activity;
//import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
//import java.util.HashMap;
//import java.util.List;
import java.util.regex.Pattern;

//import android.text.Html;
//import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
//import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;

public class eventinfo extends Activity {
	SimpleAdapter myAdapter;
	TextView myTavlingName;
	TextView myTavlingEventForm;
	TextView myTavlingDate;
	TextView myTavlingArr;
	TextView myTavlingRaceDist;
	TextView myTavlingRaceLight;
	TextView myTavlingType;
	String myTavlingGPSX;
	String myTavlingGPSY;
	Button myTavlingMapIt;
	Button myTavlingPM;
	Button myTavlingInbj;
	Button myTavlingStartList;
	String myPM;
	String myInbj;
	String myStartList;
	TextView myTavlingWebURL;
	TextView myTavlingComment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventinfo);

		myTavlingName = (TextView)findViewById(R.id.tavlingsnamn);
		myTavlingDate = (TextView)findViewById(R.id.tavlingsdatum);
		myTavlingArr = (TextView)findViewById(R.id.tavlingsarrangor);
		myTavlingEventForm = (TextView)findViewById(R.id.tavlingsform);
		myTavlingRaceDist = (TextView)findViewById(R.id.tavlingsdistans);
		myTavlingType = (TextView)findViewById(R.id.tavlingstyp);		
		myTavlingRaceLight = (TextView)findViewById(R.id.tavlingstidpunkt);
		myTavlingWebURL =  (TextView)findViewById(R.id.webURL);
		myTavlingMapIt = (Button)findViewById(R.id.mapIt);
		myTavlingInbj = (Button)findViewById(R.id.inbjudan);
		myTavlingPM = (Button)findViewById(R.id.pm);
		myTavlingStartList = (Button)findViewById(R.id.startlist);
		myTavlingComment = (TextView)findViewById(R.id.comments);

		Serializable s = this.getIntent().getSerializableExtra("arguments");
		Object[] o = (Object[]) s;

		if (o != null) {
			myTavlingEventForm.setText(o[3].toString());			
			myTavlingType.setText(o[5].toString());
			myTavlingName.setText(o[9].toString());
			myTavlingDate.setText(o[11].toString());
			myTavlingArr.setText(o[13].toString());
			myTavlingRaceDist.setText(o[15].toString());		       		
			myTavlingRaceLight.setText(o[17].toString());		       		
			myTavlingGPSX = o[19].toString();		       		
			myTavlingGPSY = o[21].toString();
//			myTavlingI = o[21].toString();
			myTavlingWebURL.setText(o[23].toString());
			myTavlingComment.setText(o[25].toString());
		}

/*		getArr = new Runnable() {
			@Override
			public void run() {
				loadArr();
			}
		};

		Thread thread = new Thread(null, getArr, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(eventinfo.this,"Please wait...", "Retreiving data...",true);
*/
		// Convert URL-string to clickable link 
		/*
		 mTextSample = (TextView) findViewById(R.id.textSample);
		 String text = "Visit my blog jtomlinson.blogspot.com";
		 mTextSample.setText(text);
		 //jmt: pattern we want to match and turn into a clickable link
		 Pattern pattern = Pattern.compile("jtomlinson.blogspot.com");
		 //jmt: prefix our pattern with http://
		 Linkify.addLinks(mTextSample, pattern, "http://");
		 */			 
		String text = o[23].toString();
		myTavlingWebURL.setText(text);
		Pattern pattern = Pattern.compile(o[23].toString());
		//		 Pattern pattern = Pattern.compile("jtomlinson.blogspot.com");
		Linkify.addLinks(myTavlingWebURL, pattern, "http://");
		//		 myTavlingWebURL.setText(Html.fromHtml(o[23].toString()));

		/*		 myTavlingWebURL.setMovementMethod(LinkMovementMethod.getInstance());
		 Pattern pattern = Pattern.compile(o[23].toString());
		 Linkify.addLinks(myTavlingWebURL, pattern, "http://");			
//		 String text = myTavlingWebURL.getText();
//		 myTavlingWebURL.setText(Html.fromHtml(text));
		 */

		//		 Bundle b = i.getExtras();		 
		//		 ListView l1 = (ListView) findViewById(R.id.listView5);
		//		 l1.setAdapter(new EfficientAdapter(this));

		//		 list = (ListView) findViewById(R.id.listView5);
		//		 loadTavlingar();
	}

/*	private void loadArr() 
	{
		try 
		{			
			kalle[10] = "ORANISATIONID";
			kalle[11] = valdTavl.getOrganisationId();
			kalle[12] = "ORGANISATIONNAME";
			kalle[13] = valdTavl.getOrganisationName();


			RestAPI andRest = new RestAPI();
			fetchOk = andRest.queryRESTurl(urlString); 		 

			if (!fetchOk) {			 
				Log.i("SNDESB","loadTavlingar : Nothing fetched from EVENTOR");
			} else {
				tavlingar = andRest.parseTavlingar();    		

				for (tavling tavl : tavlingar){
					map = new HashMap<String, String>();
					map.put("datum", tavl.getStartDate());
					map.put("namn", tavl.getName());
					Log.i("SNDESB","loadTavlingar : Fetched : " + tavl.getStartDate() + " " + tavl.getName() +"from EVENTOR");    			
					mylist.add(map);
				}
			}		 
		} catch (Throwable t)
		{
			Log.e("SNDESB",t.getMessage(),t);
		}
		runOnUiThread(returnRes);
	}   			 

	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			myAdapter.notifyDataSetChanged();
			 
			m_ProgressDialog.dismiss();
			//		m_adapter.notify....
		}
	};
*/
	// This method is called at button click because we assigned the name to the
	// "On Click property" of the button
	public void myMapItClickHandler(View view) {
		switch (view.getId()) 
		{
		case R.id.mapIt:
		{	
			String kalle[] = new String[4];
			kalle[0] = "GPSY";
			kalle[1] = myTavlingGPSY;
			kalle[2] = "GPSX";
			kalle[3] = myTavlingGPSX;
			//Log.i("SNDESB","eventinfo : clickhandler : Y/X "+ kalle[1] + kalle[3]);

			Intent i = new Intent();
			i.setClassName("work.Android", "work.Android.maphandler");
			i.putExtra("arguments", kalle);
			startActivity(i);
			return;
		}
		case R.id.inbjudan:
		{
			Toast.makeText(this, "Du vill hämta inbjudan !!",
			Toast.LENGTH_LONG).show();
/*			
			String kalle[] = new String[4];
			kalle[0] = "GPSY";
			kalle[1] = myTavlingGPSY;
			kalle[2] = "GPSX";
			kalle[3] = myTavlingGPSX;
			//Log.i("SNDESB","eventinfo : clickhandler : Y/X "+ kalle[1] + kalle[3]);

			Intent i = new Intent();
			i.setClassName("work.Android", "work.Android.maphandler");
			i.putExtra("arguments", kalle);
			startActivity(i);
*/			
			return;
		}
		case R.id.pm:
		{
			Toast.makeText(this, "Du vill hämta PM !!",
			Toast.LENGTH_LONG).show();
/*			
			String kalle[] = new String[4];
			kalle[0] = "GPSY";
			kalle[1] = myTavlingGPSY;
			kalle[2] = "GPSX";
			kalle[3] = myTavlingGPSX;
			//Log.i("SNDESB","eventinfo : clickhandler : Y/X "+ kalle[1] + kalle[3]);

			Intent i = new Intent();
			i.setClassName("work.Android", "work.Android.maphandler");
			i.putExtra("arguments", kalle);
			startActivity(i);
*/			
			return;
		}
		case R.id.startlist:
		{
			Toast.makeText(this, "You want to fetch the startlist",
			Toast.LENGTH_LONG).show();
/*			
			String kalle[] = new String[4];
			kalle[0] = "GPSY";
			kalle[1] = myTavlingGPSY;
			kalle[2] = "GPSX";
			kalle[3] = myTavlingGPSX;
			//Log.i("SNDESB","eventinfo : clickhandler : Y/X "+ kalle[1] + kalle[3]);

			Intent i = new Intent();
			i.setClassName("work.Android", "work.Android.maphandler");
			i.putExtra("arguments", kalle);
			startActivity(i);
*/			
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
}