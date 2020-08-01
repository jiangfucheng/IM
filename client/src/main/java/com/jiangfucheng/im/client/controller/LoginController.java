package com.jiangfucheng.im.client.controller;

import com.jiangfucheng.im.client.chat.HeartBeatSender;
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
 * Date: 2020/8/1
 * Time: 10:23
 *
 * @author jiangfucheng
 */
@ChatMessageController
@Slf4j
public class LoginController extends BaseController {
	private HeartBeatSender heartBeatSender;

	protected LoginController(ChatClientContext context,
							  MessageMonitor messageMonitor,
							  HeartBeatSender heartBeatSender) {
		super(context, messageMonitor);
		this.heartBeatSender = heartBeatSender;
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGIN_REQUEST)
	public void handleLoginRequest(ChannelHandlerContext ctx, Base.Message msg) {
		handleRequest(ctx, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGIN_RESPONSE)
	public void handleLoginResponse(ChannelHandlerContext ctx, Base.Message msg) {
		handleResponse(ctx, msg);
	}

	@Override
	protected void resolveResponseNotify(ChannelHandlerContext ctx, Base.Message responseMessage) {
		heartBeatSender.start();
	}
}
