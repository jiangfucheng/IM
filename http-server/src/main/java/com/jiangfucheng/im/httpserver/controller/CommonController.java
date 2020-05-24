package com.jiangfucheng.im.httpserver.controller;

import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/16
 * Time: 17:24
 *
 * @author jiangfucheng
 */
@RestController
public class CommonController {
	private SnowFlakeIdGenerator idGenerator;

	@RequestMapping("/401")
	public Response<Object> handle401() {
		return Response.error(ErrorCode.UNAUTHENTICATED, ErrorCode.UNAUTHENTICATED_MSG);
	}


	/**
	 * 生成一个全局唯一的id，给客户端在发消息之前加到Message上
	 * 注意：这里不是加到单聊或者群聊的消息id上，单聊或群聊的id应该以到达服务器的时间为准,由服务器生成
	 */
	@GetMapping("/id")
	public Response generateId() {
		return Response.ok(idGenerator.nextId());
	}
}
