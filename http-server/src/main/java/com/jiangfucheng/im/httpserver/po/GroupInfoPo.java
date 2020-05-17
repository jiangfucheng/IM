package com.jiangfucheng.im.httpserver.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jiangfucheng.im.httpserver.bo.GroupBo;
import com.jiangfucheng.im.httpserver.bo.UserBo;
import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:00
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_group_info")
public class GroupInfoPo {
	private Long id;
	private String profilePhoto;
	private String name;
	private Long createUser;
	private String introduction;
	private Long createTime;

	public GroupBo convertToGroupBo() {
		GroupBo bo = new GroupBo();
		BeanUtil.copyProperties(bo,this);
		UserBo createUser = new UserBo();
		createUser.setId(this.getId());
		bo.setCreateUser(createUser);
		return bo;
	}
}
