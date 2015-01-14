package com.g365.utils;

import java.util.Random;

public class UrlEncoder {

	
	/**
	 * 获取随机字符串
	 * 
	 * @param length
	 *            要获取的长度
	 * @return 指定长度的随机字符串
	 */
	public static String random_str(int length) {
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

	public static String base64Encoder(byte[] bytes) {
		
		
		StringBuilder result = new StringBuilder();
		String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		// prevByteBitCount表示从前一个字节取得的bit数，nextByteBitCount表示从后一个字节取得的bit数
		int prevByteBitCount = 0, nextByteBitCount = 6;
		// i表示当前的数组索引，n表示已经处理的位数
		int i = 0, n = 0;
		// byteCount表示总的位数
		int byteCount = 8 * bytes.length;
		byte b = 0;
		while (true) {
			// 处理从前后两个字节取得位数的情况
			if (prevByteBitCount > 0 && nextByteBitCount > 0) {
				// 将前一个字节的低位向左移nextByteBitCount个bit，并使下一个字节的高位（nextByteBitCount指定的位数）右移到字节的最低位，
				// 然后将两个位移结果进行逻辑或，也就是将从前一个字节和后一个字节取得的相应的bit合并为一个字节的低位
				b = (byte) (((0xff & bytes[i]) << nextByteBitCount) | ((0xff & bytes[i + 1]) >> (8 - nextByteBitCount)));
				// 将逻辑或后的结果的最高两个bit置成0
				b = (byte) (b & 0x3f);
				prevByteBitCount = 8 - nextByteBitCount;
				nextByteBitCount = 6 - prevByteBitCount;
			}
			// 处理从后一个字节取得高6位的情况
			else if (prevByteBitCount == 0) {
				// 后一个字节的高6位右移动低6位
				b = (byte) ((0xff & bytes[i]) >> (8 - nextByteBitCount));
				// 处理后面的位时，就是从前一个字节取2个bit，从后一个字字取4个bit
				prevByteBitCount = 2;
				nextByteBitCount = 4;

			}
			// 处理从前一个字节取得低6位的情况
			else if (nextByteBitCount == 0) {
				// 将前一个字节的最高两个bit置成0
				b = (byte) (0x3f & bytes[i]);
				// 处理后面的位时，从后一个字节取6个bit
				prevByteBitCount = 0;
				nextByteBitCount = 6;

			}
			result.append(base64.charAt(b));
			n += 6;
			i = n / 8;
			int remainBitCount = byteCount - n;
			if (remainBitCount < 6) {
				// 将剩余的bit补0后，仍然需要在base64编码表中查找相应的字符，并添加到结果字符串的最后
				if (remainBitCount > 0) {
					b = bytes[bytes.length - 1];
					b = (byte) (0x3f & (b << (6 - remainBitCount)));
					result.append(base64.charAt(b));
				}
				break;
			}
		}
		// 如果总bit数除3的余数为1，加一个“=”，为2，加两个“=”
		n = byteCount % 3;
		for (i = 0; i < n; i++)
			result.append("=");

		return result.toString();
	}
}
