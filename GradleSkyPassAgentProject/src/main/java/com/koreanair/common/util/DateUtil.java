package com.koreanair.common.util;

import java.util.Calendar;

public class DateUtil {
	public static String dateDiff(Calendar before, Calendar after) {
		int diffHour = after.get(Calendar.HOUR_OF_DAY) - before.get(Calendar.HOUR_OF_DAY);
		int diffMinute = after.get(Calendar.MINUTE) - before.get(Calendar.MINUTE);
		int diffSecond = after.get(Calendar.SECOND) - before.get(Calendar.SECOND);
		String diff = String.format("[%-16s][%d]Hor [%d]Min [%d]Sec" , "처리시간", diffHour, diffMinute, diffSecond);
		return diff;
	}
}
