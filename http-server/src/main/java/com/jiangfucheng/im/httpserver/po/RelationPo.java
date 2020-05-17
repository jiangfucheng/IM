package com.jiangfucheng.im.httpserver.po;

import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("im_relation")
public class RelationPo {
	@TableId
	private Long userId;
	@TableId
	private Long friendId;
	private String remarks;
	private Long createTime;
}
