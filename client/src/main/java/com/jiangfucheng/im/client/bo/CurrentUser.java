package com.jiangfucheng.im.client.bo;

import com.jiangfucheng.im.client.enums.UserStatus;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:18
 *
 * @author jiangfucheng
 */
@Data
public class CurrentUser {
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
	 * password
	 */
	private String password;
	/**
	 * 在线状态
	 */
	private volatile UserStatus status = UserStatus.OFFLINE;
}
