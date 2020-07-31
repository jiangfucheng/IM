package com.jiangfucheng.im.client.exception;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:24
 *
 * @author jiangfucheng
 */
public class UserNotLoginException extends RuntimeException {
	public UserNotLoginException(){
		super("user not login, please login first");
	}
}
