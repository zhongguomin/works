package comns.time;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 继承自Java.util.Calendar的日历类，可作为①静态工具类和②Calendar的扩展子类使用 @ PowerCalendar.java
 * 2013-5-18 @ 说明： 使用示例： 一、静态工具类 LunarCalendar lunar =
 * PowerCalendar.getLunarFromGreg(Calendar cal); Calendar cal =
 * PowerCalendar.getGregFormLunar(1986, 05, 05, null); 二、对象类 PowerCalednar pcal
 * = PowerCalendar.getInstance(); int lunarYear =
 * pcal.getLunar(PowerCalednar.LUNAR_YEAR); String chineseDateStr =
 * pcal.getLunarFullStr(); //“一九八六年五月初五”
 * 
 * @CopyRights 赋天科技
 * 
 */
public class PowerCalendar extends Calendar {
	/** 操作阴历年份的key */
	public static final String LUNAR_YEAR = "lunar_year";
	/** 操作阴历月份的key */
	public static final String LUNAR_MONTH = "lunar_month";
	/** 操作阴历日期的key */
	public static final String LUNAR_DATE = "lunar_date";
	/** 获取24节气的key */
	public static final String LUNAR_SOLAR_TERMS = "lunar_solar_terms";
	/** 序列化id */
	private static final long serialVersionUID = 5832438338743225727L;

	/**
	 * 二进制一共17位，第一个为判断是否为闰月为大月还是小月，大月30天，小月29天； 2--13位为判断大小月；最后四位为判断闰几月
	 */
	private final static long[] LUNAR_INFO = new long[] { 0x04bd8, 0x04ae0,
			0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0,
			0x055d2, 0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540,
			0x0d6a0, 0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5,
			0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
			0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3,
			0x092e0, 0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0,
			0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0,
			0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8,
			0x0e950, 0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570,
			0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5,
			0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
			0x195a6, 0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50,
			0x06d40, 0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0,
			0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
			0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7,
			0x025d0, 0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50,
			0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954,
			0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260,
			0x0ea65, 0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0,
			0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0,
			0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20,
			0x0ada0 };

	/** 大年初一离同一年元旦的天数 */
	public static final char[] SolarLunarOffsetTable = { 49, 38, 28, 46, 34,
			24, 43, 32, 21, 40, // 1910
			29, 48, 36, 25, 44, 35, 22, 41, 31, 50, // 1920
			38, 27, 46, 35, 23, 43, 32, 22, 40, 29, // 1930
			47, 36, 25, 44, 34, 23, 41, 30, 49, 38, // 1940
			26, 45, 35, 24, 43, 32, 21, 40, 28, 47, // 1950
			36, 26, 44, 33, 23, 42, 30, 48, 38, 27, // 1960
			45, 35, 24, 43, 32, 20, 39, 29, 47, 36, // 1970
			26, 45, 33, 22, 41, 30, 48, 37, 27, 46, // 1980
			35, 24, 43, 32, 50, 39, 28, 47, 36, 26, // 1990
			45, 34, 22, 40, 30, 49, 37, 27, 46, 35, // 2000
			23, 42, 31, 21, 39, 28, 48, 37, 25, 44, // 2010
			33, 22, 40, 30, 49, 38, 27, 46, 35, 24, // 2020
			42, 31, 21, 40, 28, 47, 36, 25, 43, 33, // 2030
			22, 41, 30, 49, 38, 27, 45, 34, 23, 42, // 2040
			31, 21, 40, 29, 47, 36, 25, 44, 32, 22 // 2050
	};

	/** 用于保存24节气 **/
	private static final String[] PrincipleTermNames = { "大寒", "雨水", "春分",
			"谷雨", "小满", "夏至", "大暑", "处暑", "秋分", "霜降", "小雪", "冬至" };

