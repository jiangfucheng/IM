package com.jiangfucheng.im.httpserver.controller;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.httpserver.http.PermissionClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 15:30
 *
 * @author jiangfucheng
 */
@RestController
public class PermissionController {

	private PermissionClient permissionClient;

	public PermissionController(PermissionClient permissionClient) {
		this.permissionClient = permissionClient;
	}

	@GetMapping("/permission/{user_id}/{permission_code}")
	public Response hasPermission(@PathVariable("user_id") Long userId,
								  @PathVariable("permission_code") String permissionCode) {
		boolean res = permissionClient.hasPermission(userId, permissionCode);
		return Response.ok(res ? 1 : 0);
	}
}
