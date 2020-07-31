package com.jiangfucheng.im.chatserver.utils;

import com.jiangfucheng.im.common.constants.ZookeeperConstants;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/24
 * Time: 10:21
 *
 * @author jiangfucheng
 */
public class CommonUtils {
	public static String generateZookeeperUrlPath(String ip, Integer port) {
		return ZookeeperConstants.CHAT_SERVER_HOST_PATH + "/" + ip + ":" + port;
	}
}
