package com.g365.entity;

import java.io.Serializable;

/**
 * 软件升级
 * 保存要更新数据实体类
 *
 */
public class UpdataInfo extends AppWallDownloadInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	   /**
     * 软件分类
     */
	private int uid;
	/**
	 * 软件分类 soft 或者 game
	 */
	private String type;
	/**
	 * 软件编号
	 */
	public int tid;
	/**
	 * 图标url
	 */
	public  String iconurl;
	/**
	 * 应用程序名
	 */
	public  String name;
	/**
	 * 包名
	 */
	public  String packname;
	/**
	 * 版本
	 */
	public  String version;
	/**
	 * 版本号
	 */
	public  int versioncode;
	/**
	 * md5hash加密
	 */
	public  String md5hash;
	/**
	 * 星级
	 */
	public  int star;
	/**
	 * apk大小
	 */
	public  int size;
	/**
	 * 下载url
	 */
	public  String url;
	/**
	 * 最后一次更新时间
	 */
	private  int  lastdate;
	public UpdataInfo() {
	}
	
	public UpdataInfo(String iconurl,String name,int size,String version,String packname,String url){
		
		super();
		
		this.iconurl=iconurl;
		this.name=name;
		this.size=size;
		this.version=version;
		this.packname = packname;
		this.url = url;
		
	}
	public UpdataInfo(int uid, String type, int tid, String iconurl,
			String name, String packname, String version, int versioncode,
			String md5hash, int star, int size, String url, int lastdate) {
		super();
		this.uid = uid;
		this.type = type;
		this.tid = tid;
		this.iconurl = iconurl;
		this.name = name;
		this.packname = packname;
		this.version = version;
		this.versioncode = versioncode;
		this.md5hash = md5hash;
		this.star = star;
		this.size = size;
		this.url = url;
		this.lastdate = lastdate;
	}


	
 
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}
	public String getMd5hash() {
		return md5hash;
	}
	public void setMd5hash(String md5hash) {
		this.md5hash = md5hash;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getLastdate() {
		return lastdate;
	}
	public void setLastdate(int lastdate) {
		this.lastdate = lastdate;
	}
	

	@Override
	public String toString() {
		return "UpdataInfo [uid=" + uid + ", type=" + type + ", tid=" + tid
				+ ", iconurl=" + iconurl + ", name=" + name + ", packname="
				+ packname + ", version=" + version + ", versioncode="
				+ versioncode + ", md5hash=" + md5hash + ", star=" + star
				+ ", size=" + size + ", url=" + url + ", lastdate=" + lastdate
				+ "]";
	}

}
