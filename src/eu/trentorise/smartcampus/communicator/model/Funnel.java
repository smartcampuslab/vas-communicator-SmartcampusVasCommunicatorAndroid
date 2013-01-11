package eu.trentorise.smartcampus.communicator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.trentorise.smartcampus.storage.BasicObject;

public class Funnel extends BasicObject {
	private static final long serialVersionUID = -8544225509851840357L;

	private String title;
	private Long userId;
	private Long userSocialId;
	private List<String> labelIds;
	private String sourceType;
	private List<Action> actions;

	private Map<String,Object> funnelFilterData;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getUserSocialId() {
		return userSocialId;
	}
	public void setUserSocialId(Long userSocialId) {
		this.userSocialId = userSocialId;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public Map<String, Object> getFunnelFilterData() {
		return funnelFilterData;
	}
	public void setFunnelFilterData(Map<String, Object> funnelFilterData) {
		this.funnelFilterData = funnelFilterData;
	}
	public List<String> getLabelIds() {
		return labelIds;
	}
	public void setLabelIds(List<String> labelIds) {
		this.labelIds = labelIds;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public Map<String,Action> asActionMap() {
		Map<String,Action> actionMap = new HashMap<String, Action>();
		if (actions != null && actions.size() > 0) {
			for (Action a : actions) {
				actionMap.put(a.getLabel(), a);
			}
		}
		return actionMap;
	}

}
