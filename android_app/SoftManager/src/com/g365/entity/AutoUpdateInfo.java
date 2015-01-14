package com.g365.entity;

/**
 * @类名: AutoUpdateInfo
 * 
 * @作者: Nova
 * 
 * @日期: 2014年1月17日11:22:40
 * 
 * @描述: 类<code>AutoUpdateInfo</code>当前软件升级的实体类</p>
 * 
 *      Copyright 2014 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 */
public class AutoUpdateInfo {
	/** 应用id */
	private String id;
	/** 类型 1软件 2游戏 */
	private String type;
	/** 图标 */
	private String icon;
	/** 大小 */
	private String size;
	/** 版本 */
	private String version;
	/** 语言 */
	private String language;
	/** 下载量 */
	private String downsums;
	/** 星级 */
	private String star;
	/**更新时间 */
	private String lastupdate;
	/** 简介 */
	//private String intro;
	/**更新说明 */
	//private String describe;
	/** 下载地址 */
	private String fileurl;
	
	public AutoUpdateInfo() {
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getDownsums() {
		return downsums;
	}
	public void setDownsums(String downsums) {
		this.downsums = downsums;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public String getLastupdate() {
		return lastupdate;
	}
	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}
//	public String getIntro() {
//		return intro;
//	}
//	public void setIntro(String intro) {
//		this.intro = intro;
//	}
//	public String getDescribe() {
//		return describe;
//	}
//	public void setDescribe(String describe) {
//		this.describe = describe;
//	}
	public String getFileurl() {
		return fileurl;
	}
	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}
	public AutoUpdateInfo(String id, String type, String icon, String size,
			String version, String language, String downsums, String star,
			String lastupdate,   String fileurl) {
		super();
		this.id = id;
		this.type = type;
		this.icon = icon;
		this.size = size;
		this.version = version;
		this.language = language;
		this.downsums = downsums;
		this.star = star;
		this.lastupdate = lastupdate;
		//this.intro = intro;
		//this.describe = describe;
		this.fileurl = fileurl;
	}
	
}
