package com.jiangfucheng.im.common.util;

import com.jiangfucheng.im.common.constants.ResponseCode;
import com.jiangfucheng.im.common.exception.IllegalParamException;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/12
 * Time: 22:50
 *
 * @author jiangfucheng
 */
@SuppressWarnings("unchecked")
public class ParamValidator {
	private ParamValidator() {
	}

	public static void notNull(Object obj, String errMsg) {
		if (null == obj) {
			throw new IllegalParamException(ResponseCode.PARAMTER_ERROR, errMsg);
		}
	}

	public static void notBlank(String str, String errMsg) {
		if (null == str || "".equals(str)) {
			throw new IllegalParamException(ResponseCode.PARAMTER_ERROR, errMsg);
		}
	}


}
