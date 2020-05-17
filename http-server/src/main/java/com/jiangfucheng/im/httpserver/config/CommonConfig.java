package com.jiangfucheng.im.httpserver.config;

import com.jiangfucheng.im.common.util.SnowFlakeIdGenerator;
import com.jiangfucheng.im.httpserver.http.PermissionClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 22:47
 *
 * @author jiangfucheng
 */
@Configuration
public class CommonConfig {
	@Value("${id.worker}")
	private long workerId;
	@Value("${id.data-center}")
	private long dataCenterId;

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

}
