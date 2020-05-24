package com.jiangfucheng.im.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 9:59
 *
 * @author jiangfucheng
 */
public class PinyinUtil {
	public static char getFirstLetterByFirstChar(String str) {
		char firstChar = str.charAt(0);
		char res;
		if (firstChar >= '一' && firstChar <= '龥') {
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(firstChar);
			res = pinyinArray[0].toUpperCase().charAt(0);
		} else if ((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z')) {
			res = Character.toUpperCase(firstChar);
		} else {
			res = '#';
		}

		return res;
	}

	public static int compareCharFirstLetter(char c1,char c2){
		if (c1 == '#') {
			return 1;
		} else if (c2 == '#') {
			return -1;
		}
		return c1 - c2;
	}


}
