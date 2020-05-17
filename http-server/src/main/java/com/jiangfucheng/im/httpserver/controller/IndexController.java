package com.jiangfucheng.im.httpserver.controller;

import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.httpserver.bo.UserInfoBo;
import com.jiangfucheng.im.httpserver.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.httpserver.service.ChatServerService;
import com.jiangfucheng.im.httpserver.service.UserService;
import com.jiangfucheng.im.httpserver.utils.JwtUtil;
import com.jiangfucheng.im.httpserver.vo.LoginRequestVo;
import com.jiangfucheng.im.httpserver.vo.LoginResponseVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 15:05
 *
 * @author jiangfucheng
 */
@RestController
public class IndexController {
	private final UserService userService;
	private final ChatServerService chatServerService;

	public IndexController(UserService userService, ChatServerService chatServerService) {
		this.userService = userService;
		this.chatServerService = chatServerService;
	}

	@PostMapping("/session")
	public Response login(LoginRequestVo loginRequestVo) {
		boolean hasUser = userService.hasUser(loginRequestVo.convertToLoginRequestBo());
		if (!hasUser) {
			return Response.error(ErrorCode.UNAUTHENTICATED, "账号或密码错误");
		}
		UserInfoBo userInfo = userService.getUserInfoByAccount(loginRequestVo.getAccount());
		String token = JwtUtil.generateToken(new UserTokenPayloadBo(userInfo.getUserId(), userInfo.getAccount(), userInfo.getNickName()));
		String chatServerUrl = chatServerService.getChatServer(userInfo.getUserId());
		LoginResponseVo loginVo = new LoginResponseVo();
		loginVo.setId(userInfo.getUserId());
		loginVo.setChatServer(chatServerUrl);
		loginVo.setToken(token);
		return Response.ok(token);
	}
}
