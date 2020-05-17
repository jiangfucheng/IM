package com.jiangfucheng.im.httpserver.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:55
 *
 * @author jiangfucheng
 */
@Data
public class GroupAnnouncementVo {
	private Long id;
	private String title;
	private String content;
	private Long createUser;
	private String createUserName;
	private Long createTime;
}
