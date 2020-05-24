package com.jiangfucheng.im.model.bo;

import com.jiangfucheng.im.model.utils.BeanUtil;
import com.jiangfucheng.im.model.vo.GroupAnnouncementVo;
import com.jiangfucheng.im.model.vo.GroupInfoVo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:55
 *
 * @author jiangfucheng
 */
@Data
public class GroupAnnouncementBo {
	private Long id;
	private Long groupId;
	private String title;
	private String content;
	private UserBo createUser;
	//如果与自己是好友，则备注不为空
	private String createUserRemarks;
	private Long createTime;

	public GroupAnnouncementVo convertToGroupAnnouncementVo() {
		GroupAnnouncementVo vo = new GroupAnnouncementVo();
		BeanUtil.copyProperties(vo, this);
		vo.setCreateUserName(this.getCreateUserRemarks() != null ? this.getCreateUserRemarks() : createUser.getNickName());
		return vo;
	}

	public GroupInfoVo convertToGroupInfoVo(String createUserName) {
		GroupInfoVo groupInfoVo = new GroupInfoVo();
		BeanUtil.copyProperties(groupInfoVo, this);
		groupInfoVo.setCreateUserName(createUserName);
		return groupInfoVo;
	}
}
