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
@TableName("im_group_msg")
public class GroupMessagePo {
	private Long id;
	private Long groupId;
	private Long fromId;
	private Integer msgType;
	private String content;
	private Date createTime;
}
