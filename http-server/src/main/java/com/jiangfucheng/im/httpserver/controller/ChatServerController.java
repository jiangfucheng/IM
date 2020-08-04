package com.jiangfucheng.im.httpserver.controller;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.httpserver.service.ChatServerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/4
 * Time: 17:05
 *
 * @author jiangfucheng
 */
@RestController
public class ChatServerController {
	private ChatServerService chatServerService;

	public ChatServerController(ChatServerService chatServerService) {
		this.chatServerService = chatServerService;
	}

	@GetMapping("/chat_server/url")
	public Response getChatServerUrl(){
		return Response.ok(chatServerService.getChatServer());
	}
}
