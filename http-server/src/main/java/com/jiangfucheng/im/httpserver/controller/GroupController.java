package com.jiangfucheng.im.httpserver.controller;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.common.util.ParamValidator;
import com.jiangfucheng.im.httpserver.bo.GroupAnnouncementBo;
import com.jiangfucheng.im.httpserver.bo.GroupBo;
import com.jiangfucheng.im.httpserver.bo.GroupMemberBo;
import com.jiangfucheng.im.httpserver.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.httpserver.service.GroupService;
import com.jiangfucheng.im.httpserver.service.UserService;
import com.jiangfucheng.im.httpserver.utils.PinyinUtil;
import com.jiangfucheng.im.httpserver.vo.GroupAnnouncementVo;
import com.jiangfucheng.im.httpserver.vo.GroupListElementVo;
import com.jiangfucheng.im.httpserver.vo.GroupMemberVo;
import com.jiangfucheng.im.httpserver.vo.GroupMemberWithIndexVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/16
 * Time: 23:54
 *
 * @author jiangfucheng
 */
@RestController
public class GroupController {
	private final GroupService groupService;

	public GroupController(GroupService groupService, UserService userService) {
		this.groupService = groupService;
	}

	@GetMapping("/groups")
	public Response getGroups(UserTokenPayloadBo userTokenPayloadBo) {
		Long userId = userTokenPayloadBo.getUserId();
		List<GroupBo> groupBos = groupService.queryGroupsWithUser(userId);
		List<GroupListElementVo> groupVos = groupBos.stream().map(GroupBo::convertToGroupListElementVo).collect(Collectors.toList());
		return Response.ok(groupVos);
	}

	@GetMapping("/groups/{group_id}/announcements")
	public Response getGroupAnnouncements(UserTokenPayloadBo userInfo,
										  @PathVariable("group_id") Long groupId) {
		ParamValidator.notNull(groupId, "群id不能为空");
		List<GroupAnnouncementBo> announcementBos = groupService.queryAnnouncement(userInfo.getUserId(), groupId);
		List<GroupAnnouncementVo> announcementVos = announcementBos.stream().map(GroupAnnouncementBo::convertToGroupAnnouncementVo).collect(Collectors.toList());
		return Response.ok(announcementVos);
	}

	@GetMapping("/group/{group_id}")
	public Response getGroupDetail(UserTokenPayloadBo userInfo,
								   @PathVariable("group_id") Long groupId) {
		GroupBo groupBo = groupService.queryGroupDetail(userInfo.getUserId(), groupId);
		return Response.ok(groupBo.convertToGroupInfoVo());
	}

	@GetMapping("/group/{group_id}/users")
	public Response getGroupMembers(UserTokenPayloadBo userInfo,
									@PathVariable("group_id") Long groupId) {
		List<GroupMemberBo> groupMemberBos = groupService.queryGroupMembers(userInfo.getUserId(), groupId);
		List<GroupMemberVo> groupMemberVos = groupMemberBos.stream().map(GroupMemberBo::convertToGroupMemberVo).collect(Collectors.toList());
		List<GroupMemberWithIndexVo> indexVos = new ArrayList<>();
		groupMemberVos.sort((o1, o2) -> {
			char o1FirstLetter = PinyinUtil.getFirstLetterByFirstChar(o1.getRemarks());
			char o2FirstLetter = PinyinUtil.getFirstLetterByFirstChar(o2.getRemarks());
			return PinyinUtil.compareCharFirstLetter(o1FirstLetter, o2FirstLetter);
		});
		List<GroupMemberVo> temp = new ArrayList<>();
		char initLetter = '#';
		for (GroupMemberVo vo : groupMemberVos) {
			char firstLetter = PinyinUtil.getFirstLetterByFirstChar(vo.getRemarks());
			if (firstLetter == initLetter) {
				temp.add(vo);
			} else {
				indexVos.add(new GroupMemberWithIndexVo(initLetter, temp));
				temp = new ArrayList<>();
				if (initLetter == '#') {
					initLetter = 'A';
				} else {
					initLetter = 'A' + 1;
				}
			}
		}
		return Response.ok(indexVos);
	}

}
