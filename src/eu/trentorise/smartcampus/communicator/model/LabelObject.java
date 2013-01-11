package eu.trentorise.smartcampus.communicator.model;

import java.io.Serializable;

public class LabelObject implements Serializable {
	private static final long serialVersionUID = 8743250603800689932L;

	private String name;
	private String color;

	private String id;
	
	public LabelObject() {
		super();
	}


	public LabelObject( String id, String name, String color) {
		super();
		this.name = name;
		this.color = color;
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
}
