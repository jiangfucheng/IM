package com.jiangfucheng.im.httpserver.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 22:07
 *
 * @author jiangfucheng
 */
@Configuration
@PropertySource("classpath:redis/redis-${spring.profiles.active}.yml")
public class RedisConfig {

	@Value("${redis.host}")
	private String host;

	@Value("${redis.port}")
	private Integer port;

	@Value("${redis.password}")
	private String password;

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(host);
		config.setDatabase(0);
		config.setPort(port);
		return new JedisConnectionFactory(config);
	}

	@Bean(name = "redisTemplate")
	@Primary
	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		// 设置任何字段可见
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// 设置不是final的属性可以转换
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// value序列化方式采用jackson序列化方式
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value序列化方式采用jackson序列化方式
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		template.setEnableTransactionSupport(true);
		return template;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		template.setConnectionFactory(redisConnectionFactory);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// value序列化方式采用jackson序列化方式
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value序列化方式采用jackson序列化方式
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		return template;
	}

}
