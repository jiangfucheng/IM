package com.jiangfucheng.im.client.controller;

import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.controller.base.BaseController;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 22:10
 *
 * @author jiangfucheng
 */

@ChatMessageController
@Slf4j
public class HeartBeatController extends BaseController {

	protected HeartBeatController(ChatClientContext context, MessageMonitor messageMonitor) {
		super(context, messageMonitor);
	}

	@ChatMessageMapping(messageType = Base.DataType.HEART_BEAT_REQUEST)
	public void handleHeartBeatRequest(ChannelHandlerContext ctx, Base.Message msg) {
		handleRequest(ctx, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.HEART_BEAT_RESPONSE)
	public void handleHeartBeatResponse(ChannelHandlerContext ctx, Base.Message msg) {
		handleResponse(ctx, msg);
	}

}
