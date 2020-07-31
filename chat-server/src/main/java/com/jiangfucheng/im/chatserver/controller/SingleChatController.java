package com.jiangfucheng.im.chatserver.controller;

import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.chatserver.service.MessageService;
import com.jiangfucheng.im.chatserver.service.UserService;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.model.bo.UserStatusBo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.SingleChat;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 22:44
 *
 * @author jiangfucheng
 */
@Slf4j
@ChatMessageController
public class SingleChatController {
	private CommonMessageSender commonMessageSender;
	private UserService userService;
	private MessageService messageService;

	public SingleChatController(CommonMessageSender commonMessageSender,
								UserService userService, MessageService messageService) {
		this.commonMessageSender = commonMessageSender;
		this.userService = userService;
		this.messageService = messageService;
	}

	/**
	 * 处理单聊请求
	 * 如果收到的重发的消息，即Message.getId()相同，服务端不做处理，直接保存到数据库中，由客户端处理，服务端会标记为delivered = false
	 * 如果是离线消息，客户端在拉取离线消息的时候返回最大的收到的消息id，但是排除掉不做处理的消息,那么服务端就会只把客户端已经处理过的离线消息
	 * 转存到在线消息表中，客户端未做处理的离线消息，服务端直接丢弃
	 */
	@ChatMessageMapping(messageType = Base.DataType.SINGLE_CHAT_REQUEST)
	public void handleSingleChatRequest(ChannelHandlerContext ctx, Base.Message message) {
		SingleChat.SingleChatRequest requestImMsg = message.getSingleChatRequest();
		UserStatusBo toUserStatus = userService.getUserStatus(requestImMsg.getToId());
		Long msgId;
		log.debug("chat server receive singleChatRequest : {}", requestImMsg.getContent());
		if (toUserStatus != null) {
			//用户在线
			//判断是否是从mq中转发过来的消息(ctx为null),如果是mq转发来的消息，说明上一个chat-server已经完成了存储消息的
			//操作了，只需要转发给目标客户端就好了
			if (ctx != null) {
				//用户在线，把消息存到消息库中，标记已经送达为false,把消息转发给目标客户端
				msgId = messageService.saveOnlineMessage(requestImMsg);

				Base.Message ackMessage = message.toBuilder()
						.setSingleChatRequest(requestImMsg.toBuilder().setMsgId(msgId))
						.setMessageStatus(Base.MessageStatus.ACK)
						.build();
				commonMessageSender.sendAck(ctx, ackMessage, true);
				requestImMsg = requestImMsg.toBuilder()
						.setMsgId(msgId)
						.build();
				Base.Message notifyMessage = message.toBuilder()
						.setSingleChatRequest(requestImMsg)
						.setMessageStatus(Base.MessageStatus.NOTIFY)
						.build();
				commonMessageSender.sendNotify(toUserStatus.getId(), notifyMessage);
			} else {
				//MQ转发过来的消息(notify)，直接转发给客户端
				commonMessageSender.sendNotify(toUserStatus.getId(), message);
			}

		} else {
			//用户不在线,把消息存入离线消息库，伪造response给client，这里的server一定是有ctx的，
			//在第一台服务器上就直接判断是否在线，所以直接回复给客户端response就好了
			msgId = messageService.saveOfflineMessage(requestImMsg);
			SingleChat.SingleChatResponse singleChatResponse = SingleChat.SingleChatResponse.newBuilder()
					.setToId(requestImMsg.getFromId())
					.setFromId(requestImMsg.getToId())
					.setMsgId(msgId)
					.setTimestamp(System.currentTimeMillis())
					.build();
			Base.Message responseMsg = Base.Message.newBuilder()
					.setSingleChatResponse(singleChatResponse)
					.setId(message.getId())
					.setDataType(Base.DataType.SINGLE_CHAT_RESPONSE)
					.setMessageStatus(Base.MessageStatus.NOTIFY)
					.build();
			ctx.writeAndFlush(responseMsg);
		}
	}

	@ChatMessageMapping(messageType = Base.DataType.SINGLE_CHAT_RESPONSE)
	public void handleSingleChatRespone(ChannelHandlerContext ctx, Base.Message message) {
		//把相应的消息标记为已经送达，转发给发送者
		SingleChat.SingleChatResponse singleChatResponse = message.getSingleChatResponse();
		log.debug("chat server receive SingleChatResponse message with id : {}", message.getId());
		if (ctx != null) {
			messageService.markSingleMessageDelivered(singleChatResponse.getMsgId());
			Base.Message ackMessage = message.toBuilder()
					.setMessageStatus(Base.MessageStatus.ACK)
					.build();
			commonMessageSender.sendAck(ctx, ackMessage, true);
		}
		Base.Message notifyMessage = message.toBuilder()
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.build();
		commonMessageSender.sendNotify(singleChatResponse.getToId(), notifyMessage);
	}

}
