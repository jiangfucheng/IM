package com.jiangfucheng.im.httpserver.service;

import com.jiangfucheng.im.model.bo.AccountIntroductionBo;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 22:41
 *
 * @author jiangfucheng
 */
public interface AccountService {

	List<AccountIntroductionBo> queryAccountById(Long accountId);

}
