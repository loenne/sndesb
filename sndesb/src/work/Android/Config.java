package work.Android;

import android.util.Log;

public class Config {
	
		private String SearchIntervall;
		private String SelectedOrg;
		private String SelectedClub;
		
		public String getSearchIntervall() {
			return SearchIntervall;
		}

		public void setSearchIntervall(String searchIntervall) {
			this.SearchIntervall = searchIntervall.trim();
		}

		public String getSelectedOrg() {
			return SelectedOrg;
		}

		public void setSelectedOrg(String selectedOrg) {
			this.SelectedOrg = selectedOrg.trim();
		}
		
		public String getSelectedClub() {
			return SelectedClub;
		}

		public void setSelectedClub(String selectedClub) {
			this.SelectedClub = selectedClub.trim();
		}

		public void printRecord() {
			Log.e("XTRACTOR","organisation: record:");	
			Log.e("XTRACTOR","organisation: " + this.SearchIntervall);	
			Log.e("XTRACTOR","organisation: " + this.SelectedOrg );	
			Log.e("XTRACTOR","organisation: " + this.SelectedClub );	
		}
		

		
/*		public Organisation copy(){
			Organisation copy = new Organisation();
			copy.organisationId = organisationId;
			copy.name = name;
			copy.shortName = shortName;
			copy.organisationTypeId = organisationTypeId;
			copy.parentOrganisationId = parentOrganisationId;
			return copy;
		}
*/		
/*		@Override
		public String toString() {
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
/*		public int compareTo(Organisation another) {
			if (another == null) return 1;
			// sort descending, most recent first
			return 1;  //another.date.compareTo(date);
		}
*/
}
