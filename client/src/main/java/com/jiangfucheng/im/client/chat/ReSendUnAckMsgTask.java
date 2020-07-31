package com.jiangfucheng.im.client.chat;

import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/30
 * Time: 14:39
 *
 * @author jiangfucheng
 */
@Slf4j
public class ReSendUnAckMsgTask implements Runnable {
	private ChatClientContext context;
	private Long msgId;
	private MessageMonitor messageMonitor;

	ReSendUnAckMsgTask(ChatClientContext context,
					   Long msgId,
					   MessageMonitor messageMonitor) {
		this.context = context;
		this.msgId = msgId;
		this.messageMonitor = messageMonitor;
	}


	@Override
	public void run() {
		ConcurrentHashMap<Long, Base.Message> unReceiveAckMessages = context.getUnReceiveAckMessages();
		ConcurrentHashMap<Long, Integer> unAckRetryTimes = context.getUnAckRetryTimes();
		if (unReceiveAckMessages.containsKey(msgId)) {
			//还没有收到ack消息
			unAckRetryTimes.merge(msgId, 1, (a, b) -> a + b);
			Channel channel = context.getChannel();
			Base.Message message = unReceiveAckMessages.get(msgId);
			log.debug("resend un receive ack message : " + message.getSingleChatRequest().getContent());
			channel.writeAndFlush(message);
			messageMonitor.watchMessage(msgId, MessageMonitor.Type.UN_ACK);
		} else {
			log.debug("ReSendUnAckMsgTask canceled for messageId: {}", msgId);
		}
	}
}
