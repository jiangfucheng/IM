package com.jiangfucheng.im.client.feign;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.model.vo.FriendWithIndexVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:32
 *
 * @author jiangfucheng
 */

@FeignClient(value = "user-service", url = "${http-server.ip}:${http-server.port}")
public interface FriendFeignClient {
	@GetMapping(value = "/friends", consumes = "application/json")
	Response<List<FriendWithIndexVo>> getFriendList();

	@GetMapping("/friends/id")
	Response<List<Long>> getFriendIdList();
}
