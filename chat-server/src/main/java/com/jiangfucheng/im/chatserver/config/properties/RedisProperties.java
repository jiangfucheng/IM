package com.jiangfucheng.im.chatserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/1
 * Time: 14:15
 *
 * @author jiangfucheng
 */
@ConfigurationProperties(prefix = "redis")
@Component
@Data
public class RedisProperties {
	private String host;

	private Integer port;

	private String password;
}
