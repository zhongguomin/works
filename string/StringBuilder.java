

	/**
     * 取字符串的前 preCount 个字符
     * @param str 被处理字符串
     * @param toCount 截取长度
     * @param more 后缀字符串
     * @return String
     */
	public static String substring(String str, int preCount, String more) {
		int reInt = 0;
		String reStr = "";

		if (str == null)	return "";
		
		char[] tempChar = str.toCharArray();
		for(int i=0; (i<tempChar.length && preCount>reInt); i++) {
			String s1 = str.valueOf(tempChar[i]);
			byte[] b = s1.getBytes();
			reInt += b.length;
			reStr += tempChar[i];
		}
		
		if (preCount == reInt || (preCount == reInt - 1))
			reStr += more;
		
		return reStr;
	}

