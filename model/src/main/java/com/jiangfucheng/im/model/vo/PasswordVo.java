package com.jiangfucheng.im.model.vo;

import com.jiangfucheng.im.model.bo.UserPasswordBo;
import com.jiangfucheng.im.model.utils.BeanUtil;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:44
 *
 * @author jiangfucheng
 */
@Data
public class PasswordVo {
	private String oldPassword;
	private String newPassword;

	public UserPasswordBo convertToUserPasswordBo(Long userId){
		UserPasswordBo passwordBo = new UserPasswordBo();
		BeanUtil.copyProperties(passwordBo,this);
		passwordBo.setId(userId);
		return passwordBo;
	}

}
