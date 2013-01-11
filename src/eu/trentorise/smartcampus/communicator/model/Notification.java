package eu.trentorise.smartcampus.communicator.model;

import java.util.List;
import java.util.Map;

import eu.trentorise.smartcampus.storage.BasicObject;

public class Notification extends BasicObject {
	private static final long serialVersionUID = -926149934175243387L;

	private String title;
	private String description;
	private String type;
	private String user;
	private Map<String, Object> content;
	private long timestamp;
	private boolean starred;
	private List<String> labelIds;
	private String funnelId;
	private List<EntityObject> entities;

	private NotificationAuthor author;

	private boolean readed;

	private boolean markedDeleted;
	
	public Notification() {
		super();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Map<String, Object> getContent() {
		return content;
	}

	public void setContent(Map<String, Object> content) {
		this.content = content;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isStarred() {
		return starred;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFunnelId() {
		return funnelId;
	}

	public void setFunnelId(String funnelId) {
		this.funnelId = funnelId;
	}

	public boolean isReaded() {
		return readed;
	}

	public void setReaded(boolean readed) {
		this.readed = readed;
	}

	public List<String> getLabelIds() {
		return labelIds;
	}

	public void setLabelIds(List<String> labelIds) {
		this.labelIds = labelIds;
	}

	public List<EntityObject> getEntities() {
		return entities;
	}

	public void setEntities(List<EntityObject> entities) {
		this.entities = entities;
	}

	public NotificationAuthor getAuthor() {
		return author;
	}

	public void setAuthor(NotificationAuthor author) {
		this.author = author;
	}

	public void markDeleted() {
		markedDeleted = true;
	}
	
	public boolean markedDeleted() {
		return markedDeleted;
	}
}
