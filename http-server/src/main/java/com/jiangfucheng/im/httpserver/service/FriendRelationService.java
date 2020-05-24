package com.jiangfucheng.im.httpserver.service;

import com.jiangfucheng.im.model.bo.FriendBo;
import com.jiangfucheng.im.model.bo.FriendRemarksBo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:20
 *
 * @author jiangfucheng
 */
public interface FriendRelationService {
	void updateFriendRemarks(FriendRemarksBo friendRemarksBo);

	List<FriendBo> getFriendsWithUser(Long userId);
}
