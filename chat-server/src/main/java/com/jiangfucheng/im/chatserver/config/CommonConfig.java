package com.jiangfucheng.im.chatserver.config;

import com.jiangfucheng.im.chatserver.config.properties.IdProperties;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/22
 * Time: 22:48
 *
 * @author jiangfucheng
 */
@Configuration
public class CommonConfig {
	private IdProperties idProperties;

	public CommonConfig(IdProperties idProperties) {
		this.idProperties = idProperties;
	}

	/**
	 * 雪花id生成器
	 */
	@Bean
	public SnowFlakeIdGenerator idGenerator() {
		return new SnowFlakeIdGenerator(idProperties.getWorkerId(), idProperties.getDataCenterId());
	}

	public ScheduledExecutorService scheduledExecutorService() {
		return new ScheduledThreadPoolExecutor(1);
	}
}
