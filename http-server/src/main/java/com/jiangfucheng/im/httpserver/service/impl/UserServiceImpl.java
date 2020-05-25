package com.jiangfucheng.im.httpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.httpserver.exceptions.IMException;
import com.jiangfucheng.im.httpserver.mapper.RelationMapper;
import com.jiangfucheng.im.httpserver.mapper.UserMapper;
import com.jiangfucheng.im.httpserver.service.UserService;
import com.jiangfucheng.im.model.bo.LoginRequestBo;
import com.jiangfucheng.im.model.bo.UserBo;
import com.jiangfucheng.im.model.bo.UserInfoBo;
import com.jiangfucheng.im.model.bo.UserPasswordBo;
import com.jiangfucheng.im.model.po.RelationPo;
import com.jiangfucheng.im.model.po.UserPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:48
 *
 * @author jiangfucheng
 */
@Service
public class UserServiceImpl implements UserService {
	private final UserMapper userMapper;
	private final SnowFlakeIdGenerator idGenerator;
	private final RelationMapper relationMapper;

	@Autowired
	public UserServiceImpl(UserMapper userMapper, SnowFlakeIdGenerator idGenerator, RelationMapper relationMapper) {
		this.userMapper = userMapper;
		this.idGenerator = idGenerator;
		this.relationMapper = relationMapper;
	}


	@Override
	public UserBo getUserById(Long curId, Long userId) {
		UserBo userBo = userMapper.selectById(userId).convertToUserBo();
		RelationPo relationPo = relationMapper.getRelationByUserIdAndFriendId(curId, userId);
		userBo.setIsFriend(relationPo == null ? 0 : 1);
		return userBo;
	}

	@Override
	public UserBo getUserByAccount(String account) {
		UserPo userPo = userMapper.selectOne(new QueryWrapper<UserPo>()
				.eq("account", account));

		return userPo == null ? null : userPo.convertToUserBo();
	}

	@Override
	public Long createUser(UserBo userBo) {
		if (userMapper.getUserInfoByAccount(userBo.getAccount()) != null) {
			throw new IMException(ErrorCode.USER_EXISTED, "该账号已经被注册");
		}
		UserPo userPo = userBo.convertToUserPo();
		Long userId = idGenerator.nextId();
		userPo.setId(userId);
		userMapper.insert(userPo);
		return userId;
	}

	@Override
	public UserBo updateUser(UserBo userBo) {
		userMapper.updateById(userBo.convertToUserPo());
		return userMapper.selectById(userBo.getId()).convertToUserBo();
	}

	@Override
	public void updatePassword(UserPasswordBo userPasswordBo) {
		if (!userMapper.getPasswordById(userPasswordBo.getId()).equals(userPasswordBo.getOldPassword())) {
			throw new IMException(ErrorCode.PARAM_ERROR, "密码错误");
		}
		int effect = userMapper.updatePassword(userPasswordBo.getId(), userPasswordBo.getNewPassword());
		if (effect != 1)
			throw new IMException(ErrorCode.RUNTIME_ERROR, "更新密码失败");
	}

	@Override
	public boolean hasUser(LoginRequestBo loginBo) {
		Long userId = userMapper.hasUser(loginBo.getAccount(), loginBo.getPassword());
		return userId != null;
	}

	@Override
	public UserInfoBo getUserInfoByAccount(String account) {
		UserInfoBo userInfoBo = userMapper.getUserInfoByAccount(account);
		if (userInfoBo == null)
			throw new IMException(ErrorCode.RUNTIME_ERROR, "该用户不存在");
		return userInfoBo;
	}
}
