package com.jiangfucheng.im.chatserver.scheduler;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/22
 * Time: 22:50
 *
 * @author jiangfucheng
 */
public class TaskExecuter {
	private ScheduledExecutorService executorService;

	public TaskExecuter() {

	}

	/**
	 * @param task 任务
	 */
	public void execute(Runnable task) {
		executorService.execute(task);
	}

}
