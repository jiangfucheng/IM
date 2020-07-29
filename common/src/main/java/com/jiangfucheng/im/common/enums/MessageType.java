package com.jiangfucheng.im.common.enums;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/28
 * Time: 16:16
 *
 * @author jiangfucheng
 */
public enum MessageType {
	TEXT(0),
	IMAGE(1),
	AUDIO(2),
	VEDIO(3),
	FILE(4);
	private int value;

	MessageType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
}
