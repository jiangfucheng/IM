package com.jiangfucheng.im.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jiangfucheng.im.model.utils.BeanUtil;
import com.jiangfucheng.im.model.bo.GroupAnnouncementBo;
import com.jiangfucheng.im.model.bo.UserBo;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:07
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_group_announcement")
public class GroupAnnouncementPo {
	private Long id;
	private Long groupId;
	private String title;
	private String content;
	@TableField("create_user")
	private Long createUserId;
	private Date createTime;

	public GroupAnnouncementBo convertToGroupAnnouncementBo() {
		GroupAnnouncementBo bo = new GroupAnnouncementBo();
		BeanUtil.copyProperties(bo, this);
		UserBo userBo = new UserBo();
		userBo.setId(this.getCreateUserId());
		bo.setCreateUser(userBo);
		return bo;
	}
}
