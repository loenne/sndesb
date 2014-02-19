package work.Android;

public class eventclass implements Comparable<eventclass> {

/*
   <xs:element name="EventClass">
     <xs:element ref="EventClassId" />
     <xs:element ref="Name" minOccurs="0" />
     <xs:element ref="ClassShortName" />
*/
	
	private String eventClassId;
	private String className;
	private String classShortName;
	
	public String getEventClassId() {
		return eventClassId;
	}

	public void setEventClassId(String eventclassid) {
		this.eventClassId = eventclassid.trim();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String classname) {
		this.className = classname.trim();
	}

	public String getClassShortName() {
		return classShortName;
	}

	public void setClassShortName(String classshortname) {
		this.classShortName = classshortname.trim();
	}
	
	public eventclass copy(){
		eventclass copy = new eventclass();
        copy.eventClassId = eventClassId;
        copy.className = className;
        copy.classShortName = classShortName;
        return copy;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("eventClassId: ");
		sb.append(eventClassId);		
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
 	public int compareTo(eventclass another) {
		if (another == null) return 1;
		// sort descending, most recent first
//		return 1;
		return -(another.eventClassId.compareTo(eventClassId));
//		return -(result == 0 ? another.eventNa.compareTo(eventClassId) : result);
//		return result == 0 ? firstName.compareTo(((Person) person).firstName) : result;
//		return -(another.eventDate.compareTo(eventDate));
	}	
}
