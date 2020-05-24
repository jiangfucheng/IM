package com.jiangfucheng.im.model.bo;

import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import com.jiangfucheng.im.model.vo.AccountVo;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:43
 *
 * @author jiangfucheng
 */
@Service
public class AccountIntroductionBo {
	private Integer type;
	private Long id;
	private String profilePhoto;
	private String name;

	public AccountVo convertToAccountVo(){
		AccountVo accountVo = new AccountVo();
		BeanUtil.copyProperties(accountVo, this);
		return accountVo;
	}
}
