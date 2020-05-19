package com.jiangfucheng.im.httpserver.config;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
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
	@Value("${zookeeper.address}")
	private String address;

	@Value("${zookeeper.timeout}")
	private Integer timeout;

	@Bean
	public ZooKeeper zooKeeper() {
		ZooKeeper zooKeeper = null;
		try {
			zooKeeper = new ZooKeeper(address, timeout, event -> {
				log.info("zookeeper connected!");
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return zooKeeper;
	}

	//@Bean
	public ZkClient zkClient() {
		return new ZkClient(address, timeout);
	}
}
