package com.jiangfucheng.im.common.resp;

import com.jiangfucheng.im.common.constants.ResponseCode;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 16:02
 *
 * @author jiangfucheng
 */
@Data

public class Resp<T> {
	private Integer code;
	private String msg;
	private String errMsg;
	private T data;

	private Resp() {
	}

	private Resp(int code, String msg, String errMsg, T data) {
		this.code = code;
		this.msg = msg;
		this.errMsg = errMsg;
		this.data = data;
	}

	public static Resp ok() {
		return ok(ResponseCode.OK_MSG, null);
	}

	public static <T> Resp<T> ok(T data) {
		return ok(ResponseCode.OK_MSG, data);
	}

	public static <T> Resp<T> ok(String msg, T data) {
		return new Resp<>(ResponseCode.OK, msg, msg, data);
	}

	public static <T> Resp<T> error(int code, String msg, String errMsg, T data) {
		return new Resp<>(code, msg, errMsg, data);
	}

	public static <T> Resp<T> error(int code, String errMsg, T data) {
		return error(code, errMsg, errMsg, data);
	}

	public static <T> Resp<T> error(int code, String errMsg) {
		return error(code, errMsg, errMsg, null);
	}

	public static <T> Resp<T> error() {
		return error(ResponseCode.UNKNOWN_ERROR, ResponseCode.UNKNOWN_DEFAULT_MSG, ResponseCode.UNKNOWN_DEFAULT_MSG, null);
	}


}
