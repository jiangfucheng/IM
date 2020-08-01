package com.jiangfucheng.im.chatserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/1
 * Time: 14:12
 *
 * @author jiangfucheng
 */
@ConfigurationProperties(prefix = "chat.heart-beat")
@Component
@Data
public class HeartBeatProperties {
	/**
	 * 心跳包的周期
	 */
	private Integer period;
}
