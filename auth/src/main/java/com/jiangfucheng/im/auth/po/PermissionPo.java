package com.jiangfucheng.im.auth.po;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 10:17
 *
 * @author jiangfucheng
 */
@Data
public class PermissionPo {
	private Long id;
	private Long resourceId;
	private String operation;
	private String resourceName;
	private Long createTime;
}
