package comns.bytes;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * @类名: ByteHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-24 下午03:14:42
 * 
 * @描述: 类<code>ByteHelper</code>是对字符串、字符和字节等进行各种处理的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class ByteHelper {

	/**
	 * 获取随机字符串
	 * 
	 * @param length
	 *            要获取的长度
	 * @return 指定长度的随机字符串
	 */
	public static String randomStr(int length) {

		String hash = "";
		String charStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		char chars[] = charStr.toCharArray();
		int max = chars.length;
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			hash += chars[Math.abs(random.nextInt()) % max];
		}

		return hash;
	}

	/**
	 * 将字节数组用base64编码成字符串加密
	 * 
	 * @param bytes
	 *            要编码的字节数组
	 * @return 加密后的字符串
	 */
	public static String base64Encoder(byte[] bytes) {

		StringBuilder result = new StringBuilder();
		String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

		int prevByteBitCount = 0, nextByteBitCount = 6;// prevByteBitCount表示从前一个字节取得的bit数，nextByteBitCount表示从后一个字节取得的bit数
		int i = 0, n = 0;// i表示当前的数组索引，n表示已经处理的位数
		int byteCount = 8 * bytes.length;// byteCount表示总的位数
		byte b = 0;

		while (true) {
			/* 处理从前后两个字节取得位数的情况 */
			if (prevByteBitCount > 0 && nextByteBitCount > 0) {
				/*
				 * 将前一个字节的低位向左移nextByteBitCount个bit，并使下一个字节的高位（nextByteBitCount指定的位数
				 * ）右移到字节的最低位，
				 * 然后将两个位移结果进行逻辑或，也就是将从前一个字节和后一个字节取得的相应的bit合并为一个字节的低位
				 */
				b = (byte) (((0xff & bytes[i]) << nextByteBitCount) | ((0xff & bytes[i + 1]) >> (8 - nextByteBitCount)));
				/* 将逻辑或后的结果的最高两个bit置成0 */
				b = (byte) (b & 0x3f);
				prevByteBitCount = 8 - nextByteBitCount;
				nextByteBitCount = 6 - prevByteBitCount;
			}
			/* 处理从后一个字节取得高6位的情况 */
			else if (prevByteBitCount == 0) {
				b = (byte) ((0xff & bytes[i]) >> (8 - nextByteBitCount));// 后一个字节的高6位右移动低6位
				/* 处理后面的位时，就是从前一个字节取2个bit，从后一个字字取4个bit */
				prevByteBitCount = 2;
				nextByteBitCount = 4;
			}
			/* 处理从前一个字节取得低6位的情况 */
			else if (nextByteBitCount == 0) {
				b = (byte) (0x3f & bytes[i]);// 将前一个字节的最高两个bit置成0
				/* 处理后面的位时，从后一个字节取6个bit */
				prevByteBitCount = 0;
				nextByteBitCount = 6;

			}
			result.append(base64.charAt(b));
			n += 6;
			i = n / 8;
			int remainBitCount = byteCount - n;
			if (remainBitCount < 6) {
				/* 将剩余的bit补0后，仍然需要在base64编码表中查找相应的字符，并添加到结果字符串的最后 */
				if (remainBitCount > 0) {
					b = bytes[bytes.length - 1];
					b = (byte) (0x3f & (b << (6 - remainBitCount)));
					result.append(base64.charAt(b));
				}
				break;
			}
		}
		/* 如果总bit数除3的余数为1，加一个“=”，为2，加两个“=” */
		n = byteCount % 3;
		for (i = 0; i < n; i++)
			result.append("=");

		return result.toString();
	}

	/**
	 * 将字符串中的 HTML语言替换成对应字符
	 * 
	 * @param source
	 *            源字符串
	 * @return 处理后的字符串
	 */
	public static String replaceHtmlString(String source) {

		if (source == null || source.length() <= 0)
			return source;

		source = source.replace("&#xd;", "");
		source = source.replace("&quot;", "\"");
		source = source.replace("&#039;", "'");
		source = source.replace("&apos;", "'");
		source = source.replace("&lt;", "<");
		source = source.replace("&gt;", ">");
		source = source.replace("&amp;", "&");
		source = source.replace("&ldquo;", "“");
		source = source.replace("&rdquo;", "”");
		source = source.replace("&nbsp;", " ");

		return source;
	}

	/**
	 * 将特殊字符如：'&','?','=','.','>','<','\'',';',':','/'转成'_'
	 * 
	 * @param source
	 *            源字符串
	 * @return
	 */
	public static String replaceSpecialString(String source) {

		if (source == null || source.length() <= 0) {
			return source;
		}

		source = source.replace('&', '_');
		source = source.replace('?', '_');
		source = source.replace('=', '_');
		source = source.replace('>', '_');
		source = source.replace('<', '_');
		source = source.replace('\'', '_');
		source = source.replace('\"', '_');
		source = source.replace(';', '_');
		source = source.replace(':', '_');
		source = source.replace('/', '_');

		return source;
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
	 * 将输入流转换成字符串
	 * 
	 * @param inputStream
	 *            输入流
	 * @param encode
	 *            编码（use default encode if null）
	 * @return null if inputStream is null
	 */
	public static String inStream2Str(InputStream inputStream, String encode) {

		String result = null;

		if (inputStream != null) {

			try {
				BufferedInputStream bis = new BufferedInputStream(inputStream);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int position = 0;
				while ((position = bis.read()) != -1) {
					baos.write((byte) position);
				}
				if (encode == null) {
					result = baos.toString();
				} else {
					result = baos.toString(encode);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 从源串获取正则表达式所匹配的字符串列表
	 * 
	 * @param source
	 *            源串
	 * @param regEx
	 *            正则表达式
	 * @return
	 */
	public static ArrayList<String> getRegExStr(String source, String regEx) {

		ArrayList<String> resultList = new ArrayList<String>();

		if (TextUtils.isEmpty(source) || TextUtils.isEmpty(regEx)) {
			return resultList;
		}

		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			resultList.add(matcher.group());
		}

		return resultList;
	}
}
