package com.jiangfucheng.im.httpserver.resolver;

import com.jiangfucheng.im.httpserver.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.httpserver.utils.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:56
 *
 * @author jiangfucheng
 */
@Component
public class TokenResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.getParameterType().equals(UserTokenPayloadBo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
		assert request != null;
		String token = request.getHeader("Authorization").split(" ")[1];
		return JwtUtil.getTokenBody(token);
	}
}
