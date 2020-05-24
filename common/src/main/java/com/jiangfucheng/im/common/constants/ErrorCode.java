package com.jiangfucheng.im.common.constants;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 16:16
 *
 * @author jiangfucheng
 */
public final class ErrorCode {
	public static final Integer OK = 0;
	public static final String OK_MSG = "ok";
	public static final Integer UNKNOWN_ERROR = -1;
	public static final String UNKNOWN_DEFAULT_MSG = "未知异常";
	public static final Integer PARAM_ERROR = -2;
	public static final String PARAMTER_ERROR_DEFAULT_MSG = "参数错误";
	public static final Integer UNAUTHENTICATED = -3;
	public static final String UNAUTHENTICATED_MSG = "身份未认证";
	public static final Integer UNAUTHORIZED = -3;
	public static final String UNAUTHORIZED_MSG = "没有权限";
	public static final Integer RUNTIME_ERROR = -4;
	public static final Integer RPC_ERROR = -5;
	public static final Integer USER_EXISTED = -6;
	public static final Integer MESSAGE_SEND_ERROR = -6;

	private ErrorCode() {
	}
}
