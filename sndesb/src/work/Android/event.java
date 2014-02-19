package work.Android;
///////////////////////////////////////////////////////////
//
// Class event
// Holds all information about an "event" fetched from Eventor
//
///////////////////////////////////////////////////////////

//import java.net.URL;

import android.util.Log;

//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;

public class event implements Comparable<event>{
//	static SimpleDateFormat FORMATTER = 
//		new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	
	private String eventId;
	private String name;
	private String eventClassificationId;
	private String eventStatusId;
	private String disciplineId;
	private String discipline;

	private String startDate;
	private String webURL;
	private String comment;
	private String eventForm;

//  organiser containing
	private String organisationId;
	private String organisationName;

//  eventrace containing
	private String raceDistance;
	private String raceLightCondition;
//  eventcenterposition containing
	private String GPSXCoordinates;
	private String GPSYCoordinates;
	private String unit;	
	
	
/*	
 * 
 * http://eventor.orientering.se/Events?startDate=2011-10-20&
 * endDate=2012-05-31&organisations=1&
 * classifications=Championship,National,Regional,Local,Club&
 * mode=List&disciplines=Ski
 * 
 * 
	<xs:element name="Event">
    <xs:complexType>
      <xs:sequence>
*        <xs:element ref="EventId" />
*        <xs:element ref="Name" />
        <xs:choice>
*          <xs:element ref="EventClassificationId" />
          <xs:element ref="EventClassification" />
        </xs:choice>
        <xs:choice>
*          <xs:element ref="EventStatusId" />
          <xs:element ref="EventStatus" />
        </xs:choice>
        <xs:choice minOccurs="0" maxOccurs="unbounded">
          <xs:element ref="EventAttributeId" />
          <xs:element ref="EventAttribute" />
        </xs:choice>
        <xs:choice minOccurs="0" maxOccurs="unbounded">
          <xs:element ref="DisciplineId" />
          <xs:element ref="Discipline" />
        </xs:choice>
        <xs:choice minOccurs="0">
          <xs:element ref="SeasonId" />
          <xs:element ref="Season" />
        </xs:choice>
*        <xs:element ref="StartDate" />
        <xs:element ref="FinishDate" minOccurs="0" />
        <xs:element ref="AlternativeDates" minOccurs="0" maxOccurs="unbounded" />
        <xs:element ref="EventOfficial" minOccurs="0" maxOccurs="unbounded" />
 *       <xs:element ref="Organiser" minOccurs="0" />
        <xs:choice minOccurs="0" maxOccurs="unbounded">
          <xs:element ref="EventClass" />
          <xs:element ref="ClassType" />
          <xs:element ref="ClassTypeId" />
        </xs:choice>
        <xs:element ref="EntryBreak" minOccurs="0" maxOccurs="unbounded" />
 *       <xs:element ref="EventRace" minOccurs="0" maxOccurs="unbounded" />
 *       <xs:element ref="WebURL" minOccurs="0" maxOccurs="unbounded" />
        <xs:element ref="ContactData" minOccurs="0" />
        <xs:element ref="ServiceType" minOccurs="0" maxOccurs="unbounded" />
        <xs:element ref="Service" minOccurs="0" maxOccurs="unbounded" />
        <xs:element ref="ServiceRequest" minOccurs="0" maxOccurs="unbounded" />
        <xs:element ref="Account" minOccurs="0" maxOccurs="unbounded" />
 *       <xs:element ref="Comment" minOccurs="0" />
        <xs:element ref="ClassTypeComment" minOccurs="0" />
        <xs:element ref="PunchingUnitType" minOccurs="0" maxOccurs="unbounded" />
        <xs:element ref="ModifyDate" minOccurs="0" />
        <xs:element ref="ModifiedBy" minOccurs="0" />
        <xs:element ref="HashTableEntry" minOccurs="0" maxOccurs="unbounded" />
      </xs:sequence>
  *    <xs:attribute name="eventForm" default="IndSingleDay">
        <xs:simpleType>
          <xs:restriction base="xs:NMTOKEN">
            <xs:enumeration value="IndSingleDay" />
            <xs:enumeration value="IndMultiDay" />
            <xs:enumeration value="TeamSingleDay" />
            <xs:enumeration value="TeamMultiDay" />
            <xs:enumeration value="RelaySingleDay" />
            <xs:enumeration value="RelayMultiDay" />
            <xs:enumeration value="PatrolSingleDay" />
            <xs:enumeration value="PatrolMultiDay" />
          </xs:restriction>
        </xs:simpleType>
      </xs:attribute>
    </xs:complexType>
  </xs:element>
  
  */
	
	
	
	
	
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		Log.i("SNDESB","Found competition with name: " + name );
		this.name = name.trim();
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

	public String getDisciplineId() {
		return disciplineId;
	}
	
	public void setDisciplineId(String disciplineId) {
		Log.i("SNDESB","Found competition with disciplineId: " + disciplineId );
		this.disciplineId = disciplineId.trim();
	}

	public String getDiscipline() {
		return discipline;
	}
	
	public void setDiscipline(String disciplineId) {
		Log.i("SNDESB","Found competition with discipline: " + discipline );
		this.discipline = discipline.trim();
	}
	
	public String getEventStatusId() {
		return eventStatusId;
	}

	public void setEventStatusId(String eventStatusId) {
		Log.i("SNDESB","Found competition with eventStatusid: " + eventStatusId );
		this.eventStatusId = eventStatusId.trim();
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit.trim();
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
		this.disciplineId = "";
		this.discipline = "";
		this.organisationName = "";
		this.raceDistance = "";
		this.raceLightCondition = "";
		this.GPSXCoordinates = "";
		this.GPSYCoordinates = "";
		this.webURL = "";
		this.comment = "";
		this.unit = "";
	}
	public event copy(){
		event copy = new event();
		copy.eventId = eventId;
		copy.name = name;
		copy.eventForm = eventForm;
		copy.eventClassificationId = eventClassificationId;
		copy.eventStatusId = eventStatusId;
		copy.startDate = startDate;
		copy.disciplineId = disciplineId;
		copy.discipline = discipline;
		copy.organisationId = organisationId;
		copy.organisationName = organisationName;
		copy.raceDistance = raceDistance;
		copy.raceLightCondition = raceLightCondition;
		copy.GPSXCoordinates = GPSXCoordinates;
		copy.GPSYCoordinates = GPSYCoordinates;
		copy.webURL = webURL;
		copy.comment = comment;
		copy.unit = unit;
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
		sb.append("");
		sb.append("EventStatusId: ");
		sb.append(eventStatusId);
		sb.append("disciplineId: ");
		sb.append(disciplineId);
		sb.append("discipline: ");
		sb.append(discipline);
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
		sb.append("unit: ");
		sb.append(unit);
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
 	public int compareTo(event another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return -(another.startDate.compareTo(startDate));
	}
}
