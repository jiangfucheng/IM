package com.jiangfucheng.im.client.command.executor;

import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.Control;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/25
 * Time: 13:24
 * 退出登陆执行器
 * @author jiangfucheng
 */
@Component
public class LogoutExecutor extends CommandExecutor {
	private ChatClientContext context;
	private SnowFlakeIdGenerator idGenerator;

	public LogoutExecutor(ChatClientContext context,
						  SnowFlakeIdGenerator idGenerator) {
		this.context = context;
		this.idGenerator = idGenerator;
	}

	@Override
	public void execute() {
		Channel channel = context.getChannel();
		Control.LogoutRequest logoutRequest = Control.LogoutRequest.newBuilder()
				.setTimestamp(System.currentTimeMillis())
				.setToken(context.getAuthToken())
				.build();
		Base.Message message = Base.Message.newBuilder()
				.setId(idGenerator.nextId())
				.setDataType(Base.DataType.LOGOUT_REQUEST)
				.setMessageStatus(Base.MessageStatus.REQ)
				.setLogoutRequest(logoutRequest)
				.build();
		channel.writeAndFlush(message);
	}
}
