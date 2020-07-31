package com.jiangfucheng.im.chatserver.controller;

import com.jiangfucheng.im.chatserver.chat.CommonMessageSender;
import com.jiangfucheng.im.chatserver.service.FriendService;
import com.jiangfucheng.im.chatserver.service.GroupService;
import com.jiangfucheng.im.chatserver.service.NotifyService;
import com.jiangfucheng.im.common.chat.ChatMessageController;
import com.jiangfucheng.im.common.chat.ChatMessageMapping;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.model.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/24
 * Time: 21:47
 * 控制类消息的请求类型
 *
 * @author jiangfucheng
 */
@ChatMessageController
public class RequestControlMessageController {

	private CommonMessageSender commonMessageSender;
	private FriendService friendService;
	private NotifyService notifyService;
	private GroupService groupService;

	public RequestControlMessageController(CommonMessageSender commonMessageSender,
										   FriendService friendService,
										   NotifyService notifyService, GroupService groupService) {
		this.commonMessageSender = commonMessageSender;
		this.friendService = friendService;
		this.notifyService = notifyService;
		this.groupService = groupService;
	}

	/**
	 * 申请添加好友请求
	 * 1.保存通知
	 * 2.send ack
	 * 3.构造添加好友通知，发送给目标用户
	 */
	@ChatMessageMapping(messageType = Base.DataType.ADD_FRIEND_REQUEST)
	public void handleAddFriendRequest(ChannelHandlerContext ctx, Base.Message msg) {
		if (ctx != null) {
			notifyService.saveAddFriendNotify(msg.getAddFriendRequest());
			commonMessageSender.sendAck(ctx, msg, true);
		}
		Control.AddFriendRequest addFriendRequest = msg.getAddFriendRequest();
		String token = addFriendRequest.getToken();
		UserTokenPayloadBo userInfo = JwtUtil.getTokenBody(token);
		Control.NotifyAddFriendRequest notifyAddFriendRequest = Control.NotifyAddFriendRequest.newBuilder()
				.setUserId(userInfo.getUserId())
				.setAuthMessage(addFriendRequest.getAuthMessage())
				.setTimestamp(System.currentTimeMillis())
				.build();
		Base.Message requestMsg = Base.Message.newBuilder()
				.setId(msg.getId())
				.setDataType(Base.DataType.NOTIFY_ADD_FRIEND_REQUEST)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.setNotifyAddFriendRequest(notifyAddFriendRequest)
				.build();
		commonMessageSender.sendNotify(addFriendRequest.getTargetId(), requestMsg);
	}

	/**
	 * 申请添加好友响应
	 * 服务端只会收到从mq转发来的，因为这个响应是服务端构造的，如果没有直接写到客户端，就会转发,不存在其他情况
	 * 1.直接发送给客户端
	 */
	@ChatMessageMapping(messageType = Base.DataType.ADD_FRIEND_RESPONSE)
	public void handleAddFriendResponse(ChannelHandlerContext ctx, Base.Message msg) {
		Control.AddFriendResponse addFriendResponse = msg.getAddFriendResponse();
		commonMessageSender.sendNotify(addFriendResponse.getTargetId(), msg);
	}

	/**
	 * 删除好友请求
	 * 1.在数据库中删除好友
	 * 2.在数据库中保存通知(待定)
	 * 3.send ack
	 * 4.伪造notify发送给目标好友
	 */
	@ChatMessageMapping(messageType = Base.DataType.DELETE_FRIEND_REQUEST)
	public void handleDeleteFriendRequest(ChannelHandlerContext ctx, Base.Message msg) {
		if (ctx != null) {
			friendService.deleteFriend(msg);
			notifyService.saveDeleteFriendNotify(msg.getDeleteFriendRequest());
			commonMessageSender.sendAck(ctx, msg, true);
		}
		Long friendId = msg.getDeleteFriendRequest().getFriendId();
		Control.NotifyDeleteFriendRequest notifyDeleteFriendRequest = Control.NotifyDeleteFriendRequest.newBuilder()
				.setTimestamp(System.currentTimeMillis())
				.setUserId(friendId)
				.build();
		Base.Message responseMsg = Base.Message.newBuilder()
				.setId(msg.getId())
				.setDataType(Base.DataType.NOTIFY_DELETE_FRIEND_REQUEST)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.setNotifyDeleteFriendRequest(notifyDeleteFriendRequest)
				.build();
		commonMessageSender.sendNotify(friendId, responseMsg);
	}

