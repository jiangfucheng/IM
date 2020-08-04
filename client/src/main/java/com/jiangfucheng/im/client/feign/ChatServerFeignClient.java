package com.jiangfucheng.im.client.feign;

import com.jiangfucheng.im.common.resp.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/4
 * Time: 20:35
 *
 * @author jiangfucheng
 */
@FeignClient(value = "user-service", url = "${http-server.ip}:${http-server.port}")
public interface ChatServerFeignClient {
	@GetMapping("/chat_server/url")
	Response<String> getChatServerUrl();
}
