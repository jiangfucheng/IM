package com.jiangfucheng.im.client.command;

import com.jiangfucheng.im.client.command.executor.*;
import com.jiangfucheng.im.client.exception.CommandParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 15:42
 * 命令解析器
 *
 * @author jiangfucheng
 */
@Component
public class CommandParser {
	private final ApplicationContext ctx;

	@Autowired
	public CommandParser(ApplicationContext ctx) {
		this.ctx = ctx;
	}

	public CommandExecutor parse(String command) throws CommandParseException {
		String[] params = command.split(" ");
		switch (params[0]) {
			case "exit":
				return ctx.getBean(ExitExecutor.class);
			case "login": {
				String account = params[1];
				String password = params[2];
				LoginExecutor loginExecutor = ctx.getBean(LoginExecutor.class);
				loginExecutor.setAccount(account);
				loginExecutor.setPassword(password);
				return loginExecutor;
			}
			case "logout": {
				return ctx.getBean(LogoutExecutor.class);
			}
			case "single-chat":
			case "sc": {
				String account = params[1];
				SingleChatExecutor singleChatExecutor = ctx.getBean(SingleChatExecutor.class);
				singleChatExecutor.setTargetAccount(account);
				return singleChatExecutor;
			}
			case "group-chat":
			case "gc": {
				GroupChatExecutor groupChatExecutor = ctx.getBean(GroupChatExecutor.class);
				return groupChatExecutor;
			} case "friend-list":
			case "fl":
				return ctx.getBean(FriendListExecutor.class);
			default:
				throw new CommandParseException(command);
		}
	}
}
