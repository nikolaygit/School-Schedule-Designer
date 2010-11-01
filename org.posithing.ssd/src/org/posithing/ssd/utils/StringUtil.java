package org.posithing.ssd.utils;

public class StringUtil {
	public static final String EMPTY = "";
	public static final String COMMA = ",";
	public static final String NEWLINE = "\n";
	public static final String SPACE = " ";
	public static final String EXCALMATION = "!";
	public static final String AT = "@";
	public static final String LINE = "/";
	public static final String LINE_HORIZONTAL = "-";
	public static final String OPENED_BRACKET_SQUARE = "[";
	public static final String OPENED_BRACKET_SQUARE_WITH_SPACE = " [";
	public static final String CLOSED_BRACKET_SQUARE = "]";
	public static final String OPENED_BRACKET_CURLY = "{";
	public static final String OPENED_BRACKET_CURLY_WITH_SPACE = " {";
	public static final String CLOSED_BRACKET_CURLY = "}";

	public static boolean isEmpty(String string) {
		return (string == null || string.trim().length() == 0 ? true : false);
	}
}
