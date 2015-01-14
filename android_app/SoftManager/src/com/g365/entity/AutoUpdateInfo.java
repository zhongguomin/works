package com.g365.entity;

/**
 * @����: AutoUpdateInfo
 * 
 * @����: Nova
 * 
 * @����: 2014��1��17��11:22:40
 * 
 * @����: ��<code>AutoUpdateInfo</code>��ǰ���������ʵ����</p>
 * 
 *      Copyright 2014 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 */
public class AutoUpdateInfo {
	/** Ӧ��id */
	private String id;
	/** ���� 1��� 2��Ϸ */
	private String type;
	/** ͼ�� */
	private String icon;
	/** ��С */
	private String size;
	/** �汾 */
	private String version;
	/** ���� */
	private String language;
	/** ������ */
	private String downsums;
	/** �Ǽ� */
	private String star;
	/**����ʱ�� */
	private String lastupdate;
	/** ��� */
	//private String intro;
	/**����˵�� */
	//private String describe;
	/** ���ص�ַ */
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
