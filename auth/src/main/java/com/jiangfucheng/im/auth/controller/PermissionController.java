package com.jiangfucheng.im.auth.controller;

import com.jiangfucheng.im.auth.bo.PermissionResourceBo;
import com.jiangfucheng.im.auth.service.PermissionService;
import com.jiangfucheng.im.auth.vo.PermissionResourceVo;
import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.common.utils.ParamValidator;
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
	public Response hasPermission(@PathVariable("user_id") Long userId, @PathVariable("permission_code") String permissionCode) {
		ParamValidator.notNull(userId, "用户名不能为空");
		ParamValidator.notBlank(permissionCode, "权限码不能为空");

		return Response.ok(permissionService.hasPermission(userId, permissionCode));
	}

	@PostMapping("/permission/{permission_code}")
	public Response createPermission(@PathVariable("permission_code") String permissionCode) {
		ParamValidator.notBlank(permissionCode, "权限码不能为空");

		permissionService.createPermission(permissionCode);
		return Response.ok();
	}

	@DeleteMapping("/permission/{permission_id}")
	public Response deletePermission(@PathVariable("permission_id") Long permissionId) {
		ParamValidator.notNull(permissionId, "权限id不能为空");

		permissionService.deletePermission(permissionId);
		return Response.ok();
	}

	@GetMapping("/permission/{user_id}")
	public Response queryPermission(@PathVariable("user_id") Long userId) {
		List<String> permissionCodes = permissionService.queryPermissionWithUser(userId);
		return Response.ok(permissionCodes);
	}

	@PostMapping("/permission/{user_id}/{permission_code}")
	public Response awardPermission(@PathVariable("user_id") Long userId, @PathVariable("permission_code") String permissionCode) {
		permissionService.awardPermission(userId, permissionCode);
		ParamValidator.notBlank(permissionCode, "权限码不能为空");

		return Response.ok();
	}

	@DeleteMapping("/permission/{user_id}/{permission_code}")
	public Response revokePermission(@PathVariable("user_id") Long userId, @PathVariable("permission_code") String permissionCode) {
		ParamValidator.notNull(userId, "用户名不能为空");
		ParamValidator.notBlank(permissionCode, "权限码不能为空");

		permissionService.revokePermissionByPermissionCode(userId, permissionCode);
		return Response.ok();
	}

	@DeleteMapping("/permission/{user_id}")
	public Response revokePermission(@PathVariable("user_id") Long userId, @RequestBody PermissionResourceVo permissionResourceVo) {
		ParamValidator.notNull(userId, "用户名不能为空");

		PermissionResourceBo permissionResourceBo = permissionResourceVo.convertToPermissionResourceBo(userId);
		permissionService.revokePermissionByResource(permissionResourceBo);
		return Response.ok();
	}


}
