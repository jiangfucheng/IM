package com.jiangfucheng.im.httpserver.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:01
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_sync_group_msg")
public class SyncGroupMessagePo {
	private Long id;
	private Long groupId;
	private Long userId;
	private Long lastMsgId;
	private Date createTime;
}
