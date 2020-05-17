package com.jiangfucheng.im.httpserver.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:58
 *
 * @author jiangfucheng
 */
@Data
public class GroupInfoVo {
	private Long id;
	private String profilePhoto;
	private String name;
	private Long createUser;
	private String createUserName;
	private String introduction;
	private Long createTime;

}
