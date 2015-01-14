package com.g365.utils;

import java.io.File;
import java.math.BigDecimal;

import android.os.Environment;
import android.os.StatFs;
/**
 * 读取手机 sdcard 剩余空间工具类
 * @author Administrator
 *
 */
public class MemoryStatus {

	static final int ERROR = -1;

	/**
	 * 外部存储是否可用
	 * 判断SdCard是否存在并且是可用的
	 * @return
	 */
	static public boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

	}

	/**
	 * 这个是手机内存的可用空间大小 
	 */
	static public long getAvailableInternalMemorySize() {

		File path = Environment.getDataDirectory();
		// 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		//获取block的SIZE
		long blockSize = stat.getBlockSize();
		// 获取BLOCK数量
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;

	}

	/**
	 * 这个是手机内存的总空间大小
	 * 
	 * @return
	 */
	static public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;

	}

	/**
	 * 这个是手机sdcard的可用空间大小
	 *  
	 * @return
	 */
	static public long getAvailableExternalMemorySize() {

		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			//long totalBlocks = stat.getBlockCount();
			long totalBlocks = stat.getAvailableBlocks();
			return totalBlocks * blockSize;
		} else {

			return ERROR;
		}

	}

	/**
	 * 这个是手机sdcard的总空间大小
	 * 
	 * @return
	 */
	static public long getTotalExternalMemorySize() {

		if (externalMemoryAvailable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return totalBlocks * blockSize;
		} else {

			return ERROR;
		}

	}


	
	
	
	/**
	 * 将 length大小转换成 G,MB,KB或B ，精确到小数点后两位
	 * 
	 * @param length
	 *            文件大小
	 * @return
	 */
	public static String formatFileSize(long length) {

		String result = null;

		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + (length % 1073741824 > 0 ? 3 : 0)) + "G";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0, sub_string
					+ (length % 1048576 > 0 ? 3 : 0))
					+ "M";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0, sub_string
					+ (length % 1024 > 0 ? 3 : 0))
					+ "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";

		return result;
	}
	
	
	
	/**
	 * 字节转换的方法
	 * @param size
	 * @return
	 */
	public static String getSizeString(long size){
		int roundingMode=BigDecimal.ROUND_HALF_UP;
		String str="";
		if(size<1024){
			str=size+" B";
		}else if(size<1048576){
			str=new BigDecimal(((double)size)/1024d).setScale(2, roundingMode).doubleValue()+" KB";
		}else if(size<1073741824){
			str=new BigDecimal(((double)size)/1048576d).setScale(2, roundingMode).doubleValue()+" MB";
		}else if(size<1099511627776l){
			str=new BigDecimal(((double)size)/1073741824d).setScale(3, roundingMode).doubleValue()+" GB";
		}else{
			str=new BigDecimal(((double)size)/1099511627776d).setScale(4, roundingMode).doubleValue()+" TB";
		}
		return str;
	}
}
