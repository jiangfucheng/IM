package com.jiangfucheng.im.httpserver;

import com.jiangfucheng.im.model.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.common.utils.JwtUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/15
 * Time: 0:32
 *
 * @author jiangfucheng
 */
public class JwtTest {

	@Test
	public void testGenerateToken(){
		UserTokenPayloadBo user = new UserTokenPayloadBo();
		user.setUserId(1209230902934802934L);
		user.setAccount("jiangfucheng");
		user.setNickName("姜福城");
		String token = JwtUtil.generateToken(user);
		System.out.println(token);
		Assert.assertTrue(JwtUtil.verify(token));
		Object tokenBody = JwtUtil.getTokenBody(token);
		System.out.println(tokenBody);
		System.out.println(tokenBody.getClass());
	}
}
