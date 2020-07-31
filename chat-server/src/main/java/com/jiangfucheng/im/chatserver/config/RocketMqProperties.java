package com.jiangfucheng.im.chatserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 22:31
 *
 * @author jiangfucheng
 */
@ConfigurationProperties(prefix = "chat.mq")
@Component
@Data
public class RocketMqProperties {
	private String nameServerAddr;

}
