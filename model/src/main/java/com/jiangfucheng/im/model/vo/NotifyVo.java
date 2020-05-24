package com.jiangfucheng.im.model.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 0:09
 *
 * @author jiangfucheng
 */
@Data
public class NotifyVo {
	private Long id;
	private Integer type;
	private Long fromId;
	private String name;
	private String profilePhoto;
	private String content;
	private String verifyMessage;
	private Long createTime;
}
