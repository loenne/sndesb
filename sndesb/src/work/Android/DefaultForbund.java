package work.Android;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

//import java.util.Set;

public class DefaultForbund extends ListPreference {

	    public DefaultForbund(Context context, AttributeSet attrs) {
	        super(context, attrs);

	        CharSequence[] entries = new CharSequence[20];
	        CharSequence[] entryValues = new CharSequence[20];
	        int i = 0;

	        for (i =0; i<10;i++) {
	            entries[i] = "kalle"+i;
	            entryValues[i] = ""+i;
	        }
	        setEntries(entries);
	        setEntryValues(entryValues);
	    }

	    public DefaultForbund(Context context) {
	        this(context, null);
	    }
}
