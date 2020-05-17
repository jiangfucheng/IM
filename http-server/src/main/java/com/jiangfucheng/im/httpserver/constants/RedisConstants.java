package com.jiangfucheng.im.httpserver.constants;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 20:48
 *
 * @author jiangfucheng
 */
public final class RedisConstants {
	private RedisConstants() {
	}

	/**
	 * 用户状态
	 * %s(00): 用户id
	 * value: {@link com.jiangfucheng.im.httpserver.bo.UserStatusBo}
	 */
	public static final String USER_STATUS_KEY = "USER_STATUS_KEY_%s";

	/**
	 * chat server客户端连接数
	 * %s(00): chat server地址(ip:port)
	 */
	public static final String CHAT_SERVER_CONNECTED_NUMBER = "CHAT_SERVER_CONNECTED_NUMBER_%s";


}
