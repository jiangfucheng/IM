package com.jiangfucheng.im.auth.controller;

import com.jiangfucheng.im.auth.bo.PermissionResourceBo;
import com.jiangfucheng.im.auth.service.PermissionService;
import com.jiangfucheng.im.auth.vo.PermissionResourceVo;
import com.jiangfucheng.im.common.resp.Resp;
import com.jiangfucheng.im.common.util.ParamValidator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/10
 * Time: 16:00
 *
 * @author jiangfucheng
 */
@RestController
public class PermissionController {

	private final PermissionService permissionService;

	public PermissionController(PermissionService permissionService) {
		this.permissionService = permissionService;
	}


	@GetMapping("/permission/{user_id}/{permission_code}")
	public Resp hasPermission(@PathVariable("user_id") Long userId, @PathVariable("permission_code") String permissionCode) {
		ParamValidator.notNull(userId, "用户名不能为空");
		ParamValidator.notBlank(permissionCode, "权限码不能为空");

		return Resp.ok(permissionService.hasPermission(userId, permissionCode));
	}

	@PostMapping("/permission/{permission_code}")
	public Resp createPermission(@PathVariable("permission_code") String permissionCode) {
		ParamValidator.notBlank(permissionCode, "权限码不能为空");

		permissionService.createPermission(permissionCode);
		return Resp.ok();
	}

	@DeleteMapping("/permission/{permission_id}")
	public Resp deletePermission(@PathVariable("permission_id") Long permissionId) {
		ParamValidator.notNull(permissionId, "权限id不能为空");

		permissionService.deletePermission(permissionId);
		return Resp.ok();
	}

	@GetMapping("/permission/{user_id}")
	public Resp queryPermission(@PathVariable("user_id") Long userId) {
		List<String> permissionCodes = permissionService.queryPermissionWithUser(userId);
		return Resp.ok(permissionCodes);
	}

	@PostMapping("/permission/{user_id}/{permission_code}")
	public Resp awardPermission(@PathVariable("user_id") Long userId, @PathVariable("permission_code") String permissionCode) {
		permissionService.awardPermission(userId, permissionCode);
		ParamValidator.notBlank(permissionCode, "权限码不能为空");

		return Resp.ok();
	}

	@DeleteMapping("/permission/{user_id}/{permission_code}")
	public Resp revokePermission(@PathVariable("user_id") Long userId, @PathVariable("permission_code") String permissionCode) {
		ParamValidator.notNull(userId, "用户名不能为空");
		ParamValidator.notBlank(permissionCode, "权限码不能为空");

		permissionService.revokePermissionByPermissionCode(userId, permissionCode);
		return Resp.ok();
	}

	@DeleteMapping("/permission/{user_id}")
	public Resp revokePermission(@PathVariable("user_id") Long userId, @RequestBody PermissionResourceVo permissionResourceVo) {
		ParamValidator.notNull(userId, "用户名不能为空");

		PermissionResourceBo permissionResourceBo = permissionResourceVo.convertToPermissionResourceBo(userId);
		permissionService.revokePermissionByResource(permissionResourceBo);
		return Resp.ok();
	}


}
