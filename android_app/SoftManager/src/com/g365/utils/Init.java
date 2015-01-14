package com.g365.utils;

import java.io.Serializable;

public class Init implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//²úÆ·ºÅ
	private String product;
	//ÇþµÀºÅ
	private String channel;
	
	private String type;
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
