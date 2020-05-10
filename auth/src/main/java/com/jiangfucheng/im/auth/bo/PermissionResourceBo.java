package com.jiangfucheng.im.auth.bo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 22:59
 *
 * @author jiangfucheng
 */
@Data
public class PermissionResourceBo {
	private Long userId;
	private String resourceName;
	private Long resourceId;
}
