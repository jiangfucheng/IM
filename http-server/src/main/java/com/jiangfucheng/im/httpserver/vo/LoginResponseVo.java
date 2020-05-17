package com.jiangfucheng.im.httpserver.vo;

import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 0:14
 *
 * @author jiangfucheng
 */
@Data
public class LoginResponseVo {
	private Long id;
	private String chatServer;
	private String token;
}
