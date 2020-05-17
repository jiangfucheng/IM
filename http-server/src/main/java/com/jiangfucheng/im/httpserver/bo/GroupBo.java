package com.jiangfucheng.im.httpserver.bo;

import com.jiangfucheng.im.httpserver.enums.AccountType;
import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import com.jiangfucheng.im.httpserver.vo.AccountVo;
import com.jiangfucheng.im.httpserver.vo.GroupInfoVo;
import com.jiangfucheng.im.httpserver.vo.GroupListElementVo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:45
 *
 * @author jiangfucheng
 */
@Data
public class GroupBo {
	private Long id;
	private String profilePhoto;
	private String name;
	private UserBo createUser;
	private String createUserRemarks;
	private String introduction;
	private Long createTime;

	public GroupListElementVo convertToGroupListElementVo() {
		GroupListElementVo groupListElementVo = new GroupListElementVo();
		BeanUtil.copyProperties(groupListElementVo, this);
		return groupListElementVo;
	}

	public GroupInfoVo convertToGroupInfoVo() {
		GroupInfoVo groupInfoVo = new GroupInfoVo();
		BeanUtil.copyProperties(groupInfoVo, this);
		groupInfoVo.setCreateUser(createUser.getId());
		groupInfoVo.setCreateUserName(this.getCreateUserRemarks() != null ? this.getCreateUserRemarks() : this.getCreateUser().getNickName());
		return groupInfoVo;
	}

	public AccountVo convertToAccountVo() {
		AccountVo accountVo = new AccountVo();
		accountVo.setType(AccountType.GROUP.code);
		accountVo.setId(this.getId());
		accountVo.setName(this.getName());
		accountVo.setProfilePhoto(this.profilePhoto);
		return accountVo;
	}
}
