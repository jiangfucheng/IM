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
	VIDEO(3),
	FILE(4);
	private int value;

	MessageType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static MessageType valueOf(int value) {
		switch (value) {
			case 0:
				return TEXT;
			case 1:
				return IMAGE;
			case 2:
				return AUDIO;
			case 3:
				return VIDEO;
			case 4:
				return FILE;
			default:
				throw new IllegalArgumentException("非法的类型");
		}
	}
}
