package com.jiangfucheng.im.common.constants;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/23
 * Time: 20:12
 *
 * @author jiangfucheng
 */
public class MQConstants {
	/*
		用来处理需要转发的在线消息
	 */
	public static final String ONLINE_MESSAGE_TOPIC = "MQ_ONLINE_MESSAGE";
	/*
		%s: serverIp:port
	 */
	public static final String ONGLINE_MESSAGE__TOPIC_TAG = "MQ_ONLINE_MESSAGE_TAG_%s";

	/*
		离线消息
	 */
	public static final String MESSAGE_OFFLINE_TOPIC = "MQ_OFFLINE_MESSAGE";

}
