package com.jiangfucheng.im.client.command.executor;

import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:03
 *
 * @author jiangfucheng
 */
@Component
public class ExitExecutor extends CommandExecutor {
	@Override
	public void execute() {
		System.exit(1);
	}
}
