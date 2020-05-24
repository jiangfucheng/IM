package com.jiangfucheng.im.httpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiangfucheng.im.model.bo.MessageBo;
import com.jiangfucheng.im.model.bo.NotifyBo;
import com.jiangfucheng.im.model.bo.OneMessageListBo;
import com.jiangfucheng.im.model.bo.QueryHistoryMsgBo;
import com.jiangfucheng.im.httpserver.mapper.*;
import com.jiangfucheng.im.model.po.*;
import com.jiangfucheng.im.httpserver.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:49
 *
 * @author jiangfucheng
 */
@Service
public class MessageServiceImpl implements MessageService {
	private MessageMapper messageMapper;
	private RecentlyChatFriendsMapper recentlyChatFriendsMapper;
	private RelationMapper relationMapper;
	private OfflineMessageMapper offlineMessageMapper;
	private UserMapper userMapper;
	private GroupInfoMapper groupInfoMapper;
	private GroupMessageMapper groupMessageMapper;
	private SyncGroupMessageMapper syncGroupMessageMapper;
	private RecentlyNotifyMapper recentlyNotifyMapper;
	private NotifyMapper notifyMapper;

	public MessageServiceImpl(MessageMapper messageMapper,
							  RecentlyChatFriendsMapper recentlyChatFriendsMapper,
							  RelationMapper relationMapper,
							  OfflineMessageMapper offlineMessageMapper,
							  UserMapper userMapper,
							  GroupInfoMapper groupInfoMapper, GroupMessageMapper groupMessageMapper,
							  SyncGroupMessageMapper syncGroupMessageMapper,
							  RecentlyNotifyMapper recentlyNotifyMapper,
							  NotifyMapper notifyMapper) {
		this.messageMapper = messageMapper;
		this.recentlyChatFriendsMapper = recentlyChatFriendsMapper;
		this.relationMapper = relationMapper;
		this.offlineMessageMapper = offlineMessageMapper;
		this.userMapper = userMapper;
		this.groupInfoMapper = groupInfoMapper;
		this.groupMessageMapper = groupMessageMapper;
		this.syncGroupMessageMapper = syncGroupMessageMapper;
		this.recentlyNotifyMapper = recentlyNotifyMapper;
		this.notifyMapper = notifyMapper;
	}

	@Override
	public List<OneMessageListBo> getMessageList(Long userId) {
		List<RecentlyChatFriendsPo> recentlyChatFriendsPoList = recentlyChatFriendsMapper.selectList(new QueryWrapper<RecentlyChatFriendsPo>()
				.eq("user_id", userId));
		return recentlyChatFriendsPoList.stream().map(po -> {
			OneMessageListBo oneMessageListBo = new OneMessageListBo();
			oneMessageListBo.setType(po.getType());
			oneMessageListBo.setFromId(po.getFromId());
			if (po.getType() == 0) {
				//好友发来的消息
				RelationPo relationPo = relationMapper.selectOne(new QueryWrapper<RelationPo>()
						.eq("user_id", userId)
						.eq("friend_id", po.getFromId()));
				oneMessageListBo.setFromName(relationPo.getRemarks());
				UserPo fromUser = userMapper.selectById(po.getFromId());
				oneMessageListBo.setProfilePhoto(fromUser.getProfilePhoto());
				MessagePo lastOnlineMessage = messageMapper.selectLastMessage(userId, po.getFromId());
				OfflineMessagePo lastOfflineMessage = offlineMessageMapper.selectLastOfflineMessage(userId, po.getFromId());
				if (lastOfflineMessage != null && (lastOnlineMessage.getId() < lastOfflineMessage.getId())) {
					oneMessageListBo.setLastMsg(lastOfflineMessage.getContent());
					oneMessageListBo.setLastMsgId(lastOfflineMessage.getId());
					oneMessageListBo.setLastMsgTime(lastOfflineMessage.getCreateTime().getTime());
					oneMessageListBo.setLastMsgType(lastOfflineMessage.getMsgType());
				} else if (lastOnlineMessage != null) {
					oneMessageListBo.setLastMsg(lastOnlineMessage.getContent());
					oneMessageListBo.setLastMsgId(lastOnlineMessage.getId());
					oneMessageListBo.setLastMsgTime(lastOnlineMessage.getCreateTime().getTime());
					oneMessageListBo.setLastMsgType(lastOnlineMessage.getMsgType());
				} else {
					return null;
				}
				Integer onlineWithUnreadMsg = messageMapper.selectCount(new QueryWrapper<MessagePo>()
						.eq("delivered", 0)
						.eq("from_id", po.getFromId()));
				Integer offlineWithUnreadMsg = offlineMessageMapper.selectCount(new QueryWrapper<OfflineMessagePo>()
						.eq("from_id", po.getFromId()));
				oneMessageListBo.setUnreadMsgCount(onlineWithUnreadMsg + offlineWithUnreadMsg);
			} else {
				//群消息
				GroupInfoPo groupInfoPo = groupInfoMapper.selectById(po.getFromId());
				if (groupInfoPo == null) return null;
				oneMessageListBo.setProfilePhoto(groupInfoPo.getProfilePhoto());
				oneMessageListBo.setFromName(groupInfoPo.getName());
				GroupMessagePo groupMessagePo = groupMessageMapper.selectLastMessage(po.getFromId());
				oneMessageListBo.setLastMsgId(groupMessagePo.getId());
				oneMessageListBo.setLastMsg(groupMessagePo.getContent());
				oneMessageListBo.setLastMsgType(groupMessagePo.getMsgType());
				oneMessageListBo.setLastMsgTime(groupMessagePo.getCreateTime().getTime());

				SyncGroupMessagePo syncGroupMessagePo = syncGroupMessageMapper.selectOne(new QueryWrapper<SyncGroupMessagePo>()
						.eq("group_id", groupInfoPo.getId())
						.eq("user_id", userId));
				Integer unreadMessage = groupMessageMapper.selectCount(new QueryWrapper<GroupMessagePo>()
						.le("id", syncGroupMessagePo.getLastMsgId())
						.eq("group_id", groupInfoPo.getId()));
				oneMessageListBo.setUnreadMsgCount(unreadMessage);
			}
			return oneMessageListBo;
		}).filter(Objects::nonNull).collect(Collectors.toList());

	}

	@Override
	public List<MessageBo> queryHistoryMessage(QueryHistoryMsgBo queryHistoryMsgBo) {

		return null;
	}

	@Override
	public List<NotifyBo> queryNotifiesWthUser(Long userId) {
		List<NotifyPo> recentlyNotifies = notifyMapper.selectRecentlyNotifies(userId);
		return recentlyNotifies.stream().map(notifyPo -> {
			NotifyBo notifyBo = notifyPo.convertToNotifyBo();
			UserPo userPo = userMapper.selectById(notifyBo.getFromId());
			if (userPo != null) {
				RelationPo relationPo = relationMapper.getRelationByUserIdAndFriendId(userId, notifyBo.getFromId());
				notifyBo.setName(relationPo != null ? relationPo.getRemarks() : userPo.getNickName());
				notifyBo.setProfilePhoto(userPo.getProfilePhoto());
			} else {
				GroupInfoPo groupInfoPo = groupInfoMapper.selectById(notifyBo.getFromId());
				notifyBo.setName(groupInfoPo.getName());
				notifyBo.setProfilePhoto(groupInfoPo.getProfilePhoto());
			}
			return notifyBo;
		}).collect(Collectors.toList());
	}
}
