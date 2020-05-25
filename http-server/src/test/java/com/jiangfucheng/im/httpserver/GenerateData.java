package com.jiangfucheng.im.httpserver;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import com.jiangfucheng.im.httpserver.mapper.*;
import com.jiangfucheng.im.httpserver.service.FriendRelationService;
import com.jiangfucheng.im.httpserver.service.GroupService;
import com.jiangfucheng.im.httpserver.service.UserService;
import com.jiangfucheng.im.model.bo.FriendBo;
import com.jiangfucheng.im.model.bo.GroupMemberBo;
import com.jiangfucheng.im.model.po.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/19
 * Time: 21:48
 *
 * @author jiangfucheng
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GenerateData {
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private OfflineMessageMapper offlineMessageMapper;
	@Autowired
	private GroupMessageMapper groupMessageMapper;
	@Autowired
	private GroupInfoMapper groupInfoMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	SnowFlakeIdGenerator idGenerator;
	@Autowired
	private GroupUserMapper groupUserMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private FriendRelationService relationService;
	@Autowired
	private RelationMapper relationMapper;
	@Autowired
	private GroupService groupService;
	@Autowired
	SyncGroupMessageMapper syncGroupMessageMapper;
	@Autowired
	private RecentlyChatFriendsMapper recentlyChatFriendsMapper;
	@Autowired
	private RecentlyNotifyMapper recentlyNotifyMapper;
	@Autowired
	private NotifyMapper notifyMapper;

	public static long mainUserId;

	@Test
	public void generate0MainUser() {
		UserPo userPo = new UserPo();
		mainUserId = idGenerator.nextId();
		userPo.setId(mainUserId);
		userPo.setNickName("姜福城");
		userPo.setPassword("123456");
		userPo.setAccount("jiangfucheng");
		userPo.setBirthday(new Date());
		userPo.setSignature("咸鱼本鱼");
		userPo.setSex(1);
		userPo.setProfilePhoto("/static/default_profile_photo.jpg");
		userMapper.insert(userPo);
		System.out.println("==================主用户数据构造完成==================");
	}

	@Test
	public void generate1User() {
		for (int i = 0; i < 100; i++) {
			UserPo userPo = new UserPo();
			userPo.setId(idGenerator.nextId());
			userPo.setNickName("测试用户-" + i);
			userPo.setPassword("123456");
			userPo.setAccount("ceshiyonghu-" + i);
			userPo.setBirthday(new Date());
			userPo.setSignature("咸鱼一条----" + i);
			userPo.setSex(1);
			userPo.setProfilePhoto("/static/default_profile_photo.jpg");
			userMapper.insert(userPo);
		}
		System.out.println("==================用户数据构造完成==================");
	}

	@Test
	public void generate2Groups() {
		List<UserPo> users = userMapper.selectList(new QueryWrapper<>());
		Random random = new Random();
		for (int i = 0; i < 50; i++) {
			GroupInfoPo groupInfoPo = new GroupInfoPo();
			Long groupId = idGenerator.nextId();
			UserPo createUser = users.get(random.nextInt(users.size()));
			Long createUserId = createUser.getId();
			groupInfoPo.setId(groupId);
			groupInfoPo.setCreateUserId(createUserId);
			groupInfoPo.setIntroduction("群简介测试---" + i);
			groupInfoPo.setName("测试群---" + i);
			groupInfoPo.setProfilePhoto("/static/default_profile_photo.jpg");
			GroupUserPo groupUserPo = new GroupUserPo();
			groupUserPo.setGroupId(groupId);
			groupUserPo.setId(idGenerator.nextId());
			groupUserPo.setUserId(createUserId);
			groupUserPo.setRemarks(createUser.getNickName() + "---" + groupInfoPo.getName() + "---备注");
			groupUserPo.setRole(0);

			groupInfoMapper.insert(groupInfoPo);
			groupUserMapper.insert(groupUserPo);

			for (int j = 0; j < 20; j++) {
				UserPo user = users.get(random.nextInt(users.size()));
				if (user.getId().equals(createUserId)) {
					j--;
					continue;
				}
				GroupUserPo groupUserPo1 = new GroupUserPo();
				groupUserPo1.setGroupId(groupId);
				groupUserPo1.setId(idGenerator.nextId());
				groupUserPo1.setUserId(user.getId());
				groupUserPo1.setRemarks(user.getNickName() + "---" + groupInfoPo.getName() + "---备注");
				groupUserPo1.setRole(2);
				groupUserMapper.insert(groupUserPo1);
			}
		}
		System.out.println("==================群数据构造完成==================");

	}

	@Test
	public void generate3Friends() {
		UserPo userPo = userMapper.selectById(mainUserId);
		List<UserPo> users = userMapper.selectList(new QueryWrapper<>());
		users.forEach(friend -> {
			if (!userPo.getId().equals(friend.getId())) {
				RelationPo relationPo = new RelationPo();
				relationPo.setUserId(userPo.getId());
				relationPo.setFriendId(friend.getId());
				relationPo.setId(idGenerator.nextId());
				relationPo.setRemarks(friend.getNickName() + "--在--" + userPo.getNickName() + " 中的备注");

				RelationPo relationPo1 = new RelationPo();
				relationPo1.setUserId(friend.getId());
				relationPo1.setFriendId(userPo.getId());
				relationPo1.setId(idGenerator.nextId());
				relationPo1.setRemarks(userPo.getNickName() + "--在--" + friend.getNickName() + " 中的备注");

				relationMapper.insert(relationPo);
				relationMapper.insert(relationPo1);
			}
		});
		System.out.println("==================好友数据构造完成==================");

	}

	@Test
	public void generate4FriendMessage() {
		UserPo userPo = userMapper.selectById(mainUserId);
		List<RelationPo> friends = relationMapper.selectList(new QueryWrapper<RelationPo>().eq("user_id", userPo.getId()));
		Random random = new Random();
		friends.forEach(friend -> {
			int end = random.nextInt(20) + 30;
			for (int i = 0; i < end; i++) {
				MessagePo messagePo = new MessagePo();
				messagePo.setId(idGenerator.nextId());
				messagePo.setContent("测试内容----" + i);
				if (random.nextBoolean()) {
					messagePo.setFromId(userPo.getId());
					messagePo.setToId(friend.getFriendId());
				} else {
					messagePo.setFromId(friend.getFriendId());
					messagePo.setToId(userPo.getId());
				}
				messagePo.setMsgType(0);
				if (end - i <= 10) {
					messagePo.setDelivered(1);
				} else {
					messagePo.setDelivered(0);
				}
				messageMapper.insert(messagePo);
			}
			System.out.println("==================好友消息数据构造完成==================");

		});
	}

	@Test
	public void generate5GroupMessage() {
		UserPo userPo = userMapper.selectById(mainUserId);
		List<GroupUserPo> groups = groupUserMapper.selectList(new QueryWrapper<GroupUserPo>().eq("user_id", userPo.getId()));
		Random random = new Random();
		groups.forEach(group -> {
			List<Long> msgIds = new ArrayList<>();
			for (int i = 0; i < 100; i++) {
				GroupMessagePo groupMessagePo = new GroupMessagePo();
				Long msgId = idGenerator.nextId();
				msgIds.add(msgId);
				groupMessagePo.setId(msgId);
				groupMessagePo.setMsgType(0);
				groupMessagePo.setContent("群消息测试---" + i);
				groupMessagePo.setGroupId(group.getGroupId());
				if (random.nextBoolean()) {
					groupMessagePo.setFromId(userPo.getId());
				} else {
					List<GroupMemberBo> groupMemberBos = groupService.queryGroupMembers(userPo.getId(), group.getGroupId());
					groupMessagePo.setFromId(groupMemberBos.get(random.nextInt(groupMemberBos.size())).getId());
				}
				groupMessageMapper.insert(groupMessagePo);
			}
			SyncGroupMessagePo syncGroupMessagePo = new SyncGroupMessagePo();
			syncGroupMessagePo.setId(idGenerator.nextId());
			syncGroupMessagePo.setGroupId(group.getGroupId());
			syncGroupMessagePo.setUserId(userPo.getId());
			syncGroupMessagePo.setLastMsgId(msgIds.get(random.nextInt(20) + 20));
			syncGroupMessageMapper.insert(syncGroupMessagePo);

		});
		System.out.println("==================群消息数据构造完成==================");
	}

	@Test
	public void generate6RecentlyMessage() {
		UserPo userPo = userMapper.selectById(mainUserId);
		List<FriendBo> friends = relationService.getFriendsWithUser(userPo.getId());
		List<GroupUserPo> groupUserPos = groupUserMapper.selectList(new QueryWrapper<GroupUserPo>().eq("user_id", userPo.getId()));
		friends.forEach(friend -> {
			RecentlyChatFriendsPo recentlyChatFriendsPo = new RecentlyChatFriendsPo();
			recentlyChatFriendsPo.setId(idGenerator.nextId());
			recentlyChatFriendsPo.setFromId(friend.getId());
			recentlyChatFriendsPo.setUserId(userPo.getId());
			recentlyChatFriendsPo.setType(0);
			recentlyChatFriendsMapper.insert(recentlyChatFriendsPo);
		});

		groupUserPos.forEach(groupUser -> {
			RecentlyChatFriendsPo recentlyChatFriendsPo = new RecentlyChatFriendsPo();
			recentlyChatFriendsPo.setId(idGenerator.nextId());
			recentlyChatFriendsPo.setFromId(groupUser.getId());
			recentlyChatFriendsPo.setUserId(userPo.getId());
			recentlyChatFriendsPo.setType(1);
			recentlyChatFriendsMapper.insert(recentlyChatFriendsPo);
		});
		System.out.println("==================最近聊天好友数据构造完成==================");

	}

	@Test
	public void generate7Notifies() {
		Long userId = mainUserId;
		List<RelationPo> friends = relationMapper.selectList(new QueryWrapper<RelationPo>()
				.eq("user_id", userId));
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			NotifyPo notifyPo = new NotifyPo();
			notifyPo.setId(idGenerator.nextId());
			notifyPo.setContent("通知测试---1");
			notifyPo.setFromId(friends.get(random.nextInt(friends.size())).getFriendId());
			notifyPo.setToId(userId);
			notifyPo.setType(0);
			notifyMapper.insert(notifyPo);
		}


		System.out.println("==================通知数据构造完成==================");

	}

	@Test
	public void generate8RecentlyNotifies() {
		List<NotifyPo> notifyPoList = notifyMapper.selectList(new QueryWrapper<>());
		for (int i = 0; i < 20; i++) {
			NotifyPo notifyPo = notifyPoList.get(i);
			RecentlyNotifyPo recentlyNotifyPo = new RecentlyNotifyPo();
			recentlyNotifyPo.setId(idGenerator.nextId());
			recentlyNotifyPo.setUserId(mainUserId);
			recentlyNotifyPo.setNotifyId(notifyPo.getId());
			recentlyNotifyMapper.insert(recentlyNotifyPo);
		}
		System.out.println("==================最近通知数据构造完成==================");

	}

}
