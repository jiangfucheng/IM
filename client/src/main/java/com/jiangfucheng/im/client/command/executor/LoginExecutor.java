package com.jiangfucheng.im.client.command.executor;

import com.jiangfucheng.im.client.bo.CurrentUser;
import com.jiangfucheng.im.client.context.ChatClientContext;
import com.jiangfucheng.im.client.enums.UserStatus;
import com.jiangfucheng.im.client.feign.IndexFeignClient;
import com.jiangfucheng.im.client.feign.UserFeignClient;
import com.jiangfucheng.im.common.resp.Response;
import com.jiangfucheng.im.common.utils.JwtUtil;
import com.jiangfucheng.im.model.bo.UserTokenPayloadBo;
import com.jiangfucheng.im.model.vo.LoginRequestVo;
import com.jiangfucheng.im.model.vo.LoginResponseVo;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/7/22
 * Time: 16:04
 *
 * @author jiangfucheng
 */
@Data
@Component
public class LoginExecutor extends CommandExecutor {
	private String account;
	private String password;

	private IndexFeignClient indexFeignClient;
	private UserFeignClient userFeignClient;
	private ChatClientContext context;

	public LoginExecutor(IndexFeignClient indexFeignClient,
						 ChatClientContext context,
						 UserFeignClient userFeignClient) {
		this.indexFeignClient = indexFeignClient;
		this.context = context;
		this.userFeignClient = userFeignClient;
	}

	@Override
	public void execute() {
		LoginRequestVo loginRequestVo = new LoginRequestVo();
		loginRequestVo.setAccount(this.account);
		loginRequestVo.setPassword(this.password);
		Response<LoginResponseVo> responseVo = indexFeignClient.login(loginRequestVo);
		if (responseVo.getCode() == 0) {
			System.out.println("登陆成功");
			context.setAuthToken(responseVo.getData().getToken());
			CurrentUser currentUser = new CurrentUser();
			currentUser.setAccount(this.account);
			currentUser.setPassword(this.password);
			UserTokenPayloadBo tokenBody = JwtUtil.getTokenBody(context.getAuthToken());
			currentUser.setId(tokenBody.getUserId());
			currentUser.setNickName(tokenBody.getNickName());
			currentUser.setStatus(UserStatus.ONLINE);
			context.setCurrentUser(currentUser);
			context.setChatServerUrl(responseVo.getData().getChatServer());
		}
	}
}
