package com.jiangfucheng.im.common.resp;

import com.jiangfucheng.im.common.constants.ErrorCode;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 16:02
 *
 * @author jiangfucheng
 */
@Data

public class Response<T> {
	private Integer code;
	private String msg;
	private String errMsg;
	private T data;

	private Response() {
	}

	private Response(int code, String msg, String errMsg, T data) {
		this.code = code;
		this.msg = msg;
		this.errMsg = errMsg;
		this.data = data;
	}

	public static Response ok() {
		return ok(ErrorCode.OK_MSG, null);
	}

	public static <T> Response<T> ok(T data) {
		return ok(ErrorCode.OK_MSG, data);
	}

	public static <T> Response<T> ok(String msg, T data) {
		return new Response<>(ErrorCode.OK, msg, msg, data);
	}

	public static <T> Response<T> error(int code, String msg, String errMsg, T data) {
		return new Response<>(code, msg, errMsg, data);
	}

	public static <T> Response<T> error(int code, String errMsg, T data) {
		return error(code, errMsg, errMsg, data);
	}

	public static <T> Response<T> error(int code, String errMsg) {
		return error(code, errMsg, errMsg, null);
	}

	public static <T> Response<T> error() {
		return error(ErrorCode.UNKNOWN_ERROR, ErrorCode.UNKNOWN_DEFAULT_MSG, ErrorCode.UNKNOWN_DEFAULT_MSG, null);
	}


}
