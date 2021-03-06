package com.jiangfucheng.im.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jiangfucheng.im.model.utils.BeanUtil;
import com.jiangfucheng.im.model.bo.GroupBo;
import com.jiangfucheng.im.model.bo.UserBo;
import lombok.Data;

import java.util.Date;

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
	@TableField("create_user")
	private Long createUserId;
	private String introduction;
	private Date createTime;

	public GroupBo convertToGroupBo() {
		GroupBo bo = new GroupBo();
		BeanUtil.copyProperties(bo,this);
		UserBo createUser = new UserBo();
		createUser.setId(this.getCreateUserId());
		bo.setCreateUser(createUser);
		bo.setCreateTime(this.getCreateTime().getTime());
		return bo;
	}
}
