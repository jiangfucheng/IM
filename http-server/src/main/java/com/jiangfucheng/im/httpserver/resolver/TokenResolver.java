package com.jiangfucheng.im.httpserver.resolver;

import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.httpserver.exceptions.IMException;
import com.jiangfucheng.im.model.bo.UserTokenPayloadBo;
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
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader == null || "".equals(authorizationHeader)) {
			throw new IMException(ErrorCode.UNAUTHENTICATED, "token验证失败");
		}
		String[] headerArr = authorizationHeader.split(" ");
		if (headerArr.length != 2)
			throw new IMException(ErrorCode.UNAUTHENTICATED, "token验证失败");
		String token = headerArr[1];
		if (!JwtUtil.verify(token)) {
			throw new IMException(ErrorCode.UNAUTHENTICATED, "token验证失败");
		}
		return JwtUtil.getTokenBody(token);
	}
}
