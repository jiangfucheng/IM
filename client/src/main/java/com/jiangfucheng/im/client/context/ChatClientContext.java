package com.jiangfucheng.im.client.context;

import com.jiangfucheng.im.client.bo.CurrentUser;
import com.jiangfucheng.im.client.bo.User;
import com.jiangfucheng.im.client.feign.CommonFeignClient;
import com.jiangfucheng.im.client.utils.CommonUtils;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.GroupChat;
import com.jiangfucheng.im.protobuf.SingleChat;
import io.netty.channel.Channel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:17
 *
 * @author jiangfucheng
 */
@Data
@Component
public class ChatClientContext {
	/**
	 * 用户状态
	 */
	public volatile CurrentUser currentUser;
	/**
	 * chat-server地址
	 */
	public volatile String chatServerUrl;
	/**
	 * 鉴权所用token
	 */
	public volatile String authToken;
	/**
	 * 与服务器的连接
	 */
	public volatile Channel channel;
	/**
	 * 存储未收到ack的消息
	 */
	public volatile ConcurrentHashMap<Long, Base.Message> unReceiveAckMessages;
	/**
	 * 未收到ack后重发的次数
	 */
	public volatile ConcurrentHashMap<Long, Integer> unAckRetryTimes;
	/**
	 * 存储未收到response的消息,key: messageId,value: 消息内容(用于重发)
	 */
	public volatile ConcurrentHashMap<Long, Base.Message> unCompletedMessages;
	/**
	 * 未收到response后重发的次数
	 */
	public volatile ConcurrentHashMap<Long, Integer> unCompleteRetryTimes;

	//TODO 每个客户端都需要维护以下信息
	//	1.好友列表(用于实时监听好友上下线，避免不必要的流量)
	//  2.本地消息(用于消息去重，和本地聊天记录查看)

	/**
	 * 好友列表
	 * todo 实时更新好友状态
	 */
	public CopyOnWriteArrayList<User> friendList;

	/**
	 * 本地维护 id->用户(好友或者非好友<群里的用户>) 的缓存
	 * todo 什么时候操作这个缓存
	 */
	public ConcurrentHashMap<Long, User> userCache;

	/**
	 * 单聊消息
	 * key: 好友id
	 * value: 历史消息内容
	 */
	public ConcurrentHashMap<Long, List<SingleChat.SingleChatRequest>> singleChatMessages;

	/**
	 * 群聊消息
	 * key: 群id
	 * value: 历史消息内容
	 */
	public ConcurrentHashMap<Long, List<GroupChat.GroupChatRequest>> groupChatMessages;

	@Autowired
	public ChatClientContext(CommonFeignClient commonFeignClient) {
		this.currentUser = new CurrentUser();
		this.commonFeignClient = commonFeignClient;
		this.unReceiveAckMessages = new ConcurrentHashMap<>();
		this.unAckRetryTimes = new ConcurrentHashMap<>();
		this.unCompletedMessages = new ConcurrentHashMap<>();
		this.unCompleteRetryTimes = new ConcurrentHashMap<>();
		this.friendList = new CopyOnWriteArrayList<>();
		this.userCache = new ConcurrentHashMap<>();
		this.singleChatMessages = new ConcurrentHashMap<>();
		this.groupChatMessages = new ConcurrentHashMap<>();
	}

	/**
	 * 把单聊消息添加到本地缓存，需要按顺序添加
	 */
	public void putSingleChatMessage(Long friendId, SingleChat.SingleChatRequest message) {
		List<SingleChat.SingleChatRequest> singleChatMessages = this.singleChatMessages
				.computeIfAbsent(friendId, k -> new ArrayList<>());
		int insertIndex = CommonUtils.searchInsertIndex(singleChatMessages, message, (m1, m2) -> (int) ((m1.getMsgId() - m2.getMsgId()) % Integer.MAX_VALUE));
		if (insertIndex == singleChatMessages.size()) {
			singleChatMessages.add(message);
		} else {
			singleChatMessages.set(insertIndex, message);
		}
	}

	public void putGroupChatMessage(Long groupId, GroupChat.GroupChatRequest message) {
		List<GroupChat.GroupChatRequest> groupChatMessages = this.groupChatMessages
				.computeIfAbsent(groupId, k -> new ArrayList<>());
		int insertIndex = CommonUtils.searchInsertIndex(groupChatMessages, message, (m1, m2) -> (int) ((m1.getMsgId() - m2.getMsgId()) % Integer.MAX_VALUE));
		if (insertIndex == groupChatMessages.size()) {
			groupChatMessages.add(message);
		} else {
			groupChatMessages.set(insertIndex, message);
		}
	}


	public void putUnCompletedMsg(Base.Message msg) {
		unCompletedMessages.put(msg.getId(), msg);
	}

	public void removeCompletedMsg(Long messageId) {
		Base.Message message = unCompletedMessages.remove(messageId);
		unCompleteRetryTimes.remove(messageId);
	}

	public void putUnAckMessage(Base.Message message) {
		unReceiveAckMessages.put(message.getId(), message);
	}

	public void removeAckMessage(Long messageId) {
		unReceiveAckMessages.remove(messageId);
		unAckRetryTimes.remove(messageId);
	}

	private final CommonFeignClient commonFeignClient;

	/**
	 * 向服务端请求一个消息id
	 */
	public Long generateId() {
		return commonFeignClient.generateId().getData();
	}

}
