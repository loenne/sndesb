package work.Android;

//import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import work.Android.tavling;
import work.Android.Organisation;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

public class RestAPI {

	// names of the XML tags
//	static final String RSS = "rss";
//	static final String CHANNEL = "channel";
//	static final String ITEM = "item";

	static final String EVENTLIST = "EventList";
	
	static final String EVENT = "Event";
	static final String EVENTID = "EventId";
	static final String EVENTNAME = "Name";
	static final String EVENTCLASSIFICATIONID = "EventClassificationId";
	static final String EVENTSTATUSID = "EventStatusId";
	static final String STARTDATE = "StartDate";
	static final String SDATE = "Date";
	static final String SCLOCK = "Clock";
	static final String FINISHDATE = "FinishDate";
	static final String DATE = "Date";
	static final String CLOCK = "Clock";
	static final String ALTERNATIVEDATES = "AlternativeDates";
	static final String SEQUENCE = "Sequence";
	static final String ORGANISER = "Organiser";
	static final String ORGANISATIONID = "OrganisationId";
	static final String CLASSTYPEID = "ClassTypeId";
	static final String EVENTRACE = "EventRace";
	static final String RACEDISTANCE = "raceDistance";
	static final String RACELIGHTCONDITION = "raceLightCondition";
	static final String EVENTCENTERPOSITION = "EventCenterPosition";
	static final String GPSXCOORDINATES = "x";
	static final String GPSYCOORDINATES = "y";
	static final String WEBURL = "WebURL";
	static final String COMMENT = "Comment";
	static final String PUNCHINGUNITTYPE = "PunchingUnitType";
	static final String MODIFYDATE = "ModifyDate";
	static final String MODIFIEDBY = "ModifiedBy";

	static final String EVENTRACENAME = "Name";
	static final String EVENTRACEDATE = "RaceDate";
			
	static final String ORGANISATIONLIST = "OrganisationList";

	static final String ORGANISATION = "Organisation";
	static final String ORGANISATIONNAME = "Name";
	static final String SHORTNAME = "ShortName";
	static final String ORGANISATIONTYPEID = "OrganisationTypeId";
	static final String PARENTORGANISATION = "ParentOrganisation";
	static final String PARENTORGANISATIONID = "OrganisationId";

	static final String ORGANISATIONTYPE = "OrganisationType";

	static final String ENTRYLIST = "EntryList";

	static final String ENTRY = "Entry";
	static final String ENTRYID = "EntryId";
	static final String COMPETITOR = "Competitor";
	static final String PERSON = "Person";
	static final String PERSONID = "PersonId";
	static final String PERSONNAME = "PersonName";
	static final String FAMILY = "Family";
	static final String GIVEN = "Given";
//	static final String SHORTNAME = "ShortName";
//	static final String ORGANISATIONID = "OrganisationId";
	static final String ENTRYCLASS = "EntryClass";  	
	static final String EVENTCLASSID = "EventClassId";

	static final String EVENTCLASSLIST = "EventClassList";
	static final String EVENTCLASS = "EventClass";
	static final String EVENTCLASSNAME = "Name";
	static final String EVENTCLASSSHORTNAME = "ClassShortName";
	
//	static final String TEAMNAME = "TeamName";
//	static final String TEAMCOMPETITOR = "TeamCompetitor";
//	static final String ORGANISATIONID = "OrganisationId";
	
	InputStream instream;
	String urlString = "https://eventor.orientering.se/api/";
	static int counter = 0; 
	int multiIndex;
	private ArrayList<String> multiDayNameList;
	private ArrayList<String> multiDayDateList;

