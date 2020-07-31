package com.jiangfucheng.im.chatserver.chat;

import com.jiangfucheng.im.common.chat.OnlineMessageConsumer;
import com.jiangfucheng.im.common.constants.MQConstants;
import com.jiangfucheng.im.common.constants.RedisConstants;
import com.jiangfucheng.im.common.utils.RocketMQUtil;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/22
 * Time: 0:08
 * 只有ack消息才可以做去除消息体的优化
 *
 * @author jiangfucheng
 */
@Slf4j
public class CommonMessageSender {

	private MQProducer producer;
	private RedisTemplate<String, Object> redisTemplate;
	private OnlineMessageConsumer onlineMessageConsumer;
	private int retry;
	private long retryDelayTime;
	private ChatServerContext context;

	public CommonMessageSender(MQProducer producer,
							   RedisTemplate<String, Object> redisTemplate,
							   OnlineMessageConsumer onlineMessageConsumer,
							   int retry, long retryDelayTime, ChatServerContext context) {
		this.producer = producer;
		this.redisTemplate = redisTemplate;
		this.onlineMessageConsumer = onlineMessageConsumer;
		this.retry = retry;
		this.retryDelayTime = retryDelayTime;
		this.context = context;
	}


	/**
	 * @param isOriginal 是否为原始的msg，如果为原始的msg则构造一个只有id相同的新msg以节省空间，否则直接写出传入的msg
	 */
	public void sendAck(ChannelHandlerContext ctx, Base.Message msg, boolean isOriginal) {
		ctx.writeAndFlush(msg);
	}

	/**
	 * 用于发送Request:req类型的消息
	 */
	public void sendRequest(Long userId, Base.Message msg) {
		ChannelHandlerContext ctx = context.getChannelHandlerContext(userId);
		if (ctx == null) {
			transfer(userId, msg);
		} else {
			ctx.writeAndFlush(msg);
		}
	}

	public void sendRequestWithRetry(Long userId, Base.Message msg) {
		ChannelHandlerContext ctx = context.getChannelHandlerContext(userId);
		if (ctx == null) {
			transferWithRetry(userId, msg, retry, retryDelayTime);
		} else {
			sendRequestWithRetry(ctx, msg, retry, retryDelayTime);
		}
	}

	private void sendRequestWithRetry(ChannelHandlerContext context, Base.Message msg, int retry, long delayTime) {
		new RetryMessageSender(onlineMessageConsumer) {
			@Override
			public void sendMessage() {
				context.writeAndFlush(msg);
			}
		}.sendRetryMessage(msg, retry, delayTime);
	}

	/**
	 * 通知消息，需要把整个消息的内容都转发给客户端
	 */
	public void sendNotify(Long userId, Base.Message msg) {
		ChannelHandlerContext channelHandlerContext = context.getChannelHandlerContext(userId);
		if (channelHandlerContext == null) {
			transfer(userId, msg);
		} else {
			log.debug("send notify message to with ctx : {}", channelHandlerContext);
			sendNotify(channelHandlerContext, msg);
		}

	}

	public void sendNotifyWithRetry(long userId, Base.Message msg) {
		ChannelHandlerContext channelHandlerContext = context.getChannelHandlerContext(userId);
		if (context == null) {
			transferWithRetry(userId, msg, retry, retryDelayTime);
		} else {
			sendNotifyWithRetry(channelHandlerContext, msg, retry, retryDelayTime);
		}
	}

	private void transferWithRetry(Long userId, Base.Message msg, int retry, long delayTime) {
		RetryMessageSender retryMessageSender = new RetryMessageSender(onlineMessageConsumer) {
			@Override
			public void sendMessage() {
				transfer(userId, msg);
			}
		};
		retryMessageSender.sendRetryMessage(msg, retry, delayTime);
	}


	private void sendNotifyWithRetry(ChannelHandlerContext context, Base.Message msg, int retry, long delayTime) {
		RetryMessageSender retryMessageSender = new RetryMessageSender(onlineMessageConsumer) {
			@Override
			public void sendMessage() {
				sendNotify(context, msg);
			}
		};
		retryMessageSender.sendRetryMessage(msg, retry, delayTime);
	}


	private void transfer(Long userId, Base.Message msg) {
		String connectedServerHost = (String) redisTemplate.opsForValue()
				.get(String.format(RedisConstants.USER_CONNECTED_CHAT_SERVER, userId));
		RocketMQUtil.sendMessage(producer, MQConstants.ONLINE_MESSAGE_TOPIC, connectedServerHost, msg.toByteArray());
	}

	/**
	 * @param ctx 目标ChannelContext
	 */
	private void sendNotify(ChannelHandlerContext ctx, Base.Message msg) {
		ctx.writeAndFlush(msg);
	}

}
