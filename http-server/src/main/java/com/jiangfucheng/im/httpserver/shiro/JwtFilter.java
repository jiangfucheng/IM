package com.jiangfucheng.im.httpserver.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/15
 * Time: 21:54
 *
 * @author jiangfucheng
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

	@Override
	protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
		return super.isLoginAttempt(request, response);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		String requestURI = servletRequest.getRequestURI();
		if (requestURI.matches("/sessioin/*")
				|| (requestURI.matches("/user") && servletRequest.getMethod().equalsIgnoreCase("post"))) {
		} else {
			servletResponse.sendRedirect("/401");
		}
		return true;
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		return super.onLoginSuccess(token, subject, request, response);
	}
}

