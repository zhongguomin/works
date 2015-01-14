package com.g365.utils;

import java.util.Random;

public class UrlEncoder {

	
	/**
	 * ��ȡ����ַ���
	 * 
	 * @param length
	 *            Ҫ��ȡ�ĳ���
	 * @return ָ�����ȵ�����ַ���
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
		// prevByteBitCount��ʾ��ǰһ���ֽ�ȡ�õ�bit����nextByteBitCount��ʾ�Ӻ�һ���ֽ�ȡ�õ�bit��
		int prevByteBitCount = 0, nextByteBitCount = 6;
		// i��ʾ��ǰ������������n��ʾ�Ѿ������λ��
		int i = 0, n = 0;
		// byteCount��ʾ�ܵ�λ��
		int byteCount = 8 * bytes.length;
		byte b = 0;
		while (true) {
			// �����ǰ�������ֽ�ȡ��λ�������
			if (prevByteBitCount > 0 && nextByteBitCount > 0) {
				// ��ǰһ���ֽڵĵ�λ������nextByteBitCount��bit����ʹ��һ���ֽڵĸ�λ��nextByteBitCountָ����λ�������Ƶ��ֽڵ����λ��
				// Ȼ������λ�ƽ�������߼���Ҳ���ǽ���ǰһ���ֽںͺ�һ���ֽ�ȡ�õ���Ӧ��bit�ϲ�Ϊһ���ֽڵĵ�λ
				b = (byte) (((0xff & bytes[i]) << nextByteBitCount) | ((0xff & bytes[i + 1]) >> (8 - nextByteBitCount)));
				// ���߼����Ľ�����������bit�ó�0
				b = (byte) (b & 0x3f);
				prevByteBitCount = 8 - nextByteBitCount;
				nextByteBitCount = 6 - prevByteBitCount;
			}
			// ����Ӻ�һ���ֽ�ȡ�ø�6λ�����
			else if (prevByteBitCount == 0) {
				// ��һ���ֽڵĸ�6λ���ƶ���6λ
				b = (byte) ((0xff & bytes[i]) >> (8 - nextByteBitCount));
				// ��������λʱ�����Ǵ�ǰһ���ֽ�ȡ2��bit���Ӻ�һ������ȡ4��bit
				prevByteBitCount = 2;
				nextByteBitCount = 4;

			}
			// �����ǰһ���ֽ�ȡ�õ�6λ�����
			else if (nextByteBitCount == 0) {
				// ��ǰһ���ֽڵ��������bit�ó�0
				b = (byte) (0x3f & bytes[i]);
				// ��������λʱ���Ӻ�һ���ֽ�ȡ6��bit
				prevByteBitCount = 0;
				nextByteBitCount = 6;

			}
			result.append(base64.charAt(b));
			n += 6;
			i = n / 8;
			int remainBitCount = byteCount - n;
			if (remainBitCount < 6) {
				// ��ʣ���bit��0����Ȼ��Ҫ��base64������в�����Ӧ���ַ�������ӵ�����ַ��������
				if (remainBitCount > 0) {
					b = bytes[bytes.length - 1];
					b = (byte) (0x3f & (b << (6 - remainBitCount)));
					result.append(base64.charAt(b));
				}
				break;
			}
		}
		// �����bit����3������Ϊ1����һ����=����Ϊ2����������=��
		n = byteCount % 3;
		for (i = 0; i < n; i++)
			result.append("=");

		return result.toString();
	}
}
