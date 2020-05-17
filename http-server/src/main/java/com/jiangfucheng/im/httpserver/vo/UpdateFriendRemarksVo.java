package com.jiangfucheng.im.httpserver.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 0:11
 *
 * @author jiangfucheng
 */
@Data
public class UpdateFriendRemarksVo {
	private Long friendId;
	private String remarks;
}
