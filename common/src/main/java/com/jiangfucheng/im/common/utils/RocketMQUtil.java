package com.jiangfucheng.im.common.utils;

import com.alibaba.fastjson.JSON;
import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.exception.IMException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/23
 * Time: 22:39
 *
 * @author jiangfucheng
 */
@Slf4j
public class RocketMQUtil {
	public static void sendMessage(MQProducer producer, String topic, String tag, Object data) {
		Message message = new Message(topic, tag, JSON.toJSONBytes(data));
		send(producer, topic, tag, message);
	}

	public static void sendMessage(MQProducer producer, String topic, String tag, byte[] data) {
		Message message = new Message(topic, tag, data);
		send(producer, topic, tag, message);
	}

	private static void send(MQProducer producer, String topic, String tag, Message message) {
		try {
			producer.send(message, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
					SendStatus sendStatus = sendResult.getSendStatus();
					if (sendStatus == SendStatus.SEND_OK) {
						log.debug("发送消息到rocketMq: topic: {}, tag: {}", topic, tag);
					}else {
						log.error("发送消息失败");
					}
				}

				@Override
				public void onException(Throwable e) {
					throw new IMException(ErrorCode.MESSAGE_SEND_ERROR, "消息发送失败");
				}
			});
		} catch (MQClientException | RemotingException | InterruptedException e) {
			log.error("消息队列异常，消息发送失败");
			throw new IMException(ErrorCode.UNKNOWN_ERROR, "消息队列异常");
		}
	}
}
