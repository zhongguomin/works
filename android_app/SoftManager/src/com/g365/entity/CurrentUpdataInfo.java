package com.g365.entity;

/**
 * 
 * @author nova
 *  本软件升级的实体类
 *  日期 ：2013年1月9日10:47:03
 */
public class CurrentUpdataInfo {

	private String version;
	private String url;
	private String description;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public CurrentUpdataInfo(String version, String url, String description) {
		super();
		this.version = version;
		this.url = url;
		this.description = description;
	}
	public CurrentUpdataInfo() {
		super();
	}
}
