package com.jiangfucheng.im.chatserver.chat;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 22:48
 * 维护当前服务器的上下文，比如与客户端的链接，接收等待接收response的队列
 *
 * @author jiangfucheng
 */
@Component
@Slf4j
public class ChatServerContext {
	//绑定user和user与服务器建立的channel
	private ConcurrentHashMap<Long, ChannelHandlerContext> channelsMap;
	private ConcurrentHashMap<ChannelHandlerContext, Long> channelsRevertMap;

	/*
		等待接收response的队列,只有服务端主动推送的通知消息才需要维护这个队列
		这个队里也可以用来绑定控制类消息的两端，使得客户端回复控制类消息的response中不需要带上目标userId
		key:消息id
		value:userId,用来校验回复response的人是不是伪造的
	 */
	private ConcurrentHashMap<Long, Long> receiveMessageResponseSet;

	public ChatServerContext() {
		channelsMap = new ConcurrentHashMap<>();
		channelsRevertMap = new ConcurrentHashMap<>();
		receiveMessageResponseSet = new ConcurrentHashMap<>();
	}

	public void register(Long userId, ChannelHandlerContext ctx) {
		channelsMap.put(userId, ctx);
		log.debug("{} register in chat server with ctx : {}", userId, ctx);
		log.debug("{} ========> {}", userId, channelsMap.get(userId));
		channelsRevertMap.put(ctx, userId);
	}

	public void removeChannel(Long userId) {
		ChannelHandlerContext ctx = channelsMap.remove(userId);
		channelsRevertMap.remove(ctx);
	}

	public void removeChannel(ChannelHandlerContext ctx) {
		Long userId = channelsRevertMap.remove(ctx);
		channelsMap.remove(userId);
	}

	public ChannelHandlerContext getChannelHandlerContext(Long userId) {
		return channelsMap.get(userId);
	}

	public Long getUserId(ChannelHandlerContext ctx) {
		return channelsRevertMap.get(ctx);
	}

	/**
	 * 把消息加入到等待队列中
	 *
	 * @param msgId Base.Message.getId()，不是指单聊或群聊消息的id
	 */
	public void addReceiveMessage(Long msgId, Long userId) {
		receiveMessageResponseSet.putIfAbsent(userId, msgId);
	}

	public boolean messageReceived(Long msgId, Long userId) {
		Long targetUserId = receiveMessageResponseSet.getOrDefault(msgId, null);
		/*if (userId.equals(targetUserId)) {
			receiveMessageResponseSet.remove(msgId);
			return true;
		}*/
		receiveMessageResponseSet.remove(msgId);
		return true;
	}

	public long getBindedUserId(Long messageId) {
		return receiveMessageResponseSet.get(messageId);
	}
}
