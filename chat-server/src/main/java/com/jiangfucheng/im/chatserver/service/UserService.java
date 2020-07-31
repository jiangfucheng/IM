package com.jiangfucheng.im.chatserver.service;

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

	public UserService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public UserStatusBo getUserStatus(Long userId) {
		String key = String.format(RedisConstants.USER_STATUS_KEY, userId);
		return (UserStatusBo) redisTemplate.opsForValue().get(key);
	}


}
