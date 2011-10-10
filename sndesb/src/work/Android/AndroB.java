package work.Android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;		
	}
}		