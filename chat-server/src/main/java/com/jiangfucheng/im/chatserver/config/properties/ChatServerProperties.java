package com.jiangfucheng.im.chatserver.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/1
 * Time: 14:17
 *
 * @author jiangfucheng
 */
@ConfigurationProperties(prefix = "chat.chat-server")
@Component
@Data
public class ChatServerProperties {
	private Integer port;
}
