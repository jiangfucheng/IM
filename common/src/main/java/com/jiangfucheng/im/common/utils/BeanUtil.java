package com.jiangfucheng.im.common.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 0:00
 *
 * @author jiangfucheng
 */

public class BeanUtil {

	public static void copyProperties(Object dest, Object orig) {
		try {
			BeanUtils.copyProperties(dest,orig);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