	Boolean queryRESTurl(String url) {
		// URLConnection connection;
		HttpClient httpclient = new DefaultHttpClient();	
		urlString = urlString + url;

		Log.i("SNDESB", "queryRESTUrl : 1");

		HttpGet httpget = new HttpGet(urlString);
		httpget.setHeader("ApiKey","b969340e6fd1409b8b6948b9c89f19b8");
		HttpResponse response;

		counter++;
//		Log.i("SNDESB", "queryRESTUrl : 2 : counter:" + counter);

		try {
			response = httpclient.execute(httpget);
			Log.i("SNDESB", "queryResturl : Status:[" + response.getStatusLine().toString() + "]");
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				instream = entity.getContent();
//				if (counter == 1) {
//					String result = convertStreamToString(instream);
//				}
//				Log.i("SNDESB", "queryRESTurl : entity not null");
//				instream.close();
//				return instream;
				return true;
			} else {				
				Log.i("SNDESB", "queryRESTurl : entity null");				
			}
		} catch (ClientProtocolException e) {
			Log.e("SNDESB", "There was a protocol based error", e);
		} catch (UnknownHostException e) {
			Log.e("SNDESB", "There was an unknown host error", e);
		} catch (IOException e) {
			Log.e("SNDESB", "There was an IO Stream related error", e);
		}
		Log.i("SNDESB", "queryRESTurl : AFTER try/catch. Returning false from function");
		return false;
	}
		
	public ArrayList<tavling> parseTavlingar() {
		final tavling currentTavling = new tavling();
		final ArrayList<tavling> tavlingar = new ArrayList<tavling>();
		multiDayNameList = new ArrayList<String>();
		multiDayDateList = new ArrayList<String>();
		
		RootElement root = new RootElement(EVENTLIST);
		Element item = root.getChild(EVENT);
		Element item1 = item.getChild(STARTDATE);
		Element item2 = item.getChild(EVENTRACE);
		Element item3 = item.getChild(ORGANISER);
		Element item4 = item2.getChild(EVENTRACEDATE);
//		Element item2 = item.getChild(EVENTNAME);
	//	String attr = attributes.getValue("raceDistance");
	//	String eventType = attributes.getValue("eventForm");
//		String eventType = item.getAttribute("eventForm");
		
		//	EventList
		//  	Event
		//      	Name
		//         	StartDate
		//         	EventRace
		//				Name
		//				RaceDate
		//			EventRace
		//				Name
		//				RaceDate
//		root.getChild(EVENT).setStartElementListener(new StartElementListener(){
		item.setStartElementListener(new StartElementListener(){
			public void start(org.xml.sax.Attributes attributes) {
				currentTavling.setInitialValues();

				String attr = attributes.getValue("eventForm");
				// Make sure 'eventForm' is present.
				if (attr == null) {
					Log.i("SNDESB", "Event element does not contain attribute eventForm");
					currentTavling.setEventForm("unknown");
					return;
				} else {
//					Log.i("SNDESB", "Event element is set to :" + attr);				
					currentTavling.setEventForm(attr);
				}
				// Om det är en multiday event så spara undan alla subelement (Name/Date)
				if (attr.compareTo("IndMultiDay") == 0) {
					multiIndex = 0;
					multiDayNameList.clear();
					multiDayDateList.clear();
//					Log.i("SNDESB", "Event is a multiDay event");									
				}
			}
		});		
//		Log.i("SNDESB", "parseTavling : item1 :" + item1.toString());

		// End of EVENT element
		item.setEndElementListener(new EndElementListener(){
			public void end() {
				if (currentTavling.getEventForm().compareTo("IndMultiDay") == 0 ) {

					//Loopa igenom array
					int i = 0;
					String namn = currentTavling.getName();

					for (i=0; i < multiIndex; i++) {
						currentTavling.setName(namn + " " + multiDayNameList.get(i));
						currentTavling.setStartDate(multiDayDateList.get(i));
						tavlingar.add(currentTavling.copy());
//						Log.i("SNDESB", "Added event : " + currentTavling.getName() + " to tavlingar");						
					}
				} else {
					tavlingar.add(currentTavling.copy());
				}
			}
		});

		item.getChild(EVENTID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentTavling.setEventId(body);
			}
		});

