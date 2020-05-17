package com.jiangfucheng.im.httpserver.controller;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.httpserver.bo.*;
import com.jiangfucheng.im.httpserver.service.MessageService;
import com.jiangfucheng.im.httpserver.vo.MessageListElementVo;
import com.jiangfucheng.im.httpserver.vo.MessageVo;
import com.jiangfucheng.im.httpserver.vo.NotifyVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 10:32
 *
 * @author jiangfucheng
 */
@RestController
public class MessageController {

	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping("/messages")
	public Response getMessageList(UserTokenPayloadBo userInfo) {
		List<OneMessageListBo> messageList = messageService.getMessageList(userInfo.getUserId());
		List<MessageListElementVo> messageListVo = messageList.stream().map(OneMessageListBo::convertToMessageListElementVo).collect(Collectors.toList());
		return Response.ok(messageListVo);
	}

	@GetMapping("offline_messages/{target_id}")
	public Response getHistoryMessage(UserTokenPayloadBo userInfo,
									  @PathVariable("target_id") Long targetId) {
		QueryHistoryMsgBo queryHistoryMsgBo = new QueryHistoryMsgBo();
		queryHistoryMsgBo.setUserId(userInfo.getUserId());
		queryHistoryMsgBo.setTargetId(targetId);
		List<MessageBo> messageBos = messageService.queryHistoryMessage(queryHistoryMsgBo);
		List<MessageVo> messageVos = messageBos.stream().map(MessageBo::convertToMessageVo).collect(Collectors.toList());
		return Response.ok(messageVos);
	}

	@GetMapping("/notifies")
	public Response getNotifies(UserTokenPayloadBo userInfo) {
		List<NotifyBo> notifyBos = messageService.queryNotifiesWthUser(userInfo.getUserId());
		List<NotifyVo> notifyVos = notifyBos.stream().map(NotifyBo::convertToNotifyVo).collect(Collectors.toList());
		return Response.ok(notifyVos);
	}


}
