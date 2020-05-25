package com.jiangfucheng.im.httpserver;

import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/25
 * Time: 21:26
 *
 * @author jiangfucheng
 */
public class TestDate {
	SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator(2, 3);

	@Test
	public void test() {
		idGenerator.nextId();
		System.out.println(System.currentTimeMillis());
	}
}
