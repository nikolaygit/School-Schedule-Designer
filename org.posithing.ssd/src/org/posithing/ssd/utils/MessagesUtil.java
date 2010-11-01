package org.posithing.ssd.utils;

import org.posithing.ssd.constants.SSMConstants.DAY;

public class MessagesUtil {

	public static String getDayName(DAY day) {
		if (day == DAY.MONDAY) {
			return Messages.MessagesUtil_Monday;
		} else if (day == DAY.TUESDAY) {
			return Messages.MessagesUtil_Tuesday;
		} else if (day == DAY.WEDNESDAY) {
			return Messages.MessagesUtil_Wednesday;
		} else if (day == DAY.THURSDAY) {
			return Messages.MessagesUtil_Thursday;
		} else if (day == DAY.FRIDAY) {
			return Messages.MessagesUtil_Friday;
		} else {
			return Messages.MessagesUtil_Unknown_Day;
		}
	}
}
