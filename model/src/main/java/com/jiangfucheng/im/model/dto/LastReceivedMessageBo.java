package com.jiangfucheng.im.model.dto;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 18:39
 *
 * @author jiangfucheng
 */
@Data
public class LastReceivedMessageBo {
	private Long userId;
	private Long groupId;
	private Long lastReceivedMsgId;
}
