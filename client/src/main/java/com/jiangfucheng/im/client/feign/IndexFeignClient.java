package com.jiangfucheng.im.client.feign;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.model.vo.LoginRequestVo;
import com.jiangfucheng.im.model.vo.LoginResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 17:12
 *
 * @author jiangfucheng
 */
@FeignClient(value = "user-service", url = "${http-server.ip}:${http-server.port}")
public interface IndexFeignClient {

	@PostMapping(value = "/session",consumes = "application/json")
	Response<LoginResponseVo> login(LoginRequestVo loginRequestVo);
}
