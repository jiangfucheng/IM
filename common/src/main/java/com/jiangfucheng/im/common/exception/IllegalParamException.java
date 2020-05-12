package com.jiangfucheng.im.common.exception;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/12
 * Time: 22:57
 *
 * @author jiangfucheng
 */
public class IllegalParamException extends RuntimeException {

	private int code;
	private String errMsg;

	public IllegalParamException(int code, String errMsg) {
		super(errMsg);
		this.code = code;
		this.errMsg = errMsg;
	}

}
