	package work.Android;

//	import java.net.URL;

	//import java.net.MalformedURLException;
	//import java.net.URL;
	//import java.text.ParseException;
	//import java.text.SimpleDateFormat;
	//import java.util.Date;

	public class tavlingsanmalning implements Comparable<tavlingsanmalning>{
		
/*		<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		<EntryList>
		    <Entry nonCompetitor="N">
		        <EntryId>278577</EntryId>
		        <Competitor>
		            <PersonId idManager="SWE" type="nat">4941</PersonId>
		            <OrganisationId>335</OrganisationId>
		            <CCard>
		                <CCardId>600600</CCardId>
		                <PunchingUnitType value="SI"/>
		            </CCard>
		        </Competitor>
		        <EntryClass sequence="1">
		            <EventClassId>33551</EventClassId>
		        </EntryClass>
		        <EventId>1047</EventId>        
		        <AllocationControl/>
		        <EntryDate>
		            <Date>2011-03-02</Date>
		            <Clock>10:18:37</Clock>
		        </EntryDate>
		        <ModifyDate>
		            <Date>2011-03-02</Date>
		            <Clock>10:18:38</Clock>
		        </ModifyDate>
		        <ModifiedBy>
		            <PersonId idManager="SWE" type="nat">4941</PersonId>
		        </ModifiedBy>
		    </Entry>
		    
*/
        private String entryId;
        private String personId;
        private String family;
        private String given;
        private String organisationId;
        private String organisation;    // How to get this ? 
		private String eventClassId;
		private String entryClass;		// How to get this ?
		private String shortName;		// How to get this ?
		private String eventId;
		private String eventName;
		private String eventForm;
		private String eventDate;

		public void setInitialValues() {
			this.eventId = "";
	        this.entryId = "";
	        this.personId = "";
	        this.family = "";
	        this.given = "";
	        this.organisationId = "";
	        this.organisation = ""; 
	        this.eventClassId = "";
	        this.entryClass = "";
	        this.shortName = "";
	        this.eventId = "";
	        this.eventName = "";
	        this.eventForm = "";
	        this.eventDate = "";
		}
		
		public String getEntryId() {
			return entryId;
		}

		public void setEntryId(String entryId) {
			this.entryId = entryId.trim();
		}

		public String getPersonId() {
			return personId;
		}

		public void setPersonId(String personId) {
			this.personId = personId.trim();
		}
		
		public String getFamily() {
			return family;
		}

		public void setFamily(String family) {
			this.family = family.trim();
		}

		public String getGiven() {
			return given;
		}

		public void setGiven(String given) {
			this.given = given.trim();
		}
		
		public String getOrganisationId() {
			return organisationId;
		}

		public void setOrganisationId(String organsiationId) {
			this.organisationId = organisationId.trim();
		}
		
		public String getOrganisation() {
			return organisation;
		}

		public void setOrganisation(String organsiation) {
			this.organisation = organisation.trim();
		}

		public String getShortName() {
			return shortName;
		}

		public void setShortName(String shortname) {
			this.shortName = shortname.trim();
		}
		
		public String getEventClassId() {
			return eventClassId;
		}

		public void setEventClassId(String eventClassId) {
			this.eventClassId = eventClassId.trim();
		}

		public String getEntryClass() {
			return entryClass;
		}

		public void setEntryClass(String entryClass) {
			this.entryClass = entryClass.trim();
		}
		
		public String getEventId() {
			return eventId;
		}

		public void setEventId(String eventId) {
			this.eventId = eventId.trim();
		}

		public String getEventName() {
			return eventName;
		}

		public void setEventName(String eventName) {
			this.eventName = eventName.trim();
		}
		
		public String getEventForm() {
			return eventForm;
		}

		public void setEventForm(String eventForm) {
			this.eventForm = eventForm.trim();
		}

		public String getEventDate() {
			return eventDate;
		}

		public void setEventDate(String eventDate) {
			this.eventDate = eventDate.trim();
		}

/*		
		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate.trim();
		}		
*/		
		public tavlingsanmalning copy(){
			tavlingsanmalning copy = new tavlingsanmalning();
	        copy.entryId = entryId;
	        copy.personId = personId;
	        copy.family = family;
	        copy.given = given;
	        copy.shortName = shortName;
	        copy.organisationId = organisationId;
	        copy.organisation = organisation;    // How to get this ? 
	        copy.eventClassId = eventClassId;
	        copy.entryClass = entryClass;		// How to get this ?
	        copy.eventId = eventId;
	        copy.eventName = eventName;		// How to get this ?
	        copy.eventForm = eventForm;
	        copy.eventDate = eventDate;		// How to get this ?
			return copy;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("EntryId: ");
			sb.append(entryId);		
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
	 	public int compareTo(tavlingsanmalning another) {
			if (another == null) return 1;
			// sort descending, most recent first
//			return 1;
			int result = another.eventDate.compareTo(eventDate);
			return -(result == 0 ? another.eventName.compareTo(eventName) : result);
//			return result == 0 ? firstName.compareTo(((Person) person).firstName) : result;
//			return -(another.eventDate.compareTo(eventDate));
		}
	}
