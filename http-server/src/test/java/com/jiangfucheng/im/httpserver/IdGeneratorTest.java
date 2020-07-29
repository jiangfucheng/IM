package com.jiangfucheng.im.httpserver;

import com.jiangfucheng.im.common.utils.SnowFlakeIdGenerator;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/16
 * Time: 18:02
 *
 * @author jiangfucheng
 */
public class IdGeneratorTest {

	@Test
	public void test(){
		SnowFlakeIdGenerator idGenerator = new SnowFlakeIdGenerator(4,4);
		System.out.println(idGenerator.nextId());
		System.out.println(idGenerator.nextId());
	}
}
