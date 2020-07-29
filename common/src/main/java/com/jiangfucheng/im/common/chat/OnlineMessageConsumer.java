package com.jiangfucheng.im.common.chat;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jiangfucheng.im.protobuf.Base;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/23
 * Time: 23:02
 * 用于接收其他chat-server转发的消息
 *
 * @author jiangfucheng
 */
@Slf4j
public final class OnlineMessageConsumer {
	private DefaultMQPushConsumer consumer;
	/**
	 * key:msgId
	 */
	private ConcurrentHashMap<Long, ConsumeMessageListener> listenerMap;
	/**
	 * 监听所有msgId的listener
	 */
	private final ArrayList<ConsumeMessageListener> globalListeners;

	public OnlineMessageConsumer(DefaultMQPushConsumer consumer) {
		this.consumer = consumer;
		this.listenerMap = new ConcurrentHashMap<>();
		this.globalListeners = new ArrayList<>();
		start();
	}

	private void start() {
		consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
			System.out.println("received message =========================");
			msgs.forEach(mqMsg -> {
				byte[] data = mqMsg.getBody();
				try {
					Base.Message imMsg = Base.Message.parseFrom(data);
					ConsumeMessageListener listener = listenerMap.get(imMsg.getId());
					if (listener != null) {
						listener.consume(imMsg);
					}
					//通知所有全局监听器
					globalListeners.forEach(globalListener -> globalListener.consume(imMsg));
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}

			});
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		});
		try {
			consumer.start();
			log.info("RocketMQ consumer 启动成功");
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 消息接收监听器
	 *
	 * @param listener 监听器
	 * @param msgId    表示监听哪个消息，如果为null则表示监听所有消息
	 */
	public void addConsumeListener(ConsumeMessageListener listener, Long msgId) {
		if (msgId != null) {
			listenerMap.putIfAbsent(msgId, listener);
		} else {
			synchronized (globalListeners) {
				globalListeners.add(listener);
			}
		}

	}
}
