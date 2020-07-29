package com.jiangfucheng.im.model.bo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:15
 *
 * @author jiangfucheng
 */
@Data
public class QueryHistoryMsgBo {
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 目标类型 @0:好友 @1:群
	 */
	private Integer type;
	/**
	 * 目标id
	 */
	private Long targetId;
	/**
	 * 从哪条消息开始拉取，如果为空则从最后开始拉取
	 */
	private Long lastMsgId;
	/**
	 * 拉取消息的条数
	 */
	private Integer number;
}
