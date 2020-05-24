package com.jiangfucheng.im.httpserver;

import com.jiangfucheng.im.common.utils.PinyinUtil;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/17
 * Time: 10:03
 *
 * @author jiangfucheng
 */
public class PinyinTest {

	@Test
	public void test(){
		String[] arr = {"姜福城","A姜福城","..jiangaljkfds","lakjdflksa","滚把","j348dkasljf","2332lkadjf"};
		for (String s : arr){
			System.out.println(PinyinUtil.getFirstLetterByFirstChar(s));
		}
	}
}
