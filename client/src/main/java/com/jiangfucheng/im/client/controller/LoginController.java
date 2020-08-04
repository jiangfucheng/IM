package com.jiangfucheng.im.client.controller;

import com.jiangfucheng.im.client.chat.HeartBeatSender;
import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.controller.base.BaseController;
import com.jiangfucheng.im.client.feign.FriendFeignClient;
import com.jiangfucheng.im.client.utils.MessageUtils;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/1
 * Time: 10:23
 *
 * @author jiangfucheng
 */
@ChatMessageController
@Slf4j
public class LoginController extends BaseController {
	private HeartBeatSender heartBeatSender;
	private FriendFeignClient friendFeignClient;

	protected LoginController(ChatClientContext context,
							  MessageMonitor messageMonitor,
							  HeartBeatSender heartBeatSender,
							  FriendFeignClient friendFeignClient) {
		super(context, messageMonitor);
		this.heartBeatSender = heartBeatSender;
		this.friendFeignClient = friendFeignClient;
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGIN_REQUEST)
	public void handleLoginRequest(ChannelHandlerContext ctx, Base.Message msg) {
		handleRequest(ctx, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.LOGIN_RESPONSE)
	public void handleLoginResponse(ChannelHandlerContext ctx, Base.Message msg) {
		handleResponse(ctx, msg);
	}

	@Override
	protected void resolveResponseNotify(ChannelHandlerContext ctx, Base.Message responseMessage) {
		heartBeatSender.start();
		//拉取离线消息
		pullOfflineMessage();
	}

	private void pullOfflineMessage() {
		List<Long> friendIdList = friendFeignClient.getFriendIdList().getData();
		friendIdList.forEach(friendId -> {
					Control.PullOfflineMessageRequest pullOfflineMessageRequest = Control.PullOfflineMessageRequest.newBuilder()
							.setToken(context.getAuthToken())
							.setUserId(friendId)
							.setTimestamp(System.currentTimeMillis())
							.build();
					Base.Message pullOfflineMessage = Base.Message.newBuilder()
							.setId(context.generateId())
							.setPullOfflineMessageRequest(pullOfflineMessageRequest)
							.setDataType(Base.DataType.PULL_OFFLINE_MESSAGE_REQUEST)
							.setMessageStatus(Base.MessageStatus.REQ)
							.build();
					MessageUtils.writeRequestReqMessage(context.getChannel(), context, messageMonitor, pullOfflineMessage);
				}
		);
	}
}
