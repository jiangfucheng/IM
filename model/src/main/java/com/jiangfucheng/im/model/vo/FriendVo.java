package com.jiangfucheng.im.model.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 0:12
 *
 * @author jiangfucheng
 */
@Data
public class FriendVo {
	private Long id;
	private String account;
	private String nickName;
	private String remarks;
	private Integer status;
	private String profilePhoto;
	private String signature;


}