/*		item.getChild(EVENTNAME).setStartElementListener(new StartElementListener(){
			 public void start(org.xml.sax.Attributes attributes) {
				//Binder sourced = originalBinder.withSource(xmlSource());
				String typeString = attributes.getValue("languageId");
				// Make sure 'type' is present.
				if (typeString == null) {
					Log.i("SNDESB", "Name element does not contain attribute languageId");
				    return;
				} else {
					Log.i("SNDESB", "languageId : " + typeString);					
				}
			}
*/
		item.getChild(EVENTNAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentTavling.setName(body);
//				 atts.getValue("id"); 

			}
		});		

		item.getChild(EVENTCLASSIFICATIONID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentTavling.setEventClassificationId(body);
			}
		});

		item.getChild(EVENTSTATUSID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentTavling.setEventStatusId(body);
			}
		});

		item.getChild(WEBURL).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentTavling.setWebURL(body);
			}
		});

		item.getChild(COMMENT).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentTavling.setComment(body);
			}
		});

		item1.getChild(SDATE).setEndTextElementListener(new EndTextElementListener(){

			public void end(String body) {
//				Log.e("SNDESB", "parseTavling : In item1.getChild : " + body);
				currentTavling.setStartDate(body);
			}
		});
		
		// start of EventRace element
		item2.setStartElementListener(new StartElementListener(){
			public void start(org.xml.sax.Attributes attributes) {
				String attr = attributes.getValue("raceDistance");
				// Make sure 'type' is present.
				if (attr == null) {
					Log.i("SNDESB", "EventRace element does not contain attribute raceDistance");
					currentTavling.setRaceDistance("0");
					return;
				} else {
//					Log.i("SNDESB", "EventRace/raceDistance : " + attr);					
					currentTavling.setRaceDistance(attr);
				}
				attr = attributes.getValue("raceLightCondition");
				// Make sure 'type' is present.
				if (attr == null) {
					Log.i("SNDESB", "EventRace element does not contain attribute raceLightCondition");
					currentTavling.setRaceLightCondition("0");
					return;
				} else {
//					Log.i("SNDESB", "EventRace/raceLightCondition : " + attr);					
					currentTavling.setRaceLightCondition(attr);
				}
			}
		});
		
		// end of EventRace element
		item2.setEndElementListener(new EndElementListener(){
			public void end() {
				// Skapa ett eventRace objekt. Alla eventRace objeckt ska sedan skapa tavlingsobjekt när item.endElement
				multiIndex = multiIndex + 1;
				//				tavlingar.add(currentTavling.copy());
			}
		});
		

