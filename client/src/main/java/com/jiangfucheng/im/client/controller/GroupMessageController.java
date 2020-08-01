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
 * Date: 2020/5/31
 * Time: 16:06
 *
 * @author jiangfucheng
 */
@ChatMessageController
public class GroupMessageController extends BaseController {

	protected GroupMessageController(ChatClientContext context, MessageMonitor messageMonitor) {
		super(context, messageMonitor);
	}

	@ChatMessageMapping(messageType = Base.DataType.SINGLE_CHAT_REQUEST)
	public void handleGroupChatRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.SINGLE_CHAT_RESPONSE)
	public void handleGroupChatResponse(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@Override
	protected Base.Message resolveRequestAck(ChannelHandlerContext ctx, Base.Message requestMessage) {
		return super.resolveRequestAck(ctx, requestMessage);
	}

	@Override
	protected Base.Message resolveRequestNotify(ChannelHandlerContext ctx, Base.Message requestMessage) {
		return super.resolveRequestNotify(ctx, requestMessage);
	}

	@Override
	protected void resolveResponseNotify(ChannelHandlerContext ctx, Base.Message responseMessage) {
		super.resolveResponseNotify(ctx, responseMessage);
	}
}
