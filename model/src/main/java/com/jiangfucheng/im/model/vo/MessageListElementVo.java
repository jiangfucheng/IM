package com.jiangfucheng.im.model.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 0:05
 *
 * @author jiangfucheng
 */
@Data
public class MessageListElementVo {
	private Integer type;
	private Long lastMsgId;
	private String lastMsg;
	private Integer fromId;
	private String fromName;
	private String profilePhoto;
	private Long lastMsgTime;
	private Integer unreadMsgCount;
}
