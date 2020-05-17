package com.jiangfucheng.im.httpserver.service;

import com.jiangfucheng.im.httpserver.bo.LoginRequestBo;
import com.jiangfucheng.im.httpserver.bo.UserBo;
import com.jiangfucheng.im.httpserver.bo.UserInfoBo;
import com.jiangfucheng.im.httpserver.bo.UserPasswordBo;
import com.jiangfucheng.im.httpserver.vo.LoginResponseVo;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:35
 *
 * @author jiangfucheng
 */
public interface UserService {
	/**
	 * 获取用户详情
	 *
	 * @param userId 用户id
	 */
	UserBo getUserById(Long userId);


	UserBo getUserByAccount(String account);

	/**
	 * 创建用户
	 *
	 * @param userBo 用户信息
	 * @return 用户id
	 */
	Long createUser(UserBo userBo);

	/**
	 * 更新用户信息
	 *
	 * @return 用户更新后的所有信息(不包含密码)
	 */
	UserBo updateUser(UserBo userBo);

	/**
	 * 更新用户密码
	 */
	void updatePassword(UserPasswordBo userPasswordBo);

	/**
	 * 查询用户是否存在
	 */
	boolean hasUser(LoginRequestBo loginBo);

	/**
	 * 根据账号查找id
	 */
	UserInfoBo getUserInfoByAccount(String account);


}
