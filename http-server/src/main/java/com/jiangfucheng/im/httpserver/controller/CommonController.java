package com.jiangfucheng.im.httpserver.controller;

import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.resp.Response;
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

	@RequestMapping("/401")
	public Response<Object> handle401(){
		return Response.error(ErrorCode.UNAUTHENTICATED,ErrorCode.UNAUTHENTICATED_MSG);
	}
}
