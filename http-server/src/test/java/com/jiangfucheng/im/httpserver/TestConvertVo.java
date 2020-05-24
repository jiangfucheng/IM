package com.jiangfucheng.im.httpserver;

import com.jiangfucheng.im.model.bo.UserBo;
import com.jiangfucheng.im.model.vo.UserVo;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/14
 * Time: 23:41
 *
 * @author jiangfucheng
 */
public class TestConvertVo {
	@Test
	public void testVoConvertToBo(){
		UserVo userVo = new UserVo();
		userVo.setAccount("alkjdfla");
		userVo.setBirthday(1232143L);
		userVo.setCity("cangzhou");
		UserBo userBo = userVo.convertToUserBo();
		System.out.println(userBo);
	}

	@Test
	public void demo(){
		System.out.println(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
	}
}
