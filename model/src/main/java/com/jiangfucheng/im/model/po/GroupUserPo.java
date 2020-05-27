package com.jiangfucheng.im.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jiangfucheng.im.model.bo.GroupMemberBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:02
 *
 * @author jiangfucheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("im_group_user")
public class GroupUserPo {
	@TableId
	private Long id;
	private Long userId;
	private Long groupId;
	private Integer role;
	private String remarks;
	private Date createTime;

	public GroupMemberBo convertToGroupMemberBo() {
		GroupMemberBo groupMemberBo = new GroupMemberBo();
		groupMemberBo.setId(this.getUserId());
		groupMemberBo.setRole(this.getRole());
		groupMemberBo.setRemarks(this.getRemarks());
		return groupMemberBo;
	}
}
