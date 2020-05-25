package com.jiangfucheng.im.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jiangfucheng.im.model.utils.BeanUtil;
import com.jiangfucheng.im.model.bo.UserBo;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 21:59
 *
 * @author jiangfucheng
 */
@Data
@TableName("im_user")
public class UserPo {
	private Long id;
	private String account;
	private String nickName;
	private Integer sex;
	private Date birthday;
	private String profilePhoto;
	private String password;
	private String signature;
	private String phone;
	private String email;
	private String school;
	private String country;
	private String city;
	private Date createTime;

	public UserBo convertToUserBo(){
		UserBo userBo = new UserBo();
		BeanUtil.copyProperties(userBo, this);
		userBo.setBirthday(this.getBirthday().getTime());
		userBo.setBirthday(this.getBirthday().getTime());
		return userBo;
	}
}
