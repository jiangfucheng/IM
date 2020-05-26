package com.jiangfucheng.im.httpserver.controller;

import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.httpserver.exceptions.IMException;
import com.jiangfucheng.im.httpserver.service.GroupService;
import com.jiangfucheng.im.httpserver.service.UserService;
import com.jiangfucheng.im.model.bo.GroupBo;
import com.jiangfucheng.im.model.bo.UserBo;
import com.jiangfucheng.im.model.vo.AccountVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/16
 * Time: 23:21
 *
 * @author jiangfucheng
 */
@RestController
public class AccountController {
	private final UserService userService;
	private final GroupService groupService;

	public AccountController(UserService userService, GroupService groupService) {
		this.userService = userService;
		this.groupService = groupService;
	}

	@GetMapping("/account/{account}")
	public Response queryAccount(@PathVariable("account") String account) {
		List<AccountVo> accounts = new ArrayList<>();
		UserBo userBo = userService.getUserByAccount(account);
		if (userBo != null) {
			accounts.add(userBo.convertToAccountVo());
		}
		for (int i = 0; i < account.length(); i++) {
			char c = account.charAt(i);
			if (c < '0' || c > '9') {
				return Response.ok(accounts);
			}
		}
		GroupBo groupBo = groupService.getGroupByAccount(Long.parseLong(account));
		if (groupBo != null) {
			accounts.add(groupBo.convertToAccountVo());
		}
		return Response.ok(accounts);
	}

	@GetMapping("/account/user/{account}")
	public Response getUserByAccount(@PathVariable("account") String account) {
		UserBo userBo = userService.getUserByAccount(account);
		if (userBo != null)
			throw new IMException(ErrorCode.USER_EXISTED, "账号已经被注册");
		Map<String, Integer> res = new HashMap<>();
		res.putIfAbsent("isExist", 0);
		return Response.ok(res);
	}
}
