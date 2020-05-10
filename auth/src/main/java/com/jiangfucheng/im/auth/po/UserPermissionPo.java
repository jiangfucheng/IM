package com.jiangfucheng.im.auth.po;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 10:19
 *
 * @author jiangfucheng
 */
@Data
public class UserPermissionPo {
	private Long id;
	private Long userId;
	private Long permissionId;
	private Long createTime;
}
