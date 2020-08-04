package com.jiangfucheng.im.chatserver.config;

import com.jiangfucheng.im.chatserver.chat.ChatServer;
import com.jiangfucheng.im.chatserver.chat.ChatServerContext;
import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.chatserver.config.properties.ChatServerProperties;
import com.jiangfucheng.im.chatserver.handler.ChatServerHandler;
import com.jiangfucheng.im.chatserver.service.UserService;
import com.jiangfucheng.im.common.chat.ChatMessageDispatcher;
import com.jiangfucheng.im.common.chat.OnlineMessageConsumer;
import org.I0Itec.zkclient.ZkClient;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/20
 * Time: 23:19
 *
 * @author jiangfucheng
 */
@Configuration
public class ChatConfig {
	private ChatServerProperties chatServerProperties;

	@Value("${chat.message.retry}")
	private int messageRetry;
	@Value("${chat.message.retry-delay-time}")
	private long retryDelayTime;

	public ChatConfig(ChatServerProperties chatServerProperties) {
		this.chatServerProperties = chatServerProperties;
	}


	@Bean
	public ChatServerHandler chatServerHandler(ChatMessageDispatcher messageDispatcher,
											   ChatServerContext context,
											   UserService userService) {
		return new ChatServerHandler(messageDispatcher, context, userService);
	}

	@Bean
	public ChatServer chatServer(ChatServerHandler chatServerHandler, ZkClient zkClient) {
		return new ChatServer(chatServerHandler, chatServerProperties.getPort(), zkClient);
	}

	@Bean
	public ChatMessageDispatcher chatMessageDispatcher() {
		return new ChatMessageDispatcher();
	}


	@Bean
	public CommonMessageSender commonMessageSender(MQProducer producer,
												   RedisTemplate<String, Object> redisTemplate,
												   OnlineMessageConsumer onlineMessageConsumer,
												   ChatServerContext context) {
		return new CommonMessageSender(producer, redisTemplate, onlineMessageConsumer, messageRetry, retryDelayTime, context);
	}


}
