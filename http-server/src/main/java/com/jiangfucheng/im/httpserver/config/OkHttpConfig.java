package com.jiangfucheng.im.httpserver.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 21:11
 *
 * @author jiangfucheng
 */
@Configuration
public class OkHttpConfig {
	@Value("${ok.http.connect-timeout}")
	private Integer connectTimeout;

	@Value("${ok.http.read-timeout}")
	private Integer readTimeout;

	@Value("${ok.http.write-timeout}")
	private Integer writeTimeout;

	@Value("${ok.http.max-idle-connections}")
	private Integer maxIdleConnections;

	@Value("${ok.http.keep-alive-duration}")
	private Long keepAliveDuration;

	@Bean
	public OkHttpClient okHttpClient() {
		return new OkHttpClient().newBuilder()
				// 是否开启缓存
				.retryOnConnectionFailure(false)
				.connectionPool(new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS))
				.connectTimeout(connectTimeout, TimeUnit.SECONDS)
				.readTimeout(readTimeout, TimeUnit.SECONDS)
				.writeTimeout(writeTimeout, TimeUnit.SECONDS)
				.hostnameVerifier((hostname, session) -> true)
				.build();
	}
}
