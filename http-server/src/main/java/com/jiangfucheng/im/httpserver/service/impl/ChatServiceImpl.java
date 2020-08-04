package com.jiangfucheng.im.httpserver.service.impl;

import com.jiangfucheng.im.common.constants.RedisConstants;
import com.jiangfucheng.im.common.constants.ZookeeperConstants;
import com.jiangfucheng.im.httpserver.service.ChatServerService;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:48
 *
 * @author jiangfucheng
 */
@Service
public class ChatServiceImpl implements ChatServerService {

	private ZkClient zkClient;
	private RedisTemplate redisTemplate;

	public ChatServiceImpl(ZkClient zkClient, RedisTemplate redisTemplate) {
		this.zkClient = zkClient;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public String getChatServer() {
		List<String> chatServerAddress = zkClient.getChildren(ZookeeperConstants.CHAT_SERVER_HOST_PATH);
		if (chatServerAddress == null || chatServerAddress.isEmpty()) {
			return "";
		}
		//初步算法：返回连接数最少的机器，如果相同就返回最后一台
		int min = Integer.MAX_VALUE;
		int index = -1;
		int curIndex = -1;
		for (String address : chatServerAddress) {
			curIndex++;
			String redisKey = String.format(RedisConstants.CHAT_SERVER_CONNECTED_NUMBER, address);
			Object value = redisTemplate.opsForValue().get(redisKey);
			int number = 0;
			if(value != null) {
				number = (int) value;
			}
			if (number < min) {
				index = curIndex;
				min = number;
			}
		}
		return chatServerAddress.get(index);
	}
}