/*		itemch1.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});		
*/				
		item2.getChild(EVENTCENTERPOSITION).setStartElementListener(new StartElementListener(){
			public void start(org.xml.sax.Attributes attributes) {
				String attr = attributes.getValue("y");
				// Make sure 'type' is present.
				if (attr == null) {
					Log.i("SNDESB", "EventCenterPosition element does not contain attribute y");
					currentTavling.setGPSYCoordinates("0");
					return;
				} else {
//					Log.i("SNDESB", "EventCenterPosition/y : " + attr);					
					currentTavling.setGPSYCoordinates(attr);
				}

				attr = attributes.getValue("x");
				// Make sure 'type' is present.
				if (attr == null) {
					Log.i("SNDESB", "EventCenterPosition element does not contain attribute x");
					currentTavling.setGPSXCoordinates("0");
					return;
				} else {
//					Log.i("SNDESB", "EventCenterPosition/x : " + attr);					
					currentTavling.setGPSXCoordinates(attr);
				}
			}
		});

		item2.getChild(EVENTRACENAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				if (currentTavling.getEventForm().compareTo("IndMultiDay") == 0) {
					multiDayNameList.add(body);
//					Log.i("SNDESB", "Event name " + body + " found");									
				}
			}
		});

		item4.getChild(DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				if (currentTavling.getEventForm().compareTo("IndMultiDay") == 0) {
					multiDayDateList.add(body);
//					Log.i("SNDESB", "Event date " + body + " found");									
				}
			}
		});
		
		item3.getChild(ORGANISATIONID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentTavling.setOrganisationId(body);
				currentTavling.setOrganisationName("");
			}
		});
		
		try {
			Xml.parse(instream, Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
//		Log.i("SNDESB", "parsetavlingar : before sort ");
		Collections.sort(tavlingar);
//		Log.i("SNDESB", "parsetavlingar : after sort ");
		return tavlingar;
	}
	
/*
 * 	
 */
	public List<Organisation> parseOrganisations() {
		final Organisation currentOrganisation = new Organisation();
		final List<Organisation> organisationer = new ArrayList<Organisation>();
		RootElement root = new RootElement(ORGANISATIONLIST);
		Element item = root.getChild(ORGANISATION);
		Element item2 = item.getChild(PARENTORGANISATION);
//		Log.i("SNDESB", "parseorganisations : item2 " + item2.toString());

		item.setEndElementListener(new EndElementListener(){
			public void end() {
				organisationer.add(currentOrganisation.copy());
			}
		});

/*		item2.setEndElementListener(new EndElementListener(){
			public void end() {
//				Log.e("SNDESB", "In item2.setEndElementListener");
//				organisationer.add(currentOrganisation.copy());
			}
		});
*/
/*
		item2.setEndElementListener(new EndElementListener(){
			public void end() {
				organisationer.add(currentOrganisation.copy());
			}
		});
*/		
		item.getChild(ORGANISATIONID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.e("SNDESB", "organisation : setEndText OrganisationId");
				currentOrganisation.setOrganisationId(body);
			}			
		});

  		item.getChild(ORGANISATIONNAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.e("SNDESB", "organisation : setEndText OrganisationName");
				currentOrganisation.setName(body);
			}
		});		

  		item.getChild(SHORTNAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.e("SNDESB", "organisation : setEndText ShortName");
				currentOrganisation.setShortName(body);
			}
		});		
  		
		item.getChild(ORGANISATIONTYPEID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.e("SNDESB", "organisation : setEndText organisationTypeId");
				currentOrganisation.setOrganisationTypeId(body);
			}			
		});

		item2.getChild(PARENTORGANISATIONID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.e("SNDESB", "organisation : setEndText ParentOrganisationId");
				currentOrganisation.setParentOrganisationId(body);
			}			
		});

		try {
			Log.e("SNDESB", "organisation : start parsing");		
			Xml.parse(instream, Xml.Encoding.UTF_8, root.getContentHandler());
			Log.e("SNDESB", "organisation : stop parsing");		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return organisationer;
	}

	public ArrayList<tavlingsanmalning> parseTavlAnm() {
		final tavlingsanmalning currentTavlingsanmalning = new tavlingsanmalning();
		final ArrayList<tavlingsanmalning> tavlingsanmalningar = new ArrayList<tavlingsanmalning>();
		RootElement root = new RootElement(ENTRYLIST);
		Element item = root.getChild(ENTRY);
		Element item11 = item.getChild(COMPETITOR);
		Element item111 = item11.getChild(PERSON);
		Element item1111 = item111.getChild(PERSONNAME);
		Element item112 = item11.getChild(ORGANISATION);
		Element item12 = item.getChild(ENTRYCLASS);
		Element item13 = item.getChild(EVENT);
		Element item131 = item13.getChild(STARTDATE);

		item.setStartElementListener(new StartElementListener(){
			public void start(org.xml.sax.Attributes attributes) {
				currentTavlingsanmalning.setInitialValues();
//				Log.i("SNDESB", "Entry start element");
			}
		});

		item.getChild(EVENT).setStartElementListener(new StartElementListener(){
			public void start(org.xml.sax.Attributes attributes) {
				String attr = attributes.getValue("eventForm");
				// Make sure 'type' is present.
				if (attr == null) {
					Log.i("SNDESB", "Event element does not contain attribute eventForm");
					currentTavlingsanmalning.setEventForm("unknown");
					return;
				} else {
//					Log.i("SNDESB", "Event/EventForm : " + attr);					
					currentTavlingsanmalning.setEventForm(attr);
				}
			}
		});
		
		item.setEndElementListener(new EndElementListener(){
			public void end() {
//				Log.i("SNDESB", "parseTavlAnm : endElementListener");
				tavlingsanmalningar.add(currentTavlingsanmalning.copy());
			}
		});

/*		
		item.getChild(ENTRYID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				Log.i("SNDESB", "parseTavlAnm : ENTRYID endTextElementListener : " + body);				
				currentTavlingsanmalning.setEntryId(body);
			}
		});

 		item111.getChild(PERSONID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				Log.i("SNDESB", "parseTavlAnm : PERSONID endTextElementListener : " + body);
				currentTavlingsanmalning.setPersonId(body);
			}
		});
*/
        item1111.getChild(FAMILY).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlAnm : FAMILY endTextElementListener : " + body);
				currentTavlingsanmalning.setFamily(body);
			}
		});

		item1111.getChild(GIVEN).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlAnm : GIVEN endTextElementListener : " + body);
				currentTavlingsanmalning.setGiven(body);
			}
		});

		item12.getChild(EVENTCLASSID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlAnm : EVENTCLASSID endTextElementListener : " + body);
				currentTavlingsanmalning.setEventClassId(body);
			}
		});
		
		item112.getChild(SHORTNAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlAnm : SHORTNAME endTextElementListener : " + body);
				currentTavlingsanmalning.setShortName(body);
			}
		});