	/**
	 * 删除好友响应
	 * 直接通知客户端
	 */
	@ChatMessageMapping(messageType = Base.DataType.DELETE_FRIEND_RESPONSE)
	public void handleDeleteFriendResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	/**
	 * 申请加入群聊请求
	 * 1.保存通知到数据库
	 * 2.send acc
	 * 3.伪造申请通知给群主和管理员
	 */
	@ChatMessageMapping(messageType = Base.DataType.ADD_GROUP_REQUEST)
	public void handleAddGroupRequest(ChannelHandlerContext ctx, Base.Message msg) {
		Control.AddGroupRequest addGroupRequest = msg.getAddGroupRequest();
		String token = addGroupRequest.getToken();
		Long userId = JwtUtil.getTokenBody(token).getUserId();
		notifyService.saveAddGroupNotify(addGroupRequest);
		commonMessageSender.sendAck(ctx, msg, true);
		List<Long> ownerOrManagers = groupService.getOwnerByGroupId(addGroupRequest.getGroupId());
		Control.NotifyAddGroupRequest notifyAddGroupRequest = Control.NotifyAddGroupRequest.newBuilder()
				.setFromId(userId)
				.setGroupId(addGroupRequest.getGroupId())
				.setUserId(userId)
				.setVerifyMessage(addGroupRequest.getVerifyMsg())
				.build();
		Base.Message message = Base.Message.newBuilder()
				.setId(msg.getId())
				.setDataType(Base.DataType.NOTIFY_ADD_GROUP_REQUEST)
				.setMessageStatus(Base.MessageStatus.NOTIFY)
				.setNotifyAddGroupRequest(notifyAddGroupRequest)
				.build();
		ownerOrManagers.forEach(id -> {
			commonMessageSender.sendNotify(id, message);
		});
	}

