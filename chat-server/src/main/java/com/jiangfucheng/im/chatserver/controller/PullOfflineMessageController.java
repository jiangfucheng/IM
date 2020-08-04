package com.jiangfucheng.im.chatserver.controller;

import com.alibaba.fastjson.JSON;
import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.chatserver.service.MessageService;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.model.bo.OfflineMessageBo;
import com.jiangfucheng.im.model.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/2
 * Time: 22:23
 *
 * @author jiangfucheng
 */
@ChatMessageController
@Slf4j
public class PullOfflineMessageController {
	private CommonMessageSender commonMessageSender;
	private MessageService messageService;

	public PullOfflineMessageController(CommonMessageSender commonMessageSender, MessageService messageService) {
		this.commonMessageSender = commonMessageSender;
		this.messageService = messageService;
	}

	/**
	 * 处理客户端拉取离线消息的请求
	 */
	@ChatMessageMapping(messageType = Base.DataType.PULL_OFFLINE_MESSAGE_REQUEST)
	public void handlePullOfflineMessageRequest(ChannelHandlerContext ctx, Base.Message message) {
		commonMessageSender.sendAck(ctx, message.toBuilder().setMessageStatus(Base.MessageStatus.ACK).build(), true);
		Control.PullOfflineMessageRequest pullOfflineMessageRequest = message.getPullOfflineMessageRequest();
		UserTokenPayloadBo userTokenPayloadBo = JwtUtil.getTokenBody(pullOfflineMessageRequest.getToken());
		List<OfflineMessageBo> offlineMessageBos = messageService.selectOfflineMessages(userTokenPayloadBo.getUserId(),
				pullOfflineMessageRequest.getUserId(),
				pullOfflineMessageRequest.getBeginMsgId());
		Base.Message response = buildResponse(message.getId(), JSON.toJSONString(offlineMessageBos));
		commonMessageSender.sendNotify(userTokenPayloadBo.getUserId(), response);
	}

	private Base.Message buildResponse(long id, String messageContent) {
		Control.PullOfflineMessageResponse response = Control.PullOfflineMessageResponse.newBuilder()
				.setCode(ErrorCode.OK)
				.setErrMsg(ErrorCode.OK_MSG)
				.setMsg(ErrorCode.OK_MSG)
				.setMessage(messageContent)
				.build();
		return Base.Message.newBuilder()
				.setId(id)
				.setDataType(Base.DataType.PULL_OFFLINE_MESSAGE_RESPONSE)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.setPullOfflineMessageResponse(response)
				.build();
	}


}
