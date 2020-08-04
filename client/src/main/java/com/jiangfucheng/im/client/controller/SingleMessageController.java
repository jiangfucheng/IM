package com.jiangfucheng.im.client.controller;

import com.jiangfucheng.im.client.bo.SingleMessageBo;
import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.controller.base.BaseController;
import com.jiangfucheng.im.client.feign.UserFeignClient;
import com.jiangfucheng.im.client.utils.MessageUtils;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.common.enums.MessageType;
import com.jiangfucheng.im.model.vo.UserVo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.SingleChat;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/31
 * Time: 16:06
 *
 * @author jiangfucheng
 */
@ChatMessageController
@Slf4j
public class SingleMessageController extends BaseController {

	private ChatClientContext context;
	private UserFeignClient userFeignClient;

	public SingleMessageController(ChatClientContext context,
								   UserFeignClient userFeignClient,
								   MessageMonitor messageMonitor) {
		super(context, messageMonitor);
		this.context = context;
		this.userFeignClient = userFeignClient;
	}


	@ChatMessageMapping(messageType = Base.DataType.SINGLE_CHAT_REQUEST)
	public void handleSingleChatRequest(ChannelHandlerContext ctx, Base.Message msg) {
		handleRequest(ctx, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.SINGLE_CHAT_RESPONSE)
	public void handleSingleChatResponse(ChannelHandlerContext ctx, Base.Message msg) {
		handleResponse(ctx, msg);
	}


	@Override
	protected Base.Message resolveRequestAck(ChannelHandlerContext ctx, Base.Message requestMessage) {
		Base.Message origMessage = context.getUnReceiveAckMessages().get(requestMessage.getId());

		//添加消息id
		SingleChat.SingleChatRequest newSingleChatMsg = origMessage.getSingleChatRequest()
				.toBuilder()
				.setMsgId(requestMessage.getSingleChatRequest().getMsgId())
				.build();
		return origMessage.toBuilder()
				.setSingleChatRequest(newSingleChatMsg)
				.build();
	}

	@Override
	protected Base.Message resolveRequestNotify(ChannelHandlerContext ctx, Base.Message requestMessage) {
		SingleChat.SingleChatRequest singleChatRequest = requestMessage.getSingleChatRequest();
		long friendId = singleChatRequest.getFromId();
		UserVo friend = userFeignClient.queryUserById(friendId).getData();
		String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		System.out.println(friend.getNickName() + " " + currentDate);
		if (singleChatRequest.getType() == MessageType.TEXT.value()) {
			System.out.println(singleChatRequest.getContent());
		} else {
			System.out.println("暂不支持的消息类型!!!");
		}
		Base.Message responseMessage = buildResponse(requestMessage.getId(), singleChatRequest.getMsgId(), friendId);
		//把消息存到本地缓存中
		context.putSingleChatMessage(friendId, MessageUtils.convertSingleChatRequestToSingleMessageBo(singleChatRequest));
		return responseMessage;
	}

	private Base.Message buildResponse(long protoMessageId, long messageId, Long targetId) {
		SingleChat.SingleChatResponse response = SingleChat.SingleChatResponse.newBuilder()
				.setMsgId(messageId)
				.setFromId(context.getCurrentUser().getId())
				.setTimestamp(System.currentTimeMillis())
				.setToId(targetId)
				.build();
		return Base.Message.newBuilder()
				.setId(protoMessageId)
				.setSingleChatResponse(response)
				.setDataType(Base.DataType.SINGLE_CHAT_RESPONSE)
				.setMessageStatus(Base.MessageStatus.REQ)
				.build();
	}

	@Override
	protected void resolveResponseNotify(ChannelHandlerContext ctx, Base.Message responseMessage) {
		Base.Message completedMessage = context.unCompletedMessages.get(responseMessage.getId());
		SingleChat.SingleChatResponse response = responseMessage.getSingleChatResponse();
		SingleMessageBo singleMessageBo = MessageUtils
				.convertSingleChatRequestToSingleMessageBo(completedMessage.getSingleChatRequest());
		singleMessageBo.setTime(new Date(response.getTimestamp()));
		//把消息加入到本地消息缓存中
		context.putSingleChatMessage(response.getFromId(), singleMessageBo);
	}
}
