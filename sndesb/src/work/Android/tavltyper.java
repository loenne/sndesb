package work.Android;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
//import android.view.View.OnClickListener;
import android.widget.Button; 
import android.widget.CheckBox;

public class tavltyper extends Dialog {
     
	public interface ReadyListener {
		public void ready(boolean typ1, boolean typ2, 
						  boolean typ3, boolean typ4,
						  boolean typ5, boolean typ6);
	}

	private boolean tavltyp1;
	private boolean tavltyp2;
	private boolean tavltyp3;
	private boolean tavltyp4;
	private boolean tavltyp5;
	private boolean tavltyp6;
	private ReadyListener readyListener;

	public tavltyper(Context context, boolean type1, boolean type2,
			boolean type3, boolean type4, boolean type5, boolean type6,
			ReadyListener readyListener) {

		super(context);
		this.tavltyp1 = type1;
		this.tavltyp2 = type2;
		this.tavltyp3 = type3;
		this.tavltyp4 = type4;
		this.tavltyp5 = type5;
		this.tavltyp6 = type6;
		this.readyListener = readyListener;
	}
          
  	@Override

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tavltyper);
		setTitle("Välj tävlingstyper");

        CheckBox typ1 = (CheckBox) findViewById(R.id.ttype1);
        CheckBox typ2 = (CheckBox) findViewById(R.id.ttype2);
        CheckBox typ3 = (CheckBox) findViewById(R.id.ttype3);
        CheckBox typ4 = (CheckBox) findViewById(R.id.ttype4);
        CheckBox typ5 = (CheckBox) findViewById(R.id.ttype5);
        CheckBox typ6 = (CheckBox) findViewById(R.id.ttype6);

        typ1.setChecked(tavltyp1);
        typ2.setChecked(tavltyp2);
        typ3.setChecked(tavltyp3);
        typ4.setChecked(tavltyp4);
        typ5.setChecked(tavltyp5);
        typ6.setChecked(tavltyp6);			
		
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
			readyListener.ready(typ1.isChecked(), typ2.isChecked(), 
								typ3.isChecked(), typ4.isChecked(),
								typ5.isChecked(), typ6.isChecked());
			tavltyper.this.dismiss();
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
			tavltyper.this.dismiss();
		}		
	}
}
