package eu.trentorise.smartcampus.communicator.model;

import java.io.Serializable;

import eu.trentorise.smartcampus.communicator.model.CommunicatorConstants.ORDERING;

public class NotificationFilter implements Serializable {
	private static final long serialVersionUID = 5704217339723155689L;

	private Boolean starred = null;
	private Boolean readed = null;
	private String funnelId;
	private String labelId;

	private String searchText;
	
	private ORDERING ordering;
	
	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public Boolean isStarred() {
		return starred;
	}

	public void setStarred(Boolean starred) {
		this.starred = starred;
	}

	public String getFunnelId() {
		return funnelId;
	}

	public void setFunnelId(String funnelId) {
		this.funnelId = funnelId;
	}

	public ORDERING getOrdering() {
		return ordering;
	}

	public void setOrdering(ORDERING ordering) {
		this.ordering = ordering;
	}

	public Boolean isReaded() {
		return readed;
	}

	public void setReaded(Boolean readed) {
		this.readed = readed;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
}
