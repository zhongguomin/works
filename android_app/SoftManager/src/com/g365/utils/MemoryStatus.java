package com.g365.utils;

import java.io.File;
import java.math.BigDecimal;

import android.os.Environment;
import android.os.StatFs;
/**
 * ��ȡ�ֻ� sdcard ʣ��ռ乤����
 * @author Administrator
 *
 */
public class MemoryStatus {

	static final int ERROR = -1;

	/**
	 * �ⲿ�洢�Ƿ����
	 * �ж�SdCard�Ƿ���ڲ����ǿ��õ�
	 * @return
	 */
	static public boolean externalMemoryAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

	}

	/**
	 * ������ֻ��ڴ�Ŀ��ÿռ��С 
	 */
	static public long getAvailableInternalMemorySize() {

		File path = Environment.getDataDirectory();
		// ȡ��sdcard�ļ�·��
		StatFs stat = new StatFs(path.getPath());
		//��ȡblock��SIZE
		long blockSize = stat.getBlockSize();
		// ��ȡBLOCK����
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;

	}

	/**
	 * ������ֻ��ڴ���ܿռ��С
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
	 * ������ֻ�sdcard�Ŀ��ÿռ��С
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
	 * ������ֻ�sdcard���ܿռ��С
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
	 * �� length��Сת���� G,MB,KB��B ����ȷ��С�������λ
	 * 
	 * @param length
	 *            �ļ���С
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
	 * �ֽ�ת���ķ���
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
