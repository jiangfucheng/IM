package com.jiangfucheng.im.chatserver.controller;

import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.chatserver.service.MessageService;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.model.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/2
 * Time: 22:24
 *
 * @author jiangfucheng
 */
@ChatMessageController
public class ReceivedOfflineMessageController {
	private CommonMessageSender messageSender;
	private MessageService messageService;

	public ReceivedOfflineMessageController(CommonMessageSender messageSender, MessageService messageService) {
		this.messageSender = messageSender;
		this.messageService = messageService;
	}

	@ChatMessageMapping(messageType = Base.DataType.RECEIVED_OFFLINE_MESSAGE_REQUEST)
	public void handleReceivedOfflineMessageRequest(ChannelHandlerContext ctx, Base.Message message) {
		messageSender.sendAck(ctx, message.toBuilder().setMessageStatus(Base.MessageStatus.ACK).build(), true);
		Control.ReceivedOfflineMessageRequest receivedOfflineMessageRequest = message.getReceivedOfflineMessageRequest();
		String token = receivedOfflineMessageRequest.getToken();
		UserTokenPayloadBo userPayload = JwtUtil.getTokenBody(token);
		messageService.markOfflineMessageIsDelivered(userPayload.getUserId(),
				receivedOfflineMessageRequest.getFriendId(),
				receivedOfflineMessageRequest.getReceivedMessageId());
		messageSender.sendNotify(userPayload.getUserId(), buildResponse(message.getId()));
	}

	private Base.Message buildResponse(long id) {
		Control.ReceivedOfflineMessageResponse response = Control.ReceivedOfflineMessageResponse.newBuilder()
				.setCode(ErrorCode.OK)
				.setMsg(ErrorCode.OK_MSG)
				.setErrMsg(ErrorCode.OK_MSG)
				.setTimestamp(System.currentTimeMillis())
				.build();
		return Base.Message.newBuilder()
				.setId(id)
				.setDataType(Base.DataType.RECEIVED_OFFLINE_MESSAGE_RESPONSE)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.setReceivedOfflineMessageResponse(response)
				.build();
	}

}
