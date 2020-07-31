package com.jiangfucheng.im.chatserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangfucheng.im.model.po.RecentlyChatFriendsPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:24
 *
 * @author jiangfucheng
 */
@Mapper
public interface RecentlyChatFriendsMapper extends BaseMapper<RecentlyChatFriendsPo> {
}
