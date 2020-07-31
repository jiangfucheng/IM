package com.jiangfucheng.im.client.feign;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.model.vo.MessageListElementVo;
import com.jiangfucheng.im.model.vo.MessageVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/28
 * Time: 15:14
 *
 * @author jiangfucheng
 */
@FeignClient(value = "message-service", url = "${http-server.ip}:${http-server.port}")
public interface MessageFeignClient {
	@GetMapping(value = "/messages",consumes = "application/json")
	Response<List<MessageListElementVo>> getMessageList();

	@GetMapping(value = "/history_messages/{target_id}",consumes = "application/json")
	Response<List<MessageVo>> getHistoryMessageList(@PathVariable("target_id") Long targetId);
}
