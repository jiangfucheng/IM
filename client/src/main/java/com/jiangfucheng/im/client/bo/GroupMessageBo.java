package com.jiangfucheng.im.client.bo;

import com.jiangfucheng.im.common.enums.MessageType;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/3
 * Time: 10:39
 *
 * @author jiangfucheng
 */
@Data
public class GroupMessageBo {
	private Long msgId;
	private Long groupId;
	private Long fromId;
	private MessageType messageType;
	private String content;
}
