package com.jiangfucheng.im.httpserver.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jiangfucheng.im.httpserver.bo.UserBo;
import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 21:59
 *
 * @author jiangfucheng
 */
@Data
@TableName
public class UserPo {
	private Long id;
	private String account;
	private String nickName;
	private Integer sex;
	private Long birthday;
	private String profilePhoto;
	private String password;
	private String signature;
	private String phone;
	private String email;
	private String school;
	private String country;
	private String city;
	private Long createTime;

	public UserBo convertToUserBo(){
		UserBo userBo = new UserBo();
		BeanUtil.copyProperties(userBo, this);
		return userBo;
	}
}
