package com.jiangfucheng.im.client.config;

import com.jiangfucheng.im.client.config.properties.IdGeneratorProperties;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/31
 * Time: 16:06
 *
 * @author jiangfucheng
 */
@Configuration
public class CommonConfig {
	private final IdGeneratorProperties ideaGeneratorProperties;

	@Autowired
	public CommonConfig(IdGeneratorProperties ideaGeneratorProperties) {
		this.ideaGeneratorProperties = ideaGeneratorProperties;
	}

	@Bean
	public SnowFlakeIdGenerator idGenerator() {
		return new SnowFlakeIdGenerator(ideaGeneratorProperties.getWorker(), ideaGeneratorProperties.getDataCenter());
	}
}
