package com.jiangfucheng.im.model.bo;

import com.jiangfucheng.im.model.utils.BeanUtil;
import com.jiangfucheng.im.model.enums.AccountType;
import com.jiangfucheng.im.model.po.UserPo;
import com.jiangfucheng.im.model.vo.AccountVo;
import com.jiangfucheng.im.model.vo.UserVo;
import lombok.Data;

import java.util.Date;

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
	private Integer isFriend;


	public UserPo convertToUserPo() {
		UserPo userPo = new UserPo();
		BeanUtil.copyProperties(userPo, this);
		userPo.setBirthday(new Date(this.getBirthday()));
		return userPo;
	}

	public UserVo convertToUserVo() {
		UserVo userVo = new UserVo();
		BeanUtil.copyProperties(userVo, this);
		userVo.setPassword(null);
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
