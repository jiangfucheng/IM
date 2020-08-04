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
 * Date: 2020/8/2
 * Time: 22:24
 *
 * @author jiangfucheng
 */
@ChatMessageController
public class ReceivedOfflineMessageController extends BaseController {

	protected ReceivedOfflineMessageController(ChatClientContext context, MessageMonitor messageMonitor) {
		super(context, messageMonitor);
	}

	@ChatMessageMapping(messageType = Base.DataType.RECEIVED_OFFLINE_MESSAGE_REQUEST)
	public void handleReceivedOfflineMessageRequest(ChannelHandlerContext ctx, Base.Message message) {
		handleRequest(ctx, message);
	}

	@ChatMessageMapping(messageType = Base.DataType.RECEIVED_OFFLINE_MESSAGE_RESPONSE)
	public void handleReceivedOfflineMessageResponse(ChannelHandlerContext ctx, Base.Message message) {
		handleResponse(ctx, message);
	}


}
