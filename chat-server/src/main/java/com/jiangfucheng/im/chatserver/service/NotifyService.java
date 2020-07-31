package com.jiangfucheng.im.chatserver.service;

import com.jiangfucheng.im.chatserver.mapper.GroupUserMapper;
import com.jiangfucheng.im.chatserver.mapper.NotifyMapper;
import com.jiangfucheng.im.common.enums.NotifyType;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.model.po.NotifyPo;
import com.jiangfucheng.im.protobuf.Control;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 23:49
 *
 * @author jiangfucheng
 */
@Service
public class NotifyService {
	private NotifyMapper notifyMapper;
	private SnowFlakeIdGenerator idGenerator;
	private GroupUserMapper groupUserMapper;

	public NotifyService(NotifyMapper notifyMapper, SnowFlakeIdGenerator idGenerator, GroupUserMapper groupUserMapper) {
		this.notifyMapper = notifyMapper;
		this.idGenerator = idGenerator;
		this.groupUserMapper = groupUserMapper;
	}

	public void saveAddFriendNotify(Control.AddFriendRequest addFriendRequest) {
		Long fromId = JwtUtil.getTokenBody(addFriendRequest.getToken()).getUserId();
		Long toId = addFriendRequest.getTargetId();
		NotifyPo notifyPo = new NotifyPo();
		notifyPo.setId(idGenerator.nextId());
		notifyPo.setFromId(fromId);
		notifyPo.setToId(toId);
		notifyPo.setContent(addFriendRequest.getAuthMessage());
		notifyPo.setType(NotifyType.REQUEST.code());
		notifyMapper.insert(notifyPo);
	}

	public void saveDeleteFriendNotify(Control.DeleteFriendRequest deleteFriendRequest) {
		Long fromId = JwtUtil.getTokenBody(deleteFriendRequest.getToken()).getUserId();
		Long toId = deleteFriendRequest.getFriendId();
		NotifyPo notifyPo = new NotifyPo();
		notifyPo.setId(idGenerator.nextId());
		notifyPo.setFromId(fromId);
		notifyPo.setToId(toId);
		notifyPo.setContent("您已经被好友删除");
		notifyPo.setType(NotifyType.REQUEST.code());
		notifyMapper.insert(notifyPo);
	}

	public void saveAddGroupNotify(Control.AddGroupRequest addGroupRequest) {
		List<Long> ownerAndManagersId = groupUserMapper.getGroupOwnerAndManagersByGroupId(addGroupRequest.getGroupId());
		Long fromId = JwtUtil.getTokenBody(addGroupRequest.getToken()).getUserId();
		ownerAndManagersId.forEach(id -> {
			NotifyPo notifyPo = new NotifyPo();
			notifyPo.setId(idGenerator.nextId());
			notifyPo.setFromId(fromId);
			notifyPo.setToId(id);
			notifyPo.setContent("申请加入群聊");
			notifyPo.setType(NotifyType.REQUEST.code());
			notifyMapper.insert(notifyPo);
		});
	}

	public void saveInviteToGroupNotify(Control.CreateGroupRequest createGroupRequest) {
		Long fromId = JwtUtil.getTokenBody(createGroupRequest.getToken()).getUserId();
		createGroupRequest.getInviteUserIdList().forEach(userId -> {
			NotifyPo notifyPo = new NotifyPo();
			notifyPo.setId(idGenerator.nextId());
			notifyPo.setFromId(fromId);
			notifyPo.setToId(userId);
			notifyPo.setContent("邀请您加入群聊");
			notifyPo.setType(NotifyType.REQUEST.code());
			notifyMapper.insert(notifyPo);
		});
	}
}
