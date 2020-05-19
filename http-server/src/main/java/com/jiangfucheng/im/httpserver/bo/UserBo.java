package com.jiangfucheng.im.httpserver.bo;

import com.jiangfucheng.im.httpserver.enums.AccountType;
import com.jiangfucheng.im.httpserver.po.UserPo;
import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import com.jiangfucheng.im.httpserver.vo.AccountVo;
import com.jiangfucheng.im.httpserver.vo.UserVo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:37
 *
 * @author jiangfucheng
 */
@Data
public class UserBo {
	private Long id;
	private String account;
	private String nickName;
	private Integer sex;
	private Long birthday;
	private String profilePhoto;
	private String signature;
	private String phone;
	private String password;
	private String email;
	private String school;
	private String country;
	private String city;


	public UserPo convertToUserPo() {
		UserPo userPo = new UserPo();
		BeanUtil.copyProperties(userPo, this);
		return userPo;
	}

	public UserVo convertToUserVo() {
		UserVo userVo = new UserVo();
		BeanUtil.copyProperties(userVo, this);
		return userVo;
	}

	public AccountVo convertToAccountVo() {
		AccountVo accountVo = new AccountVo();
		accountVo.setId(this.id);
		accountVo.setName(this.getNickName());
		accountVo.setProfilePhoto(this.getProfilePhoto());
		accountVo.setType(AccountType.USER.code);
		return accountVo;
	}
}
