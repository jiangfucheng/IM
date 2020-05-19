package com.jiangfucheng.im.httpserver.po;

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
@TableName("im_relation")
public class RelationPo {
	private Long id;
	private Long userId;
	private Long friendId;
	private String remarks;
	private Date createTime;
}
