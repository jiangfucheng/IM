package com.jiangfucheng.im.model.bo;

import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/8/3
 * Time: 11:24
 *
 * @author jiangfucheng
 */
@Data
public class OfflineMessageBo {
	private Long id;
	private Long fromId;
	private Long toId;
	private Integer msgType;
	private String content;
	private Date createTime;

}
