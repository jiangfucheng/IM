package com.jiangfucheng.im.httpserver.resolver;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.httpserver.exceptions.IMException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:56
 *
 * @author jiangfucheng
 */
@RestControllerAdvice
public class GlobalExceptionResolver {

	@ExceptionHandler(IMException.class)
	public Response handleImException(IMException ex) {
		return Response.error(ex.getCode(), ex.getMessage());
	}
}
