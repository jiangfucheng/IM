package com.jiangfucheng.im.client.controller;

import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.controller.base.BaseController;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/1
 * Time: 10:24
 *
 * @author jiangfucheng
 */
@ChatMessageController
public class LogoutController extends BaseController {
	protected LogoutController(ChatClientContext context, MessageMonitor messageMonitor) {
		super(context, messageMonitor);
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGOUT_REQUEST)
	public void handleLogoutRequest(ChannelHandlerContext ctx, Base.Message msg) {
		handleRequest(ctx, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGOUT_RESPONSE)
	public void handleLogoutResponse(ChannelHandlerContext ctx, Base.Message msg) {
		handleResponse(ctx, msg);
	}

	@Override
	protected void resolveResponseNotify(ChannelHandlerContext ctx, Base.Message responseMessage) {
		//直接退出系统
		//TODO:这个设计不好，后续可以改为可二次登陆的功能
		System.exit(1);
	}
}
