package com.jiangfucheng.im.httpserver.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:00
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_recently_chat_friends")
public class RecentlyChatFriendsPo {
	private Long id;
	private Long userId;
	private Long friendId;
	private Long createTime;
}
