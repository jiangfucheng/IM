package com.jiangfucheng.im.client.command.executor;

import com.jiangfucheng.im.client.bo.SingleMessageBo;
import com.jiangfucheng.im.client.chat.MessageMonitor;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.feign.MessageFeignClient;
import com.jiangfucheng.im.client.feign.UserFeignClient;
import com.jiangfucheng.im.client.utils.MessageUtils;
import com.jiangfucheng.im.common.enums.MessageType;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.model.vo.UserVo;
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
		listOfflineMessage();
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

	private void listOfflineMessage() {
//		Response<List<MessageVo>> historyMessageList = messageFeignClient.getHistoryMessageList(this.targetId);
		//todo socket拉取未送达的在线消息
		// http拉取已送达的在线消息
		if(!context.singleChatMessages.containsKey(targetId))
			return;
		List<SingleMessageBo> messageBos = context.singleChatMessages.get(targetId);
		UserVo friendVo = userFeignClient.queryUserById(targetId).getData();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		messageBos.forEach(message -> {
			if (message.getFromId().equals(targetId)) {
				//朋友发的
				System.out.println(friendVo.getNickName() + " " + simpleDateFormat.format(message.getTime()));
			} else {
				System.err.println(context.getCurrentUser().getNickName() + " " + simpleDateFormat.format(message.getTime()));
			}
			if (message.getMessageType() == MessageType.TEXT) {
				System.out.println(message.getContent());
			} else {
				System.out.println("不支持的消息类型");
			}
		});
	}

	private void sendMessage(String command) {
		String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.err.println(context.getCurrentUser().getNickName() + " " + currentDate);
		Channel channel = context.getChannel();
		Base.Message message = buildMessageRequest(command);
		MessageUtils.writeRequestReqMessage(channel, context, messageMonitor, message);
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
