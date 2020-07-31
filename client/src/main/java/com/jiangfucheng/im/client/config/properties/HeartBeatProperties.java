package com.jiangfucheng.im.client.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 21:08
 *
 * @author jiangfucheng
 */
@ConfigurationProperties(prefix = "chat.heart-beat")
@Component
@Data
public class HeartBeatProperties {
	/**
	 * 心跳包发送的周期
	 */
	private Long period;
}
