package com.jiangfucheng.im.client.feign;

import com.jiangfucheng.im.common.resp.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:33
 *
 * @author jiangfucheng
 */
@FeignClient(value = "user-service", url = "${http-server.ip}:${http-server.port}")
public interface CommonFeignClient {
	@GetMapping("/id")
	Response<Long> generateId();
}
