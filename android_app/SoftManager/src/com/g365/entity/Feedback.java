package com.g365.entity;

import java.io.Serializable;

public class Feedback  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
