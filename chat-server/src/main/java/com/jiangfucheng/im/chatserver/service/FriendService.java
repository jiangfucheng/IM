package com.jiangfucheng.im.chatserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiangfucheng.im.chatserver.mapper.RelationMapper;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.model.po.RelationPo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 23:31
 *
 * @author jiangfucheng
 */
@Service
public class FriendService {
	private RelationMapper relationMapper;

	public FriendService(RelationMapper relationMapper) {
		this.relationMapper = relationMapper;
	}

	public void deleteFriend(Base.Message message) {
		Control.DeleteFriendRequest deleteFriendRequest = message.getDeleteFriendRequest();
		Long userId = JwtUtil.getTokenBody(deleteFriendRequest.getToken()).getUserId();
		Long friendId = deleteFriendRequest.getFriendId();
		relationMapper.delete(new QueryWrapper<RelationPo>()
				.eq("user_id", userId)
				.eq("friend_id", friendId));
	}
}
