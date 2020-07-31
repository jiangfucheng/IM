package com.jiangfucheng.im.client.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/30
 * Time: 14:23
 *
 * @author jiangfucheng
 */
@ConfigurationProperties(prefix = "chat.message")
@Component
@Data
public class MessageProperties {
	/**
	 * 重发次数
	 */
	private Integer retry;
	/**
	 * 重发延迟时间
	 */
	private Integer retryDelayTime;
	/**
	 * 没有收到ack的重发次数
	 */
	private Integer failed;
	/**
	 * 没有收到ack重发延迟时间
	 */
	private Integer failedDelayTime;
}
