package com.jiangfucheng.im.httpserver.enums;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/16
 * Time: 23:28
 *
 * @author jiangfucheng
 */
public enum AccountType {
	USER(0), GROUP(1);

	public int code;

	AccountType(int code) {
		this.code = code;
	}

}
