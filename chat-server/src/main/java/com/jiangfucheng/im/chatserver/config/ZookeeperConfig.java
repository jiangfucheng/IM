package com.jiangfucheng.im.chatserver.config;

import com.jiangfucheng.im.chatserver.config.properties.ZookeeperProperties;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 22:47
 *
 * @author jiangfucheng
 */
@Configuration
@PropertySource("classpath:zookeeper/zookeeper-${spring.profiles.active}.yml")
@Slf4j
public class ZookeeperConfig {
	private ZookeeperProperties zookeeperProperties;

	public ZookeeperConfig(ZookeeperProperties zookeeperProperties) {
		this.zookeeperProperties = zookeeperProperties;
	}

	@Bean
	public ZooKeeper zooKeeper() {
		ZooKeeper zooKeeper = null;
		try {
			zooKeeper = new ZooKeeper(zookeeperProperties.getAddress(), zookeeperProperties.getTimeout(), event -> log.info("zookeeper connected!"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zooKeeper;
	}

	@Bean
	public ZkClient zkClient() {
		return new ZkClient(zookeeperProperties.getAddress(), zookeeperProperties.getTimeout());
	}
}
