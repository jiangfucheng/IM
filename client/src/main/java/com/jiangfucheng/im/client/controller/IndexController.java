package com.jiangfucheng.im.client.controller;

import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/25
 * Time: 9:36
 *
 * @author jiangfucheng
 */
@ChatMessageController
@Slf4j
public class IndexController {

	private ChatClientContext context;
	private MessageMonitor messageMonitor;

	public IndexController(ChatClientContext context, MessageMonitor messageMonitor) {
		this.context = context;
		this.messageMonitor = messageMonitor;
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGIN_REQUEST)
	public void handleLoginRequest(ChannelHandlerContext ctx, Base.Message msg) {
		if (msg.getMessageStatus() == Base.MessageStatus.ACK) {
			//取消ack定时
			context.removeAckMessage(msg.getId());
			log.debug("收到ack消息: {}", msg.getId());
			//添加resend response定时
			messageMonitor.watchMessage(msg.getId(), MessageMonitor.Type.UN_COMPLETE);
		}
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGIN_RESPONSE)
	public void handleLoginResponse(ChannelHandlerContext ctx, Base.Message msg) {
		if (msg.getMessageStatus() == Base.MessageStatus.NOTIFY) {
			context.removeCompletedMsg(msg.getId());
		}
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGOUT_REQUEST)
	public void handleLogoutRequest(ChannelHandlerContext ctx, Base.Message msg) {
		if (msg.getMessageStatus() == Base.MessageStatus.ACK) {
			//取消ack定时
			context.removeAckMessage(msg.getId());
			log.debug("收到ack消息: {}", msg.getId());

			//开启重发定时
			messageMonitor.watchMessage(msg.getId(), MessageMonitor.Type.UN_COMPLETE);

		}
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGOUT_RESPONSE)
	public void handleLogoutResponse(ChannelHandlerContext ctx, Base.Message msg) {
		if (msg.getMessageStatus() == Base.MessageStatus.NOTIFY) {
			context.removeCompletedMsg(msg.getId());
			//直接退出系统
			//TODO:后续可以加上可二次登陆的功能
			System.exit(1);
		}
	}

}
