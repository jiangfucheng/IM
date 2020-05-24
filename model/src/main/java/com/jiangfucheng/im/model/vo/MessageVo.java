package com.jiangfucheng.im.model.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 0:07
 *
 * @author jiangfucheng
 */
@Data
public class MessageVo {
	private Long id;
	private Long fromId;
	private String profilePhoto;
	private String nickName;
	private String remarks;
	private Integer msgType;
	private String content;
	private Long crateTime;
}
