package work.Android;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class showabout extends Dialog {

	public showabout(Context context) {
		super(context);
		Log.e("showAbout","constructor ");
	}
	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.showabout);
		
		setTitle("Om");
        Button buttonOk = (Button) findViewById(R.id.aboutok);
		buttonOk.setOnClickListener(new OKListener());
		Log.e("showAbout","onCreate ");
  	}
	
	private class OKListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			showabout.this.dismiss();
		}		
	}
}

