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
@TableName("im_notify")
public class NotifyPo {
	private Long id;
	private Integer type;
	private Long fromId;
	private Long toId;
	private String content;
	private Long createTime;
}
