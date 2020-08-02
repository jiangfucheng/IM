package com.jiangfucheng.im.client.handler;

import com.jiangfucheng.im.client.chat.ChatClient;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.utils.MessageUtils;
import com.jiangfucheng.im.common.chat.ChatMessageDispatcher;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/21
 * Time: 21:33
 *
 * @author jiangfucheng
 */
@ChannelHandler.Sharable
@Slf4j
public class ChatClientHandler extends SimpleChannelInboundHandler<Base.Message> {

	private ChatMessageDispatcher messageDispatcher;
	private ChatClientContext context;
	private SnowFlakeIdGenerator snowFlakeIdGenerator;
	@Autowired
	private ChatClient chatClient;

	public ChatClientHandler(ChatMessageDispatcher messageDispatcher,
							 ChatClientContext context,
							 SnowFlakeIdGenerator snowFlakeIdGenerator
	) {
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
		MessageUtils.writeRequestReqMessage(ctx, context, null, requestMsg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	/**
	 * 与服务器的连接已经断开，需要进行重连
	 * todo 重连要考虑需要重连到原来的服务器，还是连接到一个新的服务器
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("与服务器断开连接,尝试进行重连");
		//EventLoop eventLoop = ctx.channel().eventLoop();
		//重新建立与服务器的链接
		chatClient.reConnect();
	}

}
