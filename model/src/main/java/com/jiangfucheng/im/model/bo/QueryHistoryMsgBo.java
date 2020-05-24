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
	private Long userId;
	private Integer type;
	private Long targetId;
	private Long lastMsgId;
	private Integer number;
}
