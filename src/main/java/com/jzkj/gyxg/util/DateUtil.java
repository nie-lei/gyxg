package com.jzkj.gyxg.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.abs;

public final class DateUtil {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger("errorLogger");


	private static final Calendar calendar = Calendar.getInstance();
	/**
	 * convert Date to String
	 *
	 * @param d
	 * @return
	 */
	public static String format(Date d) {
		return format(d, null);
	}

	/**
	 * convert Date to String
	 *
	 * @param d
	 * @param pattern
	 * @return
	 */
	public static String format(Date d, String pattern) {
		if (d == null) {
			return "";
		}

		if (StringUtil.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		return sdf.format(d);
	}

	/**
	 * StringTODate
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static Date StringToDate(String time, String pattern) {
		if(time == null)
			return null;
		if(StringUtil.isEmail(pattern))
			pattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 当前日期增加多少小时
	 * @param hour
     * @return
     */
	public static Date hourAdd(int hour){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR, hour);
		return c.getTime();
	}

	/**
	 * 当前日期增加多少天
	 * @param day
     * @return
     */
	public static Date dayAdd(int day){
		Date date = new Date();
		return dayAdd(date,day);
	}

	public static Date dayAdd(Date date,int day){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		c.add(Calendar.DAY_OF_WEEK, day);
		return c.getTime();
	}


	public static Date yearAdd(Date date, int year) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);
		return c.getTime();
	}

	public static Date dateTimeAdd(int day){
		Date date = new Date();
		return dateTimeAdd(date,day);
	}

	public static Date dateTimeAdd(Date date,int day){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_WEEK, day);
		return c.getTime();
	}

	public static Date dateTimeSecondAdd(Date date,int second){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, second);
		return c.getTime();
	}

	public static Date toDate(String str){
		return toDate(str,"yyyy-MM-dd");
	}

	public static Date toDate(String str, String pattern){
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static int weekInYear(Date date){
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);

		if(date==null){
			c.setTime(new Date());
		}else{
			c.setTime(date);
		}

		return c.get(Calendar.WEEK_OF_YEAR);
	}

	public static int betweenMinute(String strD1, String strD2){
		Date d1 = toDate(strD1,"HH:mm");
		Date d2 = toDate(strD2,"HH:mm");
		return betweenMinute(d1,d2);
	}

	public static int betweenMinute(Date d1, Date d2){
		return abs((int)(d2.getTime() - d1.getTime())/1000/60);
	}

	public static Date addMinute(Date date, int minute){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE,minute);
		return c.getTime();
	}

	public static String getWeekName(Date date){
		if(date==null){
			return "";
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		switch (c.get(Calendar.DAY_OF_WEEK)){
			case 1:
				return "周日";
			case 2:
				return "周一";
			case 3:
				return "周二";
			case 4:
				return "周三";
			case 5:
				return "周四";
			case 6:
				return "周五";
			case 7:
				return "周六";
			default:
				return "错误";
		}
	}

	public static int timeToInt(String time){
		return StringUtil.toInt(time.toString().replace(":",""));
	}





	/**
	 * 计算天差
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getDayBad(Date date) throws ParseException {
		int days = getDays(date, 1000 * 60 * 60 * 24);
		return days;
	}

	/**
	 * 计算小时差
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getHoursBad(Date date) throws ParseException {
		int hours = getDays(date, 1000 * 60 * 60);
		return hours;
	}

	/**
	 * 计算分钟差
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static int getMinBad(Date date) throws ParseException {
		int days = getDays(date, 1000 * 60);
		return days;
	}

	private static int getDays(Date date, int i) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fromDate = simpleDateFormat.format(date);
		String toDate = simpleDateFormat.format(new Date());
		long from = simpleDateFormat.parse(fromDate).getTime();
		long to = simpleDateFormat.parse(toDate).getTime();
		return (int) ((to - from) / (i));
	}

	public static int getnowday(int year, int month, int day) {
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day += 31;
				break;
			case 2:
				if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
					day += 29;
				else
					day += 28;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				day += 30;
				break;
		}
		return day;
	}


	/**
	 * 过去一月
	 * @return
	 */
	public static String lastMonth(){
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -1);
		Date m = calendar.getTime();
		String datestr = format(m,"yyyy-MM-dd");
		return datestr;
	}

	/**
	 * 过去半年
	 * @return
	 */
	public static String lastHalfYear(){
		calendar.setTime(new Date());
		calendar.add(Calendar.MONTH, -6);
		Date m = calendar.getTime();
		String datestr = format(m,"yyyy-MM-dd");
		return datestr;
	}

	/**
	 * 过去一年
	 * @return
	 */
	public static String lastYear(){
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, -1);
		Date m = calendar.getTime();
		String datestr = format(m,"yyyy-MM-dd");
		return datestr;
	}

	/**
	 * 过去2年
	 * @return
	 */
	public static String last2Year(){
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, -2);
		Date m = calendar.getTime();
		String datestr = format(m,"yyyy-MM-dd");
		return datestr;
	}

	public static String getChangeHours(int min) {
		return min / 45 + "小时" + min % 45 + "分钟";
	}

	/**
	 * 根据时间获取今明后
	 * @param date
	 * @param type
	 * @return
	 */
	public static String getTimeDay(Date date, String format, int type) {
		if (date == null) {
			return "";
		}
		long today = toDate(format(new Date())).getTime();
		long yesterday = dayAdd(-1).getTime();
		long tomorrow = dayAdd(1).getTime();
		long acquired = dayAdd(2).getTime();
		long acquirednext = dayAdd(3).getTime();
		long dateTime = date.getTime();
		if (dateTime < yesterday) {
			return format(date, "yyyy-MM-dd HH:mm");
		} else if (dateTime < today) {
			return format(date, type == 0 ? (format + "（昨天）") : ("（昨天）" + format));
		} else if(dateTime < tomorrow) {
			return format(date, type == 0 ? format + "（今天）" : "（今天）" + format);
		} else if (dateTime < acquired) {
			return format(date, type == 0 ? format + "（明天）" : "（明天）" + format);
		} else if (dateTime < acquirednext) {
			return format(date, type == 0 ? format + "（后天）" : "（后天）" + format);
		} else {
			return format(date, "yyyy-MM-dd HH:mm");
		}
	}

	public static void main(String[] args) {
		System.out.println(DateUtil.format(yearAdd(new Date(), 1)));
	}

}