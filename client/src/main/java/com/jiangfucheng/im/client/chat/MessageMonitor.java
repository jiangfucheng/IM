package com.jiangfucheng.im.client.chat;

import com.jiangfucheng.im.client.config.properties.MessageProperties;
import com.jiangfucheng.im.client.context.ChatClientContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/30
 * Time: 14:17
 *
 * @author jiangfucheng
 */
@Slf4j
public class MessageMonitor {
	private ScheduledExecutorService executorService;
	private ChatClientContext context;
	private MessageProperties messageProperties;

	public MessageMonitor(ScheduledExecutorService executorService,
						  ChatClientContext context,
						  MessageProperties messageProperties) {
		this.executorService = executorService;
		this.context = context;
		this.messageProperties = messageProperties;
	}

	public enum Type {
		/**
		 * 失败监视器
		 */
		UN_ACK,
		/**
		 * 重发监视器
		 */
		UN_COMPLETE
	}

	public void watchMessage(Long messageId, Type type) {
		if (1 == 1) return;
		Runnable task;
		if (type == Type.UN_ACK) {
			ConcurrentHashMap<Long, Integer> unAckRetryTimes = context.getUnAckRetryTimes();
			Integer retry = unAckRetryTimes.get(messageId);
			if (retry != null && retry > messageProperties.getRetry()) {
				//todo 处理重试次数过多的情况
				unAckRetryTimes.remove(messageId);
				log.debug("未ack消息重试次数过多，发送失败 message: {}", messageId);
				return;
			}
			task = new ReSendUnAckMsgTask(context, messageId, this);
			executorService.schedule(task, messageProperties.getRetryDelayTime(), TimeUnit.MILLISECONDS);
		} else if (type == Type.UN_COMPLETE) {
			ConcurrentHashMap<Long, Integer> unCompleteRetryTimes = context.getUnCompleteRetryTimes();
			Integer retry = unCompleteRetryTimes.get(messageId);
			if (retry != null && retry > messageProperties.getFailed()) {
				//todo 通知用户消息发送失败
				unCompleteRetryTimes.remove(messageId);
				log.debug("消息重试次数过多 message: {}", messageId);
				return;
			}
			task = new ReSendUnCompleteMsgTask(context, messageId, this);
			executorService.schedule(task, messageProperties.getFailedDelayTime(), TimeUnit.MILLISECONDS);
		}
	}
}
