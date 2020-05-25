package com.jiangfucheng.im.model.vo;

import com.jiangfucheng.im.model.bo.UserBo;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:33
 *
 * @author jiangfucheng
 */
@Data
public class UserVo {
	private Long id;
	@NotBlank(message = "账号不能为空")
	private String account;
	@NotBlank(message = "昵称不能为空")
	private String nickName;
	@NotNull
	@Max(1)
	@Min(0)
	private Integer sex;
	private Long birthday;
	private String profilePhoto = "/static/default_profile_photo.jpg";
	private String signature;
	private String phone;
	private String email;
	private String school;
	private String country;
	private String city;
	private String password;
	private Integer isFriend;

	public UserBo convertToUserBo() {
		UserBo userBo = new UserBo();
		try {
			BeanUtils.copyProperties(userBo, this);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return userBo;
	}
}