	/** 用于保存24节气 **/
	private static final String[] SectionalTermNames = { "小寒", "立春", "惊蛰",
			"清明", "立夏", "芒种", "小暑", "立秋", "白露", "寒露", "立冬", "大雪" };
	private static final char[][] PrincipleTermYear = {
			{ 13, 45, 81, 113, 149, 185, 201 },
			{ 21, 57, 93, 125, 161, 193, 201 },
			{ 21, 56, 88, 120, 152, 188, 200, 201 },
			{ 21, 49, 81, 116, 144, 176, 200, 201 },
			{ 17, 49, 77, 112, 140, 168, 200, 201 },
			{ 28, 60, 88, 116, 148, 180, 200, 201 },
			{ 25, 53, 84, 112, 144, 172, 200, 201 },
			{ 29, 57, 89, 120, 148, 180, 200, 201 },
			{ 17, 45, 73, 108, 140, 168, 200, 201 },
			{ 28, 60, 92, 124, 160, 192, 200, 201 },
			{ 16, 44, 80, 112, 148, 180, 200, 201 },
			{ 17, 53, 88, 120, 156, 188, 200, 201 } };
	private static final char[][] PrincipleTermMap = {
			{ 21, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20,
					20, 20, 20, 20, 20, 19, 20, 20, 20, 19, 19, 20 },
			{ 20, 19, 19, 20, 20, 19, 19, 19, 19, 19, 19, 19, 19, 18, 19, 19,
					19, 18, 18, 19, 19, 18, 18, 18, 18, 18, 18, 18 },
			{ 21, 21, 21, 22, 21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21,
					20, 20, 20, 21, 20, 20, 20, 20, 19, 20, 20, 20, 20 },
			{ 20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 20, 20, 20, 20,
					19, 20, 20, 20, 19, 19, 20, 20, 19, 19, 19, 20, 20 },
			{ 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21,
					20, 21, 21, 21, 20, 20, 21, 21, 20, 20, 20, 21, 21 },
			{ 22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 21, 21, 21, 22,
					21, 21, 21, 21, 20, 21, 21, 21, 20, 20, 21, 21, 21 },
			{ 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23, 22, 23, 23, 23,
					22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 23 },
			{ 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,
					22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23 },
			{ 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24, 23, 23, 23, 23,
					22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23, 23 },
			{ 24, 24, 24, 24, 23, 24, 24, 24, 23, 23, 24, 24, 23, 23, 23, 24,
					23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 23 },
			{ 23, 23, 23, 23, 22, 23, 23, 23, 22, 22, 23, 23, 22, 22, 22, 23,
					22, 22, 22, 22, 21, 22, 22, 22, 21, 21, 22, 22, 22 },
			{ 22, 22, 23, 23, 22, 22, 22, 23, 22, 22, 22, 22, 21, 22, 22, 22,
					21, 21, 22, 22, 21, 21, 21, 22, 21, 21, 21, 21, 22 } };

	private static final char[][] SectionalTermMap = {
			{ 7, 6, 6, 6, 6, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 5, 5,
					5, 5, 5, 4, 5, 5 },
			{ 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4, 4, 4, 3,
					3, 4, 4, 3, 3, 3 },
			{ 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,
					5, 5, 4, 5, 5, 5, 5 },
			{ 5, 5, 6, 6, 5, 5, 5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 4, 4, 5, 5, 4, 4,
					4, 5, 4, 4, 4, 4, 5 },
			{ 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5, 5, 6, 5, 5,
					5, 5, 4, 5, 5, 5, 5 },
			{ 6, 6, 7, 7, 6, 6, 6, 7, 6, 6, 6, 6, 5, 6, 6, 6, 5, 5, 6, 6, 5, 5,
					5, 6, 5, 5, 5, 5, 4, 5, 5, 5, 5 },
			{ 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,
					7, 7, 6, 6, 6, 7, 7 },
			{ 8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,
					7, 7, 6, 7, 7, 7, 6, 6, 7, 7, 7 },
			{ 8, 8, 8, 9, 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7,
					7, 7, 6, 7, 7, 7, 7 },
			{ 9, 9, 9, 9, 8, 9, 9, 9, 8, 8, 9, 9, 8, 8, 8, 9, 8, 8, 8, 8, 7, 8,
					8, 8, 7, 7, 8, 8, 8 },
			{ 8, 8, 8, 8, 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7,
					7, 7, 6, 6, 7, 7, 7 },
			{ 7, 8, 8, 8, 7, 7, 8, 8, 7, 7, 7, 8, 7, 7, 7, 7, 6, 7, 7, 7, 6, 6,
					7, 7, 6, 6, 6, 7, 7 } };

	private static final char[][] SectionalTermYear = {
			{ 13, 49, 85, 117, 149, 185, 201, 250, 250 },
			{ 13, 45, 81, 117, 149, 185, 201, 250, 250 },
			{ 13, 48, 84, 112, 148, 184, 200, 201, 250 },
			{ 13, 45, 76, 108, 140, 172, 200, 201, 250 },
			{ 13, 44, 72, 104, 132, 168, 200, 201, 250 },
			{ 5, 33, 68, 96, 124, 152, 188, 200, 201 },
			{ 29, 57, 85, 120, 148, 176, 200, 201, 250 },
			{ 13, 48, 76, 104, 132, 168, 196, 200, 201 },
			{ 25, 60, 88, 120, 148, 184, 200, 201, 250 },
			{ 16, 44, 76, 108, 144, 172, 200, 201, 250 },
			{ 28, 60, 92, 124, 160, 192, 200, 201, 250 },
			{ 17, 53, 85, 124, 156, 188, 200, 201, 250 } };

