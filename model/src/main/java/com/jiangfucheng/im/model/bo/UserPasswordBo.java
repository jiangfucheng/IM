package com.jiangfucheng.im.model.bo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:40
 *
 * @author jiangfucheng
 */
@Data
public class UserPasswordBo {
	private Long id;
	private String oldPassword;
	private String newPassword;


}
