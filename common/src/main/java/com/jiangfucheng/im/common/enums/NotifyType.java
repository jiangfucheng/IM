package com.jiangfucheng.im.common.enums;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 23:55
 *
 * @author jiangfucheng
 */
public enum NotifyType {
	REQUEST(0), NOTIFY(1);
	private int code;

	NotifyType(int code) {
		this.code = code;
	}

	public int code() {
		return this.code;
	}
}
