package com.jiangfucheng.im.client.bo;

import com.jiangfucheng.im.common.enums.MessageType;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/3
 * Time: 10:39
 *
 * @author jiangfucheng
 */
@Data
public class SingleMessageBo {
	private Long msgId;
	private Long fromId;
	private Long toId;
	private MessageType messageType;
	private String content;
	private Date time;
}
