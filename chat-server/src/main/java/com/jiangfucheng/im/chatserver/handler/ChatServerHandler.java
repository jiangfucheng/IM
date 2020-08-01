package com.jiangfucheng.im.chatserver.handler;

import com.jiangfucheng.im.chatserver.chat.ChatServerContext;
import com.jiangfucheng.im.common.chat.ChatMessageDispatcher;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/20
 * Time: 22:43
 *
 * @author jiangfucheng
 */
@ChannelHandler.Sharable
@Slf4j
public class ChatServerHandler extends SimpleChannelInboundHandler<Base.Message> {

	private ChatMessageDispatcher messageDispatcher;
	private ChatServerContext context;


	public ChatServerHandler(ChatMessageDispatcher messageDispatcher, ChatServerContext context) {
		this.messageDispatcher = messageDispatcher;
		this.context = context;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Base.Message msg) throws Exception {
		messageDispatcher.dispatch(ctx, msg);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
			switch (idleStateEvent.state()) {
				case WRITER_IDLE: {
					break;
				}
				case READER_IDLE: {
					//长时间没有收到客户端发送的心跳包
					resolveReaderIdle(ctx);
					break;
				}
				case ALL_IDLE: {
					break;
				}
			}
		}
	}

	private void resolveReaderIdle(ChannelHandlerContext ctx) {
		/*
			1.断开与客户端的连接
			2.清理context中维护的无效连接
		 */
		ctx.close().addListener(future -> {
			log.info("客户端{}无响应，强制踢出客户端", ctx);
			context.removeChannel(ctx);
			context.clearInvalidChannel();
		});
	}


}
