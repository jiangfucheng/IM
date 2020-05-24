package com.jiangfucheng.im.httpserver.service;

import com.jiangfucheng.im.model.bo.GroupAnnouncementBo;
import com.jiangfucheng.im.model.bo.GroupBo;
import com.jiangfucheng.im.model.bo.GroupMemberBo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:44
 *
 * @author jiangfucheng
 */

public interface GroupService {

	/**
	 * 查询群列表
	 */
	List<GroupBo> queryGroupsWithUser(Long userId);

	/**
	 * 根据群账号(id)查找群详情
	 *
	 * @param account 群账号
	 */
	GroupBo getGroupByAccount(Long account);

	/**
	 * 查询群公告
	 */
	List<GroupAnnouncementBo> queryAnnouncement(Long userId, Long groupId);

	/**
	 * @param queryUserId 查询该详情的用户id
	 *                    查询群详情
	 */
	GroupBo queryGroupDetail(Long queryUserId, Long groupId);

	/**
	 * 查询群成员列表
	 *
	 * @return
	 */
	List<GroupMemberBo> queryGroupMembers(Long userId, Long groupId);
}
