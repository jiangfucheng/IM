package com.jiangfucheng.im.client.controller;

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
public class GroupMessageController {

	@ChatMessageMapping(messageType = Base.DataType.SINGLE_CHAT_REQUEST)
	public void handleGroupChatRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.SINGLE_CHAT_RESPONSE)
	public void handleGroupChatResponse(ChannelHandlerContext ctx, Base.Message msg) {

	}
}
