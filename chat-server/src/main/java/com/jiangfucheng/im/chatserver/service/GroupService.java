package com.jiangfucheng.im.chatserver.service;

import com.jiangfucheng.im.chatserver.mapper.GroupInfoMapper;
import com.jiangfucheng.im.chatserver.mapper.GroupUserMapper;
import com.jiangfucheng.im.chatserver.mapper.UserMapper;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.model.po.GroupInfoPo;
import com.jiangfucheng.im.model.po.GroupUserPo;
import com.jiangfucheng.im.protobuf.Control;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/26
 * Time: 22:37
 *
 * @author jiangfucheng
 */
@Service
public class GroupService {
	private GroupUserMapper groupUserMapper;
	private GroupInfoMapper groupInfoMapper;
	private SnowFlakeIdGenerator idGenerator;
	private UserMapper userMapper;

	public GroupService(GroupUserMapper groupUserMapper, GroupInfoMapper groupInfoMapper, SnowFlakeIdGenerator idGenerator, UserMapper userMapper) {
		this.groupUserMapper = groupUserMapper;
		this.groupInfoMapper = groupInfoMapper;
		this.idGenerator = idGenerator;
		this.userMapper = userMapper;
	}

	public List<Long> getOwnerByGroupId(Long groupId) {
		return groupUserMapper.getGroupOwnerAndManagersByGroupId(groupId);
	}

	public Long createGroupAndInviteMember(Control.CreateGroupRequest createGroupRequest) {
		GroupInfoPo groupInfoPo = new GroupInfoPo();
		Long groupId = idGenerator.nextId();
		Long createUserId = JwtUtil.getTokenBody(createGroupRequest.getToken()).getUserId();
		groupInfoPo.setId(groupId);
		groupInfoPo.setCreateUserId(createUserId);
		groupInfoPo.setProfilePhoto(createGroupRequest.getProfilePhoto());
		groupInfoPo.setIntroduction(createGroupRequest.getIntroduction());
		groupInfoPo.setName(createGroupRequest.getGroupName());
		groupInfoMapper.insert(groupInfoPo);
		List<Long> members = createGroupRequest.getInviteUserIdList();
		GroupUserPo groupOwner = new GroupUserPo(idGenerator.nextId(), createUserId, groupId, 0, userMapper.selectById(createUserId).getNickName(), null);
		groupUserMapper.insert(groupOwner);
		members.forEach(memberId -> {
			GroupUserPo groupUserPo = new GroupUserPo();
			groupUserPo.setId(idGenerator.nextId());
			groupUserPo.setGroupId(groupId);
			groupUserPo.setUserId(memberId);
			groupUserPo.setRole(2);
			groupUserPo.setRemarks(userMapper.selectById(memberId).getNickName());
			groupUserMapper.insert(groupUserPo);
		});
		return groupId;
	}

}
