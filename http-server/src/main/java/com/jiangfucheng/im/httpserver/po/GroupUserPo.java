package com.jiangfucheng.im.httpserver.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jiangfucheng.im.httpserver.bo.GroupMemberBo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:02
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_group_user")
public class GroupUserPo {
	@TableId
	private Long userId;
	@TableId
	private Long groupId;
	private Integer role;
	private String remarks;
	private Long createTime;

	public GroupMemberBo convertToGroupMemberBo() {
		GroupMemberBo groupMemberBo = new GroupMemberBo();
		groupMemberBo.setId(this.getUserId());
		groupMemberBo.setRole(this.getRole());
		return groupMemberBo;
	}
}
