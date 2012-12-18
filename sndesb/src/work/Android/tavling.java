package work.Android;

//import java.net.URL;

import android.util.Log;

//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;

public class tavling implements Comparable<tavling>{
//	static SimpleDateFormat FORMATTER = 
//		new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	private String eventId;
	private String eventForm;
	private String eventClassificationId;
	private String eventStatusId;
	private String name;
	private String startDate;
	private String organisationId;
	private String organisationName;
	private String raceDistance;
	private String raceLightCondition;
	private String GPSXCoordinates;
	private String GPSYCoordinates;
	private String webURL;
	private String comment;
	
	// For MultiDay events These will be added. Create EventRace end
//	private boolean MultiDayEvent = false;
//	private String eventRaceName;
//	private String eventRaceDate;
	

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId.trim();
	}

	public String getEventForm() {
		return eventForm;
	}

	public void setEventForm(String eventForm) {
		this.eventForm = eventForm.trim();
	}

	public String getEventClassificationId() {
		return eventClassificationId;
	}

	public void setEventClassificationId(String eventClassificationId) {
		Log.i("SNDESB","Found competition with clasificationid: " + eventClassificationId );
		this.eventClassificationId = eventClassificationId.trim();
	}

	public String getEventStatusId() {
		return eventStatusId;
	}

	public void setEventStatusId(String eventStatusId) {
		Log.i("SNDESB","Found competition with eventStatusid: " + eventStatusId );
		this.eventStatusId = eventStatusId.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Log.i("SNDESB","Found competition with name: " + name );
		this.name = name.trim();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate.trim();
	}

	public String getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId.trim();
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName.trim();
	}

	public String getRaceDistance() {
		return raceDistance;
	}

	public void setRaceDistance(String raceDistance) {
		this.raceDistance = raceDistance.trim();
	}

	public String getRaceLightCondition() {
		return raceLightCondition;
	}

	public void setRaceLightCondition(String raceLightCondition) {
		this.raceLightCondition = raceLightCondition.trim();
	}

	public String getGPSXCoordinates() {
		return GPSXCoordinates;
	}

	public void setGPSXCoordinates(String GPSXCoordinates) {
		this.GPSXCoordinates = GPSXCoordinates.trim();
	}
	
	public String getGPSYCoordinates() {
		return GPSYCoordinates;
	}

	public void setGPSYCoordinates(String GPSYCoordinates) {
		this.GPSYCoordinates = GPSYCoordinates.trim();
	}

	public String getWebURL() {
		return webURL;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL.trim();
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment.trim();
	}
	public void setInitialValues() {
		this.eventId = "";
		this.name = "";
		this.eventForm = "";
		this.eventClassificationId = "";
		this.eventStatusId = "";
		this.startDate = "";
		this.organisationId = "";
		this.organisationName = "";
		this.raceDistance = "";
		this.raceLightCondition = "";
		this.GPSXCoordinates = "";
		this.GPSYCoordinates = "";
		this.webURL = "";
		this.comment = "";
	}
	public tavling copy(){
		tavling copy = new tavling();
		copy.eventId = eventId;
		copy.name = name;
		copy.eventForm = eventForm;
		copy.eventClassificationId = eventClassificationId;
		copy.eventStatusId = eventStatusId;
		copy.startDate = startDate;
		copy.organisationId = organisationId;
		copy.organisationName = organisationName;
		copy.raceDistance = raceDistance;
		copy.raceLightCondition = raceLightCondition;
		copy.GPSXCoordinates = GPSXCoordinates;
		copy.GPSYCoordinates = GPSYCoordinates;
		copy.webURL = webURL;
		copy.comment = comment;
		return copy;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EventId: ");
		sb.append(eventId);
		sb.append('\n');
		sb.append("Name: ");
		sb.append(name);
		sb.append('\n');
		sb.append("eventClassificationId: ");
		sb.append(eventClassificationId);
		sb.append('\n');
		sb.append("EventStatusId: ");
		sb.append(eventStatusId);
		sb.append("StartDate: ");
		sb.append(startDate);
		sb.append("organisationId: ");
		sb.append(organisationId);
		sb.append("organisationName: ");
		sb.append(organisationName);
		sb.append("raceDistance: ");
		sb.append(raceDistance);
		sb.append("raceLightCondition: ");
		sb.append(raceLightCondition);
		sb.append("GPSXCoordinates: ");
		sb.append(GPSXCoordinates);
		sb.append("GPSYCoordinates: ");
		sb.append(GPSYCoordinates);
		sb.append("webURL: ");
		sb.append(webURL);
		sb.append("comment: ");
		sb.append(comment);
		return sb.toString();
	}
/*
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
*/	
	
/*	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
*/
 	public int compareTo(tavling another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return -(another.startDate.compareTo(startDate));
	}
}
