package com.jiangfucheng.im.chatserver.handler;

import com.jiangfucheng.im.common.chat.ChatMessageDispatcher;
import com.jiangfucheng.im.common.chat.OnlineMessageConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 0:41
 * 处理rocketMq转发过来的消息，并交由{@link com.jiangfucheng.im.common.chat.ChatMessageDispatcher} 去分发给相应的控制器
 *
 * @author jiangfucheng
 */
@Slf4j
public class MqMessageHandler {
	private ChatMessageDispatcher messageDispatcher;
	private OnlineMessageConsumer onlineMessageConsumer;

	public MqMessageHandler(ChatMessageDispatcher messageDispatcher, OnlineMessageConsumer onlineMessageConsumer) {
		this.messageDispatcher = messageDispatcher;
		this.onlineMessageConsumer = onlineMessageConsumer;
		init();
	}

	private void init() {
		onlineMessageConsumer.addConsumeListener(msg -> messageDispatcher.dispatch(null, msg), null);
	}
}
