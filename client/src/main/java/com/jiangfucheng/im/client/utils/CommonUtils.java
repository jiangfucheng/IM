package com.jiangfucheng.im.client.utils;

import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 16:31
 *
 * @author jiangfucheng
 */
public class CommonUtils {

	/**
	 * 查找元素在列表中的插入位置
	 */
	public static <T> int searchInsertIndex(List<T> list, T key, Comparator<T> comparator) {
		int l = 0, r = list.size();
		while (l < r) {
			int mid = l + r >> 1;
			if (comparator.compare(list.get(mid), key) > 0) {
				r = mid;
			} else {
				l = mid + 1;
			}
		}
		return l;
	}

}
