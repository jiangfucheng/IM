package com.jiangfucheng.im.client.utils;

import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.protobuf.Base;
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
		monitor.watchMessage(message.getId(), MessageMonitor.Type.UN_ACK);
		channel.writeAndFlush(message);
		log.debug("send request req message with id : {}", message.getId());
	}

	public static void writeRequestReqMessage(ChannelHandlerContext ctx, ChatClientContext context, MessageMonitor monitor, Base.Message message) {
		writeRequestReqMessage(ctx.channel(), context, monitor, message);
	}


}
