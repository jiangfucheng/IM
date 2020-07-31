package com.jiangfucheng.im.client.bo;

import com.jiangfucheng.im.client.enums.UserStatus;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/31
 * Time: 13:47
 * 用于表示用户基本信息
 * @author jiangfucheng
 */
@Data
public class User {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 在线状态
	 */
	private UserStatus status;
}
