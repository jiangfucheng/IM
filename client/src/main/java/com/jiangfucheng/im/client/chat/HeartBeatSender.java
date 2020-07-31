package com.jiangfucheng.im.client.chat;

import com.jiangfucheng.im.client.config.properties.HeartBeatProperties;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 21:06
 *
 * @author jiangfucheng
 */
public class HeartBeatSender {
	private ScheduledExecutorService scheduledExecutorService;
	private HeartBeatProperties heartBeatProperties;

	public HeartBeatSender(ScheduledExecutorService scheduledExecutorService,
						   HeartBeatProperties heartBeatProperties) {
		this.scheduledExecutorService = scheduledExecutorService;
		this.heartBeatProperties = heartBeatProperties;
	}

	public void start() {
		//scheduledExecutorService.schedule()
	}
}
