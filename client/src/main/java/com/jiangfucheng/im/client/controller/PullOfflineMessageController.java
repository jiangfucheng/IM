package com.jiangfucheng.im.client.controller;

import com.alibaba.fastjson.JSON;
import com.jiangfucheng.im.client.bo.SingleMessageBo;
import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.controller.base.BaseController;
import com.jiangfucheng.im.client.utils.MessageUtils;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.model.bo.OfflineMessageBo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/3
 * Time: 12:06
 *
 * @author jiangfucheng
 */
@ChatMessageController
@Slf4j
public class PullOfflineMessageController extends BaseController {

	protected PullOfflineMessageController(ChatClientContext context, MessageMonitor messageMonitor) {
		super(context, messageMonitor);
	}

	@ChatMessageMapping(messageType = Base.DataType.PULL_OFFLINE_MESSAGE_REQUEST)
	public void handlePullOfflineMessageRequest(ChannelHandlerContext ctx, Base.Message message) {
		handleRequest(ctx, message);
	}

	@ChatMessageMapping(messageType = Base.DataType.PULL_OFFLINE_MESSAGE_RESPONSE)
	public void handlePullOfflineMessageResponse(ChannelHandlerContext ctx, Base.Message message) {
		handleResponse(ctx, message);
	}

	@Override
	protected void resolveResponseNotify(ChannelHandlerContext ctx, Base.Message responseMessage) {
		Control.PullOfflineMessageResponse response = responseMessage.getPullOfflineMessageResponse();
		List<OfflineMessageBo> offlineMessages = JSON.parseArray(response.getMessage(), OfflineMessageBo.class);
		if (offlineMessages.size() <= 0) return;
		long currentUserId = context.getCurrentUser().getId();
		OfflineMessageBo tmpMsg = offlineMessages.get(0);
		long friendId = tmpMsg.getFromId().equals(currentUserId) ? tmpMsg.getToId() : tmpMsg.getFromId();
		long lastMsgId = -1;
		List<SingleMessageBo> singleMessageBoList = offlineMessages.stream()
				.map(MessageUtils::covertOfflineMessageBoToSingleMessageBo)
				.collect(Collectors.toList());
		for (SingleMessageBo singleMessage : singleMessageBoList) {
			context.putSingleChatMessage(friendId, singleMessage);
			lastMsgId = Math.max(lastMsgId, singleMessage.getMsgId());
		}
		log.debug("拉取离线消息完毕");
		//通知服务器已经接收离线消息完毕
		//TODO 在用户每次进入聊天界面的时候进行离线消息的拉取
		//TODO 在用户真正进入到聊天页面以后再回复ReceivedOfflineMessage包
		MessageUtils.writeRequestReqMessage(context.getChannel(), context, messageMonitor, buildReceivedOfflineMessageRequest(friendId, lastMsgId));
		log.debug("通知服务器离线消息处理完毕");
	}

	private Base.Message buildReceivedOfflineMessageRequest(long friendId, long lastMessageId) {
		Control.ReceivedOfflineMessageRequest receivedOfflineMessageRequest = Control.ReceivedOfflineMessageRequest.newBuilder()
				.setFriendId(friendId)
				.setReceivedMessageId(lastMessageId)
				.setToken(context.getAuthToken())
				.setTimestamp(System.currentTimeMillis())
				.build();
		return Base.Message.newBuilder()
				.setId(context.generateId())
				.setDataType(Base.DataType.RECEIVED_OFFLINE_MESSAGE_REQUEST)
				.setMessageStatus(Base.MessageStatus.REQ)
				.setReceivedOfflineMessageRequest(receivedOfflineMessageRequest)
				.build();
	}
}