	/**
	 * 申请加入群聊响应
	 * 1.直接转发给客户端
	 */
	@ChatMessageMapping(messageType = Base.DataType.ADD_GROUP_RESPONSE)
	public void handleAddGroupResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);

	}

	/**
	 * 创建群请求
	 * 1.保存通知，每个用户保存一份,发送人为邀请人
	 * 2.数据库创建群，并添加所有好友
	 * 3.send ack
	 * 4.伪造通知转发给所有好友
	 */
	@ChatMessageMapping(messageType = Base.DataType.CREATE_GROUP_REQUEST)
	public void handleCreateGroupRequest(ChannelHandlerContext ctx, Base.Message msg) {
		notifyService.saveInviteToGroupNotify(msg.getCreateGroupRequest());
		Long groupId = groupService.createGroupAndInviteMember(msg.getCreateGroupRequest());
		commonMessageSender.sendAck(ctx, msg, true);
		Long userId = JwtUtil.getTokenBody(msg.getCreateGroupRequest().getToken()).getUserId();
		msg.getCreateGroupRequest()
				.getInviteUserIdList()
				.forEach(memberId -> {
					Control.NotifyInviteMemberToGroupRequest notifyInviteMemberToGroupRequest = Control.NotifyInviteMemberToGroupRequest.newBuilder()
							.setFromId(userId)
							.setGroupId(groupId)
							.setInvitedUserId(memberId)
							.build();
					Base.Message message = Base.Message.newBuilder()
							.setId(msg.getId())
							.setDataType(Base.DataType.NOTIFY_INVITE_MEMBER_TO_GROUP_REQUEST)
							.setMessageStatus(Base.MessageStatus.NOTIFY)
							.setNotifyInviteMemberToGroupRequest(notifyInviteMemberToGroupRequest)
							.build();
					commonMessageSender.sendNotify(memberId, message);
				});


	}

	@ChatMessageMapping(messageType = Base.DataType.CREATE_GROUP_RESPONSE)
	public void handleCreateGroupResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	/**
	 * 退出群聊通知
	 * 1.保存通知
	 * 2.删除群-好友关系
	 * 3.sendAck
	 * 4.通知群主和管理员
	 */
	@ChatMessageMapping(messageType = Base.DataType.EXIT_GROUP_REQUEST)
	public void handleExitGroupRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.EXIT_GROUP_RESPONSE)
	public void handleExitGroupResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	/**
	 * 转让群
	 * 1.保存通知
	 * 2.修改涉及到的用户角色
	 * 3.send ack
	 * 4.通知转让后的群主
	 */
	@ChatMessageMapping(messageType = Base.DataType.CHANGE_GROUP_OWNER_REQUEST)
	public void handleChangeGroupOwnerRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}


	@ChatMessageMapping(messageType = Base.DataType.CHANGE_GROUP_OWNER_RESPONSE)
	public void handleChangeGroupOwnerResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	/**
	 * 解散群聊
	 * 1.保存通知
	 * 2.删除群信息
	 * 3.send ack
	 * 4.通知群成员群被解散
	 */
	@ChatMessageMapping(messageType = Base.DataType.DELETE_GROUP_REQUEST)
	public void handleDeleteGroupRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}


	@ChatMessageMapping(messageType = Base.DataType.DELETE_GROUP_RESPONSE)
	public void handleDeleteGroupResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	/**
	 * 修改群管理员权限
	 * 1.保存通知
	 * 2.修改群用户权限
	 * 3.send ack
	 * 4.通知涉及到的用户
	 */
	@ChatMessageMapping(messageType = Base.DataType.CHANGE_GROUP_MANAGER_AUTH_REQUEST)
	public void handleChangeGroupManagerAuthRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}


	@ChatMessageMapping(messageType = Base.DataType.CHANGE_GROUP_MANAGER_AUTH_RESPONSE)
	public void handleChangeGroupManagerAuthResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	/**
	 * 邀请用户加入群聊
	 * 1.保存通知
	 * 2.send ack
	 * 3.通知用户
	 */
	@ChatMessageMapping(messageType = Base.DataType.INVITE_MEMBER_TO_GROUP_REQUEST)
	public void handleInviteMemberToGroupRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.INVITE_MEMBER_TO_GROUP_RESPONSE)
	public void handleInviteMemberToGroupResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	/**
	 * 踢出用户
	 * 1.
	 */
	@ChatMessageMapping(messageType = Base.DataType.KICK_OUT_MEMBER_REQUEST)
	public void handleKickOutMemberRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.KICK_OUT_MEMBER_RESPONSE)
	public void handleKickOutMemberResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.ADD_GROUP_ANNOUNCEMENT_REQUEST)
	public void handleAddGroupAnnouncementRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.ADD_GROUP_ANNOUNCEMENT_RESPONSE)
	public void handleAddGroupAnnouncementResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.DELETE_ANNOUNCEMENT_REQUEST)
	public void handleDeleteAnnouncementRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.DELETE_ANNOUNCEMENT_RESPONSE)
	public void handleDeleteAnnouncementResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.EDIT_ANNOUNCEMENT_REQUEST)
	public void handleEditAnnouncementRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.EDIT_ANNOUNCEMENT_RESPONSE)
	public void handleEditAnnouncementResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.EDIT_GROUP_REMARKS_REQUEST)
	public void handleEditGroupRemarksRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.EDIT_GROUP_REMARKS_RESPONSE)
	public void handleEditGroupRemarksResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}

	@ChatMessageMapping(messageType = Base.DataType.EDIT_GROUP_PROFILE_PHOTO_REQUEST)
	public void handleEditGroupProfilePhotoRequest(ChannelHandlerContext ctx, Base.Message msg) {

	}

	@ChatMessageMapping(messageType = Base.DataType.EDIT_GROUP_PROFILE_PHOTO_RESPONSE)
	public void handleEditGroupProfilePhotoResponse(ChannelHandlerContext ctx, Base.Message msg) {
		long targetId = msg.getDeleteFriendResponse().getTargetId();
		commonMessageSender.sendNotify(targetId, msg);
	}


}
