package com.wenxt.docprint.common;

public class LOVDTO {
	
	private String label;
	private String value;

	public LOVDTO(String label, String value) {
		this.label = label;
		this.value = value;
	}
	public LOVDTO() {
	}

	public LOVDTO(Object object, Object object2) {
		this.label = object.toString();
		this.value = object2.toString();
	}
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
