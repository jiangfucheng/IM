package com.jiangfucheng.im.client.feign;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.model.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:31
 *
 * @author jiangfucheng
 */
@FeignClient(value = "user-service", url = "${http-server.ip}:${http-server.port}")
public interface UserFeignClient {

	@GetMapping(value = "/user/{id}", consumes = "application/json")
	Response<UserVo> queryUserById(@PathVariable("id") Long id);

	@GetMapping(value = "/user/account/{account}", consumes = "application/json")
	Response<UserVo> queryUserByAccount(@PathVariable("account") String account);

}
