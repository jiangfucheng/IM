package com.jiangfucheng.im.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/15
 * Time: 0:00
 *
 * @author jiangfucheng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenPayloadBo {
	private Long userId;
	private String account;
	private String nickName;
}
