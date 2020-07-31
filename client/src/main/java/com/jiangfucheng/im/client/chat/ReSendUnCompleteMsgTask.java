package com.jiangfucheng.im.client.chat;

import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/30
 * Time: 9:46
 *
 * @author jiangfucheng
 */
@Slf4j
public class ReSendUnCompleteMsgTask implements Runnable {

	private ChatClientContext context;
	private Long msgId;
	private MessageMonitor messageMonitor;

	ReSendUnCompleteMsgTask(ChatClientContext context,
							Long msgId,
							MessageMonitor messageMonitor) {
		this.context = context;
		this.msgId = msgId;
		this.messageMonitor = messageMonitor;
	}

	@Override
	public void run() {
		ConcurrentHashMap<Long, Base.Message> unCompletedMessages = context.getUnCompletedMessages();
		ConcurrentHashMap<Long, Integer> unCompleteRetryTimes = context.getUnCompleteRetryTimes();
		if (unCompletedMessages.containsKey(msgId)) {
			//还没有收到response, 重发消息
			unCompleteRetryTimes.merge(msgId, 1, (a, b) -> a + b);
			Channel channel = context.getChannel();
			Base.Message message = unCompletedMessages.get(msgId);
			log.debug("resend un receive response message : " + message.getSingleChatRequest().getContent());
			//这个时候不需要watch Ack了，但是需要做标记，因为又是一个新的流程了。
			//TODO 如果在重发的过程中服务器挂掉了，就接收不到ACK消息了，这个时候需要怎么处理。是否需要在这个阶段watch ack,如果在这个阶段watch ACK，会不会
			//     有嵌套死循环的问题
			//context.putUnAckMessage(message);
			channel.writeAndFlush(message);
			messageMonitor.watchMessage(msgId, MessageMonitor.Type.UN_COMPLETE);
		} else {
			log.debug("ReSendUnCompleteMsgTask canceled for messageId: {}", msgId);
		}
	}

}
