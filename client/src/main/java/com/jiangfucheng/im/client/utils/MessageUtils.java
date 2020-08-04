package com.jiangfucheng.im.client.utils;

import com.jiangfucheng.im.client.bo.GroupMessageBo;
import com.jiangfucheng.im.client.bo.SingleMessageBo;
import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.common.enums.MessageType;
import com.jiangfucheng.im.model.bo.OfflineMessageBo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.GroupChat;
import com.jiangfucheng.im.protobuf.SingleChat;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/30
 * Time: 21:12
 *
 * @author jiangfucheng
 */
@Slf4j
public class MessageUtils {
	/**
	 * 是否已经收到该消息的ACK
	 */
	public static boolean isReceivedAckMessage(ChatClientContext context, long messageId) {
		ConcurrentHashMap<Long, Base.Message> unReceiveAckMessages = context.getUnReceiveAckMessages();
		return !unReceiveAckMessages.containsKey(messageId);
	}

	/**
	 * 是否已经收到该消息的RESP
	 */
	public static boolean isCompleteMessage(ChatClientContext context, long messageId) {
		ConcurrentHashMap<Long, Base.Message> unCompletedMessages = context.getUnCompletedMessages();
		return !unCompletedMessages.containsKey(messageId);
	}

	public static void writeRequestReqMessage(Channel channel, ChatClientContext context, MessageMonitor monitor, Base.Message message) {
		context.putUnAckMessage(message);
		if (monitor != null) {
			//不需要监控
			//后续优化掉
			monitor.watchMessage(message.getId(), MessageMonitor.Type.UN_ACK);
		}
		channel.writeAndFlush(message);
		log.debug("send request req message with id : {}", message.getId());
	}

	public static void writeRequestReqMessage(ChannelHandlerContext ctx, ChatClientContext context, MessageMonitor monitor, Base.Message message) {
		writeRequestReqMessage(ctx.channel(), context, monitor, message);
	}

	public static SingleMessageBo convertSingleChatRequestToSingleMessageBo(SingleChat.SingleChatRequest singleChatRequest) {
		SingleMessageBo singleMessageBo = new SingleMessageBo();
		singleMessageBo.setMsgId(singleChatRequest.getMsgId());
		singleMessageBo.setFromId(singleChatRequest.getFromId());
		singleMessageBo.setToId(singleChatRequest.getToId());
		singleMessageBo.setMessageType(MessageType.valueOf(singleChatRequest.getType()));
		singleMessageBo.setContent(singleChatRequest.getContent());

		return singleMessageBo;
	}

	public static SingleMessageBo covertOfflineMessageBoToSingleMessageBo(OfflineMessageBo offlineMessageBo) {
		SingleMessageBo singleMessageBo = new SingleMessageBo();
		singleMessageBo.setMsgId(offlineMessageBo.getId());
		singleMessageBo.setFromId(offlineMessageBo.getFromId());
		singleMessageBo.setToId(offlineMessageBo.getToId());
		singleMessageBo.setMessageType(MessageType.valueOf(offlineMessageBo.getMsgType()));
		singleMessageBo.setContent(offlineMessageBo.getContent());
		singleMessageBo.setTime(offlineMessageBo.getCreateTime());
		return singleMessageBo;
	}

	public static GroupMessageBo convertGroupChatRequestToGroupMessageBo(GroupChat.GroupChatRequest groupChatRequest) {
		GroupMessageBo groupMessageBo = new GroupMessageBo();
		groupMessageBo.setGroupId(groupChatRequest.getGroupId());
		groupMessageBo.setFromId(groupChatRequest.getFromId());
		groupMessageBo.setMsgId(groupChatRequest.getMsgId());
		groupMessageBo.setMessageType(MessageType.valueOf(groupChatRequest.getType()));
		groupMessageBo.setContent(groupChatRequest.getContent());
		return groupMessageBo;
	}


}
