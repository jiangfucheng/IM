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
@TableName("im_group_msg")
public class GroupMessagePo {
	private Long id;
	private Long groupId;
	private Long fromId;
	private Integer msgType;
	private String content;
	private Long createTime;
}
