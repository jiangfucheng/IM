package com.jiangfucheng.im.client.exception;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:23
 *
 * @author jiangfucheng
 */
public class CommandParseException extends Exception {

	public CommandParseException(String command) {
		super("un parsed command: " + command);
	}

}
