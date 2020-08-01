package com.jiangfucheng.im.client.config;

import com.jiangfucheng.im.client.chat.HeartBeatSender;
import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.chat.SendHeartBeatTask;
import com.jiangfucheng.im.client.config.properties.HeartBeatProperties;
import com.jiangfucheng.im.client.config.properties.MessageProperties;
import com.jiangfucheng.im.client.context.ChatClientContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/30
 * Time: 9:42
 *
 * @author jiangfucheng
 */
@Configuration
public class TaskConfig {

	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		//TODO 优化为CPU * 2
		return new ScheduledThreadPoolExecutor(8);
	}

	@Bean
	public MessageMonitor messageMonitor(ScheduledExecutorService executorService,
										 ChatClientContext context,
										 MessageProperties messageProperties) {
		return new MessageMonitor(executorService, context, messageProperties);
	}

	@Bean
	public SendHeartBeatTask sendHeartBeatTask(ChatClientContext context,
											   MessageMonitor messageMonitor) {
		return new SendHeartBeatTask(context, messageMonitor);
	}

	@Bean
	public HeartBeatSender heartBeatSender(ScheduledExecutorService executorService,
										   HeartBeatProperties heartBeatProperties,
										   SendHeartBeatTask task) {
		return new HeartBeatSender(executorService, heartBeatProperties, task);
	}
}
