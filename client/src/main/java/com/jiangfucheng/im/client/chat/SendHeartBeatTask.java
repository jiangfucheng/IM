package com.jiangfucheng.im.client.chat;

import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.utils.MessageUtils;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;


/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 18:18
 * 发送心跳包
 *
 * @author jiangfucheng
 */
@Slf4j
public class SendHeartBeatTask implements Runnable {
	private ChatClientContext context;
	private MessageMonitor messageMonitor;

	public SendHeartBeatTask(ChatClientContext context,
							 MessageMonitor messageMonitor) {
		this.context = context;
		this.messageMonitor = messageMonitor;
	}

	@Override
	public void run() {
		Base.Message heartBeatMessage = buildHeartBeatMessage();
		Channel channel = context.getChannel();
		MessageUtils.writeRequestReqMessage(channel, context, messageMonitor, heartBeatMessage);
		log.debug("send heart beat message to chat server");
	}

	private Base.Message buildHeartBeatMessage() {
		Long messageId = context.generateId();
		return Base.Message.newBuilder()
				.setId(messageId)
				.setDataType(Base.DataType.HEART_BEAT_REQUEST)
				.setMessageStatus(Base.MessageStatus.REQ)
				.build();
	}
}
