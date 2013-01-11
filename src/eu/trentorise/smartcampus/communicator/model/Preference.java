package eu.trentorise.smartcampus.communicator.model;

import java.util.ArrayList;
import java.util.List;

import eu.trentorise.smartcampus.storage.BasicObject;

public class Preference extends BasicObject {
	private static final long serialVersionUID = 3771346946469090882L;
	
	public static final int DEF_MAX_MESSAGES = 1000;
	public static final boolean DEF_SYNC_AUTO = true;
	public static final int DEF_SYNC_PERIOD = 5;
	public static final List<Action> DEF_ACTIONS = null;

	public static final int MAX_MESSAGES = 1000;

	private List<LabelObject> labels = new ArrayList<LabelObject>();
	
	private List<Action> actions = DEF_ACTIONS;
	private Integer maxMessageNumber = DEF_MAX_MESSAGES;
	private boolean synchronizeAutomatically = DEF_SYNC_AUTO;
	private Integer syncPeriod = DEF_SYNC_PERIOD;
	
	public List<LabelObject> getLabels() {
		return labels;
	}

	public void setLabels(List<LabelObject> labels) {
		this.labels = labels;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public Integer getMaxMessageNumber() {
		return maxMessageNumber;
	}

	public void setMaxMessageNumber(Integer maxMessageNumber) {
		this.maxMessageNumber = maxMessageNumber;
	}

	public boolean isSynchronizeAutomatically() {
		return synchronizeAutomatically;
	}

	public void setSynchronizeAutomatically(boolean synchronizeAutomatically) {
		this.synchronizeAutomatically = synchronizeAutomatically;
	}

	public Integer getSyncPeriod() {
		return syncPeriod;
	}

	public void setSyncPeriod(Integer syncPeriod) {
		this.syncPeriod = syncPeriod;
	}
}
