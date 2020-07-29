package com.jiangfucheng.im.common.constants;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 22:52
 *
 * @author jiangfucheng
 */
public class RedisConstants {
	/**
	 * 用户状态
	 * %s(00): 用户id
	 * value: {@link com.jiangfucheng.im.model.bo.UserStatusBo}
	 */
	public static final String USER_STATUS_KEY = "USER_STATUS_KEY_%s";

	/**
	 * chat server客户端连接数
	 * %s(00): chat server地址(ip:port)
	 */
	public static final String CHAT_SERVER_CONNECTED_NUMBER = "CHAT_SERVER_CONNECTED_NUMBER_%s";

	/**
	 * 用户连接在哪一台聊天服务器上
	 * %s: userId
	 */
	public static final String USER_CONNECTED_CHAT_SERVER = "USER_CONNECTED_CONNECTED_CHAT_SERVER_%s";
}
