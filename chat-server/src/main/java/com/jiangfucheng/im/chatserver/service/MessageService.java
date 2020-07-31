package com.jiangfucheng.im.chatserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.chatserver.mapper.*;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.model.dto.LastReceivedMessageBo;
import com.jiangfucheng.im.model.po.GroupMessagePo;
import com.jiangfucheng.im.model.po.GroupUserPo;
import com.jiangfucheng.im.model.po.MessagePo;
import com.jiangfucheng.im.model.po.OfflineMessagePo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.GroupChat;
import com.jiangfucheng.im.protobuf.SingleChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 16:41
 *
 * @author jiangfucheng
 */
@Service
@Slf4j
public class MessageService {
	private SnowFlakeIdGenerator idGenerator;
	private MessageMapper messageMapper;
	private OfflineMessageMapper offlineMessageMapper;
	private GroupMessageMapper groupMessageMapper;
	private SyncGroupMessageMapper syncGroupMessageMapper;
	private GroupUserMapper groupUserMapper;
	private CommonMessageSender commonMessageSender;

	public MessageService(SnowFlakeIdGenerator idGenerator,
						  MessageMapper messageMapper,
						  OfflineMessageMapper offlineMessageMapper,
						  GroupMessageMapper groupMessageMapper,
						  SyncGroupMessageMapper syncGroupMessageMapper,
						  GroupUserMapper groupUserMapper,
						  CommonMessageSender commonMessageSender) {
		this.idGenerator = idGenerator;
		this.messageMapper = messageMapper;
		this.offlineMessageMapper = offlineMessageMapper;
		this.groupMessageMapper = groupMessageMapper;
		this.syncGroupMessageMapper = syncGroupMessageMapper;
		this.groupUserMapper = groupUserMapper;
		this.commonMessageSender = commonMessageSender;
	}

	public Long saveOnlineMessage(SingleChat.SingleChatRequest requestImMsg) {
		if (requestImMsg.getMsgId() != 0L) {
			//带有id的消息，说明是因为各种原因而重发的消息，不需要再做存储。
			return requestImMsg.getMsgId();
		}
		Long msgId = idGenerator.nextId();
		MessagePo messagePo = new MessagePo();
		messagePo.setId(msgId);
		messagePo.setDelivered(0);
		messagePo.setFromId(requestImMsg.getFromId());
		messagePo.setToId(requestImMsg.getToId());
		messagePo.setMsgType(requestImMsg.getType());
		messagePo.setContent(requestImMsg.getContent());
		messageMapper.insert(messagePo);
		return msgId;
	}

	public Long saveOfflineMessage(SingleChat.SingleChatRequest requestImMsg) {
		Long msgId = idGenerator.nextId();
		OfflineMessagePo offlineMessagePo = new OfflineMessagePo();
		offlineMessagePo.setId(msgId);
		offlineMessagePo.setFromId(requestImMsg.getFromId());
		offlineMessagePo.setToId(requestImMsg.getToId());
		offlineMessagePo.setMsgType(requestImMsg.getType());
		offlineMessagePo.setContent(requestImMsg.getContent());
		offlineMessageMapper.insert(offlineMessagePo);
		return msgId;
	}

	public void markSingleMessageDelivered(long msgId) {
		messageMapper.markMessageDelivered(msgId);
		log.debug("mark msg {} is delivered", msgId);
	}

	public void updateLastReceivedMsg(GroupChat.GroupChatResponse groupChatResponse) {
		LastReceivedMessageBo bo = new LastReceivedMessageBo();
		bo.setUserId(groupChatResponse.getFromId());
		bo.setGroupId(groupChatResponse.getGroupId());
		bo.setLastReceivedMsgId(groupChatResponse.getMsgId());
		syncGroupMessageMapper.updateLastReceivedMessage(bo);
	}

	public Long saveGroupMessage(GroupChat.GroupChatRequest groupChatRequest) {
		GroupMessagePo messagePo = new GroupMessagePo();
		Long msgId = idGenerator.nextId();
		messagePo.setId(msgId);
		messagePo.setContent(groupChatRequest.getContent());
		messagePo.setFromId(groupChatRequest.getFromId());
		messagePo.setMsgType(groupChatRequest.getType());
		messagePo.setGroupId(groupChatRequest.getGroupId());
		groupMessageMapper.insert(messagePo);
		return msgId;
	}

	public void sendMessageToGroupMember(Base.Message message) {
		GroupChat.GroupChatRequest chatRequest = message.getGroupChatRequest();
		List<GroupUserPo> groupUserPos = groupUserMapper.selectList(new QueryWrapper<GroupUserPo>()
				.eq("group_id", chatRequest.getGroupId()));
		Long fromUserId = chatRequest.getFromId();
		List<Long> groupMembers = groupUserPos.stream().map(GroupUserPo::getUserId).collect(Collectors.toList());
		groupMembers.stream()
				.filter(member -> !member.equals(fromUserId))
				.forEach(memberId -> commonMessageSender.sendNotify(memberId, message));
	}
}