/*
 		item112.getChild(ORGANISATIONID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				Log.i("SNDESB", "parseTavlAnm : ORGANISATIONID endTextElementListener : " + body);
				currentTavlingsanmalning.setOrganisationId(body);
			}
		});
*/					
		item13.getChild(EVENTID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlAnm : EVENTID endTextElementListener : " + body);				
				currentTavlingsanmalning.setEventId(body);
			}
		});
		
		item13.getChild(EVENTNAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlAnm : EVENTNAME endTextElementListener : " + body);
				currentTavlingsanmalning.setEventName(body);
			}
		});

		item131.getChild(DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlAnm : DATE endTextElementListener : " + body);
				currentTavlingsanmalning.setEventDate(body);
			}
		});
		
		try {
			Log.e("SNDESB", "TavlAnm : start parsing");		
			Xml.parse(instream, Xml.Encoding.UTF_8, root.getContentHandler());
			Log.e("SNDESB", "TavlAnm : stop parsing");		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
//		Log.i("SNDESB", "parsetavlingar : before sort ");
		Collections.sort(tavlingsanmalningar);
//		Log.i("SNDESB", "parsetavlingar : after sort ");

		return tavlingsanmalningar;
	}
	
/*
 *  <xs:element name="EventClass">
        <xs:element ref="EventClassId" />
        <xs:element ref="Name" minOccurs="0" />
        <xs:element ref="ClassShortName" />
   */
	
	public ArrayList<tavlingsklass> parseTavlingsKlasser() {
		final tavlingsklass currentTavlingsklass = new tavlingsklass();
		final ArrayList<tavlingsklass> tavlingsklasser = new ArrayList<tavlingsklass>();
		RootElement root = new RootElement(EVENTCLASSLIST);
		Element item = root.getChild(EVENTCLASS);
		
		item.setEndElementListener(new EndElementListener(){
			public void end() {
//				Log.i("SNDESB", "parseTavlingsKlasser : endElementListener");
				tavlingsklasser.add(currentTavlingsklass.copy());
			}
		});

		item.getChild(EVENTCLASSID).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlingsKlasser : EVENTCLASSID endTextElementListener : " + body);				
				currentTavlingsklass.setEventClassId(body);
			}
		});
		
		item.getChild(EVENTCLASSNAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlingsKlasser : EVENTCLASSNAME endTextElementListener : " + body);				
				currentTavlingsklass.setClassName(body);
			}
		});
		
		item.getChild(EVENTCLASSSHORTNAME).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
//				Log.i("SNDESB", "parseTavlingsKlasser : EVENTCLASSSHORTNAME endTextElementListener : " + body);				
				currentTavlingsklass.setClassShortName(body);
			}
		});

		try {
			Log.e("SNDESB", "TavlingsKlasser : start parsing");		
			Xml.parse(instream, Xml.Encoding.UTF_8, root.getContentHandler());
			Log.e("SNDESB", "TavlingsKlasser : stop parsing");		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
//		Log.i("SNDESB", "parsetavlingar : before sort ");
//		Collections.sort(tavlingsklasser);
//		Log.i("SNDESB", "parsetavlingar : after sort ");
		
		return tavlingsklasser;
	}	
	
/*
 * 
 */
/*	private String convertStreamToString(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;		

		Log.e("REST", "Start logg");

//		try {
			Log.e("REST", "in try");

			while ((line = reader.readLine()) != null) {
				Log.e("REST", line + "\n");
				sb.append(line + "\n");
			}
			Log.e("REST", "total line : " + line + "\n");

			//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			Log.e("REST", "in finally");

//			try {
//				is.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			
//		}
		return sb.toString();
	}
*/
}
//	public ArrayList<JSONObject> retrieveJSONArray(String urlString) {
//	public String retrieveJSONArray(String urlString) {
//		String result = queryRESTurl(urlString);		
/*
		String returnResult = new String();

		if (result != null) {
			for (int a = 0; a < result.length(); a++) {
				
			}
			Log.e("JSON", "Finished handling reply" );			
			
		} else {
			Log.e("JSON", "There was an error parsing the result" );			
		}		
		
		return returnResult;
	}
*/
		
/*		
		ArrayList<JSONObject> JOBS = new ArrayList<JSONObject>();

		if (result != null) {

			try {
				JSONObject json = new JSONObject(result);
				JSONArray jobsArray = json.getJSONArray("jobs");

				for (int a = 0; a < jobsArray.length(); a++) {
					JSONObject jobitem = jobsArray.getJSONObject(a);
					JOBS.add(jobitem);
				}
				return JOBS;

			} catch (JSONException e) {
				Log.e("JSON", "There was an error parsing the JSON", e);
			}
		}
		JSONObject myObject = new JSONObject();

		try {
			myObject.put("name","No Eventor competitions found");
			myObject.put("color", "grey");
			JOBS.add(myObject);

		} catch (JSONException e1) {
			Log.e("JSON", "There was an error creating the JSONObject", e1);
		}
		return JOBS;
	}
*/
