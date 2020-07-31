package com.jiangfucheng.im.client.config;

import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.feign.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/28
 * Time: 21:36
 *
 * @author jiangfucheng
 */
@Configuration
public class FeignConfig {
	@Bean
	public LoginInterceptor loginInterceptor(ChatClientContext context) {
		return new LoginInterceptor(context);
	}
}
