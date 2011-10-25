package work.Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

///////////////////////////////////////////////////////////
//
//	Class AndroB
//
///////////////////////////////////////////////////////////
public class AndroB extends Activity {

	String urlString = "https://eventor.orientering.se/api/";
	boolean fetchOk = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

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
				Toast.makeText(this, "Du vill hŠmta alla anmŠlningar fšr en person. Inte Implementerat Šnn !!",
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

        Intent i = new Intent();
        i.setClassName("work.Android", "work.Android.showAbout");
        startActivity(i);               
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