package com.jiangfucheng.im.httpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiangfucheng.im.httpserver.bo.*;
import com.jiangfucheng.im.httpserver.mapper.*;
import com.jiangfucheng.im.httpserver.po.*;
import com.jiangfucheng.im.httpserver.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:49
 *
 * @author jiangfucheng
 */
@Service
public class GroupServiceImpl implements GroupService {
	private final GroupInfoMapper groupMapper;
	private final UserMapper userMapper;
	private final RelationMapper relationMapper;
	private final GroupAnnouncementMapper announcementMapper;
	private final GroupUserMapper groupUserMapper;

	public GroupServiceImpl(GroupInfoMapper groupMapper,
							UserMapper userMapper,
							RelationMapper relationMapper,
							GroupAnnouncementMapper announcementMapper,
							GroupUserMapper groupUserMapper) {
		this.groupMapper = groupMapper;
		this.userMapper = userMapper;
		this.relationMapper = relationMapper;
		this.announcementMapper = announcementMapper;
		this.groupUserMapper = groupUserMapper;
	}

	@Override
	public List<GroupBo> queryGroupsWithUser(Long userId) {
		List<GroupInfoPo> groupInfos = groupMapper.selectList(new QueryWrapper<GroupInfoPo>()
				.eq("create_user", userId));
		return groupInfos.stream().map(po -> {
			GroupBo bo = po.convertToGroupBo();
			UserPo userPo = userMapper.selectById(bo.getCreateUser().getId());
			UserBo userBo = userPo.convertToUserBo();
			bo.setCreateUser(userBo);
			setRemarksToGroupBo(bo, userId);
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public GroupBo getGroupByAccount(Long account) {
		GroupInfoPo groupPo = groupMapper.selectOne(new QueryWrapper<GroupInfoPo>()
				.eq("id", account));
		GroupBo groupBo = groupPo.convertToGroupBo();
		UserBo userBo = new UserBo();
		userBo.setId(groupPo.getCreateUser());
		groupBo.setCreateUser(userBo);
		return groupBo;
	}

	@Override
	public List<GroupAnnouncementBo> queryAnnouncement(Long userId, Long groupId) {
		List<GroupAnnouncementPo> announcementPos = announcementMapper.selectList(new QueryWrapper<GroupAnnouncementPo>()
				.eq("group_id", groupId));
		return announcementPos.stream().map(po -> {
			GroupAnnouncementBo bo = po.convertToGroupAnnouncementBo();
			UserInfoBo userInfo = userMapper.getUserInfoById(userId);
			bo.getCreateUser().setAccount(userInfo.getAccount());
			bo.getCreateUser().setNickName(userInfo.getNickName());
			RelationPo relationPo = relationMapper.selectOne(new QueryWrapper<RelationPo>()
					.eq("user_id", userId)
					.eq("friend_id", bo.getCreateUser().getId()));
			if (relationPo != null)
				bo.setCreateUserRemarks(relationPo.getRemarks());
			return bo;
		}).collect(Collectors.toList());
	}

	@Override
	public GroupBo queryGroupDetail(Long queryUserId, Long groupId) {
		GroupInfoPo groupInfoPo = groupMapper.selectById(groupId);
		GroupBo groupBo = groupInfoPo.convertToGroupBo();
		UserBo userBo = userMapper.selectById(groupInfoPo.getCreateUser()).convertToUserBo();
		groupBo.setCreateUser(userBo);
		setRemarksToGroupBo(groupBo, queryUserId);
		return groupBo;
	}

	@Override
	public List<GroupMemberBo> queryGroupMembers(Long userId, Long groupId) {
		List<GroupUserPo> groupUserPos = groupUserMapper.selectList(new QueryWrapper<GroupUserPo>()
				.eq("group_id", groupId));
		return groupUserPos.stream().map(po -> {
			GroupMemberBo groupMemberBo = po.convertToGroupMemberBo();
			RelationPo relationPo = relationMapper.getRelationByUserIdAndFriendid(userId, groupMemberBo.getId());
			if (relationPo != null) {
				groupMemberBo.setIsFriend(1);
				groupMemberBo.setRemarks(relationPo.getRemarks());
			} else {
				groupMemberBo.setIsFriend(0);
			}
			return groupMemberBo;
		}).collect(Collectors.toList());
	}

	private void setRemarksToGroupBo(GroupBo groupBo, Long queryUserId) {
		RelationPo relationPo = relationMapper.getRelationByUserIdAndFriendid(queryUserId, groupBo.getCreateUser().getId());
		if (relationPo != null)
			groupBo.setCreateUserRemarks(relationPo.getRemarks());
	}
}
