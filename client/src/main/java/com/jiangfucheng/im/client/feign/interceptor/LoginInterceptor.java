package com.jiangfucheng.im.client.feign.interceptor;

import com.jiangfucheng.im.client.context.ChatClientContext;
import feign.Contract;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:47
 *
 * @author jiangfucheng
 */
public class LoginInterceptor implements RequestInterceptor {

	private final ChatClientContext context;

	@Autowired
	public LoginInterceptor(ChatClientContext context) {
		this.context = context;
	}

	@Bean
	public Contract feignContract(){
		return new feign.Contract.Default();
	}

	@Override
	public void apply(RequestTemplate requestTemplate) {
		if (context.getAuthToken() != null) {
			String headerName = "Authorization";
			String token = "Bearer " + context.getAuthToken();
			requestTemplate.header(headerName, token);
		}
	}
}
