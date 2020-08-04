package com.jiangfucheng.im.chatserver.service;

import com.jiangfucheng.im.chatserver.chat.ChatServerContext;
import com.jiangfucheng.im.common.constants.RedisConstants;
import com.jiangfucheng.im.model.bo.UserStatusBo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 15:51
 *
 * @author jiangfucheng
 */
@Service
public class UserService {
	private RedisTemplate<String, Object> redisTemplate;
	private ChatServerContext context;

	public UserService(RedisTemplate<String, Object> redisTemplate,
					   ChatServerContext context) {
		this.redisTemplate = redisTemplate;
		this.context = context;
	}

	public UserStatusBo getUserStatus(Long userId) {
		String key = String.format(RedisConstants.USER_STATUS_KEY, userId);
		return (UserStatusBo) redisTemplate.opsForValue().get(key);
	}

	public void removeUserLoginMessage(long userId) {
		//删除用户状态
		String userStatusKey = String.format(RedisConstants.USER_STATUS_KEY, userId);
		redisTemplate.delete(String.valueOf(userStatusKey));
		//删除用户登陆的服务器
		String userConnectedServerKey = String.format(RedisConstants.USER_CONNECTED_CHAT_SERVER, userId);
		redisTemplate.delete(userConnectedServerKey);
		//更新chat-server连接数
		String url = context.getLocalIp() + ":" + context.getPort();
		String redisKey = String.format(RedisConstants.CHAT_SERVER_CONNECTED_NUMBER, url);
		redisTemplate.opsForValue().decrement(redisKey);
	}

	public void addUserLoginMessage(long userId, String account) {
		//添加用户状态
		String userStatusKey = String.format(RedisConstants.USER_STATUS_KEY, userId);
		UserStatusBo userStatusBo = new UserStatusBo(userId, account);
		redisTemplate.opsForValue().set(userStatusKey, userStatusBo);

		String url = context.getLocalIp() + ":" + context.getPort();
		//添加用户连接的服务器地址
		String userConnectedServerKey = String.format(RedisConstants.USER_CONNECTED_CHAT_SERVER, userId);
		redisTemplate.opsForValue().set(userConnectedServerKey, url);
		//增加chat-server连接数
		String chatServerConnectedNumberKey = String.format(RedisConstants.CHAT_SERVER_CONNECTED_NUMBER, url);
		if (redisTemplate.hasKey(chatServerConnectedNumberKey)) {
			redisTemplate.opsForValue().increment(chatServerConnectedNumberKey);
		} else {
			redisTemplate.opsForValue().set(chatServerConnectedNumberKey, 1);
		}
	}

}
