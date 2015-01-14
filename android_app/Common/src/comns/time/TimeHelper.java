package comns.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * @类名: TimeHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-1 下午11:45:58
 * 
 * @描述: 类<code>TimeHelper</code>是时间和日期的帮助类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class TimeHelper {

	/**
	 * getIntervalTimeString函数参数</p>只显示最大单位的时间
	 */
	public static final int MODE_SHOW_SHORT_TIME = 1;
	/**
	 * getIntervalTimeString函数参数</p>显示全部单位的时间
	 */
	public static final int MODE_SHOW_LONG_TIME = 2;

	/**
	 * 获取当前时间
	 * 
	 * @return 获取当前的 Calendar
	 */
	public static Calendar getCurrentTime() {

		Calendar now = Calendar.getInstance();

		return now;
	}

	/**
	 * 获取当前时间多少秒后的时间
	 * 
	 * @param secs
	 *            指定的秒数
	 * @return 获取 secs 秒后的 Calendar
	 */
	public static Calendar getTimeAfterInSecs(int secs) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, secs);

		return cal;
	}

	/**
	 * 获取大于当前时间最近的一个由小时和分钟指定的时间（如果小于当前时间则为第二天的时间）
	 * 
	 * @param hour
	 *            指定的小时数
	 * @param minute
	 *            指定的分钟数
	 * @return 大于当前时间的最近一个被指定的 Calendar
	 */
	public static Calendar getLatestTimeInDayAt(int hour, int minute) {

		Calendar cal = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		cal.clear();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DATE);
		cal.set(year, month, day, hour, minute, 0);

		long calInMillis = cal.getTimeInMillis();
		long nowInMillis = now.getTimeInMillis();

		if (calInMillis <= nowInMillis) { // 如果设置的时间小于或等于当前时间则设置成第二天的该时间
			cal.add(Calendar.DATE, 1);
		}
		return cal;
	}

	/**
	 * 获取大于当前时间最近的一个由小时和分钟指定的时间（如果小于当前时间则为下一年时间）
	 * 
	 * @param year
	 *            指定的年份
	 * @param month
	 *            指定的月份
	 * @param day
	 *            指定的天
	 * @return 大于当前时间的最近一个被指定的 Calendar
	 */
	public static Calendar getLatestTimeInYearAt(int year, int month, int day) {

		Calendar cal = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		cal.clear();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		cal.set(year, month - 1, day, hour, minute, 0);

		long calInMillis = cal.getTimeInMillis();
		long nowInMillis = now.getTimeInMillis();

		if (calInMillis <= nowInMillis) { // 如果设置的时间小于或等于当前时间则设置成下一年的该时间
			cal.set(Calendar.YEAR, now.get(Calendar.YEAR) + 1);
		}

		return cal;
	}

	/**
	 * 获取一个指定的 Calendar，精确到天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 指定后的 Calendar
	 */
	public static Calendar getTimeAt(int year, int month, int day) {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day, 0, 0, 0);

		return cal;
	}

	/**
	 * 获取一个指定的 Calendar，精确到分钟
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @return 指定后的 Calendar
	 */
	public static Calendar getTimeAt(int year, int month, int day, int hour,
			int minute) {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day, hour, minute, 0);

		return cal;
	}

	/**
	 * 获取一个指定的 Calendar，精确到秒
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @return 指定后的 Calendar
	 */
	public static Calendar getTimeAt(int year, int month, int day, int hour,
			int minute, int second) {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day, hour, minute, second);

		return cal;
	}

	/**
	 * 获得当天的时间
	 * 
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param second
	 *            秒
	 * @return
	 */
	public static Calendar getTodayTimeAt(int hour, int minute, int second) {

		Calendar cal = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		cal.clear();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH);
		cal.set(year, month, day, hour, minute, second);

		return cal;
	}

	/**
	 * 获取当天指定时间段内的随机时间
	 * 
	 * @param beginH
	 *            起始时间（包括0和23之间的任何整数）
	 * @param endH
	 *            截止时间（包括1和24之间的任何整数）
	 * @return
	 */
	public static Calendar getRandomTimeBetween(int beginH, int endH) {

		Calendar calendar = null;

		if (beginH < 0 || beginH > 23 || endH < 1 || endH > 24
				|| beginH >= endH) {
			return calendar;
		}
		Calendar beginCalendar = getTodayTimeAt(beginH, 0, 0);
		Calendar endCalendar = getTodayTimeAt(endH - 1, 59, 0);
		long beginInMillis = beginCalendar.getTimeInMillis();
		long endInMillis = endCalendar.getTimeInMillis();
		long calInMillis = beginInMillis
				+ (long) (Math.random() * (endInMillis - beginInMillis));
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(calInMillis);

		return calendar;
	}

	/**
	 * 获取大于当前时间指定时间段内最近的随机时间
	 * 
	 * @param beginH
	 *            起始时间（包括0和23之间的任何整数）
	 * @param endH
	 *            截止时间（包括1和24之间的任何整数）
	 * @return
	 */
	public static Calendar getAvailableRandomTimeBetween(int beginH, int endH) {

		Calendar calendar = getRandomTimeBetween(beginH, endH);
		if (calendar == null) {
			return calendar;
		}
		Calendar beginCalendar = getTodayTimeAt(beginH, 0, 0);
		Calendar endCalendar = getTodayTimeAt(endH - 1, 59, 0);
		Calendar nowCalendar = Calendar.getInstance();
		long beginInMillis = beginCalendar.getTimeInMillis();
		long endInMillis = endCalendar.getTimeInMillis();
		long calInMillis = calendar.getTimeInMillis();
		long nowInMillis = nowCalendar.getTimeInMillis();
		if (nowInMillis > beginInMillis && nowInMillis < endInMillis) {
			calInMillis = nowInMillis
					+ (long) (Math.random() * (endInMillis - nowInMillis));
			calendar.setTimeInMillis(calInMillis);
		} else if (nowInMillis >= endInMillis) {
			calendar.add(Calendar.DATE, 1);
		}

		return calendar;
	}

	/**
	 * 获取下一次（明天）指定时间段内的随机时间
	 * 
	 * @param beginH
	 *            起始时间（包括0和23之间的任何整数）
	 * @param endH
	 *            截止时间（包括1和24之间的任何整数）
	 * @return
	 */
	public static Calendar getNextRandomTimeBetween(int beginH, int endH) {

		Calendar calendar = getRandomTimeBetween(beginH, endH);
		calendar.add(Calendar.DATE, 1);

		return calendar;
	}

	/**
	 * 是否属于当天时间
	 * 
	 * @param timeInMillis
	 *            时间戳
	 * @return
	 */
	public static boolean isTodayTime(long timeInMillis) {

		boolean isTodayTime = false;
		if (timeInMillis >= getTodayTimeAt(0, 0, 1).getTimeInMillis()
				&& timeInMillis <= getTodayTimeAt(23, 59, 59).getTimeInMillis()) {
			isTodayTime = true;
		}

		return isTodayTime;
	}

	/**
	 * 是否属于当天时间
	 * 
	 * @param calendar
	 *            Calendar
	 * @return
	 */
	public static boolean isTodayTime(Calendar calendar) {

		boolean isTodayTime = false;
		if (calendar.getTimeInMillis() >= getTodayTimeAt(0, 0, 1)
				.getTimeInMillis()
				&& calendar.getTimeInMillis() <= getTodayTimeAt(23, 59, 59)
						.getTimeInMillis()) {
			isTodayTime = true;
		}

		return isTodayTime;
	}

	/**
	 * 设置时间到闹钟管理器（单次触发）
	 * 
	 * @param context
	 *            上下文
	 * @param setTime
	 *            设置的时间
	 * @param pendingIntent
	 *            时间到时触发的 PendingIntent
	 */
	public static void setAlarmTime(Context context, Calendar setTime,
			PendingIntent pendingIntent) {

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, setTime.getTimeInMillis(),
				pendingIntent);
	}

	/**
	 * 设置时间到闹钟管理器（周期触发）
	 * 
	 * @param context
	 *            上下文
	 * @param setTime
	 *            设置的时间
	 * @param interval
	 *            周期
	 * @param pendingIntent
	 *            时间到时触发的 PendingIntent
	 */
	public static void setPeriodAlarmTime(Context context, Calendar setTime,
			long interval, PendingIntent pendingIntent) {

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, setTime.getTimeInMillis(),
				interval, pendingIntent);
	}

	/**
	 * 以秒为单位转换成 X时X分X秒的字符串
	 * 
	 * @param second
	 *            需要转换的秒数
	 * @return 转换后的字符串
	 */
	public static String turnTimeInSecs(int second) {
		int h = 0;
		int d = 0;
		int s = 0;
		int temp = second % 3600;
		if (second > 3600) {
			h = second / 3600;
			if (temp != 0) {
				if (temp > 60) {
					d = temp / 60;
					if (temp % 60 != 0) {
						s = temp % 60;
					}
				} else {
					s = temp;
				}
			}
		} else {
			d = second / 60;
			if (second % 60 != 0) {
				s = second % 60;
			}
		}

		return h + "时" + d + "分" + s + "秒";
	}

	/**
	 * 将 Calendar 类转换成 pattern 样式的字符串
	 * 
	 * @param cal
	 *            需要转换的 Calendar 类
	 * @param pattern
	 *            需要转换的样式，如:yyyy年MM月dd日 EE HH:mm:ss
	 * @return 指定样式的时间表示字符串
	 */
	public static String getTimeString(Calendar cal, String pattern) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		simpleDateFormat.setLenient(false);

		return simpleDateFormat.format(cal.getTime());
	}

	/**
	 * 返回传入的 Calendar 距现在有多长时间之后
	 * 
	 * @param cal
	 *            要计算的 Calendar，可以是当前时间或前后任意时间
	 * @param showMode
	 *            时间显示格式（1:只显示最大单位的时间；2:显示全部单位的时间）
	 * @return x天x小时x分x秒，如果x为0则不显示
	 */
	public static String getIntervalTimeString(Calendar cal, int showMode) {
		String tempStr = "", secondStr = "", minuteStr = "", hourStr = "", dayStr = "";
		String beforeAfter = "";

		Calendar now = Calendar.getInstance();
		long nowInMillis = now.getTimeInMillis();
		long calInMillis = cal.getTimeInMillis();

		long resultInMillis = calInMillis - nowInMillis;

		if (resultInMillis < 0) {
			resultInMillis = 0 - resultInMillis;
			beforeAfter = "前";
		} else if (resultInMillis > 0) {
			beforeAfter = "后";
		}
		long resultInSecs = resultInMillis / 1000;
		int second = (int) (resultInSecs % 60);
		int minute = (int) ((resultInSecs / 60) % 60);
		int hour = (int) (resultInSecs / (60 * 60) % 24);
		int day = (int) (resultInSecs / (60 * 60 * 24));

		if (TimeHelper.MODE_SHOW_LONG_TIME == showMode) {
			if (day != 0) {
				dayStr = day + "天";
			}
			if (hour != 0) {
				hourStr = hour + "小时";
			}
			if (minute != 0) {
				minuteStr = minute + "分";
			}
			if (second != 0) {
				secondStr = second + "秒";
			}
			tempStr = dayStr + hourStr + minuteStr + secondStr + beforeAfter;
		} else if (TimeHelper.MODE_SHOW_SHORT_TIME == showMode) {
			if (day != 0) {
				tempStr = day + "天" + beforeAfter;
			} else if (hour != 0) {
				tempStr = hour + "小时" + beforeAfter;
			} else if (minute != 0) {
				tempStr = minute + "分" + beforeAfter;
			} else if (second != 0) {
				tempStr = second + "秒" + beforeAfter;
			}
		}
		if ("" == tempStr) {
			// 如果 tempStr 为空说明传入的时间和当前时间相等
			tempStr = "1分钟内";
		}

		return tempStr;
	}

	/**
	 * 将 repeat 代表的重复星期数转换成 int 数组，1-7分表代表星期日到星期六
	 * 
	 * @param repeat
	 *            存储重复星期的数据：星期一、三、五用二进制表示为0010 1010即42
	 * @return repeat 代表的重复星期数转换成 int 数组
	 */
	public static int[] parseRepeat(int repeat) {

		int[] tempArray = new int[7];
		int index = 0;
		int tempInt = 1;
		if ((repeat & tempInt) == tempInt) {
			tempArray[index++] = 1;
		}
		tempInt <<= 1;
		if ((repeat & tempInt) == tempInt) {
			tempArray[index++] = 2;
		}
		tempInt <<= 1;
		if ((repeat & tempInt) == tempInt) {
			tempArray[index++] = 3;
		}
		tempInt <<= 1;
		if ((repeat & tempInt) == tempInt) {
			tempArray[index++] = 4;
		}
		tempInt <<= 1;
		if ((repeat & tempInt) == tempInt) {
			tempArray[index++] = 5;
		}
		tempInt <<= 1;
		if ((repeat & tempInt) == tempInt) {
			tempArray[index++] = 6;
		}
		tempInt <<= 1;
		if ((repeat & tempInt) == tempInt) {
			tempArray[index++] = 7;
		}
		int[] weekArray = new int[index];
		for (int i = 0; i < weekArray.length; i++) {
			weekArray[i] = tempArray[i];
		}
		return weekArray;
	}

	/**
	 * 获取重复响铃大于当前时间最近的一个 Calendar
	 * 
	 * @param calendar
	 *            数据库中的 Calendar
	 * @param repeat
	 *            存储重复星期的数据：如果闹钟类型为0，则星期一、三、五用二进制表示为0010
	 *            1010即42；如果闹钟类型为1，则1-7分别对应星期日到星期六
	 * @return 重复响铃大于当前时间最近的一个 Calendar
	 */
	public static Calendar getLatestRepeatTime(Calendar calendar, int repeat) {

		Calendar now = Calendar.getInstance();
		Calendar tempCalendar = null;

		int weekArray[] = parseRepeat(repeat);

		/* 获取当天时分秒的 Calendar */
		tempCalendar = getTimeAt(now.get(Calendar.YEAR),
				now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		int tempWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);// 当前星期数
		int addDay = 0;// 下次时间需要添加几天
		boolean inRepeat = false;// 当前星期数是否在重复星期中
		boolean hasBigRepeat = false;// 重复星期中是否有大于当前星期的值
		for (int week : weekArray) {
			if (week - tempWeek == 0) {
				inRepeat = true;
				break;
			}
		}
		if (inRepeat) {// 当前星期数在重复星期中，如果大于当前时间则返回，否则判断重复星期中是否有大于当前星期的值，再分别计算要添加的天数
			if (tempCalendar.getTimeInMillis() >= now.getTimeInMillis()) {
				return tempCalendar;
			} else {
				for (int week : weekArray) {
					if (week - tempWeek > 0) {
						addDay = week - tempWeek;
						hasBigRepeat = true;
						break;
					}
				}
				if (!hasBigRepeat) {
					addDay = weekArray[0] - tempWeek + 7;
				}
			}
		} else {// 当前星期数不在重复星期中，判断重复星期中是否有大于当前星期的值，再分别计算要添加的天数
			for (int week : weekArray) {
				if (week - tempWeek > 0) {
					addDay = week - tempWeek;
					hasBigRepeat = true;
					break;
				}
			}
			if (!hasBigRepeat) {
				addDay = weekArray[0] - tempWeek + 7;
			}
		}
		tempCalendar.add(Calendar.DAY_OF_WEEK, addDay);

		return tempCalendar;
	}

	/**
	 * 根据传入的数据得到星期的字符串
	 * 
	 * @param repeat
	 *            存储重复星期的数据：如果为零说明闹钟类型是响铃一次；如果不为零则为重复响铃，星期一、三、五用二进制表示为0010
	 *            1010即42
	 * @return 和 repeat 相关的字符串
	 */
	public static String getRepeatString(int repeat) {
		String tempStr = "";

		if (0 == repeat) {
			tempStr = "响铃一次";
		} else {
			int tempInt = 1;
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "周一 ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "周二 ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "周三 ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "周四 ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "周五 ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "周六 ";
			}
			tempInt = 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "周日 ";
			}
			if (repeat == 127) {
				tempStr = "每天";
			}
		}
		return tempStr;
	}

	/**
	 * 根据传入的数据得到星期的字符串
	 * 
	 * @param repeat
	 *            存储重复星期的数据：如果为零说明闹钟类型是响铃一次；如果不为零则为重复响铃，星期一、三、五用二进制表示为0010
	 *            1010即42
	 * @return 和 repeat 相关的布尔数组
	 */
	public static boolean[] getRepeatBoolean(int repeat) {

		boolean[] bData = new boolean[8];

		if (0 == repeat) {
		} else {
			int tempInt = 1;
			if ((tempInt & repeat) != 0) {
				bData[0] = true;
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				bData[1] = true;
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				bData[2] = true;
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				bData[3] = true;
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				bData[4] = true;
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				bData[5] = true;
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				bData[6] = true;
			}
			tempInt = 1;
			if ((tempInt & repeat) != 0) {
				bData[7] = true;
			}
		}

		return bData;
	}
}
