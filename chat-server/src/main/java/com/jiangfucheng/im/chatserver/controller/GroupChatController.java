package com.jiangfucheng.im.chatserver.controller;

import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.chatserver.service.MessageService;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.GroupChat;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 14:58
 *
 * @author jiangfucheng
 */
@ChatMessageController
public class GroupChatController {

	private CommonMessageSender commonMessageSender;
	private MessageService messageService;

	public GroupChatController(CommonMessageSender commonMessageSender, MessageService messageService) {
		this.commonMessageSender = commonMessageSender;
		this.messageService = messageService;
	}

	/**
	 * 处理群聊请求
	 * 1.保存消息到数据库
	 * 2.send ack
	 * 3.转发给群里面所有用户
	 * 4.服务端伪造一个response发送给客户端
	 */
	@ChatMessageMapping(messageType = Base.DataType.GROUP_CHAT_REQUEST)
	public void handleGroupChatRequest(ChannelHandlerContext ctx, Base.Message message) {
		GroupChat.GroupChatRequest groupChatRequest = message.getGroupChatRequest();
		Long msgId = 0L;
		if (ctx != null) {
			//不是转发来的消息
			msgId = messageService.saveGroupMessage(groupChatRequest);
			commonMessageSender.sendAck(ctx, message, true);
		}
		messageService.sendMessageToGroupMember(message);
		if (ctx != null) {
			GroupChat.GroupChatResponse groupChatResponse = GroupChat.GroupChatResponse.newBuilder()
					.setGroupId(groupChatRequest.getGroupId())
					.setFromId(groupChatRequest.getFromId())
					.setMsgId(msgId)
					.setTimestamp(System.currentTimeMillis())
					.build();
			Base.Message responseMessage = Base.Message.newBuilder()
					.setGroupChatResponse(groupChatResponse)
					.setId(message.getId())
					.setDataType(Base.DataType.GROUP_CHAT_RESPONSE)
					.setMessageStatus(Base.MessageStatus.NOTIFY)
					.build();
			ctx.writeAndFlush(responseMessage);
		}

	}

	/**
	 * 更新用户最后一条收到的消息
	 * send ack
	 * TODO:转发给发送人,群消息暂时不做可靠性保证
	 */
	@ChatMessageMapping(messageType = Base.DataType.GROUP_CHAT_RESPONSE)
	public void handleGroupChatResponse(ChannelHandlerContext ctx, Base.Message message) {
		GroupChat.GroupChatResponse groupChatResponse = message.getGroupChatResponse();
		if (ctx != null) {
			messageService.updateLastReceivedMsg(groupChatResponse);
			commonMessageSender.sendAck(ctx, message, true);
		}

	}
}
