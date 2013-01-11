package eu.trentorise.smartcampus.communicator.model;

import java.io.Serializable;
import java.util.Map;

public class EntityObject implements Serializable {
	private static final long serialVersionUID = -1405310311956833390L;

	private String type;
	private String id;
	private Map<String, Object> data;
	private String title;
	private Long entityId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
}
