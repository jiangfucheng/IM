package com.jiangfucheng.im.chatserver.controller;

import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 22:27
 *
 * @author jiangfucheng
 */

@ChatMessageController
@Slf4j
public class HeartBeatController {
	private CommonMessageSender messageSender;

	public HeartBeatController(CommonMessageSender messageSender) {
		this.messageSender = messageSender;
	}

	@ChatMessageMapping(messageType = Base.DataType.HEART_BEAT_REQUEST)
	public void handleHeartBeatRequest(ChannelHandlerContext ctx, Base.Message msg) {
		log.debug("receive heartbeat message from {}", ctx.channel().remoteAddress());
		Base.Message ackMessage = msg.toBuilder()
				.setMessageStatus(Base.MessageStatus.ACK)
				.build();
		messageSender.sendAck(ctx, ackMessage, true);
		ctx.writeAndFlush(buildResponse(msg.getId()));
	}

	private Base.Message buildResponse(long requestId) {
		return Base.Message.newBuilder()
				.setId(requestId)
				.setDataType(Base.DataType.HEART_BEAT_RESPONSE)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.build();
	}

}
