package com.jiangfucheng.im.httpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiangfucheng.im.httpserver.bo.FriendBo;
import com.jiangfucheng.im.httpserver.bo.FriendRemarksBo;
import com.jiangfucheng.im.httpserver.mapper.RelationMapper;
import com.jiangfucheng.im.httpserver.mapper.UserMapper;
import com.jiangfucheng.im.httpserver.po.RelationPo;
import com.jiangfucheng.im.httpserver.po.UserPo;
import com.jiangfucheng.im.httpserver.service.FriendRelationService;
import org.springframework.data.redis.core.RedisTemplate;
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
public class FriendRelationServiceImpl implements FriendRelationService {
	private final RelationMapper relationMapper;
	private final UserMapper userMapper;
	private final RedisTemplate redis;


	public FriendRelationServiceImpl(RelationMapper relationMapper, UserMapper userMapper, RedisTemplate redis) {
		this.relationMapper = relationMapper;
		this.userMapper = userMapper;
		this.redis = redis;
	}

	@Override
	public void updateFriendRemarks(FriendRemarksBo friendRemarksBo) {
		relationMapper.updateFriendRemarks(friendRemarksBo);
	}

	@Override
	public List<FriendBo> getFriendsWithUser(Long userId) {
		List<RelationPo> relationPos = relationMapper.selectList(new QueryWrapper<RelationPo>()
				.eq("user_id", userId));
		return relationPos.stream().map(po -> {
			FriendBo friendBo = new FriendBo();
			UserPo friend = userMapper.selectById(po.getFriendId());
			friendBo.setId(friend.getId());
			friendBo.setAccount(friend.getAccount());
			friendBo.setNickName(friend.getNickName());
			friendBo.setRemarks(po.getRemarks());
			friendBo.setProfilePhoto(friend.getProfilePhoto());
			friendBo.setSignature(friend.getSignature());
			/*UserStatusBo userStatusBo = (UserStatusBo) redis.opsForValue().get(String.format(RedisConstants.USER_STATUS_KEY, friend.getId()));
			if (userStatusBo != null) {
				friendBo.setStatus(1);
			} else {
				friendBo.setStatus(0);
			}*/
			friendBo.setStatus(1);
			return friendBo;
		}).collect(Collectors.toList());
	}
}
