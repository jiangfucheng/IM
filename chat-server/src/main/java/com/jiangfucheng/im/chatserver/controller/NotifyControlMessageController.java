package com.jiangfucheng.im.chatserver.controller;

import com.jiangfucheng.im.chatserver.chat.ChatServerContext;
import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.common.constants.ErrorCode;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 21:47
 *
 * @author jiangfucheng
 */
@ChatMessageController
public class NotifyControlMessageController {
	private CommonMessageSender commonMessageSender;
	private ChatServerContext context;

	public NotifyControlMessageController(CommonMessageSender commonMessageSender,
										  ChatServerContext context) {
		this.commonMessageSender = commonMessageSender;
		this.context = context;
	}

	/**
	 * 处理通知添加好友的请求,该消息由服务器转发，直接写给客户端即可
	 */
	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_ADD_FRIEND_REQUEST)
	public void handleNotifyAddFriendRequest(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getNotifyAddFriendRequest().getUserId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	/**
	 * 处理通知添加好友的响应
	 * 1.send ack
	 * 2.伪造AddFriendResponse转发给客户端
	 */
	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_ADD_FRIEND_RESPONSE)
	public void handleNotifyAddFriendResponse(ChannelHandlerContext ctx, Base.Message msg) {
		commonMessageSender.sendAck(ctx, msg, true);
		String token = msg.getAddFriendRequest().getToken();
		Long fromUserId = JwtUtil.getTokenBody(token).getUserId();
		Control.AddFriendResponse addFriendResponse = Control.AddFriendResponse.newBuilder()
				.setCode(ErrorCode.OK)
				.setErrMsg(ErrorCode.OK_MSG)
				.setMsg(ErrorCode.OK_MSG)
				.setTimestamp(System.currentTimeMillis())
				.setTargetId(fromUserId)
				.build();
		Base.Message addFriendResponseMessage = Base.Message.newBuilder()
				.setId(msg.getId())
				.setDataType(Base.DataType.ADD_FRIEND_RESPONSE)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.setAddFriendResponse(addFriendResponse)
				.build();
		commonMessageSender.sendNotify(fromUserId, addFriendResponseMessage);
	}

	/**
	 * 通知好友删除请求，由服务端转发过来的
	 * 1.直接转发给客户端
	 */
	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_DELETE_FRIEND_REQUEST)
	public void handleNotifyDeleteFriendRequest(ChannelHandlerContext ctx, Base.Message msg) {
		Long userId = context.getUserId(ctx);
		commonMessageSender.sendRequest(userId, msg);
	}

	/**
	 * 通知好友删除响应
	 * 1.send ack
	 * 2.伪造删除好友响应转发给客户端
	 */
	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_DELETE_FRIEND_RESPONSE)
	public void handleNotifyDeleteFriendResponse(ChannelHandlerContext ctx, Base.Message msg) {
		commonMessageSender.sendAck(ctx, msg, true);
		long targetId = msg.getNotifyDeleteFriendResponse().getTargetId();
		Control.DeleteFriendResponse deleteFriendResponse = Control.DeleteFriendResponse.newBuilder()
				.setCode(ErrorCode.OK)
				.setErrMsg(ErrorCode.OK_MSG)
				.setMsg(ErrorCode.OK_MSG)
				.setTimestamp(System.currentTimeMillis())
				.setTargetId(targetId)
				.build();
		Base.Message responseMsg = Base.Message.newBuilder()
				.setId(msg.getId())
				.setDataType(Base.DataType.DELETE_FRIEND_RESPONSE)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.setDeleteFriendResponse(deleteFriendResponse)
				.build();
		commonMessageSender.sendNotify(targetId, responseMsg);
	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_FRIEND_ONLINE_REQUEST)
	public void handleNotifyFriendOfflineRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_FRIEND_ONLINE_RESPONSE)
	public void handleNotifyFriendOfflineResponse(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_INVITE_MEMBER_TO_GROUP_REQUEST)
	public void handleNotifyInviteMemberToGroupRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_INVITE_MEMBER_TO_GROUP_RESPONSE)
	public void handleNotifyInviteMemberToGroupResponse(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_DELETED_GROUP_REQUEST)
	public void handleNotifyDeletedGroupRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_DELETED_GROUP_RESPONSE)
	public void handleNotifyDeletedGroupResponse(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_CHANGE_GROUP_MANAGER_AUTH_REQUEST)
	public void handleNotifyChangeGroupManagerAuthRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_CHANGE_GROUP_MANAGER_AUTH_RESPONSE)
	public void handleNotifyChangeGroupManagerAuthResponse(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_KICK_OUT_GROUP_REQUEST)
	public void handleNotifyKickOutGroupRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_KICK_OUT_GROUP_RESPONSE)
	public void handleNotifyKickOutGroupResponse(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_KICK_OUT_REQUEST)
	public void handleNotifyKickOutRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_KICK_OUT_RESPONSE)
	public void handleNotifyKickOutResponse(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_ADD_GROUP_ANNOUNCEMENT_REQUEST)
	public void handleNotifyAddGroupAnnouncementRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.NOTIFY_ADD_GROUP_ANNOUNCEMENT_RESPONSE)
	public void handleNotifyAddGroupAnnouncementResponse(ChannelHandlerContext ctx, Base.Message msg) {

	}


}
