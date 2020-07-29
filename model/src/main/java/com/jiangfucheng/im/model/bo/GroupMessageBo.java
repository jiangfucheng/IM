package com.jiangfucheng.im.model.bo;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/28
 * Time: 15:53
 *
 * @author jiangfucheng
 */
@Data
public class GroupMessageBo {
	private Long id;
	private Long groupId;
	private Long fromId;
	private Integer msgType;
	private String content;
	private Date createTime;
}
