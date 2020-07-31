package com.jiangfucheng.im.chatserver.handler;

import com.jiangfucheng.im.common.chat.ChatMessageDispatcher;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/20
 * Time: 22:43
 *
 * @author jiangfucheng
 */
@ChannelHandler.Sharable
public class ChatServerHandler extends SimpleChannelInboundHandler<Base.Message> {

	private ChatMessageDispatcher messageDispatcher;


	public ChatServerHandler(ChatMessageDispatcher messageDispatcher, RedisTemplate redisTemplate, Integer port) {
		this.messageDispatcher = messageDispatcher;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Base.Message msg) throws Exception {
		messageDispatcher.dispatch(ctx, msg);
	}

	/*@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
			switch (idleStateEvent.state()) {
				case WRITER_IDLE: {
					break;
				}
				case READER_IDLE: {
					break;
				}
				case ALL_IDLE: {
					break;
				}
			}
		}
	}*/
}
