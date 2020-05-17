package com.jiangfucheng.im.httpserver.vo;

import com.jiangfucheng.im.httpserver.bo.PermissionResourceBo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 22:56
 *
 * @author jiangfucheng
 */
@Data
public class PermissionResourceVo {
	@NotNull(message = "资源id不能为空")
	private Long resourceId;
	@NotBlank(message = "资源名不能为空")
	private String resourceName;

	public PermissionResourceBo convertToPermissionResourceBo(Long userId) {
		PermissionResourceBo bo = new PermissionResourceBo();
		bo.setResourceId(resourceId);
		bo.setResourceName(resourceName);
		bo.setUserId(userId);
		return bo;
	}
}
