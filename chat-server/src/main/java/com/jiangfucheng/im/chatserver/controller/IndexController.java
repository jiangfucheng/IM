package com.jiangfucheng.im.chatserver.controller;

import com.jiangfucheng.im.chatserver.chat.ChatServerContext;
import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.chatserver.service.UserService;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.model.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 23:18
 *
 * @author jiangfucheng
 */
@ChatMessageController
@Slf4j
public class IndexController {

	private RedisTemplate<String, Object> redisTemplate;
	private CommonMessageSender commonMessageSender;
	private String localIp;
	@Value("${chat.chat-server.port}")
	private Integer port;
	private ChatServerContext context;
	private UserService userService;

	public IndexController(RedisTemplate<String, Object> redisTemplate,
						   CommonMessageSender commonMessageSender,
						   ChatServerContext context,
						   UserService userService) {
		this.redisTemplate = redisTemplate;
		this.commonMessageSender = commonMessageSender;
		this.context = context;
		this.userService = userService;
		try {
			this.localIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGIN_REQUEST)
	public void handleLoginRequest(ChannelHandlerContext ctx, Base.Message msg) {
		Base.Message ackMessage = msg.toBuilder()
				.setMessageStatus(Base.MessageStatus.ACK)
				.build();
		commonMessageSender.sendAck(ctx, ackMessage, true);

		Control.LoginRequest loginRequest = msg.getLoginRequest();
		String token = loginRequest.getToken();
		Control.LoginResponse loginResponse;
		if (!JwtUtil.verify(token)) {
			loginResponse = Control.LoginResponse.newBuilder()
					.setCode(ErrorCode.UNAUTHENTICATED)
					.setMsg("认证失败")
					.setErrMsg("认证失败")
					.setTimestamp(System.currentTimeMillis())
					.build();
		} else {
			UserTokenPayloadBo userInfo = JwtUtil.getTokenBody(token);
			context.register(userInfo.getUserId(), ctx);
			loginResponse = Control.LoginResponse.newBuilder()
					.setCode(ErrorCode.OK)
					.setMsg(ErrorCode.OK_MSG)
					.setErrMsg(ErrorCode.OK_MSG)
					.build();
			userService.addUserLoginMessage(userInfo.getUserId(), userInfo.getAccount());
			log.info(userInfo.getNickName() + " login success on this server");
		}

		Base.Message responseMsg = Base.Message.newBuilder()
				.setId(msg.getId())
				.setLoginResponse(loginResponse)
				.setDataType(Base.DataType.LOGIN_RESPONSE)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.build();
		ctx.writeAndFlush(responseMsg);
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGOUT_REQUEST)
	public void handleLogoutRequest(ChannelHandlerContext ctx, Base.Message msg) {
		Base.Message ackMessage = msg.toBuilder().setMessageStatus(Base.MessageStatus.ACK).build();
		commonMessageSender.sendAck(ctx, ackMessage, true);
		String token = msg.getLogoutRequest().getToken();
		UserTokenPayloadBo userInfo = JwtUtil.getTokenBody(token);
		userService.removeUserLoginMessage(userInfo.getUserId());
		Control.LogoutResponse response = Control.LogoutResponse.newBuilder()
				.setCode(ErrorCode.OK)
				.setMsg(ErrorCode.OK_MSG)
				.setErrMsg(ErrorCode.OK_MSG)
				.build();
		Base.Message responseMsg = Base.Message.newBuilder()
				.setId(msg.getId())
				.setLogoutResponse(response)
				.setDataType(Base.DataType.LOGOUT_RESPONSE)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.build();
		ctx.writeAndFlush(responseMsg);
		clearChannel(userInfo.getUserId());
	}

	private void clearChannel(Long userId) {
		context.removeChannel(userId);
	}

}
