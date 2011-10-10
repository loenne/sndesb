package work.Android;


//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;



//public class forbund implements Comparable<forbund>{
public class forbund {
//	static SimpleDateFormat FORMATTER = 
//		new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	private String organisationId;
	private String name;
	private String shortName;
	private String organisationTypeId;
	private String parentOrganisationId;

/*	public forbund(String sName) {
		shortName = sName;		
	}
*/
	public String getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName.trim();
	}

	public String getOrganisationTypeId() {
		return organisationTypeId;
	}

	public void setOrganisationTypeId(String organisationTypeId) {
		this.organisationTypeId = organisationTypeId.trim();
	}

	public String getParentOrganisationId() {
		return parentOrganisationId;
	}

	public void setParentOrganisationId(String parentOrganisationId) {
		this.parentOrganisationId = parentOrganisationId.trim();
	}

	
	public forbund copy(){
		forbund copy = new forbund();
		copy.organisationId = organisationId;
		copy.name = name;
		copy.shortName = shortName;
		copy.organisationTypeId = organisationTypeId;
		copy.parentOrganisationId = parentOrganisationId;
		return copy;
	}
	
	@Override
	public String toString() {
/*
		StringBuilder sb = new StringBuilder();
  		sb.append("OrganisationId: "); 
		sb.append(organisationId);
		sb.append('\n');
		sb.append("Name: ");
		sb.append(name);
		sb.append('\n');
		sb.append("ShortName: ");
		sb.append(shortName);
		sb.append('\n');
		sb.append("OrganisationTypeId: ");
		sb.append(organisationTypeId);
		sb.append("parentOrganisationId: ");
		sb.append(parentOrganisationId);
		return sb.toString();
*/
		return shortName.toString();
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
/*	public int compareTo(forbund another) {
		if (another == null) return 1;
		// sort descending, most recent first
		return 1;  //another.date.compareTo(date);
	}
*/
}
