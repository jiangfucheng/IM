package com.jiangfucheng.im.httpserver.exceptions;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 17:19
 *
 * @author jiangfucheng
 */
public class IMException extends RuntimeException {
	private int code;

	public IMException(int code, String msg) {
		super(msg);
		this.code = code;
	}
}
