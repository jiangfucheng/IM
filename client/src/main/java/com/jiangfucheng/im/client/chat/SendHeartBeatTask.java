package com.jiangfucheng.im.client.chat;

import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 18:18
 * 发送心跳包
 *
 * @author jiangfucheng
 */
@Component
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
		channel.writeAndFlush(heartBeatMessage);
		context.putUnAckMessage(heartBeatMessage);
		messageMonitor.watchMessage(heartBeatMessage.getId(), MessageMonitor.Type.UN_ACK);
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
