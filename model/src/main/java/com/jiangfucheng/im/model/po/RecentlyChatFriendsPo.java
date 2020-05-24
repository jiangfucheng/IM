package com.jiangfucheng.im.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

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
	private Integer type;
	private Long userId;
	private Long fromId;
	private Date createTime;
}
