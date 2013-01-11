package eu.trentorise.smartcampus.communicator.model;

import java.io.Serializable;

public class Action implements Serializable{
	private static final long serialVersionUID = -6025860857466434429L;

	public static final int EMAIL_ACTION_TYPE = 1;
	
	private String label;
	private int type;
	private String value;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public static Action createEmailAction(String email) {
		Action a = new Action();
		a.setLabel(email);
		a.setValue(email);
		a.setType(EMAIL_ACTION_TYPE);
		return a;
	}
}
