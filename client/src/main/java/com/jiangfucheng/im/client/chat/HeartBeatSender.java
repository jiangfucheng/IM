package com.jiangfucheng.im.client.chat;

import com.jiangfucheng.im.client.config.properties.HeartBeatProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 21:06
 *
 * @author jiangfucheng
 */
@Slf4j
public class HeartBeatSender {
	private ScheduledExecutorService scheduledExecutorService;
	private HeartBeatProperties heartBeatProperties;
	private SendHeartBeatTask task;

	public HeartBeatSender(ScheduledExecutorService scheduledExecutorService,
						   HeartBeatProperties heartBeatProperties,
						   SendHeartBeatTask task) {
		this.scheduledExecutorService = scheduledExecutorService;
		this.heartBeatProperties = heartBeatProperties;
		this.task = task;
	}

	public void start() {
		scheduledExecutorService.scheduleAtFixedRate(task,
				heartBeatProperties.getPeriod(),
				heartBeatProperties.getPeriod(),
				TimeUnit.MILLISECONDS);
		log.info("heart beat sender started");
		//TODO 在客户端没有和服务器通信后的一个周期再发送心跳包，减少流量浪费
	}
}
