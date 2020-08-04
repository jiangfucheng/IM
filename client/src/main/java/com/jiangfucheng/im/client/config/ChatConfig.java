package com.jiangfucheng.im.client.config;

import com.jiangfucheng.im.client.chat.ChatClient;
import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.feign.ChatServerFeignClient;
import com.jiangfucheng.im.client.handler.ChatClientHandler;
import com.jiangfucheng.im.common.chat.ChatMessageDispatcher;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 21:42
 *
 * @author jiangfucheng
 */
@Configuration
public class ChatConfig {
	/**
	 * 消息处理器
	 */
	@Bean
	public ChatClientHandler chatClientHandler(ChatMessageDispatcher messageDispatcher,
											   ChatClientContext context,
											   SnowFlakeIdGenerator snowFlakeIdGenerator,
											   MessageMonitor messageMonitor) {
		return new ChatClientHandler(messageDispatcher, context, snowFlakeIdGenerator, messageMonitor);
	}

	/**
	 * 客户端
	 */
	@Bean
	public ChatClient chatClient(ChatClientContext context,
								 ChatServerFeignClient chatServerFeignClient) {
		return new ChatClient(context, chatServerFeignClient);
	}

	/**
	 * 消息分发器
	 */
	@Bean
	public ChatMessageDispatcher chatMessageDispatcher() {
		return new ChatMessageDispatcher();
	}

}
