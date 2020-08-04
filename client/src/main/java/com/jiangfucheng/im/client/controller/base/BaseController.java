package com.jiangfucheng.im.client.controller.base;

import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.utils.MessageUtils;
import com.jiangfucheng.im.protobuf.Base;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 21:31
 *
 * @author jiangfucheng
 */
@Slf4j
public class BaseController {

	protected ChatClientContext context;
	protected MessageMonitor messageMonitor;

	protected BaseController(ChatClientContext context, MessageMonitor messageMonitor) {
		this.context = context;
		this.messageMonitor = messageMonitor;
	}

	protected void handleRequest(ChannelHandlerContext ctx, Base.Message msg) {
		boolean isAck = msg.getMessageStatus() == Base.MessageStatus.ACK;
		if (isAck) {
			if (MessageUtils.isReceivedAckMessage(context, msg.getId())) {
				log.debug("the message is received ack before arrived, messageId: {}", msg.getId());
				return;
			}
			log.debug("receive ack for requestMessage with id: {}", msg.getId());

			Base.Message newMessage = resolveRequestAck(ctx, msg);

			context.removeAckMessage(msg.getId());
			context.putUnCompletedMsg(newMessage);
			messageMonitor.watchMessage(msg.getId(), MessageMonitor.Type.UN_COMPLETE);
			log.debug("watch response for requestMessage for id : {}", msg.getId());
		} else {
			//notify
			Base.Message responseMessage = resolveRequestNotify(ctx, msg);
			//不需要watch response的ACK
			ctx.writeAndFlush(responseMessage);
			log.debug("send response with id: {}", responseMessage.getId());
		}
	}

	protected void handleResponse(ChannelHandlerContext ctx, Base.Message msg) {
		boolean isAck = msg.getMessageStatus() == Base.MessageStatus.ACK;
		if (isAck) {
			//response消息不需要服务端回复ACK的
			log.debug("receive ack for responseMessage  with id: {}", msg.getId());
		} else {
			//notify
			if (MessageUtils.isCompleteMessage(context, msg.getId())) {
				return;
			}
			resolveResponseNotify(ctx, msg);
			//标记这个消息id的链路为已经完成
			context.removeCompletedMsg(msg.getId());
			log.debug("receive response for responseMessage with id : {}", msg.getId());
		}
	}

	/**
	 * 客户端收到Request ACK添加服务器传回的某些信息并返回(eg: 服务器会给单聊、群聊消息添加消息id)
	 * 由子类扩展
	 * @return 服务器修改后的内容，用于保存到本地
	 */
	protected Base.Message resolveRequestAck(ChannelHandlerContext ctx, Base.Message requestMessage) {
		return requestMessage;
	}

	/**
	 * 处理收到Request Notify的内容，如打印到本地，或其他操作
	 * 由子类扩展
	 * @return 需要返回的Response
	 */
	protected Base.Message resolveRequestNotify(ChannelHandlerContext ctx, Base.Message requestMessage) {
		return null;
	}

	/**
	 *  一条消息链路完成后需要的操作
	 *  由子类扩展
	 */
	protected void resolveResponseNotify(ChannelHandlerContext ctx, Base.Message responseMessage) {

	}

}
