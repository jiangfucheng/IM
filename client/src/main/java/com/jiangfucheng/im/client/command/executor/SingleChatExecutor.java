package com.jiangfucheng.im.client.command.executor;

import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.feign.MessageFeignClient;
import com.jiangfucheng.im.client.feign.UserFeignClient;
import com.jiangfucheng.im.client.utils.MessageUtils;
import com.jiangfucheng.im.common.enums.MessageType;
import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.model.vo.MessageVo;
import com.jiangfucheng.im.protobuf.Base;
import com.jiangfucheng.im.protobuf.SingleChat;
import io.netty.channel.Channel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:04
 *
 * @author jiangfucheng
 */
@Component
@Data
public class SingleChatExecutor extends CommandExecutor {

	private Long targetId;
	private String targetAccount;

	private final ChatClientContext context;
	private MessageFeignClient messageFeignClient;
	private SnowFlakeIdGenerator idGenerator;
	private UserFeignClient userFeignClient;
	private MessageMonitor messageMonitor;

	private static final Integer DEFAULT_HISTORY_MESSAGE_NUMBER = 10;

	@Autowired
	public SingleChatExecutor(ChatClientContext context,
							  MessageFeignClient messageFeignClient,
							  SnowFlakeIdGenerator idGenerator,
							  UserFeignClient userFeignClient,
							  MessageMonitor messageMonitor) {
		this.context = context;
		this.messageFeignClient = messageFeignClient;
		this.idGenerator = idGenerator;
		this.userFeignClient = userFeignClient;
		this.messageMonitor = messageMonitor;
	}

	@Override
	public void execute() {
		//listHistoryMessage(DEFAULT_HISTORY_MESSAGE_NUMBER);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				System.out.print(">");
				String command = reader.readLine();
				if (":q".equals(command)) {
					break;
				} else {
					sendMessage(command);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void listHistoryMessage(int msgNumber) {
		Response<List<MessageVo>> historyMessageList = messageFeignClient.getHistoryMessageList(this.targetId);
		//todo 打印历史消息
	}

	private void sendMessage(String command) {
		String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.err.println(context.getCurrentUser().getNickName() + " " + currentDate);
		Channel channel = context.getChannel();
		Base.Message message = buildMessageRequest(command);
		MessageUtils.writeRequestReqMessage(channel,context,messageMonitor,message);
		System.out.println(command);
	}

	private Base.Message buildMessageRequest(String command) {
		SingleChat.SingleChatRequest singleChatRequest = SingleChat.SingleChatRequest.newBuilder()
				.setType(MessageType.TEXT.value())
				.setFromId(context.getCurrentUser().getId())
				.setToId(targetId)
				.setContent(command)
				.setToken(context.getAuthToken())
				.build();
		return Base.Message.newBuilder()
				.setId(idGenerator.nextId())
				.setSingleChatRequest(singleChatRequest)
				.setDataType(Base.DataType.SINGLE_CHAT_REQUEST)
				.setMessageStatus(Base.MessageStatus.REQ)
				.build();
	}

	public void setTargetAccount(String account) {
		this.targetAccount = account;
		this.targetId = userFeignClient.queryUserByAccount(this.targetAccount)
				.getData()
				.getId();
	}
}
