package com.jiangfucheng.im.client.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 16:32
 *
 * @author jiangfucheng
 */
public class CommonUtilsTest {

	@Test
	public void testSearchFirstIndex() {
		List<Integer> list = Arrays.asList(1, 3, 5, 7, 9);
		Comparator<Integer> comparator = Comparator.comparingInt(i -> i);
		Assert.assertEquals(0, CommonUtils.searchInsertIndex(list, 0, comparator));
		Assert.assertEquals(1, CommonUtils.searchInsertIndex(list, 2, comparator));
		Assert.assertEquals(5, CommonUtils.searchInsertIndex(list, 10, comparator));
	}
}
