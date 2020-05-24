package com.jiangfucheng.im.httpserver.service.impl;

import com.jiangfucheng.im.model.bo.AccountIntroductionBo;
import com.jiangfucheng.im.httpserver.mapper.GroupInfoMapper;
import com.jiangfucheng.im.httpserver.mapper.UserMapper;
import com.jiangfucheng.im.httpserver.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:48
 *
 * @author jiangfucheng
 */
@Service
public class AccountServiceImpl implements AccountService {
	private final UserMapper userMapper;
	private final GroupInfoMapper groupInfoMapper;

	public AccountServiceImpl(UserMapper userMapper, GroupInfoMapper groupInfoMapper) {
		this.userMapper = userMapper;
		this.groupInfoMapper = groupInfoMapper;
	}

	@Override
	public List<AccountIntroductionBo> queryAccountById(Long accountId) {
		return null;
	}
}
