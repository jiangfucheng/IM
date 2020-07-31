package com.jiangfucheng.im.client.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/30
 * Time: 14:35
 *
 * @author jiangfucheng
 */
@Component
@ConfigurationProperties(prefix = "id")
@Data
public class IdGeneratorProperties {
	private Integer worker;
	private Integer dataCenter;
}
