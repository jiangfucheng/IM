package com.jiangfucheng.im.auth.vo;

import com.jiangfucheng.im.auth.bo.PermissionResourceBo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 22:56
 *
 * @author jiangfucheng
 */
@Data
public class PermissionResourceVo {
	private Long resourceId;
	private String resourceName;

	public PermissionResourceBo convertToPermissionResourceBo(Long userId) {
		PermissionResourceBo bo = new PermissionResourceBo();
		bo.setResourceId(resourceId);
		bo.setResourceName(resourceName);
		bo.setUserId(userId);
		return bo;
	}
}