	/** 阳历节日表 */
	private final static String[][] GRE_FESTVIAL = {
			// 一月
			{ "元旦", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "" },
			// 二月
			{ "", "", "", "", "", "", "", "", "", "", "", "", "", "情人节", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "" },
			// 三月
			{ "", "", "", "", "", "", "", "妇女节", "", "", "", "植树节", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"" },
			// 四月
			{ "愚人节", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"" },
			// 五月
			{ "劳动节", "", "", "青年节", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"" },
			// 六月
			{ "儿童节", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"" },
			// 七月
			{ "建党节", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"" },
			// 八月
			{ "建军节", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"" },
			// 九月
			{ "", "", "", "", "", "", "", "", "", "教师节", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"" },
			// 十月
			{ "国庆节", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"" },
			// 十一月
			{ "", "", "", "", "", "", "", "", "", "", "光棍节", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"" },
			// 十二月
			{ "艾滋病日", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					"", "", "", "", "", "", "", "", "", "圣诞节", "", "", "", "",
					"", "" }, };

	/** 普通公历一年每个月的天数 */
	private static final char[] GregMonthDayCount = {31, 28, 31, 30, 31, 30,
			31, 31, 30, 31, 30, 31 };
	/** 中文的月份数字 */
	private final static String CHINESE_MONTH_NUMBER[] = { "闰", "正", "二", "三",
			"四", "五", "六", "七", "八", "九", "十", "十一", "腊" };
	/** 中文的月份数字 */
	private final static String CHINESE_DATE_NUMBER[] = { "零", "一", "二", "三",
			"四", "五", "六", "七", "八", "九", "十", "二十", "三十", "廿", "卅", "初" };
	/** 十二生肖 */
	private final static String[] CHINA_ANIMAL = new String[] { "鼠", "牛", "虎",
			"兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };
	/** 干支中的干 */
	private final static String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊",
			"己", "庚", "辛", "壬", "癸" };
	/** 干支中的支 */
	private final static String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰",
			"巳", "午", "未", "申", "酉", "戌", "亥" };

	

	/**
	 * 转换阳历日期到阴历日期
	 * 
	 * @param y
	 *            阳历年份
	 * @param m
	 *            阳历月份
	 * @param d
	 *            阳历日期
	 * @return 计算出的阴历结果
	 */
	public static final LunarCalendar getLunarCalendar(int y, int m, int d) {
		LunarCalendar lunarCal = new LunarCalendar();
		int startYear = 1900, satrtMonth = 1, startDate = 31;
		int isLeap;
		int tempYear = 0, temp = 0, leap = 0;
		Calendar baseCal = new GregorianCalendar(startYear, satrtMonth - 1,
				startDate);
		Calendar tartgetCal = new GregorianCalendar(y, m - 1, d);
		int offset = (int) ((tartgetCal.getTimeInMillis() - baseCal
				.getTimeInMillis()) / 86400000L);
		for (tempYear = startYear; tempYear < 2100 && offset > 0; tempYear++) {
			temp = getLunarYearDays(tempYear);
			offset -= temp;
		}
		if (offset < 0) {// 如果offset被减成了负数
			offset += temp;
			tempYear--;
		}
		leap = getLunarLeapMonth(tempYear); // 获取结果农历年的闰月月份，不润为0
		isLeap = 0;
		int tempMonth = 0;
		for (tempMonth = 1; tempMonth < 13 && offset > 0; tempMonth++) {
			// 闰月
			if (leap > 0 && tempMonth == (leap + 1) && isLeap == 0) {
				--tempMonth;
				isLeap = 1;
				temp = getLunarLeapMonthDays((int) tempYear);
			} else {
				temp = getLunarMonthDays((int) tempYear, tempMonth);
			}
			// 解除闰月
			if (isLeap == 1 && tempMonth == (leap + 1))
				isLeap = 0;
			offset -= temp;
		}
		if (offset == 0 && leap > 0 && tempMonth == leap + 1) {
			if (isLeap == 1) {
				isLeap = 0;
			} else {
				isLeap = 1;
				--tempMonth;
			}
		}
		if (offset < 0) {
			offset += temp;
			--tempMonth;
		}
		lunarCal.chineseYear = tempYear;
		lunarCal.chineseMonth = tempMonth;
		lunarCal.chineseDate = offset + 1;
		if (leap == 1) {
			lunarCal.chineseMonth = -lunarCal.chineseMonth;
		}
		return lunarCal;
	}

	/**
	 * 转换阳历日期到阴历日期
	 * 
	 * @param cal
	 *            将要被转换的阳历日期
	 * @return 计算出的阴历日期
	 */
	public static final LunarCalendar getLunarFromGreg(Calendar cal) {
		return getLunarCalendar(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 将农历日期转换为阳历日期
	 * 
	 * @param iYear
	 *            农历年份
	 * @param iMonth
	 *            农历月份
	 * @param iDay
	 *            农历日期
	 * @param cal
	 *            被设置计算结果年月日的Calendar，如果为null，则新建一个Calendar，设置计算出的阳历年月日并返回
	 * @return
	 */
	public static final Calendar getGregFormLunar(int iYear, int iMonth,
			int iDay, Calendar cal) {
		System.out.println("Start Params:"+iYear+","+iMonth+","+iDay);
		int iSYear, iSMonth, iSDay;
		// 农历iYear年iMonth月iDay日里iYear年元旦的天数
		int iOffsetDays = getNewYearOffsetDays(iYear, iMonth, iDay)
				+ SolarLunarOffsetTable[iYear - 1901];
		System.out.println("Start iOffsetDays"+iOffsetDays);
		int iYearDays = isGregLeapYear(iYear) ? 366 : 365;		
		if (iOffsetDays >= iYearDays) {
			iSYear = iYear + 1;
			iOffsetDays -= iYearDays;
		} else {
			iSYear = iYear;
		}
		iSDay = iOffsetDays + 1;
		for (iSMonth = 1; iOffsetDays >= 0; iSMonth++) {
			iSDay = iOffsetDays + 1;
			System.out.println("iSMonth:"+iSMonth+",iOffsetDays"+iOffsetDays);
			iOffsetDays -= getGregMonthDays(iSYear, iSMonth);
		}
		iSMonth--;
		if (cal == null) {
			cal = Calendar.getInstance();
		}
		cal.set(iSYear, iSMonth - 1, iSDay, 0, 0);
		return cal;
	}

	/**
	 * 获取农历年份的中文
	 * 
	 * @param year
	 *            阴历年份
	 * @return
	 */
	public static final String getLunarYearStr(int year) {
		if (year > 9999 || year < 1000) {
			throw new UnsupportedOperationException("year must int 1000-9999");
		}
		int v1 = year / 1000;
		int v2 = year / 100 - (year / 1000) * 10;
		int v3 = year / 10 - (year / 100) * 10;
		int v4 = year - (year / 10) * 10;
		System.out.println("v1:" + v1 + "v1:" + v2 + "v1:" + v3 + "v1:" + v4);
		return CHINESE_DATE_NUMBER[v1] + CHINESE_DATE_NUMBER[v2]
				+ CHINESE_DATE_NUMBER[v3] + CHINESE_DATE_NUMBER[v4] + "年";
	}

	/**
	 * 获取农历月份的中文日期
	 * 
	 * @param month
	 *            阴历月份（负为闰月）
	 * @return
	 */
	public static final String getLunarMonthStr(int month) {
		String str = "";
		if (month < 0) {
			str += CHINESE_MONTH_NUMBER[0];
		}
		str += CHINESE_MONTH_NUMBER[Math.abs(month)] + "月";
		return str;
	}

	/**
	 * 传出农历对应的日期，如：初一
	 * 
	 * @param day
	 * @return
	 */
	public static final String getLunarDateStr(int day) {
		String str = "";
		if (day == 10)
			return CHINESE_DATE_NUMBER[15] + CHINESE_DATE_NUMBER[10];
		if (day == 20)
			return CHINESE_DATE_NUMBER[11];
		if (day == 30)
			return CHINESE_DATE_NUMBER[12];
		int two = (int) ((day) / 10);
		if (two == 0)
			str = CHINESE_DATE_NUMBER[15];
		if (two == 1)
			str = CHINESE_DATE_NUMBER[10];
		if (two == 2)
			str = CHINESE_DATE_NUMBER[13];
		if (two == 3)
			str = CHINESE_DATE_NUMBER[14];
		int one = (int) (day % 10);
		str += CHINESE_DATE_NUMBER[one];
		return str;
	}

	/**
	 * 获取中文日期的完全中文字符串
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static final String getLunarFullStr(int year, int month, int date) {
		return getLunarYearStr(year) + getLunarMonthStr(month)
				+ getLunarDateStr(date);
	}

	/**
	 * 用于获取24节气的值
	 * 
	 * @param gregorianYear
	 * @param gregorianMonth
	 * @param gregorianDate
	 * @return 24节气的值
	 */
	public static final String getLunarSolarTerms(int gregorianYear,
			int gregorianMonth, int gregorianDate) {
		String str = "";
		if (gregorianYear < 1901 || gregorianYear > 2100)
			return null;
		if (gregorianDate == getSectionalTerm(gregorianYear, gregorianMonth)) {
			str = SectionalTermNames[gregorianMonth - 1];
		} else if (gregorianDate == getPrincipleTerm(gregorianYear,
				gregorianMonth)) {
			str = PrincipleTermNames[gregorianMonth - 1];
		}
		return str;
	}

	/**
	 * 获取阴历年的生肖
	 * 
	 * @param year
	 * @return
	 */
	public static final String getLunarAnimal(int year) {
		return CHINA_ANIMAL[(year - 4) % 12];
	}

	/**
	 * 获取阴历年的干支
	 * 
	 * @param year
	 * @return
	 */
	public static final String getLunarCyclical(int year) {
		int num = year - 1900 + 36;
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	private static int getPrincipleTerm(int y, int m) {
		if (y < 1901 || y > 2100)
			return 0;
		int index = 0;
		int ry = y - 1901 + 1;
		while (ry >= PrincipleTermYear[m - 1][index])
			index++;
		int term = PrincipleTermMap[m - 1][4 * index + ry % 4];
		if ((ry == 171) && (m == 3))
			term = 21;
		if ((ry == 181) && (m == 5))
			term = 21;
		return term;
	}

	private static int getSectionalTerm(int y, int m) {
		if (y < 1901 || y > 2100)
			return 0;
		int index = 0;
		int ry = y - 1901 + 1;
		while (ry >= SectionalTermYear[m - 1][index])
			index++;
		int term = SectionalTermMap[m - 1][4 * index + ry % 4];
		if ((ry == 121) && (m == 4))
			term = 5;
		if ((ry == 132) && (m == 4))
			term = 5;
		if ((ry == 194) && (m == 6))
			term = 6;
		return term;
	}

	/**
	 * 
	 * 计算该农历日期离大年初一的天数
	 * 
	 * @param iYear
	 * 
	 * @param iMonth
	 * 
	 * @param iDay
	 * 
	 * @return
	 */

	private static int getNewYearOffsetDays(int iYear, int iMonth, int iDay) {
		int iOffsetDays = 0;
		int iLeapMonth = getLunarLeapMonth(iYear);
		if ((iLeapMonth > 0) && (iLeapMonth == iMonth - 12)) {
			iMonth = iLeapMonth;
			iOffsetDays += getLunarMonthDays(iYear, iMonth);
		}
		for (int i = 1; i < iMonth; i++) {
			iOffsetDays += getLunarMonthDays(iYear, i);
			if (i == iLeapMonth)
				iOffsetDays += getLunarMonthDays(iYear, iLeapMonth + 12);
		}
		iOffsetDays += iDay - 1;
		return iOffsetDays;
	}

	/**
	 * 传回农历 y年m月的总天数
	 * 
	 * @param y
	 * @param m
	 * @return
	 */
	final public static int getLunarMonthDays(int y, int m) {
		if ((LUNAR_INFO[y - 1900] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}

	/**
	 * 传回农历 y年的总天数
	 * 
	 * @param y
	 *            农历年份
	 * @return
	 */
	public static final int getLunarYearDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((LUNAR_INFO[y - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + getLunarLeapMonthDays(y));
	}

	/**
	 * 传回农历 y年闰月的天数
	 * 
	 * @param y
	 *            农历年份
	 * @return
	 */
	public static final int getLunarLeapMonthDays(int y) {
		if (getLunarLeapMonth(y) != 0) {
			if ((LUNAR_INFO[y - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	/**
	 * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
	 * 
	 * @param y
	 * @return
	 */
	public final static int getLunarLeapMonth(int y) {
		return (int) (LUNAR_INFO[y - 1900] & 0xf);
	}

	/**
	 * 获取农历的下一个月
	 * 
	 * @param y
	 * @param m
	 * @return
	 */
	private static int nextLunarMonth(int y, int m) {
		int leapMonth = getLunarLeapMonth(y);
		if (m > 0 && m == leapMonth) {
			return -m;
		} else if (m < 0) {
			m = -m;
		}
		m += 1;
		if (m == 13) {
			m = 1;
		}
		return m;
	}

	/**
	 * 获取农历的上一个月
	 * 
	 * @param y
	 * @param m
	 * @return
	 */
	private static int prevLunarMonth(int y, int m) {
		if (m < 0) {
			return -m;
		} else {
			m = m - 1;
			if (m == 0) {
				m = 12;
			}
		}
		int leapMonth = getLunarLeapMonth(y);
		if (m == leapMonth) {
			m = -m;
		}
		return -m;
	}

	private static boolean isGregLeapYear(int iYear) {
		return ((iYear % 4 == 0) && (iYear % 100 != 0) || iYear % 400 == 0);
	}

	/**
	 * 返回公历指定年月的天数
	 * 
	 * @param y
	 * @param m
	 * @return
	 */
	public static int getGregMonthDays(int y, int m) {
		int d = GregMonthDayCount[m - 1];
		if (m == 2 && isGregLeapYear(y))
			d++; // 公历闰年二月多一天
		return d;
	}

	public static class LunarCalendar {
		int chineseDate;
		int chineseMonth;
		int chineseYear;
		
		public LunarCalendar() {
		}

		public int getChineseDate() {
			return chineseDate;
		}

		public void setChineseDate(int chineseDate) {
			this.chineseDate = chineseDate;
		}

		public int getChineseMonth() {
			return chineseMonth;
		}

		public void setChineseMonth(int chineseMonth) {
			this.chineseMonth = chineseMonth;
		}

		public int getChineseYear() {
			return chineseYear;
		}

		public void setChineseYear(int chineseYear) {
			this.chineseYear = chineseYear;
		}

		public boolean isChineseLeap() {
			return chineseMonth < 0;
		}

		public LunarCalendar(int chineseDate, int chineseMonth,
				int chineseYear, boolean isChineseLeap) {
			super();
			this.chineseDate = chineseDate;
			this.chineseMonth = chineseMonth;
			this.chineseYear = chineseYear;
		}
	}

	public static PowerCalendar getInstance() {
		return new PowerCalendar();
	}

	private Calendar mCalendar;
	private LunarCalendar mLunar;

	public PowerCalendar() {
		super();
		mCalendar = Calendar.getInstance();
		updateLunar();
	}

	public PowerCalendar(Calendar cal) {
		super();
		mCalendar = cal;
		updateLunar();
	}

	public PowerCalendar(TimeZone zone, Locale aLocale) {
		super(zone, aLocale);
		mCalendar = Calendar.getInstance(zone, aLocale);
		updateLunar();
	}

	private void updateLunar() {
		mLunar = getLunarFromGreg(mCalendar);
	}

	@Override
	public void add(int field, int amount) {
		mCalendar.add(field, amount);
		updateLunar();
	}

	public void add(String field, int amount) {
		int temp = 0;
		if (field.equals(LUNAR_YEAR)) {
			mLunar.chineseYear += amount;
			mLunar.chineseMonth = Math.abs(mLunar.chineseMonth);
			temp = getLunarMonthDays(mLunar.chineseYear, mLunar.chineseMonth);
			if (mLunar.chineseDate > temp) {
				mLunar.chineseDate = temp;
			}
		} else if (field.equals(LUNAR_MONTH)) {
			int count = Math.abs(amount);
			for (int i = 0; i < count; i++) {
				if (amount < 0) {
					temp = prevLunarMonth(mLunar.chineseYear,
							mLunar.chineseMonth);
					if (Math.abs(temp) > Math.abs(mLunar.chineseMonth)) {
						mLunar.chineseYear--;						
					}
				} else {
					temp = nextLunarMonth(mLunar.chineseYear,
							mLunar.chineseMonth);
					if (Math.abs(temp) < Math.abs(mLunar.chineseMonth)) {
						mLunar.chineseYear++;
					}
				}
				mLunar.chineseMonth = temp;
			}
		} else if (field.equals(LUNAR_DATE)) {
			mLunar.chineseDate += amount;
			// TODO 分三种情况计算，增加后的chineseDate为负数，当前阴历月范围内，大于当前阴历月范围
		} else {
			throw new UnsupportedOperationException(
					"Only LUNAR_YEAR/LUNAR_MONTH/LUNAR_DATE can add");
		}
		mCalendar = getGregFormLunar(mLunar.chineseYear, mLunar.chineseMonth,
				mLunar.chineseDate, mCalendar);
	}

	@Override
	public boolean after(Object when) {
		return mCalendar.after(when);
	}

	@Override
	public boolean before(Object when) {
		return mCalendar.before(when);
	}

	@Override
	public Object clone() {
		return mCalendar.clone();
	}

	@Override
	public int compareTo(Calendar anotherCalendar) {
		return mCalendar.compareTo(anotherCalendar);
	}

	@Override
	public boolean equals(Object obj) {
		return mCalendar.equals(obj);
	}

	@Override
	public int get(int field) {
		return mCalendar.get(field);
	}

	@Override
	public int getActualMaximum(int field) {
		return mCalendar.getActualMaximum(field);
	}

	@Override
	public int getActualMinimum(int field) {
		return mCalendar.getActualMinimum(field);
	}

	@Override
	public int getFirstDayOfWeek() {
		return mCalendar.getFirstDayOfWeek();
	}

	@Override
	public int getMinimalDaysInFirstWeek() {
		return mCalendar.getMinimalDaysInFirstWeek();
	}

	@Override
	public long getTimeInMillis() {
		return mCalendar.getTimeInMillis();
	}

	@Override
	public TimeZone getTimeZone() {
		return mCalendar.getTimeZone();
	}

	@Override
	public int hashCode() {
		return mCalendar.hashCode();
	}

	@Override
	public boolean isLenient() {
		return mCalendar.isLenient();
	}

	@Override
	public void roll(int field, int amount) {
		mCalendar.roll(field, amount);
		updateLunar();
	}

	@Override
	public void set(int field, int value) {
		mCalendar.set(field, value);
		updateLunar();
	}

	/**
	 * 设置阴历指定的参数值
	 * 
	 * @param filed
	 *            LUNAR_YEAR或LUNAR_MONTH或LUNAR_DATE
	 * @param value
	 *            要设定的值
	 */
	public void setLunar(String filed, int value) {
		if (filed.equals(LUNAR_YEAR)) {
			mLunar.setChineseYear(value);
		} else if (filed.equals(LUNAR_MONTH)) {
			mLunar.setChineseMonth(value);
		} else if (filed.equals(LUNAR_DATE)) {
			mLunar.setChineseDate(value);
		}else{
			throw new UnsupportedOperationException(
					"Only LUNAR_YEAR/LUNAR_MONTH/LUNAR_DATE can set");
		}
		mCalendar = getGregFormLunar(mLunar.chineseYear, mLunar.chineseMonth,
				mLunar.chineseDate, mCalendar);
	}

	/**
	 * 设定Power日历对象的阴历年月日的值
	 * 
	 * @param lunarYear
	 * @param lunarMonth
	 * @param lunarDate
	 */
	public void setLunar(int lunarYear, int lunarMonth, int lunarDate) {
		mLunar.setChineseYear(lunarYear);
		mLunar.setChineseMonth(lunarMonth);
		mLunar.setChineseDate(lunarDate);
		mCalendar = getGregFormLunar(lunarYear, lunarMonth, lunarDate,
				mCalendar);
	}

	/**
	 * 
	 * @param filed
	 * @return 返回农历年份、月份（负为闰月）、日期其中一种参数，返回0，代表所要获取的filed不存在
	 */
	public int getLunar(String filed) {
		if (filed.equals(LUNAR_YEAR)) {
			return mLunar.getChineseYear();
		} else if (filed.equals(LUNAR_MONTH)) {
			return mLunar.getChineseMonth();
		} else if (filed.equals(LUNAR_DATE)) {
			return mLunar.getChineseDate();
		}
		return 0;
	}

	/**
	 * 获取阴历指定参数字符串
	 * 
	 * @param filed
	 *            可获取参数：年份、月份、日期、24节气（如果不是节气，返回空字符串）
	 * @return
	 */
	public String getLunarFiledStr(String filed) {
		if (filed.equals(LUNAR_YEAR)) {
			return getLunarYearStr(mLunar.getChineseYear());
		} else if (filed.equals(LUNAR_MONTH)) {
			return getLunarMonthStr(mLunar.chineseMonth);
		} else if (filed.equals(LUNAR_DATE)) {
			return getLunarDateStr(mLunar.getChineseDate());
		} else if (filed.equals(LUNAR_SOLAR_TERMS)) {
			return getLunarSolarTerms(mCalendar.get(Calendar.YEAR),
					mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));
		}else{
			throw new UnsupportedOperationException(
					"Only LUNAR_YEAR/LUNAR_MONTH/LUNAR_DATE/LUNAR_SOLAR_TERMS can getStr");
		}
	}

	/**
	 * 获取当前日历对象的获取阳历节日，如果没有，返回空字符串
	 * 
	 * @return
	 */
	public String geGregFestival() {
		return GRE_FESTVIAL[mCalendar.get(Calendar.MONTH)][mCalendar
				.get(Calendar.DATE) - 1];
	}
	
	/**
	 * 获取当前公历月的天数
	 * @return
	 */
	public int getCurrentGregMonthDays(){
		return getGregMonthDays(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
	}

	/**
	 * 获取当前日历对象的阴历节日，如果没有，返回空字符串
	 * 
	 * @return
	 */
	public String geLunarFestival() {
		int year = mLunar.getChineseYear();
		int month = mLunar.getChineseMonth();
		int day = mLunar.getChineseDate();
		String message = "";
		if (((month) == 1) && day == 1) {
			message = "春节";
		} else if (((month) == 1) && day == 15) {
			message = "元宵";
		} else if (((month) == 5) && day == 5) {
			message = "端午";
		} else if ((month == 7) && day == 7) {
			message = "七夕";
		} else if (((month) == 8) && day == 15) {
			message = "中秋";
		} else if ((month == 9) && day == 9) {
			message = "重阳";
		} else if ((month == 12) && day == 8) {
			message = "腊八";
		} else {
			if (month == 12) {
				if ((((getLunarMonthDays(year, month) == 29) && day == 29))
						|| ((((getLunarMonthDays(year, month) == 30) && day == 30)))) {
					message = "除夕";
				}
			}
		}
		return message;
	}

	/**
	 * 获取当前日历对象的阴历年月日中文字符串
	 * 
	 * @return
	 */
	public String getLunarFullStr() {
		return getLunarFullStr(mLunar.getChineseYear(),
				mLunar.getChineseMonth(), mLunar.getChineseDate());
	}

	/**
	 * 获取当前日历对象的阴历年生肖
	 * 
	 * @return
	 */
	public String getLunarYearAnimal() {
		return getLunarAnimal(mLunar.chineseYear);
	}

	/**
	 * 获取当前日历对象的阴历年干支
	 * 
	 * @return
	 */
	public String getLunarYearCyclical() {
		return getLunarCyclical(mLunar.chineseYear);
	}

	@Override
	public void setFirstDayOfWeek(int value) {
		if (mCalendar==null){
			return;
		}
		mCalendar.setFirstDayOfWeek(value);
		updateLunar();
	}

	@Override
	public void setLenient(boolean lenient) {
		super.setLenient(lenient);
		if (mCalendar!=null) {
			mCalendar.setLenient(lenient);
		}
	}

	@Override
	public void setMinimalDaysInFirstWeek(int value) {
		if (mCalendar==null){
			return;
		}
		mCalendar.setMinimalDaysInFirstWeek(value);
		updateLunar();
	}

	@Override
	public void setTimeInMillis(long millis) {
		mCalendar.setTimeInMillis(millis);
		updateLunar();
	}

	@Override
	public void setTimeZone(TimeZone value) {
		if (mCalendar==null){
			return;
		}
		mCalendar.setTimeZone(value);
	}

	@Override
	public String toString() {
		String s = mCalendar.get(Calendar.YEAR) + "年"
				+ (mCalendar.get(Calendar.MONTH) + 1) + "月"
				+ mCalendar.get(Calendar.DAY_OF_MONTH) + "日，阴历："
				+ getLunarFullStr() + "(" + getLunarAnimal(mLunar.chineseYear)
				+ "," + getLunarCyclical(mLunar.chineseYear) + ")";
		return s;
	}

	@Override
	public int getGreatestMinimum(int field) {
		return mCalendar.getGreatestMinimum(field);
	}

	@Override
	public int getLeastMaximum(int field) {
		return mCalendar.getLeastMaximum(field);
	}

	@Override
	public int getMaximum(int field) {
		return mCalendar.getMaximum(field);
	}

	@Override
	public int getMinimum(int field) {
		return mCalendar.getMinimum(field);
	}

	@Override
	public void roll(int field, boolean up) {
		mCalendar.roll(field, up);
		updateLunar();
	}

	@Override
	protected void computeFields() {
		updateLunar();
	}

	@Override
	protected void computeTime() {
		updateLunar();
	}

}