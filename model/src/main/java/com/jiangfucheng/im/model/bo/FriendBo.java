package com.jiangfucheng.im.model.bo;

import com.jiangfucheng.im.httpserver.utils.BeanUtil;
import com.jiangfucheng.im.model.vo.FriendVo;
import lombok.Data;

/**
 * Created by IntelliJ IDEA.
 * Date: 2020/5/13
 * Time: 23:22
 *
 * @author jiangfucheng
 */
@Data
public class FriendBo {
	private Long id;
	private String account;
	private String nickName;
	private String remarks;
	//在线状态
	private Integer status;
	private String profilePhoto;
	private String signature;

	public FriendVo convertToFriendVo() {
		FriendVo vo = new FriendVo();
		BeanUtil.copyProperties(vo, this);
		return vo;
	}
}
