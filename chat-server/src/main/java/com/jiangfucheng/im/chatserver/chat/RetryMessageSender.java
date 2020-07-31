package com.jiangfucheng.im.chatserver.chat;

import com.jiangfucheng.im.common.chat.OnlineMessageConsumer;
import com.jiangfucheng.im.protobuf.Base;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 0:26
 *
 * @author jiangfucheng
 */
@Slf4j
public abstract class RetryMessageSender {
	private OnlineMessageConsumer onlineMessageConsumer;

	public RetryMessageSender(OnlineMessageConsumer onlineMessageConsumer) {
		this.onlineMessageConsumer = onlineMessageConsumer;
	}

	public void sendRetryMessage(Base.Message msg, int retry, long delayTime) {
		CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
		AtomicBoolean receivedResponse = new AtomicBoolean(false);
		onlineMessageConsumer.addConsumeListener(response -> {
			receivedResponse.set(true);
			try {
				cyclicBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}, msg.getId());
		while (true) {
			try {
				sendMessage();
				cyclicBarrier.await(delayTime, TimeUnit.MILLISECONDS);
				if (receivedResponse.get()) {
					log.debug("可重试消息发送成功,msgId: {}", msg.getId());
					break;
				}
			} catch (Exception ex) {
				if (retry == 0) {
					log.error("消息发送失败msgId: {}", msg.getId());
					break;
				}
				retry--;
				log.warn("消息发送失败，正在重试,msgId: {}", msg.getId());
				cyclicBarrier.reset();
			}
		}
	}

	public abstract void sendMessage();
}
