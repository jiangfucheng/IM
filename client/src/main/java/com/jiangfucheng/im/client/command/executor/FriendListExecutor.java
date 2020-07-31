package com.jiangfucheng.im.client.command.executor;

import com.jiangfucheng.im.client.bo.User;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.enums.UserStatus;
import com.jiangfucheng.im.client.feign.FriendFeignClient;
import com.jiangfucheng.im.model.vo.FriendWithIndexVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:04
 *
 * @author jiangfucheng
 */
@Component
public class FriendListExecutor extends CommandExecutor {

	private final ChatClientContext context;
	private final FriendFeignClient friendFeignClient;

	@Autowired
	public FriendListExecutor(ChatClientContext context, FriendFeignClient friendFeignClient) {
		this.context = context;
		this.friendFeignClient = friendFeignClient;
	}

	@Override
	public void execute() {

		if (context.friendList.size() == 0) {
			List<FriendWithIndexVo> friendListVo = friendFeignClient.getFriendList().getData();
			friendListVo.forEach(friendWithIndexVo -> context.friendList.addAll(
					friendWithIndexVo.getFriends()
							.stream()
							.map(friendVo -> {
								User friend = new User();
								friend.setId(friendVo.getId());
								friend.setAccount(friendVo.getAccount());
								friend.setNickName(friendVo.getNickName());
								friend.setRemark(friendVo.getRemarks());
								friend.setStatus(friendVo.getStatus() == 1 ? UserStatus.ONELINE : UserStatus.OFFLIINE);
								return friend;
							})
							.collect(Collectors.toList())));
		}
		context.friendList.forEach(friend -> System.out.printf("%s\t|\t%s\t|\t%s\t|\t%s\n",
				friend.getAccount(),
				friend.getNickName(),
				friend.getRemark(),
				friend.getStatus() == UserStatus.ONELINE ? "在线" : "离线"));
	}
}
