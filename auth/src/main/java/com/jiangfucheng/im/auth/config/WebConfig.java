package com.jiangfucheng.im.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/12
 * Time: 23:17
 *
 * @author jiangfucheng
 */
@Configuration
public class WebConfig {
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor(){
		return new MethodValidationPostProcessor();
	}
}
