package com.jiangfucheng.im.httpserver.config;

import com.jiangfucheng.im.common.util.SnowFlakeIdGenerator;
import com.jiangfucheng.im.httpserver.http.PermissionClient;
import com.jiangfucheng.im.httpserver.resolver.TokenResolver;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 22:47
 *
 * @author jiangfucheng
 */
@Configuration
public class CommonConfig implements WebMvcConfigurer {
	@Value("${id.worker}")
	private long workerId;
	@Value("${id.data-center}")
	private long dataCenterId;

	private TokenResolver tokenResolver;

	public CommonConfig(TokenResolver tokenResolver) {
		this.tokenResolver = tokenResolver;
	}

	/**
	 * 雪花id生成器
	 */
	@Bean
	public SnowFlakeIdGenerator idGenerator() {
		return new SnowFlakeIdGenerator(workerId, dataCenterId);
	}

	@Bean
	public PermissionClient permissionClient(OkHttpClient okHttpClient) {
		return new PermissionClient(okHttpClient);
	}


	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(tokenResolver);
	}

	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*"); // 1
		corsConfiguration.addAllowedHeader("*"); // 2
		corsConfiguration.addAllowedMethod("*"); // 3
		corsConfiguration.setAllowCredentials(true);
		return corsConfiguration;
	}


	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig()); // 4
		return new CorsFilter(source);
	}
}
