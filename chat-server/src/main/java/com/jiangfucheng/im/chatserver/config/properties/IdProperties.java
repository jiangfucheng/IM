package com.jiangfucheng.im.chatserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/1
 * Time: 14:21
 *
 * @author jiangfucheng
 */
@ConfigurationProperties(prefix = "id")
@Component
@Data
public class IdProperties {
	private long workerId;
	private long dataCenterId;

}
