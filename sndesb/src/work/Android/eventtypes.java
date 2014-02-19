package work.Android;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.Button; 
import android.widget.CheckBox;

public class eventtypes extends Dialog {
     
	public interface ReadyListener {
		public void ready(boolean typ1, boolean typ2, 
						  boolean typ3, boolean typ4,
						  boolean typ5, boolean typ6,
						  boolean typ7, boolean typ8,
						  boolean typ9, boolean typ10);
	}

	private boolean eventtype1;
	private boolean eventtype2;
	private boolean eventtype3;
	private boolean eventtype4;
	private boolean eventtype5;
	private boolean eventtype6;
	private boolean eventdiscipline1;
	private boolean eventdiscipline2;
	private boolean eventdiscipline3;
	private boolean eventdiscipline4;
	private ReadyListener readyListener;

	public eventtypes(Context context, boolean type1, boolean type2,
			boolean type3, boolean type4, boolean type5, boolean type6,
			boolean type7, boolean type8, boolean type9, boolean type10,
			ReadyListener readyListener) {

		super(context);
		this.eventtype1 = type1;
		this.eventtype2 = type2;
		this.eventtype3 = type3;
		this.eventtype4 = type4;
		this.eventtype5 = type5;
		this.eventtype6 = type6;
		this.eventdiscipline1 = type7;
		this.eventdiscipline2 = type8;
		this.eventdiscipline3 = type9;
		this.eventdiscipline4 = type10;
		this.readyListener = readyListener;
	}
          
  	@Override

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventtypes);
		setTitle("Välj tävlingstyper");

        CheckBox typ1 = (CheckBox) findViewById(R.id.ttype1);
        CheckBox typ2 = (CheckBox) findViewById(R.id.ttype2);
        CheckBox typ3 = (CheckBox) findViewById(R.id.ttype3);
        CheckBox typ4 = (CheckBox) findViewById(R.id.ttype4);
        CheckBox typ5 = (CheckBox) findViewById(R.id.ttype5);
        CheckBox typ6 = (CheckBox) findViewById(R.id.ttype6);
        CheckBox discipline1 = (CheckBox) findViewById(R.id.tdtype1);
        CheckBox discipline2 = (CheckBox) findViewById(R.id.tdtype2);
        CheckBox discipline3 = (CheckBox) findViewById(R.id.tdtype3);
        CheckBox discipline4 = (CheckBox) findViewById(R.id.tdtype4);

        typ1.setChecked(eventtype1);
        typ2.setChecked(eventtype2);
        typ3.setChecked(eventtype3);
        typ4.setChecked(eventtype4);
        typ5.setChecked(eventtype5);
        typ6.setChecked(eventtype6);			
        discipline1.setChecked(eventdiscipline1);			
        discipline2.setChecked(eventdiscipline2);			
        discipline3.setChecked(eventdiscipline3);			
        discipline4.setChecked(eventdiscipline4);			
		
        Button buttonOk = (Button) findViewById(R.id.ttokbutton);
		buttonOk.setOnClickListener(new OKListener());

       	Button buttonCancel = (Button) findViewById(R.id.ttcancelbutton);
		buttonCancel.setOnClickListener(new CancelListener());
  	}
     
	private class OKListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
	        CheckBox typ1 = (CheckBox) findViewById(R.id.ttype1);
	        CheckBox typ2 = (CheckBox) findViewById(R.id.ttype2);
	        CheckBox typ3 = (CheckBox) findViewById(R.id.ttype3);
	        CheckBox typ4 = (CheckBox) findViewById(R.id.ttype4);
	        CheckBox typ5 = (CheckBox) findViewById(R.id.ttype5);
	        CheckBox typ6 = (CheckBox) findViewById(R.id.ttype6);
	        CheckBox discipline1 = (CheckBox) findViewById(R.id.tdtype1);
	        CheckBox discipline2 = (CheckBox) findViewById(R.id.tdtype2);
	        CheckBox discipline3 = (CheckBox) findViewById(R.id.tdtype3);
	        CheckBox discipline4 = (CheckBox) findViewById(R.id.tdtype4);
    		Log.e("SNDESB", "eventtypes OKListner calling readyListner tdtyper4:" + R.id.tdtype4);
	        
			readyListener.ready(typ1.isChecked(), typ2.isChecked(), 
								typ3.isChecked(), typ4.isChecked(),
								typ5.isChecked(), typ6.isChecked(),
								discipline1.isChecked(), discipline2.isChecked(),
								discipline3.isChecked(), discipline4.isChecked());
			eventtypes.this.dismiss();
		}		
	}
	private class CancelListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
/*	        CheckBox typ1 = (CheckBox) findViewById(R.id.ttype1);
	        CheckBox typ2 = (CheckBox) findViewById(R.id.ttype2);
	        CheckBox typ3 = (CheckBox) findViewById(R.id.ttype3);
	        CheckBox typ4 = (CheckBox) findViewById(R.id.ttype4);
	        CheckBox typ5 = (CheckBox) findViewById(R.id.ttype5);
	        CheckBox typ6 = (CheckBox) findViewById(R.id.ttype6);
*/
/*
 			readyListener.ready(typ1.isChecked(), typ2.isChecked(), 
 
								typ3.isChecked(), typ4.isChecked(),
								typ5.isChecked(), typ6.isChecked());
*/
			eventtypes.this.dismiss();
		}		
	}
}
