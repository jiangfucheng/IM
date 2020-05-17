package com.jiangfucheng.im.httpserver.vo;

import com.jiangfucheng.im.httpserver.bo.LoginRequestBo;
import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 15:14
 *
 * @author jiangfucheng
 */
@Data
public class LoginRequestVo {
	private String account;
	private String password;

	public LoginRequestBo convertToLoginRequestBo() {
		LoginRequestBo bo = new LoginRequestBo();
		BeanUtil.copyProperties(bo, this);
		return bo;
	}
}
