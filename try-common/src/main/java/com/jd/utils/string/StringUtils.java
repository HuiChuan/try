package com.jd.utils.string;

public class StringUtils {

	public static boolean isBlank(String str) {
		int strLen;
		if (str != null && (strLen = str.length()) != 0) {
			for(int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(str.charAt(i))) {
					return false;
				}
			}
			return true;
		} else {
			return true;
		}
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	}

	public static boolean areNotEmpty(String[] values) {
		   boolean result = true;
		   if ((values == null) || (values.length == 0))
		       result = false;
		   else {
		       for (String value : values) {
		           result &= !isEmpty(value);
		       }
		   }
		   return result;
		}
		
	public static boolean isEmpty(String value) {
		int strLen;
		if ((value == null) || ((strLen = value.length()) == 0))
			return true;
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String join(Object[] objects, String separator) {
		if (objects == null){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Object obj : objects){
			if (sb.length() > 0){
				sb.append(separator);
			}
			sb.append(obj);
		}
		return sb.toString();
	}
}