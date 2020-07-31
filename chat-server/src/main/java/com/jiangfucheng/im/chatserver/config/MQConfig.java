package com.jiangfucheng.im.chatserver.config;

import com.jiangfucheng.im.chatserver.handler.MqMessageHandler;
import com.jiangfucheng.im.common.chat.ChatMessageDispatcher;
import com.jiangfucheng.im.common.chat.OnlineMessageConsumer;
import com.jiangfucheng.im.common.constants.MQConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 22:12
 *
 * @author jiangfucheng
 */
@Configuration
@Slf4j
public class MQConfig {

	private RocketMqProperties mqProperties;
	private static final String MQ_PRODUCER_GROUP = "CHAT_SERVER_PRODUCER_GROUP";
	private static final String MQ_CONSUMER_GROUP = "CHAT_SERVER_CONSUMER_GROUP_%s";
	@Value("${chat.chat-server.port}")
	private int port;
	private String localIp;


	public MQConfig(RocketMqProperties mqProperties) {
		this.mqProperties = mqProperties;
		try {
			this.localIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Bean
	public MQProducer producer() {
		DefaultMQProducer producer = new DefaultMQProducer(MQ_PRODUCER_GROUP);
		producer.setNamesrvAddr(mqProperties.getNameServerAddr());
		try {
			producer.start();
			log.info("RocketMQ producer启动成功");
		} catch (MQClientException e) {
			log.warn(e.getErrorMessage());
			log.error("RocketMQ producer启动失败");
		}
		return producer;
	}

	@Bean
	public DefaultMQPushConsumer pushConsumer() {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(String.format(MQ_CONSUMER_GROUP, generateServerUrlStr()));
		consumer.setNamesrvAddr(mqProperties.getNameServerAddr());
		try {
			String tag = generateServerUrl();
			consumer.subscribe(MQConstants.ONLINE_MESSAGE_TOPIC, tag);
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		return consumer;
	}

	private String generateServerUrl() {
		return localIp + ":" + port;
	}

	private String generateServerUrlStr() {
		return localIp.replaceAll("\\.", "_") + "-" + this.port;
	}

	@Bean
	public OnlineMessageConsumer onlineMessageConsumer(DefaultMQPushConsumer pushConsumer) {
		return new OnlineMessageConsumer(pushConsumer);
	}

	@Bean
	public MqMessageHandler mqMessageHandler(ChatMessageDispatcher messageDispatcher,
											 OnlineMessageConsumer onlineMessageConsumer) {
		return new MqMessageHandler(messageDispatcher, onlineMessageConsumer);
	}
}
