package eu.trentorise.smartcampus.communicator.model;

import java.io.Serializable;

public class NotificationAuthor implements Serializable{
	private static final long serialVersionUID = -1045073082737340872L;

	private Long socialId;
	private String name;
	
	public Long getSocialId() {
		return socialId;
	}
	public void setSocialId(Long socialId) {
		this.socialId = socialId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
