package com.jiangfucheng.im.httpserver.dto;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 0:10
 *
 * @author jiangfucheng
 */
@Data
public class AnnouncementDto {
	private Long id;
	private Long groupId;
	private String title;
	private String content;
	private Long createUserId;
	private String createUserNickName;
	private String createUserRemarks;
	private Long createTime;
}
