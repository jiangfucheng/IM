package com.jiangfucheng.im.model.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 0:03
 *
 * @author jiangfucheng
 */
@Data
public class GroupMemberVo {
	private Long id;
	private String account;
	private String remarks;
	private Integer role;
	private Integer isFriend;
}
