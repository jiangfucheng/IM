package com.jiangfucheng.im.httpserver.controller;

import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.common.utils.PinyinUtil;
import com.jiangfucheng.im.httpserver.service.FriendRelationService;
import com.jiangfucheng.im.model.bo.FriendBo;
import com.jiangfucheng.im.model.bo.FriendRemarksBo;
import com.jiangfucheng.im.model.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.model.vo.FriendVo;
import com.jiangfucheng.im.model.vo.FriendWithIndexVo;
import com.jiangfucheng.im.model.vo.UpdateFriendRemarksVo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 10:55
 *
 * @author jiangfucheng
 */
@RestController
public class FriendController {

	private final FriendRelationService friendRelationService;

	public FriendController(FriendRelationService friendRelationService) {
		this.friendRelationService = friendRelationService;
	}

	@PutMapping("/friend/remark/{friend_id}")
	public Response updateFriendRemarks(UserTokenPayloadBo userInfo,
										@PathVariable("friend_id") Long friendId,
										@RequestBody FriendRemarksBo remarksBo) {
		remarksBo.setFriendId(friendId);
		remarksBo.setUserId(userInfo.getUserId());
		friendRelationService.updateFriendRemarks(remarksBo);
		UpdateFriendRemarksVo resVo = new UpdateFriendRemarksVo();
		resVo.setFriendId(friendId);
		resVo.setRemarks(remarksBo.getRemarks());
		return Response.ok(resVo);
	}

	@GetMapping("/friends")
	public Response getFriendList(UserTokenPayloadBo userInfo) {
		List<FriendBo> friendBos = friendRelationService.getFriendsWithUser(userInfo.getUserId());
		List<FriendVo> friendVos = friendBos.stream().map(FriendBo::convertToFriendVo).sorted((vo1, vo2) -> {
			String name1, name2;
			if (vo1.getRemarks() != null) {
				name1 = vo1.getRemarks();
			} else {
				name1 = vo1.getNickName();
			}
			if (vo2.getRemarks() != null) {
				name2 = vo2.getRemarks();
			} else {
				name2 = vo2.getNickName();
			}
			char o1FirstLetter = PinyinUtil.getFirstLetterByFirstChar(name1);
			char o2FirstLetter = PinyinUtil.getFirstLetterByFirstChar(name2);
			return PinyinUtil.compareCharFirstLetter(o1FirstLetter, o2FirstLetter);
		}).collect(Collectors.toList());

		List<FriendWithIndexVo> indexVos = new ArrayList<>();
		char initLetter = '#';
		for (int i = 0; i < 27; i++) {
			FriendWithIndexVo indexVo = new FriendWithIndexVo();
			indexVo.setIndex(initLetter);
			indexVo.setRemarks(initLetter);
			indexVo.setFriends(new ArrayList<>());
			if (initLetter == '#') {
				initLetter = 'A';
			} else {
				initLetter = (char) (initLetter + 1);
			}
			indexVos.add(indexVo);
		}
		for (FriendVo vo : friendVos) {
			char firstLetter = PinyinUtil.getFirstLetterByFirstChar(vo.getRemarks());
			if (firstLetter == '#') {
				indexVos.get(0).getFriends().add(vo);
			} else {
				indexVos.get(firstLetter - 'A' + 1).getFriends().add(vo);
			}
		}
		indexVos = indexVos.stream().filter(indexVo -> indexVo.getFriends().size() > 0).collect(Collectors.toList());
		return Response.ok(indexVos);
	}

}
