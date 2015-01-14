package comns.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * @����: TimeHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-1 ����11:45:58
 * 
 * @����: ��<code>TimeHelper</code>��ʱ������ڵİ�����</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class TimeHelper {

	/**
	 * getIntervalTimeString��������</p>ֻ��ʾ���λ��ʱ��
	 */
	public static final int MODE_SHOW_SHORT_TIME = 1;
	/**
	 * getIntervalTimeString��������</p>��ʾȫ����λ��ʱ��
	 */
	public static final int MODE_SHOW_LONG_TIME = 2;

	/**
	 * ��ȡ��ǰʱ��
	 * 
	 * @return ��ȡ��ǰ�� Calendar
	 */
	public static Calendar getCurrentTime() {

		Calendar now = Calendar.getInstance();

		return now;
	}

	/**
	 * ��ȡ��ǰʱ���������ʱ��
	 * 
	 * @param secs
	 *            ָ��������
	 * @return ��ȡ secs ���� Calendar
	 */
	public static Calendar getTimeAfterInSecs(int secs) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, secs);

		return cal;
	}

	/**
	 * ��ȡ���ڵ�ǰʱ�������һ����Сʱ�ͷ���ָ����ʱ�䣨���С�ڵ�ǰʱ����Ϊ�ڶ����ʱ�䣩
	 * 
	 * @param hour
	 *            ָ����Сʱ��
	 * @param minute
	 *            ָ���ķ�����
	 * @return ���ڵ�ǰʱ������һ����ָ���� Calendar
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

		if (calInMillis <= nowInMillis) { // ������õ�ʱ��С�ڻ���ڵ�ǰʱ�������óɵڶ���ĸ�ʱ��
			cal.add(Calendar.DATE, 1);
		}
		return cal;
	}

	/**
	 * ��ȡ���ڵ�ǰʱ�������һ����Сʱ�ͷ���ָ����ʱ�䣨���С�ڵ�ǰʱ����Ϊ��һ��ʱ�䣩
	 * 
	 * @param year
	 *            ָ�������
	 * @param month
	 *            ָ�����·�
	 * @param day
	 *            ָ������
	 * @return ���ڵ�ǰʱ������һ����ָ���� Calendar
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

		if (calInMillis <= nowInMillis) { // ������õ�ʱ��С�ڻ���ڵ�ǰʱ�������ó���һ��ĸ�ʱ��
			cal.set(Calendar.YEAR, now.get(Calendar.YEAR) + 1);
		}

		return cal;
	}

	/**
	 * ��ȡһ��ָ���� Calendar����ȷ����
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @param day
	 *            ��
	 * @return ָ����� Calendar
	 */
	public static Calendar getTimeAt(int year, int month, int day) {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day, 0, 0, 0);

		return cal;
	}

	/**
	 * ��ȡһ��ָ���� Calendar����ȷ������
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @param day
	 *            ��
	 * @param hour
	 *            Сʱ
	 * @param minute
	 *            ����
	 * @return ָ����� Calendar
	 */
	public static Calendar getTimeAt(int year, int month, int day, int hour,
			int minute) {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day, hour, minute, 0);

		return cal;
	}

	/**
	 * ��ȡһ��ָ���� Calendar����ȷ����
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @param day
	 *            ��
	 * @param hour
	 *            Сʱ
	 * @param minute
	 *            ����
	 * @param second
	 *            ��
	 * @return ָ����� Calendar
	 */
	public static Calendar getTimeAt(int year, int month, int day, int hour,
			int minute, int second) {

		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, day, hour, minute, second);

		return cal;
	}

	/**
	 * ��õ����ʱ��
	 * 
	 * @param hour
	 *            Сʱ
	 * @param minute
	 *            ����
	 * @param second
	 *            ��
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
	 * ��ȡ����ָ��ʱ����ڵ����ʱ��
	 * 
	 * @param beginH
	 *            ��ʼʱ�䣨����0��23֮����κ�������
	 * @param endH
	 *            ��ֹʱ�䣨����1��24֮����κ�������
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
	 * ��ȡ���ڵ�ǰʱ��ָ��ʱ�������������ʱ��
	 * 
	 * @param beginH
	 *            ��ʼʱ�䣨����0��23֮����κ�������
	 * @param endH
	 *            ��ֹʱ�䣨����1��24֮����κ�������
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
	 * ��ȡ��һ�Σ����죩ָ��ʱ����ڵ����ʱ��
	 * 
	 * @param beginH
	 *            ��ʼʱ�䣨����0��23֮����κ�������
	 * @param endH
	 *            ��ֹʱ�䣨����1��24֮����κ�������
	 * @return
	 */
	public static Calendar getNextRandomTimeBetween(int beginH, int endH) {

		Calendar calendar = getRandomTimeBetween(beginH, endH);
		calendar.add(Calendar.DATE, 1);

		return calendar;
	}

	/**
	 * �Ƿ����ڵ���ʱ��
	 * 
	 * @param timeInMillis
	 *            ʱ���
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
	 * �Ƿ����ڵ���ʱ��
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
	 * ����ʱ�䵽���ӹ����������δ�����
	 * 
	 * @param context
	 *            ������
	 * @param setTime
	 *            ���õ�ʱ��
	 * @param pendingIntent
	 *            ʱ�䵽ʱ������ PendingIntent
	 */
	public static void setAlarmTime(Context context, Calendar setTime,
			PendingIntent pendingIntent) {

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, setTime.getTimeInMillis(),
				pendingIntent);
	}

	/**
	 * ����ʱ�䵽���ӹ����������ڴ�����
	 * 
	 * @param context
	 *            ������
	 * @param setTime
	 *            ���õ�ʱ��
	 * @param interval
	 *            ����
	 * @param pendingIntent
	 *            ʱ�䵽ʱ������ PendingIntent
	 */
	public static void setPeriodAlarmTime(Context context, Calendar setTime,
			long interval, PendingIntent pendingIntent) {

		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, setTime.getTimeInMillis(),
				interval, pendingIntent);
	}

	/**
	 * ����Ϊ��λת���� XʱX��X����ַ���
	 * 
	 * @param second
	 *            ��Ҫת��������
	 * @return ת������ַ���
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

		return h + "ʱ" + d + "��" + s + "��";
	}

	/**
	 * �� Calendar ��ת���� pattern ��ʽ���ַ���
	 * 
	 * @param cal
	 *            ��Ҫת���� Calendar ��
	 * @param pattern
	 *            ��Ҫת������ʽ����:yyyy��MM��dd�� EE HH:mm:ss
	 * @return ָ����ʽ��ʱ���ʾ�ַ���
	 */
	public static String getTimeString(Calendar cal, String pattern) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		simpleDateFormat.setLenient(false);

		return simpleDateFormat.format(cal.getTime());
	}

	/**
	 * ���ش���� Calendar �������ж೤ʱ��֮��
	 * 
	 * @param cal
	 *            Ҫ����� Calendar�������ǵ�ǰʱ���ǰ������ʱ��
	 * @param showMode
	 *            ʱ����ʾ��ʽ��1:ֻ��ʾ���λ��ʱ�䣻2:��ʾȫ����λ��ʱ�䣩
	 * @return x��xСʱx��x�룬���xΪ0����ʾ
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
			beforeAfter = "ǰ";
		} else if (resultInMillis > 0) {
			beforeAfter = "��";
		}
		long resultInSecs = resultInMillis / 1000;
		int second = (int) (resultInSecs % 60);
		int minute = (int) ((resultInSecs / 60) % 60);
		int hour = (int) (resultInSecs / (60 * 60) % 24);
		int day = (int) (resultInSecs / (60 * 60 * 24));

		if (TimeHelper.MODE_SHOW_LONG_TIME == showMode) {
			if (day != 0) {
				dayStr = day + "��";
			}
			if (hour != 0) {
				hourStr = hour + "Сʱ";
			}
			if (minute != 0) {
				minuteStr = minute + "��";
			}
			if (second != 0) {
				secondStr = second + "��";
			}
			tempStr = dayStr + hourStr + minuteStr + secondStr + beforeAfter;
		} else if (TimeHelper.MODE_SHOW_SHORT_TIME == showMode) {
			if (day != 0) {
				tempStr = day + "��" + beforeAfter;
			} else if (hour != 0) {
				tempStr = hour + "Сʱ" + beforeAfter;
			} else if (minute != 0) {
				tempStr = minute + "��" + beforeAfter;
			} else if (second != 0) {
				tempStr = second + "��" + beforeAfter;
			}
		}
		if ("" == tempStr) {
			// ��� tempStr Ϊ��˵�������ʱ��͵�ǰʱ�����
			tempStr = "1������";
		}

		return tempStr;
	}

	/**
	 * �� repeat ������ظ�������ת���� int ���飬1-7�ֱ���������յ�������
	 * 
	 * @param repeat
	 *            �洢�ظ����ڵ����ݣ�����һ���������ö����Ʊ�ʾΪ0010 1010��42
	 * @return repeat ������ظ�������ת���� int ����
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
	 * ��ȡ�ظ�������ڵ�ǰʱ�������һ�� Calendar
	 * 
	 * @param calendar
	 *            ���ݿ��е� Calendar
	 * @param repeat
	 *            �洢�ظ����ڵ����ݣ������������Ϊ0��������һ���������ö����Ʊ�ʾΪ0010
	 *            1010��42�������������Ϊ1����1-7�ֱ��Ӧ�����յ�������
	 * @return �ظ�������ڵ�ǰʱ�������һ�� Calendar
	 */
	public static Calendar getLatestRepeatTime(Calendar calendar, int repeat) {

		Calendar now = Calendar.getInstance();
		Calendar tempCalendar = null;

		int weekArray[] = parseRepeat(repeat);

		/* ��ȡ����ʱ����� Calendar */
		tempCalendar = getTimeAt(now.get(Calendar.YEAR),
				now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		int tempWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);// ��ǰ������
		int addDay = 0;// �´�ʱ����Ҫ��Ӽ���
		boolean inRepeat = false;// ��ǰ�������Ƿ����ظ�������
		boolean hasBigRepeat = false;// �ظ��������Ƿ��д��ڵ�ǰ���ڵ�ֵ
		for (int week : weekArray) {
			if (week - tempWeek == 0) {
				inRepeat = true;
				break;
			}
		}
		if (inRepeat) {// ��ǰ���������ظ������У�������ڵ�ǰʱ���򷵻أ������ж��ظ��������Ƿ��д��ڵ�ǰ���ڵ�ֵ���ٷֱ����Ҫ��ӵ�����
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
		} else {// ��ǰ�����������ظ������У��ж��ظ��������Ƿ��д��ڵ�ǰ���ڵ�ֵ���ٷֱ����Ҫ��ӵ�����
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
	 * ���ݴ�������ݵõ����ڵ��ַ���
	 * 
	 * @param repeat
	 *            �洢�ظ����ڵ����ݣ����Ϊ��˵����������������һ�Σ������Ϊ����Ϊ�ظ����壬����һ���������ö����Ʊ�ʾΪ0010
	 *            1010��42
	 * @return �� repeat ��ص��ַ���
	 */
	public static String getRepeatString(int repeat) {
		String tempStr = "";

		if (0 == repeat) {
			tempStr = "����һ��";
		} else {
			int tempInt = 1;
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "��һ ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "�ܶ� ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "���� ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "���� ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "���� ";
			}
			tempInt <<= 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "���� ";
			}
			tempInt = 1;
			if ((tempInt & repeat) != 0) {
				tempStr += "���� ";
			}
			if (repeat == 127) {
				tempStr = "ÿ��";
			}
		}
		return tempStr;
	}

	/**
	 * ���ݴ�������ݵõ����ڵ��ַ���
	 * 
	 * @param repeat
	 *            �洢�ظ����ڵ����ݣ����Ϊ��˵����������������һ�Σ������Ϊ����Ϊ�ظ����壬����һ���������ö����Ʊ�ʾΪ0010
	 *            1010��42
	 * @return �� repeat ��صĲ�������
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
