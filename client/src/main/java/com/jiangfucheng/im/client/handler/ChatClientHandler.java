package com.jiangfucheng.im.client.handler;

import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.common.chat.ChatMessageDispatcher;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 21:33
 *
 * @author jiangfucheng
 */
@ChannelHandler.Sharable
public class ChatClientHandler extends SimpleChannelInboundHandler<Base.Message> {
	private ChatMessageDispatcher messageDispatcher;
	private ChatClientContext context;
	private SnowFlakeIdGenerator snowFlakeIdGenerator;

	public ChatClientHandler(ChatMessageDispatcher messageDispatcher,
							 ChatClientContext context,
							 SnowFlakeIdGenerator snowFlakeIdGenerator) {
		this.messageDispatcher = messageDispatcher;
		this.context = context;
		this.snowFlakeIdGenerator = snowFlakeIdGenerator;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Base.Message message) throws Exception {
		messageDispatcher.dispatch(channelHandlerContext, message);
	}


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//和服务端的连接建立好以后，向chat server服务器发送登陆请求
		Control.LoginRequest loginRequest = Control.LoginRequest.newBuilder()
				.setToken(context.getAuthToken())
				.setTimestamp(System.currentTimeMillis())
				.build();
		Base.Message requestMsg = Base.Message.newBuilder()
				.setId(snowFlakeIdGenerator.nextId())
				.setLoginRequest(loginRequest)
				.setDataType(Base.DataType.LOGIN_REQUEST)
				.setMessageStatus(Base.MessageStatus.REQ)
				.build();
		ctx.writeAndFlush(requestMsg);
		context.putUnCompletedMsg( requestMsg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
