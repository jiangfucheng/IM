package com.jiangfucheng.im.httpserver.bo;

import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import com.jiangfucheng.im.httpserver.vo.GroupMemberVo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:04
 *
 * @author jiangfucheng
 */
@Data
public class GroupMemberBo {
	private Long id;
	private String account;
	private String remarks;
	private Integer role;
	private Integer isFriend;

	public GroupMemberVo convertToGroupMemberVo() {
		GroupMemberVo vo = new GroupMemberVo();
		BeanUtil.copyProperties(vo, this);
		return vo;
	}
}
